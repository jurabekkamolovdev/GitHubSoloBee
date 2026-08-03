import type { ConfigService } from '@nestjs/config';

export interface S3Config {
  endpoint: string;
  publicEndpoint: string;
  accessKey: string;
  secretKey: string;
  region: string;
  bucketName: string;
}

export function buildS3Config(config: ConfigService): S3Config {
  return {
    endpoint: config.get<string>('S3_ENDPOINT', 'http://minio:9000'),
    publicEndpoint: config.get<string>(
      'S3_PUBLIC_ENDPOINT',
      'http://localhost:9000',
    ),
    accessKey: config.get<string>('S3_ACCESS_KEY', ''),
    secretKey: config.get<string>('S3_SECRET_KEY', ''),
    region: config.get<string>('S3_REGION', 'us-east-1'),
    bucketName: config.get<string>('S3_BUCKET_NAME', 'solobee-media'),
  };
}
