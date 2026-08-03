import { v4 as uuidv4 } from 'uuid';

export enum ProgressStatus {
  LOCKED = 'LOCKED',
  UNLOCKED = 'UNLOCKED',
  COMPLETED = 'COMPLETED',
}

export class TopicProgress {
  private id: string;
  private studentUserId: string;
  private topicId: string;
  private status: ProgressStatus;
  private starsEarned: number;

  constructor(params: {
    id: string;
    studentUserId: string;
    topicId: string;
    status?: ProgressStatus;
    starsEarned?: number;
  }) {
    this.id = params.id;
    this.studentUserId = params.studentUserId;
    this.topicId = params.topicId;
    this.status = params.status ?? ProgressStatus.LOCKED;
    this.starsEarned = params.starsEarned ?? 0;
  }

  static create(
    studentUserId: string,
    topicId: string,
    status?: ProgressStatus,
  ): TopicProgress {
    return new TopicProgress({ id: uuidv4(), studentUserId, topicId, status });
  }

  complete(): void {
    if (this.status === ProgressStatus.COMPLETED) return;
    this.status = ProgressStatus.COMPLETED;
    this.starsEarned = 3;
  }

  unlock(): void {
    if (this.status !== ProgressStatus.LOCKED) return;
    this.status = ProgressStatus.UNLOCKED;
  }

  isAlreadyCompleted(): boolean {
    return this.status === ProgressStatus.COMPLETED;
  }

  getId(): string {
    return this.id;
  }
  getStudentUserId(): string {
    return this.studentUserId;
  }
  getTopicId(): string {
    return this.topicId;
  }
  getStatus(): ProgressStatus {
    return this.status;
  }
  getStarsEarned(): number {
    return this.starsEarned;
  }
}
