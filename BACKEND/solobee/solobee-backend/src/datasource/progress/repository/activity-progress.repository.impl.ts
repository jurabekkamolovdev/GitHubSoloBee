import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository, In, Between } from 'typeorm';
import { type IActivityProgressRepository } from './activity-progress.repository.interface';
import { ActivityProgress } from 'src/domain/progress/model/activity-progress.model';
import { ActivityProgressSnapshot } from 'src/domain/progress/service/progress.service.interface';
import { ActivityProgressEntity } from '../model/activity-progress.entity';
import { ActivityProgressMapper } from '../mapper/activity-progress.mapper';

@Injectable()
export class ActivityProgressRepositoryImpl implements IActivityProgressRepository {
  private readonly dayNames = [
    'Dushanba',
    'Seshanba',
    'Chorshanba',
    'Payshanba',
    'Juma',
    'Shanba',
    'Yakshanba',
  ];
  constructor(
    @InjectRepository(ActivityProgressEntity)
    private readonly repo: Repository<ActivityProgressEntity>,
    private readonly mapper: ActivityProgressMapper,
  ) {}

  async save(progress: ActivityProgress): Promise<ActivityProgress> {
    const entity = this.mapper.toEntity(progress);
    const saved = await this.repo.save(entity);
    return this.mapper.toDomain(saved);
  }

  async findByStudentAndActivity(
    studentUserId: string,
    activityId: string,
  ): Promise<ActivityProgress | null> {
    const entity = await this.repo.findOne({
      where: { studentUserId, activityId },
    });
    return entity ? this.mapper.toDomain(entity) : null;
  }

  async countCompletedByStudentInActivities(
    studentUserId: string,
    activityIds: string[],
  ): Promise<number> {
    return this.repo.count({
      where: { studentUserId, activityId: In(activityIds), isCompleted: true },
    });
  }

  async countCompletedByStudentAndDate(
    studentUserId: string,
    date: Date,
  ): Promise<number> {
    const startOfDay = new Date(date);
    startOfDay.setHours(0, 0, 0, 0);

    const endOfDay = new Date(date);
    endOfDay.setHours(23, 59, 59, 999);

    return this.repo.count({
      where: {
        studentUserId,
        isCompleted: true,
        updatedAt: Between(startOfDay, endOfDay),
      },
    });
  }

  async countCompletedGroupedByDate(
    studentUserId: string,
    startDate: Date,
    endDate: Date,
  ): Promise<{ date: string; count: number }[]> {
    const raw = await this.repo
      .createQueryBuilder('ap')
      .select("TO_CHAR(ap.updatedAt, 'YYYY-MM-DD')", 'date')
      .addSelect('COUNT(*)', 'count')
      .where('ap.studentUserId = :studentUserId', { studentUserId })
      .andWhere('ap.isCompleted = true')
      .andWhere('ap.updatedAt BETWEEN :start AND :end', {
        start: startDate,
        end: endDate,
      })
      .groupBy('date')
      .getRawMany();

    return raw.map((r) => ({ date: r.date, count: Number(r.count) }));
  }

  async findSnapshotsByStudentAndTopic(
    studentUserId: string,
    topicId: string,
  ): Promise<ActivityProgressSnapshot[]> {
    const rows = await this.repo
      .createQueryBuilder('ap')
      .innerJoin('ap.activity', 'a')
      .where('ap.studentUserId = :studentUserId', { studentUserId })
      .andWhere('a.topicId = :topicId', { topicId })
      .select(['ap.activityId', 'ap.attemptCount', 'ap.isCompleted'])
      .getRawMany<{
        ap_activityId: string;
        ap_attemptCount: number;
        ap_isCompleted: boolean;
      }>();

    return rows.map((r) => ({
      activityId: r.ap_activityId,
      attemptCount: Number(r.ap_attemptCount),
      isCompleted: !!r.ap_isCompleted,
    }));
  }
}
