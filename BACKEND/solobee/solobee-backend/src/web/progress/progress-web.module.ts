import { Module } from '@nestjs/common';
import { ProgressController } from './controller/progress.controller';
import { ProgressDomainModule } from 'src/domain/progress/progress-domain.module';

@Module({
  imports: [ProgressDomainModule],
  controllers: [ProgressController],
})
export class ProgressControllerModule {}
