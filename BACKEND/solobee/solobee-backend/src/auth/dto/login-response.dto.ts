import { ApiProperty } from '@nestjs/swagger';
import { BaseResponse } from 'src/core/utils/base-response';

export class LoginResponseDataDto {
  @ApiProperty({ example: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...' })
  accessToken: string;

  @ApiProperty({ example: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...' })
  refreshToken: string;
}

export class LoginResponseDto extends BaseResponse {
  @ApiProperty({ type: LoginResponseDataDto })
  data: LoginResponseDataDto;
}
