import {
  CreateCategoryDto,
  CreateSubCategoryDto,
  CreateTopicDto,
  CreateActivityDto,
  UpdateCategoryDto,
  UpdateSubCategoryDto,
  UpdateTopicDto,
  UpdateActivityDto,
  ReorderTopicsDto,
} from 'src/web/courses/model/request/courses-request.dto';

export interface ICoursesService {
  createCategory(dto: CreateCategoryDto): Promise<any>;
  updateCategory(id: string, dto: UpdateCategoryDto): Promise<any>;
  deleteCategory(id: string): Promise<void>;
  findAllCategories(): Promise<any[]>;
  findSubCategoriesByCategory(categoryId: string): Promise<any[]>;

  findSubCategoryById(id: string): Promise<any>;
  findTopicsBySubCategory(
    subCategoryId: string,
    studentUserId?: string,
  ): Promise<any[]>;
  createSubCategory(dto: CreateSubCategoryDto): Promise<any>;
  updateSubCategory(id: string, dto: UpdateSubCategoryDto): Promise<any>;
  deleteSubCategory(id: string): Promise<void>;

  createTopic(dto: CreateTopicDto): Promise<any>;
  updateTopic(id: string, dto: UpdateTopicDto): Promise<any>;
  reorderTopics(dto: ReorderTopicsDto): Promise<any[]>;
  deleteTopic(id: string): Promise<void>;
  findTopicById(id: string): Promise<any>;
  findActivitiesByTopic(
    topicId: string,
    studentUserId?: string,
    opts?: { revealAll?: boolean },
  ): Promise<any[]>;

  createActivity(dto: CreateActivityDto): Promise<any>;
  updateActivity(id: string, dto: UpdateActivityDto): Promise<any>;
  deleteActivity(id: string): Promise<void>;
}

export const COURSES_SERVICE = Symbol('COURSES_SERVICE');
