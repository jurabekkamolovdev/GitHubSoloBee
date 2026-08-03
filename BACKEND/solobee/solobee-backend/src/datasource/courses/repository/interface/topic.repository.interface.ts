import { Topic } from 'src/domain/courses/model/topic.model';

export interface ITopicRepository {
  save(topic: Topic): Promise<Topic>;
  findById(id: string, withActivities?: boolean): Promise<Topic | null>;
  findBySubCategoryId(subCategoryId: string): Promise<Topic[]>;
  findByIds(ids: string[]): Promise<Topic[]>;
  findConflict(
    subCategoryId: string,
    orderIndex: number,
  ): Promise<Topic | null>;
  updateOrderIndex(id: string, orderIndex: number): Promise<void>;
  delete(id: string): Promise<void>;
  findNextInSubCategory(
    subCategoryId: string,
    afterOrderIndex: number,
  ): Promise<TopicRef | null>;
}

export const TOPIC_REPOSITORY = Symbol('TOPIC_REPOSITORY');

export interface TopicRef {
  id: string;
  subCategoryId: string;
  orderIndex: number;
}
