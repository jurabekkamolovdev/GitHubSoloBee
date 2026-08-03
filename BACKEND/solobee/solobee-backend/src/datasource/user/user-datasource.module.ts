import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { UserEntity } from './model/user.entity';
import { UserDataMapper } from './mapper/user-data.mapper';
import { USER_REPOSITORY } from './repository/user.repository.interface';
import { UserRepositoryImpl } from './repository/user.repository.impl';

@Module({
  imports: [TypeOrmModule.forFeature([UserEntity])],
  providers: [
    UserDataMapper,
    {
      provide: USER_REPOSITORY,
      useClass: UserRepositoryImpl,
    },
  ],
  exports: [USER_REPOSITORY],
})
export class UserDatasourceModule {}
