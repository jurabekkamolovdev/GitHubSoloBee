import { Injectable } from '@nestjs/common';
import { SubCategoryEntity } from '../model/sub-category.entity';
import { SubCategory } from '../../../domain/courses/model/sub-category.model';
import { TopicDataMapper } from './topic.data.mapper';

@Injectable()
export class SubCategoryDataMapper {
  constructor(private readonly topicMapper: TopicDataMapper) {}

  toDomain(e: SubCategoryEntity): SubCategory {
    return new SubCategory({
      id: e.id,
      categoryId: e.categoryId,
      name: e.name,
      thumbnailKey: e.thumbnailKey ?? null,
      orderIndex: e.orderIndex,
      topics: e.topics?.map((t) => this.topicMapper.toDomain(t)) ?? [],
      createdAt: e.createdAt,
      updatedAt: e.updatedAt,
    });
  }

  toEntity(s: SubCategory): SubCategoryEntity {
    const e = new SubCategoryEntity();
    e.id = s.getId();
    e.categoryId = s.getCategoryId();
    e.name = s.getName();
    e.thumbnailKey = s.getThumbnailKey() ?? null;
    e.orderIndex = s.getOrderIndex();
    e.createdAt = s.getCreatedAt();
    e.updatedAt = s.getUpdatedAt();
    return e;
  }
}
