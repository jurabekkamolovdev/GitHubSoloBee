import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { AvatarEntity } from './model/avatar.entity';
import { AvatarDataMapper } from './mapper/avatar.data.mapper';
import { AVATAR_REPOSITORY } from './repository/avatar.repository.interface';
import { AvatarRepositoryImpl } from './repository/avatar.repository.impl';

@Module({
  imports: [TypeOrmModule.forFeature([AvatarEntity])],
  providers: [
    AvatarDataMapper,
    {
      provide: AVATAR_REPOSITORY,
      useClass: AvatarRepositoryImpl,
    },
  ],
  exports: [AVATAR_REPOSITORY],
})
export class AvatarDatasourceModule {}
