import { Module } from '@nestjs/common';
import { EventEmitterModule } from '@nestjs/event-emitter';
import { PROGRESS_SERVICE } from './service/progress.service.interface';
import { ProgressServiceImpl } from './service/progress.service.impl';
import { ProgressListener } from './service/progress.listener';
import { ProgressDatasourceModule } from 'src/datasource/progress/progress-datasource.module';
import { StudentDatasourceModule } from 'src/datasource/student/student-datasource.module';
import { CoursesDatasourceModule } from 'src/datasource/courses/courses-datasource.module';

@Module({
  imports: [
    EventEmitterModule,
    ProgressDatasourceModule,
    StudentDatasourceModule,
    CoursesDatasourceModule,
  ],
  providers: [
    {
      provide: PROGRESS_SERVICE,
      useClass: ProgressServiceImpl,
    },
    ProgressListener,
  ],
  exports: [PROGRESS_SERVICE],
})
export class ProgressDomainModule {}
