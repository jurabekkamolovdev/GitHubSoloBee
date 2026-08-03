import { Injectable, NotFoundException, Inject } from '@nestjs/common';
import { ICoursesService } from './courses.service.interface';
import {
  CATEGORY_REPOSITORY,
  type ICategoryRepository,
} from 'src/datasource/courses/repository/interface/category.repository.interface';
import {
  SUB_CATEGORY_REPOSITORY,
  type ISubCategoryRepository,
} from 'src/datasource/courses/repository/interface/sub-category.repository.interface';
import {
  TOPIC_REPOSITORY,
  type ITopicRepository,
} from 'src/datasource/courses/repository/interface/topic.repository.interface';
import {
  ACTIVITY_REPOSITORY,
  type IActivityRepository,
} from 'src/datasource/courses/repository/interface/activity.repository.interface';
import { Category } from '../model/category.model';
import { SubCategory } from '../model/sub-category.model';
import { Topic } from '../model/topic.model';
import { Activity } from '../model/activity.model';
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
import {
  STORAGE_SERVICE,
  type IStorageService,
} from 'src/infrastructure/storage/storage.interface';
import { CourseMapper } from './course.mapper';
import {
  PROGRESS_SERVICE,
  type IProgressService,
} from 'src/domain/progress/service/progress.service.interface';

@Injectable()
export class CoursesServiceImpl implements ICoursesService {
  constructor(
    @Inject(CATEGORY_REPOSITORY)
    private readonly categoryRepository: ICategoryRepository,
    @Inject(SUB_CATEGORY_REPOSITORY)
    private readonly subCategoryRepository: ISubCategoryRepository,
    @Inject(TOPIC_REPOSITORY)
    private readonly topicRepository: ITopicRepository,
    @Inject(ACTIVITY_REPOSITORY)
    private readonly activityRepository: IActivityRepository,
    @Inject(STORAGE_SERVICE)
    private readonly storageService: IStorageService,
    @Inject(PROGRESS_SERVICE)
    private readonly progressService: IProgressService,
    private readonly mapper: CourseMapper,
  ) {}

  async createCategory(dto: CreateCategoryDto): Promise<any> {
    const category = Category.create({
      name: dto.name,
      backgroundColor: dto.backgroundColor ?? null,
      foregroundColor: dto.foregroundColor ?? null,
      orderIndex: dto.orderIndex ?? 0,
    });
    const saved = await this.categoryRepository.save(category);
    return this.mapper.category(saved);
  }

  async updateCategory(id: string, dto: UpdateCategoryDto): Promise<any> {
    const category = await this.categoryRepository.findById(id);
    if (!category) throw new NotFoundException('Category not found');
    const updated = new (category.constructor as any)({
      id: category.getId(),
      name: dto.name ?? category.getName(),
      backgroundColor: dto.backgroundColor ?? category.getBackgroundColor(),
      foregroundColor: dto.foregroundColor ?? category.getForegroundColor(),
      orderIndex: dto.orderIndex ?? category.getOrderIndex(),
      createdAt: category.getCreatedAt(),
      updatedAt: new Date(),
    }) as Category;
    const saved = await this.categoryRepository.save(updated);
    return this.mapper.category(saved);
  }

  async deleteCategory(id: string): Promise<void> {
    const category = await this.categoryRepository.findById(id, true);
    if (!category) throw new NotFoundException('Category not found');
    const keys = category.getAllStorageKeys();
    await this.categoryRepository.delete(id);
    await this.storageService.deleteFiles(keys);
  }

  async findAllCategories(): Promise<any[]> {
    const categories = await this.categoryRepository.findAll(true);
    const countMap =
      await this.categoryRepository.getActivityCountByCategoryId();
    return categories.map((cat) =>
      this.mapper.category(cat, countMap.get(cat.getId()) ?? 0),
    );
  }

  async findSubCategoriesByCategory(categoryId: string): Promise<any[]> {
    const category = await this.categoryRepository.findById(categoryId, false);
    if (!category) throw new NotFoundException('Category not found');
    const subs = await this.subCategoryRepository.findByCategoryId(categoryId);
    return this.mapper.subCategories(subs);
  }

  async findSubCategoryById(id: string): Promise<any> {
    const sub = await this.subCategoryRepository.findById(id);
    if (!sub) throw new NotFoundException('SubCategory not found');
    return this.mapper.subCategory(sub);
  }

  async findTopicsBySubCategory(
    subCategoryId: string,
    studentUserId?: string,
  ): Promise<any[]> {
    const sub = await this.subCategoryRepository.findById(subCategoryId);
    if (!sub) throw new NotFoundException('SubCategory not found');
    const topics =
      await this.topicRepository.findBySubCategoryId(subCategoryId);
    const completedIds = studentUserId
      ? await this.progressService.getCompletedTopicIds(
          studentUserId,
          topics.map((t) => t.getId()),
        )
      : undefined;
    // const completedIds = studentUserId ? new Set(studentUserId) : undefined;
    return this.mapper.topics(topics, completedIds);
  }

  async createSubCategory(dto: CreateSubCategoryDto): Promise<any> {
    const sub = SubCategory.create({
      categoryId: dto.categoryId,
      name: dto.name,
      thumbnailKey: dto.thumbnailKey ?? null,
      orderIndex: dto.orderIndex ?? 0,
    });
    const saved = await this.subCategoryRepository.save(sub);
    return this.mapper.subCategory(saved);
  }

  async updateSubCategory(id: string, dto: UpdateSubCategoryDto): Promise<any> {
    const sub = await this.subCategoryRepository.findById(id);
    if (!sub) throw new NotFoundException('SubCategory not found');

    const orphanKey =
      dto.thumbnailKey !== undefined
        ? sub.updateThumbnail(dto.thumbnailKey)
        : null;

    if (dto.name) sub.updateName(dto.name);

    const saved = await this.subCategoryRepository.save(sub);
    if (orphanKey) await this.storageService.deleteFiles([orphanKey]);
    return this.mapper.subCategory(saved);
  }

  async deleteSubCategory(id: string): Promise<void> {
    const sub = await this.subCategoryRepository.findById(id, true);
    if (!sub) throw new NotFoundException('SubCategory not found');
    const keys = sub.getAllStorageKeys();
    await this.subCategoryRepository.delete(id);
    await this.storageService.deleteFiles(keys);
  }

  async createTopic(dto: CreateTopicDto): Promise<any> {
    const topic = Topic.create({
      subCategoryId: dto.subCategoryId,
      thumbnailKey: dto.thumbnailKey ?? null,
      orderIndex: dto.orderIndex ?? 0,
    });
    const saved = await this.topicRepository.save(topic);
    return this.mapper.topic(saved);
  }

  async updateTopic(id: string, dto: UpdateTopicDto): Promise<any> {
    const topic = await this.topicRepository.findById(id);
    if (!topic) throw new NotFoundException('Topic not found');

    const orphanKey =
      dto.thumbnailKey !== undefined
        ? topic.updateThumbnail(dto.thumbnailKey)
        : null;

    if (
      dto.orderIndex !== undefined &&
      dto.orderIndex !== topic.getOrderIndex()
    ) {
      const conflict = await this.topicRepository.findConflict(
        topic.getSubCategoryId(),
        dto.orderIndex,
      );
      if (conflict && conflict.getId() !== topic.getId()) {
        const oldIndex = topic.getOrderIndex();
        const tempIndex = -(Date.now() % 1_000_000);
        await this.topicRepository.updateOrderIndex(
          conflict.getId(),
          tempIndex,
        );
        topic.setOrderIndex(dto.orderIndex);
        await this.topicRepository.save(topic);
        await this.topicRepository.updateOrderIndex(conflict.getId(), oldIndex);
      } else {
        topic.setOrderIndex(dto.orderIndex);
      }
    }

    const saved = await this.topicRepository.save(topic);
    if (orphanKey) await this.storageService.deleteFiles([orphanKey]);
    return this.mapper.topic(saved);
  }

  async reorderTopics(dto: ReorderTopicsDto): Promise<any[]> {
    const ids = dto.items.map((i) => i.id);
    const topics = await this.topicRepository.findByIds(ids);
    if (topics.length !== ids.length)
      throw new NotFoundException('One or more topics not found');

    // Phase 1 — move to temp slots to avoid transient collisions
    for (let i = 0; i < topics.length; i++) {
      await this.topicRepository.updateOrderIndex(
        topics[i].getId(),
        -1_000_000 - i,
      );
    }
    // Phase 2 — write final order
    for (const item of dto.items) {
      await this.topicRepository.updateOrderIndex(item.id, item.orderIndex);
    }

    const updated = await this.topicRepository.findByIds(ids);
    return updated
      .sort((a, b) => a.getOrderIndex() - b.getOrderIndex())
      .map((t) => this.mapper.topic(t));
  }

  async deleteTopic(id: string): Promise<void> {
    const topic = await this.topicRepository.findById(id, true);
    if (!topic) throw new NotFoundException('Topic not found');
    const keys = topic.getAllStorageKeys();
    await this.topicRepository.delete(id);
    await this.storageService.deleteFiles(keys);
  }

  async findTopicById(id: string): Promise<any> {
    const topic = await this.topicRepository.findById(id);
    if (!topic) throw new NotFoundException('Topic not found');
    return this.mapper.topic(topic);
  }

  async findActivitiesByTopic(
    topicId: string,
    studentUserId?: string,
    opts: { revealAll?: boolean } = {},
  ): Promise<any[]> {
    const topic = await this.topicRepository.findById(topicId);
    if (!topic) throw new NotFoundException('Topic not found');
    const activities = await this.activityRepository.findByTopicId(topicId);
    const progressMap = studentUserId
      ? await this.progressService.getActivityProgressByTopic(
          studentUserId,
          topicId,
        )
      : undefined;
    // const progressMap = undefined;
    return this.mapper.activities(activities, progressMap, opts.revealAll);
  }

  async createActivity(dto: CreateActivityDto): Promise<any> {
    const activity = Activity.create({
      topicId: dto.topicId,
      type: dto.type,
      orderIndex: dto.orderIndex ?? 0,
      payload: dto.payload ?? {},
    });
    const saved = await this.activityRepository.save(activity);
    return this.mapper.activity(saved);
  }

  async updateActivity(id: string, dto: UpdateActivityDto): Promise<any> {
    const activity = await this.activityRepository.findById(id);
    if (!activity) throw new NotFoundException('Activity not found');

    const oldKeys = new Set(activity.getStorageKeys());

    if (dto.payload !== undefined) activity.applyPayloadPatch(dto.payload);
    if (dto.orderIndex !== undefined) activity.setOrderIndex(dto.orderIndex);

    const newKeys = new Set(activity.getStorageKeys());
    const orphanKeys = [...oldKeys].filter((k) => !newKeys.has(k));

    const saved = await this.activityRepository.save(activity);
    if (orphanKeys.length) await this.storageService.deleteFiles(orphanKeys);
    return this.mapper.activity(saved);
  }

  async deleteActivity(id: string): Promise<void> {
    const activity = await this.activityRepository.findById(id);
    if (!activity) throw new NotFoundException('Activity not found');
    const keys = activity.getStorageKeys();
    await this.activityRepository.delete(id);
    await this.storageService.deleteFiles(keys);
  }
}
