import { Injectable } from '@nestjs/common';
import { CreateStudentDto } from '../model/request/create-student.dto';
import { ICreateStudent } from 'src/domain/student/service/student.service.interface';
import { Student } from 'src/domain/student/model/student.model';
import { StudentListItemResponseDto } from '../model/response/student-response.dto';

@Injectable()
export class StudentWebMapper {
  toCreateParams(dto: CreateStudentDto): ICreateStudent {
    return {
      firstName: dto.firstName,
      lastName: dto.lastName,
      userName: dto.userName,
      password: dto.password,
      age: dto.age,
      avatarId: dto.avatarId,
    };
  }
  toListItemDto(student: Student): StudentListItemResponseDto {
    const dto = new StudentListItemResponseDto();
    dto.id = student.getId();
    dto.username = student.getUsername();
    dto.firstName = student.getFirstName();
    dto.lastName = student.getLastName();
    dto.age = student.getAge();
    return dto;
  }

  //   toNewStudentResponseDto(student: INewStudent): NewStudentResponseDto {
  //     const dto = new NewStudentResponseDto();
  //     dto.id = student.id;
  //     dto.fullName = student.fullName;
  //     dto.username = student.username;
  //     dto.plainPassword = student.plainPassword;
  //     dto.kindergartenId = student.kindergartenId;
  //     return dto;
  //   }

  //   toObjectResponse(student: Student): StudentObjectResponseDto {
  //     const response = new StudentObjectResponseDto();
  //     response.data = this.toResponseDto(student);
  //     return response;
  //   }

  //   toNewStudentObjectResonse(
  //     newStudent: INewStudent,
  //   ): NewStudentObjectResponseDto {
  //     const response = new NewStudentObjectResponseDto();
  //     response.data = this.toNewStudentResponseDto(newStudent);
  //     return response;
  //   }

  //   toProfileDto(student: Student): StudentProfileDto {
  //     const dto = new StudentProfileDto();
  //     dto.firstName = student.getFirstName();
  //     dto.lastName = student.getLastName();
  //     dto.score = student.getScore();
  //     return dto;
  //   }
}

// // Username: jurabek-c4e2
// // Password: )M$E63PAth
