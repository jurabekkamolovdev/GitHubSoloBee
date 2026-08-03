// domain/avatar/repository/avatar.repository.interface.ts
import { Avatar } from 'src/domain/avatar/model/avatar.model';

export interface IAvatarRepository {
  save(avatar: Avatar): Promise<Avatar>;
  findById(id: string): Promise<Avatar | null>;
  findAll(): Promise<Avatar[]>;
  delete(id: string): Promise<void>;
}

export const AVATAR_REPOSITORY = Symbol('AVATAR_REPOSITORY');
