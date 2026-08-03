// domain/avatar/mapper/avatar-data.mapper.ts
import { Injectable } from '@nestjs/common';
import { Avatar } from 'src/domain/avatar/model/avatar.model';
import { AvatarEntity } from '../model/avatar.entity';

@Injectable()
export class AvatarDataMapper {
  toEntity(avatar: Avatar): AvatarEntity {
    const entity = new AvatarEntity();
    entity.id = avatar.getId();
    entity.gender = avatar.getGender();
    entity.thumbnailKey = avatar.getThumbnailKey();
    entity.orderIndex = avatar.getOrderIndex();
    return entity;
  }

  toDomain(entity: AvatarEntity): Avatar {
    return new Avatar({
      id: entity.id,
      gender: entity.gender,
      thumbnailKey: entity.thumbnailKey,
      orderIndex: entity.orderIndex,
      createdAt: entity.createdAt,
      updatedAt: entity.updatedAt,
    });
  }
}
