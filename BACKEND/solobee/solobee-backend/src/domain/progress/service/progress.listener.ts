import { Injectable, Logger, Inject } from '@nestjs/common';
import { OnEvent } from '@nestjs/event-emitter';
import { TopicProgress, ProgressStatus } from '../model/topic-progress.model';
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
  TOPIC_REPOSITORY,
  type ITopicRepository,
} from 'src/datasource/courses/repository/interface/topic.repository.interface';
import {
  STUDENT_REPOSITORY,
  type IStudentRepository,
} from 'src/datasource/student/repository/student.repository.interface';

@Injectable()
export class ProgressListener {
  private readonly logger = new Logger(ProgressListener.name);

  constructor(
    @Inject(ACTIVITY_PROGRESS_REPOSITORY)
    private readonly activityProgressRepository: IActivityProgressRepository,
    @Inject(TOPIC_PROGRESS_REPOSITORY)
    private readonly topicProgressRepository: ITopicProgressRepository,
    @Inject(ACTIVITY_REPOSITORY)
    private readonly activityRefRepository: IActivityRepository,
    @Inject(TOPIC_REPOSITORY)
    private readonly topicRefRepository: ITopicRepository,
    @Inject(STUDENT_REPOSITORY)
    private readonly studentRepository: IStudentRepository,
  ) {}

  @OnEvent('activity.completed')
  async handleActivityCompleted(payload: {
    studentUserId: string;
    activityId: string;
    topicId: string;
  }): Promise<void> {
    this.logger.debug(`Processing completion for topic: ${payload.topicId}`);

    const activityIds = await this.activityRefRepository.findIdsByTopicId(
      payload.topicId,
    );
    if (activityIds.length === 0) return;

    const completedCount =
      await this.activityProgressRepository.countCompletedByStudentInActivities(
        payload.studentUserId,
        activityIds,
      );

    if (completedCount >= activityIds.length) {
      await this.completeTopic(payload.studentUserId, payload.topicId);
    }
  }

  private async completeTopic(userId: string, topicId: string): Promise<void> {
    let topicProgress =
      await this.topicProgressRepository.findByStudentAndTopic(userId, topicId);

    if (!topicProgress) {
      topicProgress = TopicProgress.create(userId, topicId);
    }

    if (topicProgress.isAlreadyCompleted()) return;

    // Domain o'zi status va yulduzlarni boshqaradi
    topicProgress.complete();
    await this.topicProgressRepository.save(topicProgress);

    // Student rich domain orqali ball qo'shiladi
    const student = await this.studentRepository.findById(userId);
    if (student) {
      student.addScore(100);
      await this.studentRepository.save(student);
    }

    await this.unlockNextTopic(userId, topicId);
  }

  private async unlockNextTopic(
    userId: string,
    currentTopicId: string,
  ): Promise<void> {
    const currentTopic = await this.topicRefRepository.findById(currentTopicId);
    if (!currentTopic) return;

    const nextTopic = await this.topicRefRepository.findNextInSubCategory(
      currentTopic.getSubCategoryId(),
      currentTopic.getOrderIndex(),
    );
    if (!nextTopic) return;

    const existingProgress =
      await this.topicProgressRepository.findByStudentAndTopic(
        userId,
        nextTopic.id,
      );

    if (!existingProgress) {
      const unlocked = TopicProgress.create(
        userId,
        nextTopic.id,
        ProgressStatus.UNLOCKED,
      );
      await this.topicProgressRepository.save(unlocked);
    }

    // Student rich domain orqali keyingi tavsiya yangilanadi (Home Page uchun)
    const student = await this.studentRepository.findById(userId);
    if (student) {
      student.setCurrentTopic(nextTopic.id, currentTopic.getSubCategoryId());
      await this.studentRepository.save(student);
    }
  }
}
