import { Module } from '@nestjs/common';
import { ConfigModule } from '@nestjs/config';
import { MinioAdapter } from './minio.adapter';
import { STORAGE_SERVICE } from './storage.interface';

@Module({
  imports: [ConfigModule],
  providers: [
    {
      provide: STORAGE_SERVICE,
      useClass: MinioAdapter,
    },
  ],
  exports: [STORAGE_SERVICE],
})
export class StorageModule {}
