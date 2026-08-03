import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { UserEntity } from '../model/user.entity';
import { type IUserRepository } from './user.repository.interface';
import { User } from 'src/domain/user/model/user.model';
import { UserDataMapper } from '../mapper/user-data.mapper';
import { Role } from 'src/core/utils/role.enum';
import { IUpdateUser } from 'src/domain/user/service/user.service.interface';

@Injectable()
export class UserRepositoryImpl implements IUserRepository {
  constructor(
    @InjectRepository(UserEntity)
    private readonly users: Repository<UserEntity>,
    private readonly mapper: UserDataMapper,
  ) {}

  async save(domainUser: User): Promise<User> {
    const entity = this.mapper.toEntity(domainUser);
    const saved = await this.users.save(entity);
    return this.mapper.toDomain(saved);
  }

  async findById(id: string): Promise<User | null> {
    const entity = await this.users.findOne({ where: { id } });
    if (!entity) return null;
    return this.mapper.toDomain(entity);
  }

  async findByUsername(username: string): Promise<User | null> {
    const entity = await this.users.findOne({ where: { username } });
    if (!entity) return null;
    return this.mapper.toDomain(entity);
  }

  async findByKindergartenIdAndRole(
    kindergartenId: string,
    role: Role,
  ): Promise<User[]> {
    const entities = await this.users.find({ where: { kindergartenId, role } });
    return entities.map((e) => this.mapper.toDomain(e));
  }

  async update(id: string, data: IUpdateUser): Promise<User | null> {
    const exists = await this.users.findOne({ where: { id } });
    if (!exists) return null;

    const partialEntity = this.mapper.toPartialEntity(data);
    await this.users.update({ id }, partialEntity);

    const updated = await this.users.findOne({ where: { id } });
    return updated ? this.mapper.toDomain(updated) : null;
  }

  async delete(id: string): Promise<boolean> {
    const result = await this.users.delete({ id });
    return (result.affected ?? 0) > 0;
  }

  async deleteByKindergartenId(kindergartenId: string): Promise<boolean> {
    const result = await this.users.delete({ kindergartenId });
    return (result.affected ?? 0) > 0;
  }
}
