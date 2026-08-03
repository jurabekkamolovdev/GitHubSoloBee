import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { ActivityEntity } from '../../model/activity.entity';
import { IActivityRepository } from '../interface/activity.repository.interface';
import { Activity } from 'src/domain/courses/model/activity.model';
import { ActivityDataMapper } from '../../mapper/activity.data.mapper';

@Injectable()
export class ActivityRepositoryImpl implements IActivityRepository {
  constructor(
    @InjectRepository(ActivityEntity)
    private readonly repo: Repository<ActivityEntity>,
    private readonly mapper: ActivityDataMapper,
  ) {}

  async save(activity: Activity): Promise<Activity> {
    const saved = await this.repo.save(this.mapper.toEntity(activity));
    return this.mapper.toDomain(saved);
  }

  async findById(id: string): Promise<Activity | null> {
    const e = await this.repo.findOne({ where: { id } });
    return e ? this.mapper.toDomain(e) : null;
  }

  async findByTopicId(topicId: string): Promise<Activity[]> {
    const rows = await this.repo.find({
      where: { topicId },
      order: { orderIndex: 'ASC' },
    });
    return rows.map((e) => this.mapper.toDomain(e));
  }

  async delete(id: string): Promise<void> {
    await this.repo.delete(id);
  }

  async findIdsByTopicId(topicId: string): Promise<string[]> {
    const entities = await this.repo.find({
      where: { topicId },
      select: ['id'],
    });
    return entities.map((e) => e.id);
  }
}
