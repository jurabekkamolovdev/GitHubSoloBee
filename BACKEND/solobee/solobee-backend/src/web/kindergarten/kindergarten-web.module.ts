import { Module } from '@nestjs/common';
import { KindergartenDomainModule } from 'src/domain/kindergarten/kindergarten-domain.module';
import { KindergartenController } from './controller/kindergarten.controller';
import { KindergartenWebMapper } from './mapper/kindergarten-web.mapper';

@Module({
  imports: [KindergartenDomainModule],
  providers: [KindergartenWebMapper],
  controllers: [KindergartenController],
})
export class KindergartenControllerModule {}
