import { Injectable } from '@nestjs/common';
import { TopicProgress } from 'src/domain/progress/model/topic-progress.model';
import { TopicProgressEntity } from '../model/topic-progress.entity';

@Injectable()
export class TopicProgressMapper {
  toDomain(entity: TopicProgressEntity): TopicProgress {
    return new TopicProgress({
      id: entity.id,
      studentUserId: entity.studentUserId,
      topicId: entity.topicId,
      status: entity.status,
      starsEarned: entity.starsEarned,
    });
  }

  toEntity(domain: TopicProgress): TopicProgressEntity {
    const entity = new TopicProgressEntity();
    entity.id = domain.getId();
    entity.studentUserId = domain.getStudentUserId();
    entity.topicId = domain.getTopicId();
    entity.status = domain.getStatus();
    entity.starsEarned = domain.getStarsEarned();
    return entity;
  }
}
