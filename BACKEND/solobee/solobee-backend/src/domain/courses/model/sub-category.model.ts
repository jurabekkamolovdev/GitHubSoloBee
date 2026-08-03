import { v4 as uuidv4 } from 'uuid';
import { Topic } from './topic.model';

interface SubCategoryParams {
  id: string;
  categoryId: string;
  name: string;
  thumbnailKey?: string | null;
  orderIndex: number;
  topics?: Topic[];
  createdAt?: Date;
  updatedAt?: Date;
}

export class SubCategory {
  private id: string;
  private categoryId: string;
  private name: string;
  private thumbnailKey: string | null;
  private orderIndex: number;
  private topics: Topic[];
  private createdAt: Date;
  private updatedAt: Date;

  constructor(params: SubCategoryParams) {
    this.id = params.id;
    this.categoryId = params.categoryId;
    this.name = params.name;
    this.thumbnailKey = params.thumbnailKey ?? null;
    this.orderIndex = params.orderIndex;
    this.topics = params.topics ?? [];
    this.createdAt = params.createdAt ?? new Date();
    this.updatedAt = params.updatedAt ?? new Date();
  }

  static create(
    params: Omit<SubCategoryParams, 'id' | 'createdAt' | 'updatedAt'>,
  ): SubCategory {
    return new SubCategory({ ...params, id: uuidv4() });
  }

  updateThumbnail(newKey: string | null): string | null {
    const orphan = this.thumbnailKey !== newKey ? this.thumbnailKey : null;
    this.thumbnailKey = newKey;
    this.updatedAt = new Date();
    return orphan;
  }

  updateName(name: string): void {
    this.name = name;
    this.updatedAt = new Date();
  }

  getAllStorageKeys(): string[] {
    const keys: string[] = [];
    if (this.thumbnailKey) keys.push(this.thumbnailKey);
    for (const t of this.topics) keys.push(...t.getAllStorageKeys());
    return keys;
  }

  getId(): string {
    return this.id;
  }
  getCategoryId(): string {
    return this.categoryId;
  }
  getName(): string {
    return this.name;
  }
  getThumbnailKey(): string | null {
    return this.thumbnailKey;
  }
  getOrderIndex(): number {
    return this.orderIndex;
  }
  getTopics(): Topic[] {
    return this.topics;
  }
  getCreatedAt(): Date {
    return this.createdAt;
  }
  getUpdatedAt(): Date {
    return this.updatedAt;
  }
}
