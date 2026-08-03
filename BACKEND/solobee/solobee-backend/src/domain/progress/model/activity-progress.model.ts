import { v4 as uuidv4 } from 'uuid';
import { ActivityType } from 'src/domain/courses/model/activity.model';

export const ATTEMPT_THRESHOLDS: Record<ActivityType, number> = {
  [ActivityType.LEARN]: 4,
  [ActivityType.WRITING]: 1,
  [ActivityType.WORDHUNT]: 1,
  [ActivityType.PICQUEST]: 1,
};

export class ActivityProgress {
  private id: string;
  private studentUserId: string;
  private activityId: string;
  private attemptCount: number;
  private isCompleted: boolean;

  constructor(params: {
    id: string;
    studentUserId: string;
    activityId: string;
    attemptCount?: number;
    isCompleted?: boolean;
  }) {
    this.id = params.id;
    this.studentUserId = params.studentUserId;
    this.activityId = params.activityId;
    this.attemptCount = params.attemptCount ?? 0;
    this.isCompleted = params.isCompleted ?? false;
  }

  static create(studentUserId: string, activityId: string): ActivityProgress {
    return new ActivityProgress({
      id: uuidv4(),
      studentUserId,
      activityId,
    });
  }

  recordAttempt(threshold: number): boolean {
    if (this.isCompleted) return false;
    this.attemptCount += 1;
    if (this.attemptCount >= threshold) {
      this.isCompleted = true;
      return true;
    }
    return false;
  }

  getId(): string {
    return this.id;
  }
  getStudentUserId(): string {
    return this.studentUserId;
  }
  getActivityId(): string {
    return this.activityId;
  }
  getAttemptCount(): number {
    return this.attemptCount;
  }
  getIsCompleted(): boolean {
    return this.isCompleted;
  }
}
