import { Injectable } from '@nestjs/common';
import { CategoryEntity } from '../model/category.entity';
import { Category } from '../../../domain/courses/model/category.model';
import { SubCategoryDataMapper } from './sub-category.data.mapper';

@Injectable()
export class CategoryDataMapper {
  constructor(private readonly subCategoryMapper: SubCategoryDataMapper) {}

  toDomain(e: CategoryEntity): Category {
    return new Category({
      id: e.id,
      name: e.name,
      backgroundColor: e.backgroundColor ?? null,
      foregroundColor: e.foregroundColor ?? null,
      orderIndex: e.orderIndex,
      subCategories:
        e.subCategories?.map((s) => this.subCategoryMapper.toDomain(s)) ?? [],
      createdAt: e.createdAt,
      updatedAt: e.updatedAt,
    });
  }

  toEntity(c: Category): CategoryEntity {
    const e = new CategoryEntity();
    e.id = c.getId();
    e.name = c.getName();
    e.backgroundColor = c.getBackgroundColor() ?? null;
    e.foregroundColor = c.getForegroundColor() ?? null;
    e.orderIndex = c.getOrderIndex();
    e.createdAt = c.getCreatedAt();
    e.updatedAt = c.getUpdatedAt();
    return e;
  }
}
