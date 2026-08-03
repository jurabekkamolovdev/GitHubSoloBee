import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { type IKindergartenRepository } from './kindergarten.repository.interface';
import { Kindergarten } from 'src/domain/kindergarten/model/kindergarten.model';
import { KindergartenEntity } from '../model/kindergarten.entity';
import { KindergartenDataMapper } from '../mapper/kindergarten-data.mapper';

@Injectable()
export class KindergartenRepositoryImpl implements IKindergartenRepository {
  constructor(
    @InjectRepository(KindergartenEntity)
    private readonly kindergartens: Repository<KindergartenEntity>,
    private readonly mapper: KindergartenDataMapper,
  ) {}

  async save(domainUser: Kindergarten): Promise<Kindergarten> {
    const entity = this.mapper.toEntity(domainUser);
    const saved = await this.kindergartens.save(entity);
    return this.mapper.toDomain(saved);
  }

  async findAll(): Promise<Kindergarten[]> {
    const entities: KindergartenEntity[] = await this.kindergartens.find();
    return entities.map((e) => this.mapper.toDomain(e));
  }

  async findById(id: string): Promise<Kindergarten | null> {
    const entity = await this.kindergartens.findOne({ where: { id } });
    if (!entity) return null;
    return this.mapper.toDomain(entity);
  }

  async delete(id: string): Promise<boolean> {
    const result = await this.kindergartens.delete({ id });
    return (result.affected ?? 0) > 0;
  }
}
