import { Injectable } from '@nestjs/common';
import { TopicEntity } from '../model/topic.entity';
import { Topic } from '../../../domain/courses/model/topic.model';
import { ActivityDataMapper } from './activity.data.mapper';

@Injectable()
export class TopicDataMapper {
  constructor(private readonly activityMapper: ActivityDataMapper) {}

  toDomain(e: TopicEntity): Topic {
    return new Topic({
      id: e.id,
      subCategoryId: e.subCategoryId,
      thumbnailKey: e.thumbnailKey ?? null,
      orderIndex: e.orderIndex,
      activities:
        e.activities?.map((a) => this.activityMapper.toDomain(a)) ?? [],
      createdAt: e.createdAt,
      updatedAt: e.updatedAt,
    });
  }

  toEntity(t: Topic): TopicEntity {
    const e = new TopicEntity();
    e.id = t.getId();
    e.subCategoryId = t.getSubCategoryId();
    e.thumbnailKey = t.getThumbnailKey() ?? null;
    e.orderIndex = t.getOrderIndex();
    e.createdAt = t.getCreatedAt();
    e.updatedAt = t.getUpdatedAt();
    return e;
  }
}
