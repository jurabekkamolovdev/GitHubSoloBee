import { apiClient } from './client';

export type AvatarGender = 'BOY' | 'GIRL';

export interface Avatar {
  id: string;
  gender: AvatarGender;
  thumbnailUrl: string | null;
  orderIndex: number;
}

export interface GroupedAvatars {
  boy: Avatar[];
  girl: Avatar[];
}

/**
 * Write payload for creating an avatar.
 * NOTE: field names (`thumbnailKey`, `orderIndex`) follow the same convention
 * as TopicWrite / SubCategoryWrite in courses.ts (frontend uploads to S3 first,
 * then sends the resulting key — not the URL). Double-check these against the
 * real `CreateAvatarDto` on the backend and adjust if the names differ.
 */
export interface AvatarCreate {
  gender: AvatarGender;
  thumbnailKey: string;
  orderIndex?: number;
}

export const avatarsApi = {
  // === AVATAR ===
  getAvatars: () => apiClient.get<never, GroupedAvatars>('/avatars'),

  createAvatar: (data: AvatarCreate) =>
    apiClient.post<never, Avatar>('/avatars', data),

  deleteAvatar: (id: string) => apiClient.delete(`/avatars/${id}`),

  // === STORAGE / S3 ===
  getPresignedUrl: (folder: string, fileName: string, contentType: string) =>
    apiClient.post<never, { presignedUrl: string; fileKey: string; publicUrl: string }>(
      '/storage/presigned-url',
      { folder, fileName, contentType },
    ),

  uploadFileToS3: async (file: File, folder: string = 'avatars') => {
    const { presignedUrl, fileKey, publicUrl } = await avatarsApi.getPresignedUrl(
      folder,
      file.name,
      file.type,
    );
    const response = await fetch(presignedUrl, {
      method: 'PUT',
      body: file,
      headers: { 'Content-Type': file.type },
    });
    if (!response.ok) throw new Error('Upload to S3 failed');
    return { fileKey, publicUrl };
  },
};