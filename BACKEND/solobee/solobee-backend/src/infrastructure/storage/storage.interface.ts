export interface IStorageService {
  uploadImageWithCompression(
    folder: string,
    file: Express.Multer.File,
  ): Promise<string>;
  getPresignedUploadUrl(
    folder: string,
    fileName: string,
    contentType: string,
  ): Promise<PresignedUploadResult>;
  getPublicUrl(key: string | null | undefined): string | null;
  deleteFile(key: string | null): Promise<void>;
  deleteFiles(keys: string[]): Promise<void>;
  getPresignedDownloadUrl(fileKey: string): Promise<string | null>;
}

export const STORAGE_SERVICE = Symbol('STORAGE_SERVICE');

export interface PresignedUploadResult {
  presignedUrl: string;
  fileKey: string;
  publicUrl: string;
}
