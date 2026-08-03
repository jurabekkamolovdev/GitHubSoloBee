import { Module } from '@nestjs/common';
import { COURSES_SERVICE } from './service/courses.service.interface';
import { CoursesDatasourceModule } from 'src/datasource/courses/courses-datasource.module';
import { StorageModule } from 'src/infrastructure/storage/storage.module';
import { CourseMapper } from './service/course.mapper';
import { CoursesServiceImpl } from './service/courses.service.impl';
import { ProgressDomainModule } from '../progress/progress-domain.module';

@Module({
  imports: [CoursesDatasourceModule, StorageModule, ProgressDomainModule],
  providers: [
    CourseMapper,
    {
      provide: COURSES_SERVICE,
      useClass: CoursesServiceImpl,
    },
  ],
  exports: [COURSES_SERVICE],
})
export class CoursesDomainModule {}
