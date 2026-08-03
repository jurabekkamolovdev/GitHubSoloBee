import { v4 as uuidv4 } from 'uuid';
import { SubCategory } from './sub-category.model';

interface CategoryParams {
  id: string;
  name: string;
  backgroundColor?: string | null;
  foregroundColor?: string | null;
  orderIndex: number;
  subCategories?: SubCategory[];
  createdAt?: Date;
  updatedAt?: Date;
}

export class Category {
  private id: string;
  private name: string;
  private backgroundColor: string | null;
  private foregroundColor: string | null;
  private orderIndex: number;
  private subCategories: SubCategory[];
  private createdAt: Date;
  private updatedAt: Date;

  constructor(params: CategoryParams) {
    this.id = params.id;
    this.name = params.name;
    this.backgroundColor = params.backgroundColor ?? null;
    this.foregroundColor = params.foregroundColor ?? null;
    this.orderIndex = params.orderIndex;
    this.subCategories = params.subCategories ?? [];
    this.createdAt = params.createdAt ?? new Date();
    this.updatedAt = params.updatedAt ?? new Date();
  }

  static create(params: ICreateCategory): Category {
    return new Category({ ...params, id: uuidv4() });
  }

  getAllStorageKeys(): string[] {
    return this.subCategories.flatMap((s) => s.getAllStorageKeys());
  }

  getId(): string {
    return this.id;
  }
  getName(): string {
    return this.name;
  }
  getBackgroundColor(): string | null {
    return this.backgroundColor;
  }
  getForegroundColor(): string | null {
    return this.foregroundColor;
  }
  getOrderIndex(): number {
    return this.orderIndex;
  }
  getSubCategories(): SubCategory[] {
    return this.subCategories;
  }
  getCreatedAt(): Date {
    return this.createdAt;
  }
  getUpdatedAt(): Date {
    return this.updatedAt;
  }
}

export interface ICreateCategory {
  name: string;
  backgroundColor?: string | null;
  foregroundColor?: string | null;
  orderIndex: number;
  subCategories?: SubCategory[];
}
