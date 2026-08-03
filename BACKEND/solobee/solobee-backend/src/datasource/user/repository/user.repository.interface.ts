import { Role } from 'src/core/utils/role.enum';
import { User } from 'src/domain/user/model/user.model';
import { IUpdateUser } from 'src/domain/user/service/user.service.interface';

export interface IUserRepository {
  save(user: User): Promise<User>;
  findById(id: string): Promise<User | null>;
  findByUsername(username: string): Promise<User | null>;
  findByKindergartenIdAndRole(
    kindergartenId: string,
    role: Role,
  ): Promise<User[]>;
  update(id: string, user: IUpdateUser): Promise<User | null>;
  delete(id: string): Promise<boolean>;
  deleteByKindergartenId(kindergartenId: string): Promise<boolean>;
}

export const USER_REPOSITORY = Symbol('USER_REPOSITORY');
