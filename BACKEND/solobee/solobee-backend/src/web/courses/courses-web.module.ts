import { Module } from '@nestjs/common';
import { CoursesController } from './controller/courses.controller';
import { CoursesWebMapper } from './mapper/courses-web.mapper';
import { CoursesDomainModule } from 'src/domain/courses/courses-domain.module';

@Module({
  imports: [CoursesDomainModule],
  providers: [CoursesWebMapper],
  controllers: [CoursesController],
})
export class CoursesControllerModule {}
