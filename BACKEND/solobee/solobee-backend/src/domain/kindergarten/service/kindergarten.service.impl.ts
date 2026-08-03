import {
  Injectable,
  Inject,
  ConflictException,
  NotFoundException,
} from '@nestjs/common';
import { Kindergarten } from '../model/kindergarten.model';
import { User } from 'src/domain/user/model/user.model';
import {
  type IKindergartenService,
  ICreateKindergarten,
} from './kindergarten.service.interface';
import {
  KINDERGARTEN_REPOSITORY,
  type IKindergartenRepository,
} from 'src/datasource/kindergarten/repository/kindergarten.repository.interface';
import {
  USER_SERVICE,
  type IUserService,
} from 'src/domain/user/service/user.service.interface';
import { Role } from 'src/core/utils/role.enum';

@Injectable()
export class KindergartenServiceImpl implements IKindergartenService {
  constructor(
    @Inject(KINDERGARTEN_REPOSITORY)
    private readonly kindergartenRepository: IKindergartenRepository,
    @Inject(USER_SERVICE)
    private readonly userService: IUserService,
  ) {}

  async create(params: ICreateKindergarten): Promise<Kindergarten> {
    const existingUser: User | null = await this.userService.findByUsername(
      params.adminUsername,
    );
    if (existingUser) {
      throw new ConflictException(
        `Admin username "${params.adminUsername}" already exists`,
      );
    }

    const kindergarten: Kindergarten = Kindergarten.create(
      params.name,
      params.address,
    );

    const savedKindergarten: Kindergarten =
      await this.kindergartenRepository.save(kindergarten);

    await this.userService.create({
      username: params.adminUsername,
      role: Role.KINDERGARTEN_ADMIN,
      kindergartenId: savedKindergarten.getId(),
      password: params.adminPassword,
    });

    return savedKindergarten;
  }

  async findAll(): Promise<Kindergarten[]> {
    return this.kindergartenRepository.findAll();
  }

  async delete(id: string): Promise<boolean> {
    const kindergarten: Kindergarten | null =
      await this.kindergartenRepository.findById(id);

    if (!kindergarten) {
      throw new NotFoundException('Kindergarten not found');
    }

    await this.userService.deleteByKindergartenId(id);

    return this.kindergartenRepository.delete(id);
  }
}
