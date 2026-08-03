// domain/avatar/avatar-domain.module.ts
import { Module } from '@nestjs/common';
import { AVATAR_SERVICE } from './service/avatar.service.interface';
import { AvatarServiceImpl } from './service/avatar.service.impl';
import { AvatarDatasourceModule } from 'src/datasource/avatar/avatar-datasource.module';
import { StorageModule } from 'src/infrastructure/storage/storage.module';

@Module({
  imports: [AvatarDatasourceModule, StorageModule],
  providers: [
    {
      provide: AVATAR_SERVICE,
      useClass: AvatarServiceImpl,
    },
  ],
  exports: [AVATAR_SERVICE],
})
export class AvatarDomainModule {}
