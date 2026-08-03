import {
  Injectable,
  Inject,
  NotFoundException,
  ConflictException,
} from '@nestjs/common';
import {
  type IUserService,
  ICreateUser,
  IUpdateUser,
} from './user.service.interface';
import { User } from '../model/user.model';
import {
  USER_REPOSITORY,
  type IUserRepository,
} from 'src/datasource/user/repository/user.repository.interface';
import { Role } from 'src/core/utils/role.enum';

@Injectable()
export class UserServiceImpl implements IUserService {
  constructor(
    @Inject(USER_REPOSITORY)
    private readonly userRepository: IUserRepository,
  ) {}

  async create(params: ICreateUser): Promise<User> {
    const existing = await this.userRepository.findByUsername(params.username);
    if (existing) {
      throw new ConflictException(
        `Username "${params.username}" already taken`,
      );
    }
    const user = await User.create(params);
    return this.userRepository.save(user);
  }

  async findByUsername(username: string): Promise<User | null> {
    return this.userRepository.findByUsername(username);
  }

  async findById(id: string): Promise<User | null> {
    return this.userRepository.findById(id);
  }

  async findByKindergartenIdAndRole(
    kindergartenId: string,
    role: Role,
  ): Promise<User[]> {
    return this.userRepository.findByKindergartenIdAndRole(
      kindergartenId,
      role,
    );
  }

  async update(id: string, updateData: IUpdateUser): Promise<User | null> {
    const updated = await this.userRepository.update(id, updateData);
    if (!updated) {
      throw new NotFoundException(`User with id "${id}" not found`);
    }
    return updated;
  }

  async delete(id: string): Promise<boolean> {
    const deleted = await this.userRepository.delete(id);
    if (!deleted) {
      throw new NotFoundException(`User with id "${id}" not found`);
    }
    return deleted;
  }

  async deleteByKindergartenId(kindergartenId: string): Promise<boolean> {
    return this.userRepository.deleteByKindergartenId(kindergartenId);
  }
}
