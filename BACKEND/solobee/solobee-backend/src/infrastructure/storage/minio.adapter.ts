import { Injectable, Logger, OnModuleInit } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import {
  S3Client,
  PutObjectCommand,
  CreateBucketCommand,
  HeadBucketCommand,
  PutBucketPolicyCommand,
  GetObjectCommand,
  DeleteObjectCommand,
  DeleteObjectsCommand,
} from '@aws-sdk/client-s3';
import { getSignedUrl } from '@aws-sdk/s3-request-presigner';
import { v4 as uuidv4 } from 'uuid';
import { buildS3Config } from '../config/s3.config';
import { IStorageService, PresignedUploadResult } from './storage.interface';
import sharp from 'sharp';

@Injectable()
export class MinioAdapter implements IStorageService, OnModuleInit {
  private readonly s3Client: S3Client;
  private readonly signingS3Client: S3Client;
  private readonly bucketName: string;
  private readonly publicEndpoint: string;
  private readonly logger = new Logger(MinioAdapter.name);

  constructor(private readonly configService: ConfigService) {
    const cfg = buildS3Config(configService);

    this.bucketName = cfg.bucketName;
    this.publicEndpoint = cfg.publicEndpoint;

    const base = {
      credentials: {
        accessKeyId: cfg.accessKey,
        secretAccessKey: cfg.secretKey,
      },
      region: cfg.region,
      forcePathStyle: true,
    };

    this.s3Client = new S3Client({ ...base, endpoint: cfg.endpoint });
    this.signingS3Client = new S3Client({
      ...base,
      endpoint: cfg.publicEndpoint,
    });
  }

  async onModuleInit() {
    await this.ensureBucketExists();
  }

  private async ensureBucketExists(): Promise<void> {
    try {
      await this.s3Client.send(
        new HeadBucketCommand({ Bucket: this.bucketName }),
      );
      this.logger.log(`Bucket "${this.bucketName}" mavjud`);
    } catch (error) {
      if (error instanceof Error) {
        const awsError = error as any;
        if (
          awsError.name === 'NotFound' ||
          awsError.$metadata?.httpStatusCode === 404
        ) {
          await this.s3Client.send(
            new CreateBucketCommand({ Bucket: this.bucketName }),
          );
          await this.setPublicPolicy(this.bucketName);
          this.logger.log(`Bucket "${this.bucketName}" yaratildi`);
        } else {
          this.logger.error('Bucket tekshirishda xato:', error);
        }
      }
    }
  }

  private async setPublicPolicy(bucket: string): Promise<void> {
    const policy = {
      Version: '2012-10-17',
      Statement: [
        {
          Effect: 'Allow',
          Principal: '*',
          Action: ['s3:GetObject'],
          Resource: [`arn:aws:s3:::${bucket}/*`],
        },
      ],
    };
    await this.s3Client.send(
      new PutBucketPolicyCommand({
        Bucket: bucket,
        Policy: JSON.stringify(policy),
      }),
    );
  }

  async uploadImageWithCompression(
    folder: string,
    file: Express.Multer.File,
  ): Promise<string> {
    const objectKey = `${folder}/${uuidv4()}.webp`;
    const compressed = await sharp(file.buffer)
      .webp({ quality: 80 })
      .toBuffer();
    await this.s3Client.send(
      new PutObjectCommand({
        Bucket: this.bucketName,
        Key: objectKey,
        Body: compressed,
        ContentType: 'image/webp',
      }),
    );
    return objectKey;
  }

  async getPresignedUploadUrl(
    folder: string,
    originalFileName: string,
    contentType: string,
  ): Promise<PresignedUploadResult> {
    const ext = originalFileName.split('.').pop();
    const objectKey = `${folder}/${uuidv4()}.${ext}`;
    const command = new PutObjectCommand({
      Bucket: this.bucketName,
      Key: objectKey,
      ContentType: contentType,
    });

    const presignedUrl = await getSignedUrl(this.signingS3Client, command, {
      expiresIn: 300,
    });

    return {
      presignedUrl,
      fileKey: objectKey,
      publicUrl: `${this.publicEndpoint}/${this.bucketName}/${objectKey}`,
    };
  }

  getPublicUrl(key: string | null | undefined): string | null {
    if (!key) return null;
    return `${this.publicEndpoint}/${this.bucketName}/${key}`;
  }

  async deleteFile(key: string | null): Promise<void> {
    if (!key) return;
    await this.s3Client.send(
      new DeleteObjectCommand({ Bucket: this.bucketName, Key: key }),
    );
  }

  async deleteFiles(keys: string[]): Promise<void> {
    const valid = keys.filter(Boolean);
    if (!valid.length) return;
    await this.s3Client.send(
      new DeleteObjectsCommand({
        Bucket: this.bucketName,
        Delete: { Objects: valid.map((Key) => ({ Key })) },
      }),
    );
  }

  async getPresignedDownloadUrl(fileKey: string): Promise<string | null> {
    if (!fileKey || fileKey.startsWith('http')) return fileKey;
    try {
      const command = new GetObjectCommand({
        Bucket: this.bucketName,
        Key: fileKey,
      });
      return await getSignedUrl(this.signingS3Client, command, {
        expiresIn: 18000,
      });
    } catch (error) {
      this.logger.error(`Presigned download URL xatosi: ${fileKey}`, error);
      return null;
    }
  }
}
