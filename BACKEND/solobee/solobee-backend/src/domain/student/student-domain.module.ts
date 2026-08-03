import { Module } from '@nestjs/common';
import { UserDomainModule } from '../user/user-domain.module';
import { STUDENT_SERVICE } from './service/student.service.interface';
import { StudentServiceImpl } from './service/student.service.impl';
import { StudentDatasourceModule } from 'src/datasource/student/student-datasource.module';
import { AvatarDomainModule } from '../avatar/avatar-domain.module';
import { StorageModule } from 'src/infrastructure/storage/storage.module';
import { ProgressDomainModule } from '../progress/progress-domain.module';

@Module({
  imports: [
    UserDomainModule,
    StudentDatasourceModule,
    AvatarDomainModule,
    StorageModule,
    ProgressDomainModule,
  ],
  providers: [
    {
      provide: STUDENT_SERVICE,
      useClass: StudentServiceImpl,
    },
  ],
  exports: [STUDENT_SERVICE],
})
export class StudentDomainModule {}
