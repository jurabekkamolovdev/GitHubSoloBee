import { SubCategory } from 'src/domain/courses/model/sub-category.model';

export interface ISubCategoryRepository {
  save(sub: SubCategory): Promise<SubCategory>;
  findById(
    id: string,
    withTopicsAndActivities?: boolean,
  ): Promise<SubCategory | null>;
  findByCategoryId(categoryId: string): Promise<SubCategory[]>;
  delete(id: string): Promise<void>;
}

export const SUB_CATEGORY_REPOSITORY = Symbol('SUB_CATEGORY_REPOSITORY');
