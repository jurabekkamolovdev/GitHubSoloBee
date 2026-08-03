import { Injectable } from '@nestjs/common';
import { Kindergarten } from 'src/domain/kindergarten/model/kindergarten.model';
import { KindergartenEntity } from '../model/kindergarten.entity';

@Injectable()
export class KindergartenDataMapper {
  toDomain(entity: KindergartenEntity): Kindergarten {
    return new Kindergarten(
      entity.id,
      entity.name,
      entity.address,
      entity.isActive,
      entity.createdAt,
      entity.updatedAt,
    );
  }

  toEntity(domain: Kindergarten): KindergartenEntity {
    const entity = new KindergartenEntity();
    entity.id = domain.getId();
    entity.name = domain.getName();
    entity.address = domain.getAddress();
    entity.isActive = domain.getIsActive();
    entity.createdAt = domain.getCreatedAt();
    entity.updatedAt = domain.getUpdatedAt();
    return entity;
  }
}
