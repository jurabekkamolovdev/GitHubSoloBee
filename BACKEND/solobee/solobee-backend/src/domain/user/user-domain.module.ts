import { Module } from '@nestjs/common';
import { USER_SERVICE } from './service/user.service.interface';
import { UserServiceImpl } from './service/user.service.impl';
import { UserDatasourceModule } from 'src/datasource/user/user-datasource.module';

@Module({
  imports: [UserDatasourceModule],
  providers: [
    {
      provide: USER_SERVICE,
      useClass: UserServiceImpl,
    },
  ],
  exports: [USER_SERVICE],
})
export class UserDomainModule {}
