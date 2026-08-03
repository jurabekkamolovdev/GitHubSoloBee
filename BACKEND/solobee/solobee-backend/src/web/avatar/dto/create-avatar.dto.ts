// presentation/avatar/dto/create-avatar.dto.ts
import {
  IsNotEmpty,
  IsOptional,
  IsEnum,
  IsString,
  IsNumber,
} from 'class-validator';
import { AvatarGender } from 'src/datasource/avatar/model/avatar.entity';

export class CreateAvatarDto {
  @IsNotEmpty()
  @IsEnum(AvatarGender)
  gender: AvatarGender;

  @IsNotEmpty()
  @IsString()
  thumbnailKey: string;

  @IsOptional()
  @IsNumber()
  orderIndex?: number;
}
