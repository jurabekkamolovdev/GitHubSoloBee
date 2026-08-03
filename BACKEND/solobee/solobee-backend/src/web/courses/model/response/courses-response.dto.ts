import { ApiProperty, ApiExtraModels, getSchemaPath } from '@nestjs/swagger';
import { BaseResponse } from '../../../../core/utils/base-response';

export class LearnPayloadDto {
  @ApiProperty({
    example: 'http://localhost:9000/savodxon-media/learn/letter-a-hero.webp',
    nullable: true,
    description: 'Hero image shown on the Learn screen',
  })
  imageUrl: string | null;

  @ApiProperty({
    example: 'http://localhost:9000/savodxon-media/learn/letter-a.mp3',
    nullable: true,
    description: 'Pronunciation audio played on the Learn screen',
  })
  audioUrl: string | null;
}

export class WritingTracePayloadDto {
  @ApiProperty({
    enum: ['trace'],
    example: 'trace',
    description:
      'Trace mode: user traces a letter shape. Used for alphabet topics.',
  })
  mode: 'trace';

  @ApiProperty({
    example: 'A',
    description: 'Letter character the student traces',
  })
  char: string;
}

export class WritingSpellOptionDto {
  @ApiProperty({
    example: 'R',
    description: 'Letter character',
  })
  char: string;

  @ApiProperty({
    example: 'https://s3.solobee.uz/solobee-media/writing/xxx.png',
    description: 'Image URL for the letter',
  })
  imageUrl: string;
}

export class WritingSpellPayloadDto {
  @ApiProperty({
    enum: ['spell'],
    example: 'spell',
    description:
      'Spell mode: user picks letters in order. Used for word topics.',
  })
  mode: 'spell';

  @ApiProperty({
    example: 'RED',
    description: 'Correct answer the user must spell',
  })
  answer: string;

  @ApiProperty({
    example: 'https://s3.solobee.uz/solobee-media/writing/xxx.png',
    description: 'Main image for the word (optional)',
    required: false,
    nullable: true,
  })
  imageUrl: string | null;

  @ApiProperty({
    example: 'https://s3.solobee.uz/solobee-media/writing/xxx.mp3',
    description: 'Audio URL for the word pronunciation',
    required: false,
    nullable: true,
  })
  audioUrl: string | null;

  @ApiProperty({
    type: [WritingSpellOptionDto],
    description: 'Letter options with images (including distractors)',
  })
  options: WritingSpellOptionDto[];
}
export class WordhuntOptionDto {
  @ApiProperty({
    example: 'http://localhost:9000/savodxon-media/wordhunt/option-a.webp',
    description:
      'Fully pre-rendered option image. Client displays this as-is — all ' +
      'styling (gradient, background, text) is baked into the upload.',
  })
  imageUrl: string;

  @ApiProperty({
    example: 'http://localhost:9000/savodxon-media/wordhunt/letter-a.mp3',
    nullable: true,
    description:
      "Optional per-option audio played from the option's speaker button",
  })
  audioUrl: string | null;

  @ApiProperty({ example: true })
  isCorrect: boolean;
}

export class WordhuntPayloadDto {
  @ApiProperty({
    example:
      'http://localhost:9000/savodxon-media/wordhunt/letter-a-prompt.webp',
    nullable: true,
    description:
      'Prompt image shown at the top — the letter/word the student must identify',
  })
  imageUrl: string | null;

  @ApiProperty({ type: [WordhuntOptionDto] })
  options: WordhuntOptionDto[];
}

export class PicQuestOptionDto {
  @ApiProperty({
    example: 'http://localhost:9000/savodxon-media/picquest/banana.webp',
  })
  imageUrl: string;

  @ApiProperty({ example: 'Banana', required: false })
  label?: string;

  @ApiProperty({ example: true })
  isCorrect: boolean;
}

export class PicQuestPayloadDto {
  @ApiProperty({
    example:
      'http://localhost:9000/savodxon-media/picquest/letter-a-prompt.mp3',
    nullable: true,
    description:
      'Prompt audio played before the student picks an image (e.g. letter pronunciation)',
  })
  audioUrl: string | null;

  @ApiProperty({ type: [PicQuestOptionDto] })
  options: PicQuestOptionDto[];
}

@ApiExtraModels(
  LearnPayloadDto,
  WritingTracePayloadDto,
  WritingSpellPayloadDto,
  WordhuntPayloadDto,
  PicQuestPayloadDto,
)
export class ActivityTabDto {
  @ApiProperty({ example: 'a1b2c3d4-e5f6-7890-abcd-ef1234567890' })
  id: string;

  @ApiProperty({ example: 'b2c3d4e5-f6a7-8901-bcde-f12345678901' })
  topicId: string;

  @ApiProperty({
    example: 'LEARN',
    enum: ['LEARN', 'WRITING', 'WORDHUNT', 'PICQUEST'],
    description:
      'Discriminator for the `payload` field. Clients should switch on this ' +
      'value to pick the correct payload shape:\n' +
      '- `LEARN` → LearnPayloadDto\n' +
      '- `WRITING` → WritingTracePayloadDto | WritingSpellPayloadDto (discriminated by payload.mode)\n' +
      '- `WORDHUNT` → WordhuntPayloadDto\n' +
      '- `PICQUEST` → PicQuestPayloadDto',
  })
  type: string;

  @ApiProperty({
    example: 'Learn',
    enum: ['Learn', 'Writing', 'Wordhunt', 'PicQuest'],
    description:
      'Display title for the tab. Static English labels for now; localization ' +
      'will be handled server-side once sessions carry a language.',
  })
  title: string;

  @ApiProperty({
    example: true,
    description:
      'Whether this tab is unlocked for the current student. ' +
      'First tab is always enabled; each subsequent tab is enabled only when ' +
      'the previous one is completed.',
  })
  enabled: boolean;

  @ApiProperty({
    example: false,
    description: 'Whether the current student has finished this tab.',
  })
  completed: boolean;

  @ApiProperty({
    example: 2,
    description:
      'Number of successful attempts the current student has logged.',
  })
  attemptCount: number;

  @ApiProperty({
    example: 4,
    description:
      'Total successful attempts required to complete this tab. ' +
      'LEARN: 4, WRITING/WORDHUNT/PICQUEST: 1. Clients should render progress ' +
      'as `attemptCount / totalAttemptCount` rather than hard-coding thresholds.',
  })
  totalAttemptCount: number;

  @ApiProperty({
    nullable: true,
    description:
      'Type-specific game configuration. **`null` when `enabled` is false** — ' +
      'content is withheld until the preceding tab is completed, so a decoded ' +
      "response can't leak locked content. When non-null, shape depends on " +
      '`type` (see the `type` field).',
    oneOf: [
      { $ref: getSchemaPath(LearnPayloadDto) },
      { $ref: getSchemaPath(WritingTracePayloadDto) },
      { $ref: getSchemaPath(WritingSpellPayloadDto) },
      { $ref: getSchemaPath(WordhuntPayloadDto) },
      { $ref: getSchemaPath(PicQuestPayloadDto) },
    ],
  })
  payload:
    | LearnPayloadDto
    | WritingTracePayloadDto
    | WritingSpellPayloadDto
    | WordhuntPayloadDto
    | PicQuestPayloadDto
    | null;
}

export class TopicResponseDto {
  @ApiProperty({ example: 'b2c3d4e5-f6a7-8901-bcde-f12345678901' })
  id: string;

  @ApiProperty({ example: 'c3d4e5f6-a7b8-9012-cdef-123456789012' })
  subCategoryId: string;

  @ApiProperty({ example: 'Banana' })
  name: string;

  @ApiProperty({
    example: 'https://cdn.example.com/img/banana.png',
    nullable: true,
  })
  thumbnailUrl: string | null;

  @ApiProperty({
    example: false,
    description: 'Whether the student has completed this topic',
  })
  completed: boolean;
}

export class SubCategoryResponseDto {
  @ApiProperty({ example: 'c3d4e5f6-a7b8-9012-cdef-123456789012' })
  id: string;

  @ApiProperty({ example: 'd4e5f6a7-b8c9-0123-defa-234567890123' })
  categoryId: string;

  @ApiProperty({ example: 'Fruits' })
  name: string;

  @ApiProperty({
    example: 'https://cdn.example.com/img/fruits-thumb.png',
    nullable: true,
  })
  thumbnailUrl: string | null;
}

export class CategoryResponseDto {
  @ApiProperty({ example: 'd4e5f6a7-b8c9-0123-defa-234567890123' })
  id: string;

  @ApiProperty({ example: 'Basics' })
  name: string;

  @ApiProperty({ example: '#E8F4FF', nullable: true })
  backgroundColor: string | null;

  @ApiProperty({ example: '#1565C0', nullable: true })
  foregroundColor: string | null;

  @ApiProperty({
    example: 26,
    description:
      'Total number of activities across all topics in this category',
  })
  lessonCount: number;

  @ApiProperty({ type: [SubCategoryResponseDto] })
  subCategories: SubCategoryResponseDto[];
}

export class CategoryArrayResponseDto extends BaseResponse {
  @ApiProperty({ type: [CategoryResponseDto] })
  data: CategoryResponseDto[];
}

export class SubCategoryArrayResponseDto extends BaseResponse {
  @ApiProperty({ type: [SubCategoryResponseDto] })
  data: SubCategoryResponseDto[];
}

export class TopicArrayResponseDto extends BaseResponse {
  @ApiProperty({ type: [TopicResponseDto] })
  data: TopicResponseDto[];
}

export class TopicActivitiesResponseDto extends BaseResponse {
  @ApiProperty({
    type: [ActivityTabDto],
    description:
      'Activity tabs for the topic, always in fixed order: ' +
      'Learn → Writing → Wordhunt → PicQuest. ' +
      'Tabs that the admin has not created for this topic are omitted.',
  })
  data: ActivityTabDto[];
}
