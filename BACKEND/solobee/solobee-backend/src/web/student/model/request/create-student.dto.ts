import {
  IsNotEmpty,
  IsString,
  IsNumber,
  IsInt,
  IsOptional,
  Min,
  Max,
} from 'class-validator';
import { Type } from 'class-transformer';
import { ApiProperty, ApiPropertyOptional } from '@nestjs/swagger';

export class CreateStudentDto {
  @ApiProperty({ example: 'Aziz' })
  @IsNotEmpty()
  @IsString()
  firstName: string;

  @ApiProperty({ example: 'Karimov' })
  @IsNotEmpty()
  @IsString()
  lastName: string;

  @ApiProperty({ example: 'Azik123' })
  @IsNotEmpty()
  @IsString()
  userName: string;

  @ApiProperty({ example: 'password123' })
  @IsNotEmpty()
  @IsString()
  password: string;

  @ApiProperty({ example: '6' })
  @IsNotEmpty()
  @IsNumber()
  age: number;

  @ApiProperty({ example: 'idq232324234dsd' })
  @IsNotEmpty()
  @IsString()
  avatarId: string;
}

export class GetStudentsQueryDto {
  @ApiPropertyOptional({
    example: 0,
    description: 'Nechinchi elementdan boshlab olish',
  })
  @IsOptional()
  @Type(() => Number)
  @IsInt()
  @Min(0)
  offset: number = 0;

  @ApiPropertyOptional({ example: 10, description: 'Nechta element olish' })
  @IsOptional()
  @Type(() => Number)
  @IsInt()
  @Min(1)
  @Max(100)
  limit: number = 10;
}
