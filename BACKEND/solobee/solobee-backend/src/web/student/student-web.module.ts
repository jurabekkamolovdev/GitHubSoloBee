import { Module } from '@nestjs/common';
import { StudentDomainModule } from 'src/domain/student/student-domain.module';
import { StudentController } from './controller/student.controller';
import { AppJwtModule } from 'src/infrastructure/jwt/jwt.module';
import { StudentWebMapper } from './mapper/student-web.mapper';

@Module({
  imports: [StudentDomainModule, AppJwtModule],
  providers: [StudentWebMapper],
  controllers: [StudentController],
})
export class StudentControllerModule {}
