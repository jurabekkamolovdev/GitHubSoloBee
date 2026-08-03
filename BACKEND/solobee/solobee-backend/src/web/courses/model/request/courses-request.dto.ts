import {
  IsString,
  IsNotEmpty,
  IsOptional,
  IsNumber,
  IsUUID,
  IsObject,
  IsEnum,
  IsArray,
  ArrayMinSize,
  ValidateNested,
} from 'class-validator';
import { Type } from 'class-transformer';
import { ApiProperty, ApiExtraModels, getSchemaPath } from '@nestjs/swagger';
import { ActivityType } from 'src/domain/courses/model/activity.model';

export class CreateCategoryDto {
  @IsNotEmpty()
  @IsString()
  name: string;

  @IsOptional()
  @IsString()
  backgroundColor?: string;

  @IsOptional()
  @IsString()
  foregroundColor?: string;

  @IsOptional()
  @IsNumber()
  orderIndex?: number;
}

export class UpdateCategoryDto extends CreateCategoryDto {}

export class CreateSubCategoryDto {
  @IsNotEmpty()
  @IsString()
  name: string;

  @IsNotEmpty()
  @IsUUID()
  categoryId: string;

  @IsOptional()
  @IsString()
  thumbnailKey?: string;

  @IsOptional()
  @IsNumber()
  orderIndex?: number;
}

export class UpdateSubCategoryDto {
  @IsOptional()
  @IsString()
  name?: string;

  @IsOptional()
  @IsString()
  thumbnailKey?: string;

  @IsOptional()
  @IsNumber()
  orderIndex?: number;
}

export class CreateTopicDto {
  @IsNotEmpty()
  @IsUUID()
  subCategoryId: string;

  @IsOptional()
  @IsString()
  thumbnailKey?: string;

  @IsOptional()
  @IsNumber()
  orderIndex?: number;
}

export class UpdateTopicDto {
  @IsOptional()
  @IsString()
  thumbnailKey?: string;

  @IsOptional()
  @IsNumber()
  orderIndex?: number;
}

export class ReorderItemDto {
  @IsNotEmpty()
  @IsUUID()
  id: string;

  @IsNumber()
  orderIndex: number;
}

export class ReorderTopicsDto {
  @IsArray()
  @ArrayMinSize(1)
  @ValidateNested({ each: true })
  @Type(() => ReorderItemDto)
  items: ReorderItemDto[];
}

export class LearnPayloadInputDto {
  @ApiProperty({
    example: 'learn/90f2369f-c29c-4d93-82ad-5b9d85464f3c.png',
    required: false,
    description: 'Storage key of the hero image shown on the Learn screen',
  })
  imageKey?: string;

  @ApiProperty({
    example: 'learn/d329bb28-1363-418f-89b7-8b5ce1d3f19a.mp3',
    required: false,
    description:
      'Storage key of the pronunciation audio played on the Learn screen',
  })
  audioKey?: string;
}

export class WritingTracePayloadInputDto {
  @ApiProperty({
    enum: ['trace'],
    example: 'trace',
    description:
      'Trace mode: user traces a letter shape. Used for alphabet topics.',
  })
  mode: 'trace';
}

export class WritingSpellPayloadInputDto {
  @ApiProperty({
    enum: ['spell'],
    example: 'spell',
    description:
      'Spell mode: user picks letters in order. Used for word topics.',
  })
  mode: 'spell';

  @ApiProperty({
    example: 'BANANA',
    description: 'Correct answer the user must spell',
  })
  answer: string;

  @ApiProperty({
    example: ['A', 'F', 'G', 'C', 'B', 'R', 'K', 'S', 'P', 'O'],
    type: [String],
    description:
      'Letters to display (including distractors), in the order shown',
  })
  shuffledLetters: string[];
}

export class WordhuntOptionInputDto {
  @ApiProperty({
    example: 'wordhunt/option-a.webp',
    description:
      'Storage key of the fully pre-rendered option image. The mobile client ' +
      'displays this image as-is (gradient, background color, and text are ' +
      'baked into the upload), so no text styling is done on-device.',
  })
  imageKey: string;

  @ApiProperty({
    example: 'wordhunt/letter-a.mp3',
    required: false,
    description:
      "Optional per-option audio storage key played from the option's speaker button",
  })
  audioKey?: string;

  @ApiProperty({
    example: true,
    description: 'Whether this is the correct answer',
  })
  isCorrect: boolean;
}

export class WordhuntPayloadInputDto {
  @ApiProperty({
    example: 'wordhunt/letter-a-prompt.webp',
    required: false,
    description:
      'Storage key of the prompt image shown at the top (e.g. the letter the student must find)',
  })
  imageKey?: string;

  @ApiProperty({ type: [WordhuntOptionInputDto] })
  options: WordhuntOptionInputDto[];
}

export class PicQuestOptionInputDto {
  @ApiProperty({
    example: 'picquest/letter-a.webp',
    description: 'Storage key of the image shown as this option',
  })
  imageKey: string;

  @ApiProperty({
    example: 'Letter A',
    required: false,
    description: 'Optional label shown under the image',
  })
  label?: string;

  @ApiProperty({
    example: true,
    description: 'Whether this is the correct answer',
  })
  isCorrect: boolean;
}

export class PicQuestPayloadInputDto {
  @ApiProperty({
    example: 'picquest/letter-a-prompt.mp3',
    required: false,
    description:
      'Storage key of the prompt audio (e.g. letter pronunciation) played before the student picks an image',
  })
  audioKey?: string;

  @ApiProperty({ type: [PicQuestOptionInputDto] })
  options: PicQuestOptionInputDto[];
}

@ApiExtraModels(
  LearnPayloadInputDto,
  WritingTracePayloadInputDto,
  WritingSpellPayloadInputDto,
  WordhuntPayloadInputDto,
  PicQuestPayloadInputDto,
)
export class CreateActivityDto {
  @IsNotEmpty()
  @IsEnum(ActivityType)
  @ApiProperty({
    enum: ActivityType,
    example: ActivityType.LEARN,
    description: 'Activity type. Determines the required shape of `payload`.',
  })
  type: ActivityType;

  @IsNotEmpty()
  @IsUUID()
  @ApiProperty({
    example: 'b2c3d4e5-f6a7-8901-bcde-f12345678901',
    description: 'UUID of the parent topic',
  })
  topicId: string;

  @IsNotEmpty()
  @IsObject()
  @ApiProperty({
    description:
      'Type-specific payload. Shape depends on `type`:\n' +
      '- `LEARN` → LearnPayloadInputDto — hero `imageKey` + `audioKey`.\n' +
      '- `WRITING` → WritingTracePayloadInputDto | WritingSpellPayloadInputDto (discriminated by `mode`).\n' +
      '- `WORDHUNT` → WordhuntPayloadInputDto — top-level prompt `imageKey?` + `options[]` of `{ imageKey, audioKey?, isCorrect }`.\n' +
      '- `PICQUEST` → PicQuestPayloadInputDto — top-level prompt `audioKey?` + `options[]` of `{ imageKey, label?, isCorrect }`.\n\n' +
      'Storage keys come from the presigned-URL upload flow (see POST /storage/presigned-url).',
    oneOf: [
      { $ref: getSchemaPath(LearnPayloadInputDto) },
      { $ref: getSchemaPath(WritingTracePayloadInputDto) },
      { $ref: getSchemaPath(WritingSpellPayloadInputDto) },
      { $ref: getSchemaPath(WordhuntPayloadInputDto) },
      { $ref: getSchemaPath(PicQuestPayloadInputDto) },
    ],
  })
  payload: Record<string, any>;

  @IsOptional()
  @IsNumber()
  @ApiProperty({
    required: false,
    example: 0,
    description: 'Sort order within the topic',
  })
  orderIndex?: number;
}

export class UpdateActivityDto {
  @IsOptional()
  @IsObject()
  @ApiProperty({
    required: false,
    description:
      'Partial payload patch. Merged into the stored payload by the server — ' +
      'only include fields you want to change. For WORDHUNT/PICQUEST, ' +
      '`options` are merged by array index, so previously uploaded media on ' +
      'untouched options is preserved. See CreateActivityDto.payload for ' +
      'the per-type shapes.',
    oneOf: [
      { $ref: getSchemaPath(LearnPayloadInputDto) },
      { $ref: getSchemaPath(WritingTracePayloadInputDto) },
      { $ref: getSchemaPath(WritingSpellPayloadInputDto) },
      { $ref: getSchemaPath(WordhuntPayloadInputDto) },
      { $ref: getSchemaPath(PicQuestPayloadInputDto) },
    ],
  })
  payload?: Record<string, any>;

  @IsOptional()
  @IsNumber()
  @ApiProperty({ required: false, example: 0 })
  orderIndex?: number;
}
