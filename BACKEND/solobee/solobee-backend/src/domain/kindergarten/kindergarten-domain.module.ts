import { Module } from '@nestjs/common';
import { KindergartenDatasourceModule } from 'src/datasource/kindergarten/kindergarten-datasource.module';
import { UserDomainModule } from '../user/user-domain.module';
import { KINDERGARTEN_SERVICE } from './service/kindergarten.service.interface';
import { KindergartenServiceImpl } from './service/kindergarten.service.impl';

@Module({
  imports: [KindergartenDatasourceModule, UserDomainModule],
  providers: [
    {
      provide: KINDERGARTEN_SERVICE,
      useClass: KindergartenServiceImpl,
    },
  ],
  exports: [KINDERGARTEN_SERVICE],
})
export class KindergartenDomainModule {}
