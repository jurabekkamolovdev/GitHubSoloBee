import { Injectable } from '@nestjs/common';
import { ActivityEntity } from '../model/activity.entity';
import { Activity } from '../../../domain/courses/model/activity.model';

@Injectable()
export class ActivityDataMapper {
  toDomain(e: ActivityEntity): Activity {
    return new Activity({
      id: e.id,
      topicId: e.topicId,
      type: e.type,
      orderIndex: e.orderIndex,
      payload: e.payload,
      createdAt: e.createdAt,
      updatedAt: e.updatedAt,
    });
  }

  toEntity(a: Activity): ActivityEntity {
    const e = new ActivityEntity();
    e.id = a.getId();
    e.topicId = a.getTopicId();
    e.type = a.getType();
    e.orderIndex = a.getOrderIndex();
    e.payload = a.getPayload();
    e.createdAt = a.getCreatedAt();
    e.updatedAt = a.getUpdatedAt();
    return e;
  }
}
