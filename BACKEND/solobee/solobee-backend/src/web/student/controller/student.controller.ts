import {
  Controller,
  Get,
  Post,
  Body,
  Inject,
  Query,
  Req,
  Param,
  Delete,
} from '@nestjs/common';
import {
  ApiTags,
  ApiBearerAuth,
  ApiOperation,
  ApiResponse,
  ApiExtraModels,
} from '@nestjs/swagger';
import {
  STUDENT_SERVICE,
  type IStudentService,
} from 'src/domain/student/service/student.service.interface';
import { Roles } from 'src/core/decorators/roles.decorator';
import { Role } from 'src/core/utils/role.enum';
import {
  CreateStudentDto,
  GetStudentsQueryDto,
} from '../model/request/create-student.dto';
import { ErrorResponse, ObjectResponse } from 'src/core/utils/base-response';
import {
  StudentArrayResponseDto,
  StudentProfileResponseDto,
  StudentStatisticsResponseDto,
  StudentWeeklyStatisticsResponseDto,
} from '../model/response/student-response.dto';
import { StudentWebMapper } from '../mapper/student-web.mapper';
import { Public } from 'src/core/decorators/public.decorator';
import { JwtPayload } from 'src/infrastructure/jwt/jwt.strategy';

@ApiTags('Students')
@ApiBearerAuth()
@ApiExtraModels(StudentArrayResponseDto, ErrorResponse)
@Controller('students')
export class StudentController {
  constructor(
    @Inject(STUDENT_SERVICE)
    private readonly studentService: IStudentService,
    private readonly webMapper: StudentWebMapper,
  ) {}

  @Public()
  @Post('register')
  @ApiOperation({ summary: 'Register a new student' })
  @ApiResponse({
    status: 201,
    description: 'Student successfully registered',
    type: ObjectResponse,
  })
  @ApiResponse({ status: 400, description: 'Bad Request', type: ErrorResponse })
  async create(
    // @Req() request: { user: JwtPayload },
    @Body() dto: CreateStudentDto,
  ) {
    // const kindergartenId: string = request.user.kindergartenId;

    const response: boolean = await this.studentService.createStudent(
      this.webMapper.toCreateParams(dto),
    );

    return new ObjectResponse(response);
  }

  // @Roles(Role.ADMIN)
  // @Get()
  // @ApiOperation({ summary: 'Get all students in the kindergarten' })
  // @ApiResponse({
  //   status: 200,
  //   description: 'List of students',
  //   type: StudentArrayResponseDto,
  // })
  // @ApiResponse({
  //   status: 401,
  //   description: 'Unauthorized',
  //   type: ErrorResponse,
  // })
  // async findAllByKindergartenId() {
  //   const students = await this.studentService.findAllStudents(kindergartenId);

  //   const response = new StudentArrayResponseDto();
  //   response.data = students.map((s) => this.webMapper.toResponseDto(s));
  //   return response;
  // }

  @Roles(Role.SUPER_ADMIN)
  @Delete(':id')
  @ApiOperation({ summary: 'Delete a student (SUPER_ADMIN only)' })
  @ApiResponse({
    status: 200,
    description: 'Student successfully deleted',
    type: ObjectResponse,
  })
  @ApiResponse({
    status: 401,
    description: 'Unauthorized',
    type: ErrorResponse,
  })
  async deleteStudent(@Param('id') id: string) {
    const response = await this.studentService.deleteStudent(id);
    return new ObjectResponse(response);
  }

  @Roles(Role.SUPER_ADMIN)
  @Get()
  @ApiOperation({ summary: 'Get all students' })
  @ApiResponse({
    status: 200,
    description: 'List of students',
    type: StudentArrayResponseDto,
  })
  @ApiResponse({
    status: 401,
    description: 'Unauthorized',
    type: ErrorResponse,
  })
  async getAll(@Query() query: GetStudentsQueryDto) {
    const { items, total } = await this.studentService.findAll(
      query.offset,
      query.limit,
    );

    return new ObjectResponse({
      items: items.map((s) => this.webMapper.toListItemDto(s)),
      total,
    });
  }

  @Roles(Role.STUDENT)
  @Get('profile')
  @ApiOperation({ summary: 'Get student profile' })
  @ApiResponse({
    status: 200,
    description: 'Student profile',
    type: StudentProfileResponseDto,
  })
  async getProfile(@Req() req: { user: JwtPayload }) {
    return this.studentService.getStudentProfile(req.user.id);
  }

  @Roles(Role.STUDENT)
  @Get('statistics')
  @ApiOperation({ summary: "Get student's today statistics" })
  @ApiResponse({
    status: 200,
    description: "Student's today statistics",
    type: StudentStatisticsResponseDto,
  })
  async getStatistics(@Req() req: { user: JwtPayload }) {
    return this.studentService.getStudentStatistics(req.user.id);
  }

  @Roles(Role.STUDENT)
  @Get('statistics/weekly')
  @ApiOperation({ summary: "Get student's weekly statistics" })
  @ApiResponse({
    status: 200,
    description: "Student's weekly statistics (Monday–Sunday)",
    type: StudentWeeklyStatisticsResponseDto,
  })
  async getWeeklyStatistics(@Req() req: { user: JwtPayload }) {
    return this.studentService.getWeeklyStatistics(req.user.id);
  }
}
