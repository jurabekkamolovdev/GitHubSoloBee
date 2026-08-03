import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { StudentEntity } from './model/student.entity';
import { StudentDataMapper } from './mapper/student-data.mapper';
import { STUDENT_REPOSITORY } from './repository/student.repository.interface';
import { StudentRepositoryImpl } from './repository/student.repository.impl';

@Module({
  imports: [TypeOrmModule.forFeature([StudentEntity])],
  providers: [
    StudentDataMapper,
    {
      provide: STUDENT_REPOSITORY,
      useClass: StudentRepositoryImpl,
    },
  ],
  exports: [STUDENT_REPOSITORY],
})
export class StudentDatasourceModule {}
