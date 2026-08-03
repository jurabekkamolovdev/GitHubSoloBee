import { Module } from '@nestjs/common';
import { AppConfigModule } from './config/config.module';
import { DatabaseModule } from './database/database.module';
import { AppJwtModule } from './jwt/jwt.module';
import { StorageModule } from './storage/storage.module';

@Module({
  imports: [AppConfigModule, DatabaseModule, AppJwtModule, StorageModule],
  exports: [AppConfigModule, DatabaseModule, AppJwtModule, StorageModule],
})
export class InfrastructureModule {}
