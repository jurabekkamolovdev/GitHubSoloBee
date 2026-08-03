import { ProgressStatus } from '../model/topic-progress.model';

export interface ActivityAttemptResult {
  attemptCount: number;
  threshold: number;
  completed: boolean;
}

export interface ActivityProgressSnapshot {
  activityId: string;
  attemptCount: number;
  isCompleted: boolean;
}

export interface IProgressService {
  reportActivityAttempt(
    userId: string,
    activityId: string,
    result?: string,
  ): Promise<ActivityAttemptResult>;

  getActivityProgressByTopic(
    userId: string,
    topicId: string,
  ): Promise<Map<string, Omit<ActivityProgressSnapshot, 'activityId'>>>;

  getCompletedTopicIds(
    userId: string,
    topicIds: string[],
  ): Promise<Set<string>>;

  getCompletedActivitiesCountByDate(
    userId: string,
    date: Date,
  ): Promise<number>;

  getTopicStatus(userId: string, topicId: string): Promise<ProgressStatus>;

  getWeeklyStatistics(
    userId: string,
    referenceDate: Date,
  ): Promise<IWeeklyStatistics>;
}

export interface IDailyCompletion {
  date: string;
  dayOfWeek: string;
  completed: number;
}

export interface IWeeklyStatistics {
  weekStart: string;
  weekEnd: string;
  days: IDailyCompletion[];
}

export const PROGRESS_SERVICE = Symbol('PROGRESS_SERVICE');
