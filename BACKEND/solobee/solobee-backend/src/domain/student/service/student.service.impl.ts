import { Injectable, Inject, BadRequestException } from '@nestjs/common';
import {
  type IStudentService,
  ICreateStudent,
  IStudentProfile,
  IStudentStatistics,
} from './student.service.interface';
import { Student } from '../model/student.model';
import {
  USER_SERVICE,
  type IUserService,
} from 'src/domain/user/service/user.service.interface';
import {
  STUDENT_REPOSITORY,
  type IStudentRepository,
} from 'src/datasource/student/repository/student.repository.interface';
import { Role } from 'src/core/utils/role.enum';
import { User } from 'src/domain/user/model/user.model';
import {
  AVATAR_SERVICE,
  type IAvatarService,
} from 'src/domain/avatar/service/avatar.service.interface';
import {
  STORAGE_SERVICE,
  type IStorageService,
} from 'src/infrastructure/storage/storage.interface';
import {
  PROGRESS_SERVICE,
  type IProgressService,
  IWeeklyStatistics,
} from 'src/domain/progress/service/progress.service.interface';

@Injectable()
export class StudentServiceImpl implements IStudentService {
  constructor(
    @Inject(USER_SERVICE)
    private readonly userService: IUserService,
    @Inject(STUDENT_REPOSITORY)
    private readonly studentRepository: IStudentRepository,
    @Inject(AVATAR_SERVICE)
    private readonly avatarService: IAvatarService,
    @Inject(STORAGE_SERVICE)
    private readonly storageService: IStorageService,
    @Inject(PROGRESS_SERVICE)
    private readonly progressService: IProgressService,
  ) {}

  async createStudent(params: ICreateStudent): Promise<boolean> {
    const user: User = await this.userService.create({
      username: params.userName,
      role: Role.STUDENT,
      password: params.password,
      kindergartenId: params.kindergartenId,
    });

    await this.avatarService.getById(params.avatarId);

    const student: Student = Student.create(
      user.getId(),
      user.getUsername(),
      params.firstName,
      params.lastName,
      params.age,
      params.avatarId,
      params.birthDate,
      params.address,
      params.parentPhone,
    );

    return this.studentRepository.save(student);
  }

  async findAll(
    offset: number,
    limit: number,
  ): Promise<{ items: Student[]; total: number }> {
    return this.studentRepository.findAll(offset, limit);
  }

  async deleteStudent(studentId: string): Promise<boolean> {
    // console.log(studentId);
    const student = await this.studentRepository.findById(studentId);
    // console.log(student);
    if (!student) throw new BadRequestException('Student topilmadi');

    return this.userService.delete(student.getUserId());
  }

  async getStudentProfile(studentId: string): Promise<IStudentProfile> {
    const student: Student | null =
      await this.studentRepository.findByUserId(studentId);

    if (!student) throw new BadRequestException('Student profile not found');

    const studentAvatar = await this.avatarService.getById(
      student.getAvatarId(),
    );

    return {
      firstName: student.getFirstName(),
      lastName: student.getLastName(),
      username: student.getUsername(),
      age: student.getAge(),
      avatar: this.storageService.getPublicUrl(studentAvatar.getThumbnailKey()),
    };
  }
  async getStudentStatistics(studentId: string): Promise<IStudentStatistics> {
    const completedToday =
      await this.progressService.getCompletedActivitiesCountByDate(
        studentId,
        new Date(),
      );

    return { completedToday };
  }

  async getWeeklyStatistics(studentId: string): Promise<IWeeklyStatistics> {
    return this.progressService.getWeeklyStatistics(studentId, new Date());
  }
}
