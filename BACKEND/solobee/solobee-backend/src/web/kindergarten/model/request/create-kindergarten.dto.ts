import { IsNotEmpty, IsOptional, IsString, MinLength } from 'class-validator';
import { ApiProperty } from '@nestjs/swagger';

export class CreateKindergartenDto {
  @ApiProperty({ example: "Savodxon Bog'chasi" })
  @IsNotEmpty()
  @IsString()
  name: string;

  @ApiProperty({ example: 'Toshkent sh., Yunusobod' })
  @IsOptional()
  @IsString()
  address?: string;

  @ApiProperty({ example: 'k_admin' })
  @IsNotEmpty()
  @IsString()
  adminUsername: string;

  @ApiProperty({ example: 'password123' })
  @IsNotEmpty()
  @IsString()
  @MinLength(6)
  adminPassword: string;
}
