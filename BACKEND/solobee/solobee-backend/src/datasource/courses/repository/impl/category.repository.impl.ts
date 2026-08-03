import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { CategoryEntity } from '../../model/category.entity';
import { ActivityEntity } from '../../model/activity.entity';
import { ICategoryRepository } from '../interface/category.repository.interface';
import { Category } from 'src/domain/courses/model/category.model';
import { CategoryDataMapper } from '../../mapper/category.data.mapper';

@Injectable()
export class CategoryRepositoryImpl implements ICategoryRepository {
  constructor(
    @InjectRepository(CategoryEntity)
    private readonly repo: Repository<CategoryEntity>,
    @InjectRepository(ActivityEntity)
    private readonly activitiesRepo: Repository<ActivityEntity>,
    private readonly mapper: CategoryDataMapper,
  ) {}

  async save(category: Category): Promise<Category> {
    const saved = await this.repo.save(this.mapper.toEntity(category));
    return this.mapper.toDomain(saved);
  }

  async findById(id: string, withAll = false): Promise<Category | null> {
    const relations = withAll
      ? [
          'subCategories',
          'subCategories.topics',
          'subCategories.topics.activities',
        ]
      : ['subCategories'];
    const e = await this.repo.findOne({ where: { id }, relations });
    return e ? this.mapper.toDomain(e) : null;
  }

  async findAll(withSubCategories = false): Promise<Category[]> {
    const e = await this.repo.find({
      order: { orderIndex: 'ASC' },
      relations: withSubCategories ? ['subCategories'] : [],
    });
    return e.map((c) => this.mapper.toDomain(c));
  }

  async getActivityCountByCategoryId(): Promise<Map<string, number>> {
    const rows = await this.activitiesRepo
      .createQueryBuilder('a')
      .innerJoin('a.topic', 't')
      .innerJoin('t.subCategory', 'sc')
      .select('sc.categoryId', 'categoryId')
      .addSelect('COUNT(a.id)', 'count')
      .groupBy('sc.categoryId')
      .getRawMany<{ categoryId: string; count: string }>();

    return new Map(rows.map((r) => [r.categoryId, parseInt(r.count, 10)]));
  }

  async delete(id: string): Promise<void> {
    await this.repo.delete(id);
  }
}
