// domain/avatar/service/avatar.service.impl.ts
import { Injectable, Inject, NotFoundException } from '@nestjs/common';
import {
  type IAvatarService,
  ICreateAvatar,
  INewAvatar,
  IGroupedAvatars,
} from './avatar.service.interface';
import { Avatar } from '../model/avatar.model';
import { AvatarGender } from 'src/datasource/avatar/model/avatar.entity';
import {
  AVATAR_REPOSITORY,
  type IAvatarRepository,
} from 'src/datasource/avatar/repository/avatar.repository.interface';
import {
  STORAGE_SERVICE,
  type IStorageService,
} from 'src/infrastructure/storage/storage.interface';

@Injectable()
export class AvatarServiceImpl implements IAvatarService {
  constructor(
    @Inject(AVATAR_REPOSITORY)
    private readonly avatarRepository: IAvatarRepository,
    @Inject(STORAGE_SERVICE)
    private readonly storageService: IStorageService,
  ) {}

  async createAvatar(params: ICreateAvatar): Promise<INewAvatar> {
    const avatar: Avatar = Avatar.create({
      gender: params.gender,
      thumbnailKey: params.thumbnailKey,
      orderIndex: params.orderIndex ?? 0,
    });

    const saved: Avatar = await this.avatarRepository.save(avatar);
    return this.toResponse(saved);
  }

  async getAllAvatarsGrouped(): Promise<IGroupedAvatars> {
    const avatars: Avatar[] = await this.avatarRepository.findAll();
    const responses: INewAvatar[] = avatars.map((a) => this.toResponse(a));

    return {
      boy: responses.filter((a) => a.gender === AvatarGender.BOY),
      girl: responses.filter((a) => a.gender === AvatarGender.GIRL),
    };
  }

  async deleteAvatar(id: string): Promise<void> {
    const avatar: Avatar | null = await this.avatarRepository.findById(id);

    if (!avatar) {
      throw new NotFoundException(`Avatar with id "${id}" not found`);
    }

    await this.storageService.deleteFile(avatar.getThumbnailKey());
    await this.avatarRepository.delete(id);
  }

  async getById(id: string): Promise<Avatar> {
    const avatar: Avatar | null = await this.avatarRepository.findById(id);

    if (!avatar) {
      throw new NotFoundException(`Avatar with id "${id}" not found`);
    }

    return avatar;
  }

  private toResponse(avatar: Avatar): INewAvatar {
    return {
      id: avatar.getId(),
      gender: avatar.getGender(),
      thumbnailUrl: this.storageService.getPublicUrl(avatar.getThumbnailKey()),
      orderIndex: avatar.getOrderIndex(),
    };
  }
}
