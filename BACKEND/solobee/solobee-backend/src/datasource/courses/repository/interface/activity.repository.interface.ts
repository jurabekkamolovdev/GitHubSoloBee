import { Activity } from 'src/domain/courses/model/activity.model';

export interface IActivityRepository {
  save(activity: Activity): Promise<Activity>;
  findById(id: string): Promise<Activity | null>;
  findByTopicId(topicId: string): Promise<Activity[]>;
  delete(id: string): Promise<void>;
  findIdsByTopicId(topicId: string): Promise<string[]>;
}

export const ACTIVITY_REPOSITORY = Symbol('ACTIVITY_REPOSITORY');
