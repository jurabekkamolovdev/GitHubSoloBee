import { Injectable } from '@nestjs/common';
import { ActivityProgress } from 'src/domain/progress/model/activity-progress.model';
import { ActivityProgressEntity } from '../model/activity-progress.entity';

@Injectable()
export class ActivityProgressMapper {
  toDomain(entity: ActivityProgressEntity): ActivityProgress {
    return new ActivityProgress({
      id: entity.id,
      studentUserId: entity.studentUserId,
      activityId: entity.activityId,
      attemptCount: entity.attemptCount,
      isCompleted: entity.isCompleted,
    });
  }

  toEntity(domain: ActivityProgress): ActivityProgressEntity {
    const entity = new ActivityProgressEntity();
    entity.id = domain.getId();
    entity.studentUserId = domain.getStudentUserId();
    entity.activityId = domain.getActivityId();
    entity.attemptCount = domain.getAttemptCount();
    entity.isCompleted = domain.getIsCompleted();
    return entity;
  }
}
