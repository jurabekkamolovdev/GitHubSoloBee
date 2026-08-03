import { Injectable, Inject } from '@nestjs/common';
import { Category } from '../model/category.model';
import { SubCategory } from '../model/sub-category.model';
import { Topic } from '../model/topic.model';
import {
  Activity,
  ActivityType,
  LearnPayload,
  WritingPayload,
  WritingSpellPayload,
  WordhuntPayload,
  PicQuestPayload,
} from '../model/activity.model';
import {
  STORAGE_SERVICE,
  type IStorageService,
} from 'src/infrastructure/storage/storage.interface';
// import { ATTEMPT_THRESHOLDS } from '../../progress/application/progress.service';

const TAB_ORDER: ActivityType[] = [
  ActivityType.LEARN,
  ActivityType.WRITING,
  ActivityType.WORDHUNT,
  ActivityType.PICQUEST,
];

const TAB_TITLES: Record<ActivityType, string> = {
  [ActivityType.LEARN]: 'Learn',
  [ActivityType.WRITING]: 'Writing',
  [ActivityType.WORDHUNT]: 'Wordhunt',
  [ActivityType.PICQUEST]: 'PicQuest',
};

@Injectable()
export class CourseMapper {
  constructor(
    @Inject(STORAGE_SERVICE)
    private readonly storage: IStorageService,
  ) {}

  private url(key?: string | null): string | null {
    return this.storage.getPublicUrl(key ?? null);
  }

  private sortedBy<T>(
    items: T[] | null | undefined,
    getIndex: (item: T) => number,
  ): T[] {
    return [...(items ?? [])].sort((a, b) => getIndex(a) - getIndex(b));
  }

  activity(act: Activity): Record<string, unknown> {
    const type = act.getType();
    const base = {
      id: act.getId(),
      topicId: act.getTopicId(),
      type,
      createdAt: act.getCreatedAt(),
      updatedAt: act.getUpdatedAt(),
    };

    switch (type) {
      case ActivityType.LEARN: {
        const p = act.getPayload() as LearnPayload;
        return {
          ...base,
          payload: {
            imageUrl: this.url(p.imageKey),
            audioUrl: this.url(p.audioKey),
          },
        };
      }
      case ActivityType.WORDHUNT: {
        const p = act.getPayload() as WordhuntPayload;
        return {
          ...base,
          payload: {
            imageUrl: this.url(p.imageKey),
            options: p.options.map((o) => ({
              imageUrl: this.url(o.imageKey),
              audioUrl: o.audioKey ? this.url(o.audioKey) : null,
              isCorrect: o.isCorrect,
            })),
          },
        };
      }
      case ActivityType.PICQUEST: {
        const p = act.getPayload() as PicQuestPayload;
        return {
          ...base,
          payload: {
            audioUrl: p.audioKey ? this.url(p.audioKey) : null,
            options: p.options.map((o) => ({
              imageUrl: this.url(o.imageKey),
              label: o.label,
              isCorrect: o.isCorrect,
            })),
          },
        };
      }
      // course.mapper.ts

      case ActivityType.WRITING: {
        const p = act.getPayload() as WritingPayload;
        if (p.mode === 'spell') {
          const sp = p as WritingSpellPayload;
          return {
            ...base,
            payload: {
              mode: 'spell',
              answer: sp.answer,
              imageUrl: sp.imageKey ? this.url(sp.imageKey) : null,
              audioUrl: sp.audioKey ? this.url(sp.audioKey) : null,
              options: sp.options.map((o) => ({
                char: o.char,
                imageUrl: this.url(o.imageKey),
              })),
            },
          };
        }
        // trace — storage key yo'q, faqat char xom qaytariladi
        return { ...base, payload: { mode: 'trace', char: p.char } };
      }
    }
  }

  activities(
    list: Activity[] | null | undefined,
    progressMap?: Map<string, { attemptCount: number; isCompleted: boolean }>,
    revealAll = false,
  ): Record<string, unknown>[] {
    const byType = new Map<ActivityType, Activity>();
    for (const a of list ?? []) byType.set(a.getType(), a);

    let prevCompleted = true;
    return TAB_ORDER.filter((type) => byType.has(type)).map((type) => {
      const act = byType.get(type)!;
      const snap = progressMap?.get(act.getId());
      const completed = snap?.isCompleted ?? false;
      const attemptCount = snap?.attemptCount ?? 0;
      const enabled = revealAll || prevCompleted;
      prevCompleted = completed;

      const full = this.activity(act);
      return {
        id: act.getId(),
        topicId: act.getTopicId(),
        type: act.getType(),
        title: TAB_TITLES[type],
        enabled,
        completed,
        attemptCount,
        // totalAttemptCount: ATTEMPT_THRESHOLDS[type],
        payload: enabled ? full['payload'] : null,
      };
    });
  }

  topic(
    topic: Topic,
    completed = false,
    enabled = true,
  ): Record<string, unknown> {
    return {
      id: topic.getId(),
      subCategoryId: topic.getSubCategoryId(),
      thumbnailUrl: this.url(topic.getThumbnailKey()),
      enabled,
      completed,
    };
  }

  topics(
    list: Topic[] | null | undefined,
    completedIds?: Set<string>,
  ): Record<string, unknown>[] {
    const sorted = this.sortedBy(list, (t) => t.getOrderIndex());
    let prevCompleted = true;
    return sorted.map((t) => {
      const completed = completedIds?.has(t.getId()) ?? false;
      const enabled = prevCompleted;
      prevCompleted = completed;
      return this.topic(t, completed, enabled);
    });
  }

  subCategory(sub: SubCategory): Record<string, unknown> {
    return {
      id: sub.getId(),
      categoryId: sub.getCategoryId(),
      name: sub.getName(),
      thumbnailUrl: this.url(sub.getThumbnailKey()),
    };
  }

  subCategories(
    list: SubCategory[] | null | undefined,
  ): Record<string, unknown>[] {
    return this.sortedBy(list, (s) => s.getOrderIndex()).map((s) =>
      this.subCategory(s),
    );
  }

  category(cat: Category, lessonCount?: number): Record<string, unknown> {
    const sortedSubs = this.sortedBy(cat.getSubCategories(), (s) =>
      s.getOrderIndex(),
    );
    return {
      id: cat.getId(),
      name: cat.getName(),
      backgroundColor: cat.getBackgroundColor(),
      foregroundColor: cat.getForegroundColor(),
      subCategories: sortedSubs.map((s) => this.subCategory(s)),
      ...(lessonCount !== undefined
        ? { lessonCount, completedLessonCount: 0 }
        : {}),
    };
  }
}
