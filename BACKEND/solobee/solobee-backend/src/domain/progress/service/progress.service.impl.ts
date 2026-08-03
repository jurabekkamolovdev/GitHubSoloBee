import { Injectable, Inject, NotFoundException } from '@nestjs/common';
import { EventEmitter2 } from '@nestjs/event-emitter';
import {
  type IProgressService,
  ActivityAttemptResult,
  ActivityProgressSnapshot,
  IDailyCompletion,
  IWeeklyStatistics,
} from './progress.service.interface';
import {
  ActivityProgress,
  ATTEMPT_THRESHOLDS,
} from '../model/activity-progress.model';
import { ProgressStatus } from '../model/topic-progress.model';
import {
  ACTIVITY_PROGRESS_REPOSITORY,
  type IActivityProgressRepository,
} from 'src/datasource/progress/repository/activity-progress.repository.interface';
import {
  TOPIC_PROGRESS_REPOSITORY,
  type ITopicProgressRepository,
} from 'src/datasource/progress/repository/topic-progress.repository.interface';

import {
  ACTIVITY_REPOSITORY,
  type IActivityRepository,
} from 'src/datasource/courses/repository/interface/activity.repository.interface';
import {
  ActivityType,
  WritingPayload,
} from 'src/domain/courses/model/activity.model';

@Injectable()
export class ProgressServiceImpl implements IProgressService {
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
    @Inject(ACTIVITY_PROGRESS_REPOSITORY)
    private readonly activityProgressRepository: IActivityProgressRepository,
    @Inject(TOPIC_PROGRESS_REPOSITORY)
    private readonly topicProgressRepository: ITopicProgressRepository,
    @Inject(ACTIVITY_REPOSITORY)
    private readonly activityRefRepository: IActivityRepository,
    private readonly eventEmitter: EventEmitter2,
  ) {}

  async reportActivityAttempt(
    userId: string,
    activityId: string,
    result?: string,
  ): Promise<ActivityAttemptResult> {
    const activity = await this.activityRefRepository.findById(activityId);
    if (!activity) throw new NotFoundException('Activity not found');

    const type = activity.getType();
    const threshold = ATTEMPT_THRESHOLDS[activity.getType()] ?? 1;

    if (type === ActivityType.WRITING) {
      const payload = activity.getPayload() as WritingPayload;
      if (payload.mode === 'spell') {
        const isCorrect =
          result !== undefined &&
          payload.answer.trim().toLowerCase() === result.trim().toLowerCase();

        if (!isCorrect) {
          return {
            attemptCount: 0,
            threshold,
            completed: false,
          };
        }
      }
    }

    let progress =
      await this.activityProgressRepository.findByStudentAndActivity(
        userId,
        activityId,
      );

    if (!progress) {
      progress = ActivityProgress.create(userId, activityId);
    }

    const justCompleted = progress.recordAttempt(threshold);

    await this.activityProgressRepository.save(progress);

    if (justCompleted) {
      this.eventEmitter.emit('activity.completed', {
        studentUserId: userId,
        activityId,
        topicId: activity.getTopicId(),
      });
    }

    return {
      attemptCount: progress.getAttemptCount(),
      threshold,
      completed: progress.getIsCompleted(),
    };
  }

  async getActivityProgressByTopic(
    userId: string,
    topicId: string,
  ): Promise<Map<string, Omit<ActivityProgressSnapshot, 'activityId'>>> {
    const snapshots =
      await this.activityProgressRepository.findSnapshotsByStudentAndTopic(
        userId,
        topicId,
      );
    const map = new Map<string, Omit<ActivityProgressSnapshot, 'activityId'>>();
    for (const s of snapshots) {
      map.set(s.activityId, {
        attemptCount: s.attemptCount,
        isCompleted: s.isCompleted,
      });
    }
    return map;
  }

  async getCompletedTopicIds(
    userId: string,
    topicIds: string[],
  ): Promise<Set<string>> {
    if (topicIds.length === 0) return new Set();
    const ids = await this.topicProgressRepository.findCompletedTopicIds(
      userId,
      topicIds,
    );
    return new Set(ids);
  }

  async getCompletedActivitiesCountByDate(
    userId: string,
    date: Date,
  ): Promise<number> {
    return this.activityProgressRepository.countCompletedByStudentAndDate(
      userId,
      date,
    );
  }

  async getTopicStatus(
    userId: string,
    topicId: string,
  ): Promise<ProgressStatus> {
    const status =
      await this.topicProgressRepository.findStatusByStudentAndTopic(
        userId,
        topicId,
      );
    return status ?? ProgressStatus.LOCKED;
  }

  async getWeeklyStatistics(
    userId: string,
    referenceDate: Date = new Date(),
  ): Promise<IWeeklyStatistics> {
    const { weekStart, weekEnd } = this.getWeekRange(referenceDate);

    const grouped =
      await this.activityProgressRepository.countCompletedGroupedByDate(
        userId,
        weekStart,
        weekEnd,
      );

    const countByDate = new Map(grouped.map((g) => [g.date, g.count]));

    const days: IDailyCompletion[] = [];
    for (let i = 0; i < 7; i++) {
      const current = new Date(weekStart);
      current.setDate(weekStart.getDate() + i);
      const dateKey = current.toISOString().slice(0, 10);

      days.push({
        date: dateKey,
        dayOfWeek: this.dayNames[i],
        completed: countByDate.get(dateKey) ?? 0,
      });
    }

    return {
      weekStart: weekStart.toISOString().slice(0, 10),
      weekEnd: weekEnd.toISOString().slice(0, 10),
      days,
    };
  }

  private getWeekRange(referenceDate: Date): {
    weekStart: Date;
    weekEnd: Date;
  } {
    const date = new Date(referenceDate);
    const day = date.getDay();
    const diffToMonday = day === 0 ? -6 : 1 - day;

    const weekStart = new Date(date);
    weekStart.setDate(date.getDate() + diffToMonday);
    weekStart.setHours(0, 0, 0, 0);

    const weekEnd = new Date(weekStart);
    weekEnd.setDate(weekStart.getDate() + 6);
    weekEnd.setHours(23, 59, 59, 999);

    return { weekStart, weekEnd };
  }
}
