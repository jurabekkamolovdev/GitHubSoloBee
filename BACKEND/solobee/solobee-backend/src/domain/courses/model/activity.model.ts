import { v4 as uuidv4 } from 'uuid';

export class Activity<T extends ActivityType = ActivityType> {
  private id: string;
  private topicId: string;
  private type: T;
  private orderIndex: number;
  private payload: PayloadByType<T>;
  private createdAt: Date;
  private updatedAt: Date;

  constructor(params: ActivityParams<T>) {
    this.id = params.id;
    this.topicId = params.topicId;
    this.type = params.type;
    this.orderIndex = params.orderIndex;
    this.payload = params.payload;
    this.createdAt = params.createdAt ?? new Date();
    this.updatedAt = params.updatedAt ?? new Date();
  }

  static create<T extends ActivityType>(
    params: ICreateActivity<T>,
  ): Activity<T> {
    return new Activity<T>({ ...params, id: uuidv4() });
  }

  getStorageKeys(): string[] {
    const keys: string[] = [];
    switch (this.type) {
      case ActivityType.LEARN: {
        const p = this.payload as LearnPayload;
        if (p.imageKey) keys.push(p.imageKey);
        if (p.audioKey) keys.push(p.audioKey);
        break;
      }
      case ActivityType.WORDHUNT: {
        const p = this.payload as WordhuntPayload;
        if (p.imageKey) keys.push(p.imageKey);
        for (const opt of p.options ?? []) {
          if (opt?.imageKey) keys.push(opt.imageKey);
          if (opt?.audioKey) keys.push(opt.audioKey);
        }
        break;
      }
      case ActivityType.PICQUEST: {
        const p = this.payload as PicQuestPayload;
        if (p.audioKey) keys.push(p.audioKey);
        for (const opt of p.options ?? []) {
          if (opt?.imageKey) keys.push(opt.imageKey);
        }
        break;
      }
      case ActivityType.WRITING: {
        const p = this.payload as WritingPayload;
        if (p.mode === 'spell') {
          if (p.audioKey) keys.push(p.audioKey);
          if (p.imageKey) keys.push(p.imageKey);
          for (const opt of p.options ?? []) {
            if (opt?.imageKey) keys.push(opt.imageKey);
          }
        }
        break;
      }
    }
    return keys;
  }

  applyPayloadPatch(patch: Partial<PayloadByType<T>>): void {
    if (!patch) return;
    const base = this.payload;

    if (
      this.type === ActivityType.WORDHUNT ||
      this.type === ActivityType.PICQUEST
    ) {
      type OptionsPayload = WordhuntPayload | PicQuestPayload;
      type AnyOption = OptionsPayload['options'][number];

      const existingOptions: AnyOption[] =
        (base as OptionsPayload).options ?? [];
      const patchOptions: AnyOption[] | undefined = (
        patch as Partial<OptionsPayload>
      ).options;

      this.payload = {
        ...base,
        ...patch,
        ...(patchOptions
          ? {
              options: patchOptions.map(
                (opt, i): AnyOption => ({
                  ...(existingOptions[i] ?? {}),
                  ...opt,
                }),
              ),
            }
          : {}),
      };
    } else if (this.type === ActivityType.WRITING) {
      const baseWriting = base as WritingPayload;
      const patchWriting = patch as Partial<WritingPayload>;

      if (baseWriting.mode === 'spell' && patchWriting.mode === 'spell') {
        const baseSpell = baseWriting as WritingSpellPayload;
        const patchSpell = patchWriting as Partial<WritingSpellPayload>;
        const existingOptions = baseSpell.options ?? [];
        const patchOptions = patchSpell.options;

        this.payload = {
          ...baseSpell,
          ...patchSpell,
          ...(patchOptions
            ? {
                options: patchOptions.map((opt, i) => ({
                  ...(existingOptions[i] ?? {}),
                  ...opt,
                })),
              }
            : {}),
        } as PayloadByType<T>;
      } else {
        // trace → trace, yoki mode o'zgarishi
        this.payload = { ...base, ...patch };
      }
    } else {
      this.payload = { ...base, ...patch };
    }

    this.updatedAt = new Date();
  }

  setOrderIndex(index: number): void {
    this.orderIndex = index;
    this.updatedAt = new Date();
  }

  getId(): string {
    return this.id;
  }
  getTopicId(): string {
    return this.topicId;
  }
  getType(): T {
    return this.type;
  }
  getOrderIndex(): number {
    return this.orderIndex;
  }
  getPayload(): PayloadByType<T> {
    return this.payload;
  }
  getCreatedAt(): Date {
    return this.createdAt;
  }
  getUpdatedAt(): Date {
    return this.updatedAt;
  }
}

export interface ICreateActivity<T extends ActivityType> {
  topicId: string;
  type: T;
  orderIndex: number;
  payload: PayloadByType<T>;
}

type PayloadByType<T extends ActivityType> = ActivityPayloadMap[T];

interface ActivityParams<T extends ActivityType> {
  id: string;
  topicId: string;
  type: T;
  orderIndex: number;
  payload: PayloadByType<T>;
  createdAt?: Date;
  updatedAt?: Date;
}

export interface ActivityPayloadMap {
  [ActivityType.LEARN]: LearnPayload;
  [ActivityType.WRITING]: WritingPayload;
  [ActivityType.WORDHUNT]: WordhuntPayload;
  [ActivityType.PICQUEST]: PicQuestPayload;
}

export enum ActivityType {
  LEARN = 'LEARN',
  WRITING = 'WRITING',
  WORDHUNT = 'WORDHUNT',
  PICQUEST = 'PICQUEST',
}

export interface LearnPayload {
  imageKey?: string;
  audioKey?: string;
}

export interface WritingTracePayload {
  mode: 'trace';
  char: string;
}

export interface WritingSpellPayload {
  mode: 'spell';
  answer: string;
  imageKey?: string;
  options: {
    imageKey: string;
    char: string;
  }[];
  audioKey?: string;
}

export type WritingPayload = WritingTracePayload | WritingSpellPayload;

export interface WordhuntPayload {
  imageKey?: string;
  options: {
    imageKey: string;
    audioKey?: string;
    isCorrect: boolean;
  }[];
}

export interface PicQuestPayload {
  audioKey?: string;
  options: {
    imageKey: string;
    label?: string;
    isCorrect: boolean;
  }[];
}
