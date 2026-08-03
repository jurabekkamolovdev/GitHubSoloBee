import { v4 as uuidv4 } from 'uuid';
import { Activity } from './activity.model';

interface TopicParams {
  id: string;
  subCategoryId: string;
  thumbnailKey?: string | null;
  orderIndex: number;
  activities?: Activity[];
  createdAt?: Date;
  updatedAt?: Date;
}

export class Topic {
  private id: string;
  private subCategoryId: string;
  private thumbnailKey: string | null;
  private orderIndex: number;
  private activities: Activity[];
  private createdAt: Date;
  private updatedAt: Date;

  constructor(params: TopicParams) {
    this.id = params.id;
    this.subCategoryId = params.subCategoryId;
    this.thumbnailKey = params.thumbnailKey ?? null;
    this.orderIndex = params.orderIndex;
    this.activities = params.activities ?? [];
    this.createdAt = params.createdAt ?? new Date();
    this.updatedAt = params.updatedAt ?? new Date();
  }

  static create(
    params: Omit<TopicParams, 'id' | 'createdAt' | 'updatedAt'>,
  ): Topic {
    return new Topic({ ...params, id: uuidv4() });
  }

  updateThumbnail(newKey: string | null): string | null {
    const orphan = this.thumbnailKey !== newKey ? this.thumbnailKey : null;
    this.thumbnailKey = newKey;
    this.updatedAt = new Date();
    return orphan;
  }

  setOrderIndex(index: number): void {
    this.orderIndex = index;
    this.updatedAt = new Date();
  }

  getAllStorageKeys(): string[] {
    const keys: string[] = [];
    if (this.thumbnailKey) keys.push(this.thumbnailKey);
    for (const a of this.activities) keys.push(...a.getStorageKeys());
    return keys;
  }

  getId(): string {
    return this.id;
  }
  getSubCategoryId(): string {
    return this.subCategoryId;
  }
  getThumbnailKey(): string | null {
    return this.thumbnailKey;
  }
  getOrderIndex(): number {
    return this.orderIndex;
  }
  getActivities(): Activity[] {
    return this.activities;
  }
  getCreatedAt(): Date {
    return this.createdAt;
  }
  getUpdatedAt(): Date {
    return this.updatedAt;
  }
}
