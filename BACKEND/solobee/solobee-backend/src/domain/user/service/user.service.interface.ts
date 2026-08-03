import { Role } from 'src/core/utils/role.enum';
import { User } from '../model/user.model';

export interface IUserService {
  create(params: ICreateUser): Promise<User>;

  findByUsername(username: string): Promise<User | null>;

  findById(id: string): Promise<User | null>;

  findByKindergartenIdAndRole(
    kindergartenId: string,
    role: Role,
  ): Promise<User[]>;

  update(id: string, updateData: IUpdateUser): Promise<User | null>;

  delete(id: string): Promise<boolean>;

  deleteByKindergartenId(kindergartenId: string): Promise<boolean>;
}

export const USER_SERVICE = Symbol('USER_SERVICE');

export interface ICreateUser {
  username: string;
  role: Role;
  password?: string | null;
  kindergartenId?: string | null;
}

export interface IUpdateUser {
  username?: string;
  passwordHash?: string;
  role?: Role;
  kindergartenId?: string | null;
  isActive?: boolean;
  refreshTokenHash?: string | null;
  tokenVersion?: number;
}
