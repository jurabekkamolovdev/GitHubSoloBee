import { ApiProperty } from '@nestjs/swagger';
import { BaseResponse } from '../../../../core/utils/base-response';

export class StudentListItemResponseDto {
  @ApiProperty()
  id: string;

  @ApiProperty()
  username: string;

  @ApiProperty()
  firstName: string;

  @ApiProperty()
  lastName: string;

  @ApiProperty()
  age: number;
}

export class StudentArrayResponseDto extends BaseResponse {
  @ApiProperty({ type: [StudentListItemResponseDto] })
  items: StudentListItemResponseDto[];

  @ApiProperty({ example: 42, description: 'Jami studentlar soni' })
  total: number;
}

export class StudentProfileDataDto {
  @ApiProperty()
  firstName: string;

  @ApiProperty()
  lastName: string;

  @ApiProperty()
  username: string;

  @ApiProperty()
  age: number;

  @ApiProperty()
  avatar: string;
}

export class StudentProfileResponseDto extends BaseResponse {
  @ApiProperty({ type: StudentProfileDataDto })
  data: StudentProfileDataDto;
}

export class StudentStatisticsDto {
  @ApiProperty({
    example: 5,
    description: 'Bugun tugallangan activity-lar soni',
  })
  completedToday: number;
}

export class StudentStatisticsResponseDto extends BaseResponse {
  @ApiProperty({ type: StudentStatisticsDto })
  declare data: StudentStatisticsDto;
}

export class DailyCompletionDto {
  @ApiProperty({ example: '2026-07-27' })
  date: string;

  @ApiProperty({ example: 'Dushanba' })
  dayOfWeek: string;

  @ApiProperty({
    example: 3,
    description: 'Shu kuni tugallangan activity-lar soni',
  })
  completed: number;
}

export class StudentWeeklyStatisticsDto {
  @ApiProperty({ example: '2026-07-27', description: 'Hafta boshi (Dushanba)' })
  weekStart: string;

  @ApiProperty({
    example: '2026-08-02',
    description: 'Hafta oxiri (Yakshanba)',
  })
  weekEnd: string;

  @ApiProperty({ type: [DailyCompletionDto] })
  days: DailyCompletionDto[];
}

export class StudentWeeklyStatisticsResponseDto extends BaseResponse {
  @ApiProperty({ type: StudentWeeklyStatisticsDto })
  declare data: StudentWeeklyStatisticsDto;
}
