import { apiClient } from './client';

export type ActivityType = 'LEARN' | 'WRITING' | 'WORDHUNT' | 'PICQUEST';

export interface Activity {
  id: string;
  type: ActivityType;
  topicId: string;
  payload: any;
}

export interface Topic {
  id: string;
  subCategoryId: string;
  thumbnailUrl?: string | null;
  activities?: Activity[];
}

export interface SubCategory {
  id: string;
  name: string;
  categoryId: string;
  thumbnailUrl?: string | null;
  topics?: Topic[];
}

export interface Category {
  id: string;
  name: string;
  backgroundColor: string;
  foregroundColor: string;
  images: { url: string; order: number }[];
  lessonCount?: number;
  subCategories?: SubCategory[];
}

/**
 * Write payloads: the frontend receives URLs, but writes send storage keys.
 * The admin only supplies a *Key when the user actually picks a new file —
 * omitting the field tells the backend to keep whatever is already stored.
 */
export interface SubCategoryWrite {
  name?: string;
  categoryId?: string;
  thumbnailKey?: string | null;
  orderIndex?: number;
}

export interface TopicWrite {
  subCategoryId?: string;
  thumbnailKey?: string | null;
  orderIndex?: number;
}

export const coursesApi = {
  // === CATEGORY ===
  getCategories: () => apiClient.get<never, Category[]>('/courses/categories'),

  createCategory: (data: Partial<Category> & { orderIndex?: number }) =>
    apiClient.post<never, Category>('/courses/categories', data),

  updateCategory: (id: string, data: Partial<Category> & { orderIndex?: number }) =>
    apiClient.put<never, Category>(`/courses/categories/${id}`, data),

  deleteCategory: (id: string) =>
    apiClient.delete(`/courses/categories/${id}`),

  // === SUBCATEGORY ===
  getSubCategory: (id: string) =>
    apiClient.get<never, SubCategory>(`/courses/subcategories/${id}`),

  getTopicsBySubCategory: (subCategoryId: string) =>
    apiClient.get<never, Topic[]>(`/courses/subcategories/${subCategoryId}/topics`),

  createSubCategory: (data: SubCategoryWrite & { name: string; categoryId: string }) =>
    apiClient.post<never, SubCategory>('/courses/subcategories', data),

  updateSubCategory: (id: string, data: SubCategoryWrite) =>
    apiClient.put<never, SubCategory>(`/courses/subcategories/${id}`, data),

  deleteSubCategory: (id: string) =>
    apiClient.delete(`/courses/subcategories/${id}`),

  // === TOPIC ===
  createTopic: (data: TopicWrite & { subCategoryId: string }) =>
    apiClient.post<never, Topic>('/courses/topics', data),

  updateTopic: (id: string, data: TopicWrite) =>
    apiClient.put<never, Topic>(`/courses/topics/${id}`, data),

  reorderTopics: (items: { id: string; orderIndex: number }[]) =>
    apiClient.put<never, Topic[]>('/courses/topics/reorder', { items }),

  deleteTopic: (id: string) =>
    apiClient.delete(`/courses/topics/${id}`),

  getTopicDetails: (id: string) =>
    apiClient.get<never, Topic>(`/courses/topics/${id}`),

  getActivitiesByTopic: (topicId: string) =>
    apiClient.get<never, Activity[]>(`/courses/topics/${topicId}/activities`),

  // === ACTIVITY ===
  createActivity: (data: { type: ActivityType; topicId: string; payload: any; orderIndex?: number }) =>
    apiClient.post<never, Activity>('/courses/activities', data),

  updateActivity: (id: string, data: { payload?: any; orderIndex?: number }) =>
    apiClient.put<never, Activity>(`/courses/activities/${id}`, data),

  deleteActivity: (id: string) =>
    apiClient.delete(`/courses/activities/${id}`),

  // === STORAGE / S3 ===
  getPresignedUrl: (folder: string, fileName: string, contentType: string) =>
    apiClient.post<never, { presignedUrl: string; fileKey: string; publicUrl: string }>(
      '/storage/presigned-url',
      { folder, fileName, contentType },
    ),

  // uploadFileToS3: async (file: File, folder: string = 'categories') => {
  //   const { presignedUrl, fileKey, publicUrl } = await coursesApi.getPresignedUrl(
  //     folder,
  //     file.name,
  //     file.type,
  //   );
  //   const response = await fetch(presignedUrl, {
  //     method: 'PUT',
  //     body: file,
  //     headers: { 'Content-Type': file.type },
  //   });
  //   if (!response.ok) throw new Error('Upload to S3 failed');
  //   return { fileKey, publicUrl };
  // },
  uploadFileToS3: async (file: File, folder: string = 'categories') => {
    const { presignedUrl, fileKey, publicUrl } = await coursesApi.getPresignedUrl(
      folder,
      file.name,
      file.type,
    );
    const response = await fetch(presignedUrl, {
      method: 'PUT',
      body: file,
      headers: { 'Content-Type': file.type },
    });
    console.log('PUT response status:', response.status);
    if (!response.ok) throw new Error('Upload to S3 failed');
    return { fileKey, publicUrl };
  },
};
