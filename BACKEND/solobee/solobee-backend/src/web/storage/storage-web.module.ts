import { Module } from '@nestjs/common';
import { StorageModule } from 'src/infrastructure/storage/storage.module';
import { StorageController } from './controller/storage.controller';

@Module({
  imports: [StorageModule],
  controllers: [StorageController],
})
export class StorageControllerModule {}
