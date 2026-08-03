import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { In, Repository, MoreThan } from 'typeorm';
import { TopicEntity } from '../../model/topic.entity';
import {
  ITopicRepository,
  TopicRef,
} from '../interface/topic.repository.interface';
import { Topic } from 'src/domain/courses/model/topic.model';
import { TopicDataMapper } from '../../mapper/topic.data.mapper';

@Injectable()
export class TopicRepositoryImpl implements ITopicRepository {
  constructor(
    @InjectRepository(TopicEntity)
    private readonly repo: Repository<TopicEntity>,
    private readonly mapper: TopicDataMapper,
  ) {}

  async save(topic: Topic): Promise<Topic> {
    const saved = await this.repo.save(this.mapper.toEntity(topic));
    return this.mapper.toDomain(saved);
  }

  async findById(id: string, withActivities = false): Promise<Topic | null> {
    const e = await this.repo.findOne({
      where: { id },
      relations: withActivities ? ['activities'] : [],
    });
    return e ? this.mapper.toDomain(e) : null;
  }

  async findBySubCategoryId(subCategoryId: string): Promise<Topic[]> {
    const rows = await this.repo.find({
      where: { subCategoryId },
      order: { orderIndex: 'ASC' },
    });
    return rows.map((e) => this.mapper.toDomain(e));
  }

  async findByIds(ids: string[]): Promise<Topic[]> {
    const rows = await this.repo.find({ where: { id: In(ids) } });
    return rows.map((e) => this.mapper.toDomain(e));
  }

  async findConflict(
    subCategoryId: string,
    orderIndex: number,
  ): Promise<Topic | null> {
    const e = await this.repo.findOne({ where: { subCategoryId, orderIndex } });
    return e ? this.mapper.toDomain(e) : null;
  }

  async updateOrderIndex(id: string, orderIndex: number): Promise<void> {
    await this.repo.update(id, { orderIndex });
  }

  async delete(id: string): Promise<void> {
    await this.repo.delete(id);
  }
  async findNextInSubCategory(
    subCategoryId: string,
    afterOrderIndex: number,
  ): Promise<TopicRef | null> {
    const entity = await this.repo.findOne({
      where: { subCategoryId, orderIndex: MoreThan(afterOrderIndex) },
      order: { orderIndex: 'ASC' },
    });
    if (!entity) return null;
    return {
      id: entity.id,
      subCategoryId: entity.subCategoryId,
      orderIndex: entity.orderIndex,
    };
  }
}
