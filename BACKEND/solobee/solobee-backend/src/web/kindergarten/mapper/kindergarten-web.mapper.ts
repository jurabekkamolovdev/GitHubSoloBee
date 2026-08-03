import { Injectable } from '@nestjs/common';
import { CreateKindergartenDto } from '../model/request/create-kindergarten.dto';
import { ICreateKindergarten } from 'src/domain/kindergarten/service/kindergarten.service.interface';
import { Kindergarten } from 'src/domain/kindergarten/model/kindergarten.model';
import {
  KindergartenObjectResponseDto,
  KindergartenResponseDto,
} from '../model/response/kindergarten-response.dto';

@Injectable()
export class KindergartenWebMapper {
  toCreateParams(dto: CreateKindergartenDto): ICreateKindergarten {
    return {
      name: dto.name,
      adminUsername: dto.adminUsername,
      adminPassword: dto.adminPassword,
      address: dto.address,
    };
  }

  toObjectResponse(kindergarten: Kindergarten): KindergartenObjectResponseDto {
    const response = new KindergartenObjectResponseDto();
    response.data = kindergarten as any;
    return response;
  }

  toResponseDto(kindergarten: Kindergarten): KindergartenResponseDto {
    const dto = new KindergartenResponseDto();
    dto.id = kindergarten.getId();
    dto.name = kindergarten.getName();
    dto.address = kindergarten.getAddress();
    dto.createdAt = kindergarten.getCreatedAt();
    return dto;
  }
}
