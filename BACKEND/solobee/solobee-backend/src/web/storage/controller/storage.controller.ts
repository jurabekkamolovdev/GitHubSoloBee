import { Controller, Post, Body, Inject } from '@nestjs/common';
import {
  STORAGE_SERVICE,
  type IStorageService,
} from 'src/infrastructure/storage/storage.interface';
import { Roles } from '../../../core/decorators/roles.decorator';
import { Role } from '../../../core/utils/role.enum';
import { ApiTags, ApiOperation, ApiBody, ApiBearerAuth } from '@nestjs/swagger';

@ApiTags('Storage')
@ApiBearerAuth()
@Controller('storage')
export class StorageController {
  constructor(
    @Inject(STORAGE_SERVICE)
    private readonly storageService: IStorageService,
  ) {}

  @Roles(Role.SUPER_ADMIN)
  @Post('presigned-url')
  @ApiOperation({
    summary: 'Get a temporary URL to directly upload a file to MinIO/S3',
  })
  @ApiBody({
    schema: {
      type: 'object',
      properties: {
        folder: { type: 'string', example: 'categories' },
        fileName: { type: 'string', example: 'image.png' },
        contentType: { type: 'string', example: 'image/png' },
      },
    },
  })
  getUploadUrl(
    @Body('folder') folder: string,
    @Body('fileName') fileName: string,
    @Body('contentType') contentType: string,
  ) {
    return this.storageService.getPresignedUploadUrl(
      folder || 'misc',
      fileName,
      contentType,
    );
  }
}
