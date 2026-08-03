import { Category } from 'src/domain/courses/model/category.model';

export interface ICategoryRepository {
  save(category: Category): Promise<Category>;
  findById(id: string, withAll?: boolean): Promise<Category | null>;
  findAll(withSubCategories?: boolean): Promise<Category[]>;
  getActivityCountByCategoryId(): Promise<Map<string, number>>;
  delete(id: string): Promise<void>;
}

export const CATEGORY_REPOSITORY = Symbol('CATEGORY_REPOSITORY');
