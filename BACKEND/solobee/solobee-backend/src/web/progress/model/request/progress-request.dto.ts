import { IsOptional, IsString } from 'class-validator';
import { ApiPropertyOptional } from '@nestjs/swagger';

export class ReportAttemptDto {
  @ApiPropertyOptional({
    description: 'Optional extra data',
    example: 'RED',
  })
  @IsOptional()
  @IsString()
  result?: string;
}
