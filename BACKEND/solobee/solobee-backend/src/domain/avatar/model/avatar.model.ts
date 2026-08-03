import { v4 as uuidv4 } from 'uuid';
import { AvatarGender } from 'src/datasource/avatar/model/avatar.entity';

interface AvatarParams {
  id: string;
  gender: AvatarGender;
  thumbnailKey: string;
  orderIndex: number;
  createdAt?: Date;
  updatedAt?: Date;
}

export class Avatar {
  private id: string;
  private gender: AvatarGender;
  private thumbnailKey: string;
  private orderIndex: number;
  private createdAt: Date;
  private updatedAt: Date;

  constructor(params: AvatarParams) {
    this.id = params.id;
    this.gender = params.gender;
    this.thumbnailKey = params.thumbnailKey;
    this.orderIndex = params.orderIndex;
    this.createdAt = params.createdAt ?? new Date();
    this.updatedAt = params.updatedAt ?? new Date();
  }

  static create(
    params: Omit<AvatarParams, 'id' | 'createdAt' | 'updatedAt'>,
  ): Avatar {
    return new Avatar({ ...params, id: uuidv4() });
  }

  getId(): string {
    return this.id;
  }
  getGender(): AvatarGender {
    return this.gender;
  }
  getThumbnailKey(): string {
    return this.thumbnailKey;
  }
  getOrderIndex(): number {
    return this.orderIndex;
  }
  getCreatedAt(): Date {
    return this.createdAt;
  }
  getUpdatedAt(): Date {
    return this.updatedAt;
  }
}
