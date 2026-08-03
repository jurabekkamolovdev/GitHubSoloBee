import {
  TopicProgress,
  ProgressStatus,
} from 'src/domain/progress/model/topic-progress.model';

export interface ITopicProgressRepository {
  save(progress: TopicProgress): Promise<TopicProgress>;
  findByStudentAndTopic(
    studentUserId: string,
    topicId: string,
  ): Promise<TopicProgress | null>;
  findCompletedTopicIds(
    studentUserId: string,
    topicIds: string[],
  ): Promise<string[]>;
  findStatusByStudentAndTopic(
    studentUserId: string,
    topicId: string,
  ): Promise<ProgressStatus | null>;
}

export const TOPIC_PROGRESS_REPOSITORY = Symbol('TOPIC_PROGRESS_REPOSITORY');
