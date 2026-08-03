// domain/avatar/repository/avatar.repository.impl.ts
import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { type IAvatarRepository } from './avatar.repository.interface';
import { Avatar } from 'src/domain/avatar/model/avatar.model';
import { AvatarEntity } from '../model/avatar.entity';
import { AvatarDataMapper } from '../mapper/avatar.data.mapper';

@Injectable()
export class AvatarRepositoryImpl implements IAvatarRepository {
  constructor(
    @InjectRepository(AvatarEntity)
    private readonly avatars: Repository<AvatarEntity>,
    private readonly mapper: AvatarDataMapper,
  ) {}

  async save(avatar: Avatar): Promise<Avatar> {
    const entity = this.mapper.toEntity(avatar);
    const saved = await this.avatars.save(entity);
    return this.mapper.toDomain(saved);
  }

  async findById(id: string): Promise<Avatar | null> {
    const entity = await this.avatars.findOne({ where: { id } });
    if (!entity) return null;
    return this.mapper.toDomain(entity);
  }

  async findAll(): Promise<Avatar[]> {
    const entities = await this.avatars.find({
      order: { orderIndex: 'ASC' },
    });
    return entities.map((e) => this.mapper.toDomain(e));
  }

  async delete(id: string): Promise<void> {
    await this.avatars.delete(id);
  }
}
