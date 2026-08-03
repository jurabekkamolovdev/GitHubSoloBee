import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository, In } from 'typeorm';
import { type ITopicProgressRepository } from './topic-progress.repository.interface';
import {
  TopicProgress,
  ProgressStatus,
} from 'src/domain/progress/model/topic-progress.model';
import { TopicProgressEntity } from '../model/topic-progress.entity';
import { TopicProgressMapper } from '../mapper/topic-progress.mapper';

@Injectable()
export class TopicProgressRepositoryImpl implements ITopicProgressRepository {
  constructor(
    @InjectRepository(TopicProgressEntity)
    private readonly repo: Repository<TopicProgressEntity>,
    private readonly mapper: TopicProgressMapper,
  ) {}

  async save(progress: TopicProgress): Promise<TopicProgress> {
    const entity = this.mapper.toEntity(progress);
    const saved = await this.repo.save(entity);
    return this.mapper.toDomain(saved);
  }

  async findByStudentAndTopic(
    studentUserId: string,
    topicId: string,
  ): Promise<TopicProgress | null> {
    const entity = await this.repo.findOne({
      where: { studentUserId, topicId },
    });
    return entity ? this.mapper.toDomain(entity) : null;
  }

  async findCompletedTopicIds(
    studentUserId: string,
    topicIds: string[],
  ): Promise<string[]> {
    const entities = await this.repo.find({
      where: {
        studentUserId,
        topicId: In(topicIds),
        status: ProgressStatus.COMPLETED,
      },
      select: ['topicId'],
    });
    return entities.map((e) => e.topicId);
  }

  async findStatusByStudentAndTopic(
    studentUserId: string,
    topicId: string,
  ): Promise<ProgressStatus | null> {
    const entity = await this.repo.findOne({
      where: { studentUserId, topicId },
      select: ['status'],
    });
    return entity?.status ?? null;
  }
}
