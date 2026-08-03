import { Injectable } from '@nestjs/common';
import { User } from 'src/domain/user/model/user.model';
import { UserEntity } from '../model/user.entity';
import { Role } from 'src/core/utils/role.enum';

@Injectable()
export class UserDataMapper {
  toDomain(entity: UserEntity): User {
    return new User({
      id: entity.id,
      username: entity.username,
      passwordHash: entity.passwordHash,
      role: entity.role,
      kindergartenId: entity.kindergartenId,
      isActive: entity.isActive,
      refreshTokenHash: entity.refreshTokenHash,
      tokenVersion: entity.tokenVersion,
      createdAt: entity.createdAt,
      updatedAt: entity.updatedAt,
    });
  }

  toEntity(domain: User): UserEntity {
    const entity = new UserEntity();
    entity.id = domain.getId();
    entity.username = domain.getUsername();
    entity.passwordHash = domain.getPasswordHash();
    entity.role = domain.getRole();
    entity.kindergartenId = domain.getKindergartenId();
    entity.isActive = domain.getIsActive();
    entity.refreshTokenHash = domain.getRefreshTokenHash();
    entity.tokenVersion = domain.getTokenVersion();
    entity.createdAt = domain.getCreatedAt();
    entity.updatedAt = domain.getUpdatedAt();
    return entity;
  }

  toPartialEntity(
    data: Partial<{
      id: string;
      username: string;
      passwordHash: string;
      role: Role;
      kindergartenId: string | null;
      isActive: boolean;
      refreshTokenHash: string | null;
      tokenVersion: number;
    }>,
  ): Partial<UserEntity> {
    const partial: Partial<UserEntity> = {};
    if (data.id !== undefined) partial.id = data.id;
    if (data.username !== undefined) partial.username = data.username;
    if (data.passwordHash !== undefined)
      partial.passwordHash = data.passwordHash;
    if (data.role !== undefined) partial.role = data.role;
    if (data.kindergartenId !== undefined)
      partial.kindergartenId = data.kindergartenId;
    if (data.isActive !== undefined) partial.isActive = data.isActive;
    if (data.refreshTokenHash !== undefined)
      partial.refreshTokenHash = data.refreshTokenHash;
    if (data.tokenVersion !== undefined)
      partial.tokenVersion = data.tokenVersion;
    return partial;
  }
}
