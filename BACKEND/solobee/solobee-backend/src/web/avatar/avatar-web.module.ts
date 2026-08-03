import { Module } from '@nestjs/common';
import { AvatarController } from './controller/avatar.controller';
import { AvatarDomainModule } from 'src/domain/avatar/avatar-domain.module';

@Module({
  imports: [AvatarDomainModule],
  controllers: [AvatarController],
})
export class AvatarControllerModule {}
