import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { KindergartenEntity } from './model/kindergarten.entity';
import { KindergartenDataMapper } from './mapper/kindergarten-data.mapper';
import { KINDERGARTEN_REPOSITORY } from './repository/kindergarten.repository.interface';
import { KindergartenRepositoryImpl } from './repository/kindergarten.repository.impl';

@Module({
  imports: [TypeOrmModule.forFeature([KindergartenEntity])],
  providers: [
    KindergartenDataMapper,
    {
      provide: KINDERGARTEN_REPOSITORY,
      useClass: KindergartenRepositoryImpl,
    },
  ],
  exports: [KINDERGARTEN_REPOSITORY],
})
export class KindergartenDatasourceModule {}
