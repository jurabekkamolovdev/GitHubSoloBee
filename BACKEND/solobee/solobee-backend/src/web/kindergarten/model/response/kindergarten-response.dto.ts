import { ApiProperty } from '@nestjs/swagger';
import { BaseResponse } from '../../../../core/utils/base-response';

export class KindergartenResponseDto {
  @ApiProperty({ example: '65f1a2b3c4d5e6f7a8b9c0d1' })
  id: string;

  @ApiProperty({ example: "Savodxon Bog'chasi" })
  name: string;

  @ApiProperty({ example: 'Toshkent sh., Yunusobod' })
  address?: string | null;

  @ApiProperty({ example: '2026-03-12T23:27:17.000Z' })
  createdAt: Date;
}
export class KindergartenObjectResponseDto extends BaseResponse {
  @ApiProperty({ type: KindergartenResponseDto })
  data: KindergartenResponseDto;
}

export class KindergartenArrayResponseDto extends BaseResponse {
  @ApiProperty({ type: [KindergartenResponseDto] })
  data: KindergartenResponseDto[];
}
