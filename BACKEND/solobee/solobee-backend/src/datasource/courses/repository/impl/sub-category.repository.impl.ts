import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { SubCategoryEntity } from '../../model/sub-category.entity';
import { ISubCategoryRepository } from '../interface/sub-category.repository.interface';
import { SubCategory } from 'src/domain/courses/model/sub-category.model';
import { SubCategoryDataMapper } from '../../mapper/sub-category.data.mapper';

@Injectable()
export class SubCategoryRepositoryImpl implements ISubCategoryRepository {
  constructor(
    @InjectRepository(SubCategoryEntity)
    private readonly repo: Repository<SubCategoryEntity>,
    private readonly mapper: SubCategoryDataMapper,
  ) {}

  async save(sub: SubCategory): Promise<SubCategory> {
    const saved = await this.repo.save(this.mapper.toEntity(sub));
    return this.mapper.toDomain(saved);
  }

  async findById(
    id: string,
    withTopicsAndActivities = false,
  ): Promise<SubCategory | null> {
    const relations = withTopicsAndActivities
      ? ['topics', 'topics.activities']
      : [];
    const e = await this.repo.findOne({ where: { id }, relations });
    return e ? this.mapper.toDomain(e) : null;
  }

  async findByCategoryId(categoryId: string): Promise<SubCategory[]> {
    const rows = await this.repo.find({
      where: { categoryId },
      order: { orderIndex: 'ASC' },
    });
    return rows.map((e) => this.mapper.toDomain(e));
  }

  async delete(id: string): Promise<void> {
    await this.repo.delete(id);
  }
}
