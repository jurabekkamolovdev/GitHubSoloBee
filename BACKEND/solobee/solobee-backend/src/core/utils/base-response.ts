import { ApiProperty } from '@nestjs/swagger';

export class BaseResponse {
  @ApiProperty({ example: 'success' })
  status: 'success' | 'fail';

  @ApiProperty({ example: '2026-03-12T23:27:17.000Z' })
  timestamp: string;

  constructor(status: 'success' | 'fail' = 'success') {
    this.status = status;
    this.timestamp = new Date().toISOString();
  }
}

export class ErrorResponse extends BaseResponse {
  @ApiProperty({ example: '/auth/login' })
  path: string;

  @ApiProperty({
    example: {
      message: 'Invalid credentials',
      error: 'Unauthorized',
      statusCode: 401,
    },
  })
  error: any;

  constructor(path: string, error: any) {
    super('fail');
    this.path = path;
    this.error = error;
  }
}

export class ObjectResponse<T> extends BaseResponse {
  @ApiProperty()
  data: T;

  constructor(data: T) {
    super('success');
    this.data = data;
  }
}

export class ArrayResponse<T> extends BaseResponse {
  @ApiProperty()
  data: T[];

  constructor(data: T[]) {
    super('success');
    this.data = data;
  }
}

export class PaginationMeta {
  @ApiProperty({ example: 100 })
  totalItems: number;

  @ApiProperty({ example: 10 })
  itemCount: number;

  @ApiProperty({ example: 10 })
  itemsPerPage: number;

  @ApiProperty({ example: 10 })
  totalPages: number;

  @ApiProperty({ example: 1 })
  currentPage: number;
}

export class PaginatedResponse<T> extends BaseResponse {
  @ApiProperty()
  data: T[];

  @ApiProperty({ type: PaginationMeta })
  meta: PaginationMeta;

  constructor(data: T[], meta: PaginationMeta) {
    super('success');
    this.data = data;
    this.meta = meta;
  }
}
