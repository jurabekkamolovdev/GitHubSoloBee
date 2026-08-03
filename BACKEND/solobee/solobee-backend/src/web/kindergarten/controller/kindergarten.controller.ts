import {
  Controller,
  Post,
  Get,
  Param,
  Delete,
  Body,
  Inject,
} from '@nestjs/common';
import { Roles } from 'src/core/decorators/roles.decorator';
import { Role } from 'src/core/utils/role.enum';
import { CreateKindergartenDto } from '../model/request/create-kindergarten.dto';
import { KindergartenObjectResponseDto } from '../model/response/kindergarten-response.dto';
import { KindergartenArrayResponseDto } from '../model/response/kindergarten-response.dto';
import { KindergartenWebMapper } from '../mapper/kindergarten-web.mapper';
import { KINDERGARTEN_SERVICE } from 'src/domain/kindergarten/service/kindergarten.service.interface';
import { type IKindergartenService } from 'src/domain/kindergarten/service/kindergarten.service.interface';
import {
  ErrorResponse,
  ObjectResponse,
} from '../../../core/utils/base-response';
import {
  ApiTags,
  ApiOperation,
  ApiResponse,
  ApiBearerAuth,
  ApiExtraModels,
} from '@nestjs/swagger';

@ApiTags('Kindergartens')
@ApiBearerAuth()
@ApiExtraModels(
  KindergartenObjectResponseDto,
  KindergartenArrayResponseDto,
  ErrorResponse,
)
@Controller('kindergartens')
export class KindergartenController {
  constructor(
    @Inject(KINDERGARTEN_SERVICE)
    private readonly kindergartenService: IKindergartenService,
    private readonly webMapper: KindergartenWebMapper,
  ) {}

  @Roles(Role.SUPER_ADMIN)
  @Post()
  @ApiOperation({ summary: 'Create a new kindergarten' })
  @ApiResponse({
    status: 201,
    description: 'Kindergarten successfully created',
    type: KindergartenObjectResponseDto,
  })
  @ApiResponse({ status: 400, description: 'Bad Request', type: ErrorResponse })
  @ApiResponse({ status: 403, description: 'Forbidden', type: ErrorResponse })
  async create(@Body() createDto: CreateKindergartenDto) {
    const result = await this.kindergartenService.create(
      this.webMapper.toCreateParams(createDto),
    );
    return this.webMapper.toObjectResponse(result);
  }

  @Roles(Role.SUPER_ADMIN)
  @Get()
  @ApiOperation({ summary: 'Get all kindergartens' })
  @ApiResponse({
    status: 200,
    description: 'List of kindergartens',
    type: KindergartenArrayResponseDto,
  })
  @ApiResponse({ status: 403, description: 'Forbidden', type: ErrorResponse })
  async findAll() {
    const kindergartens = await this.kindergartenService.findAll();
    const response = new KindergartenArrayResponseDto();
    response.data = kindergartens.map((k) => this.webMapper.toResponseDto(k));
    return response;
  }

  @Roles(Role.SUPER_ADMIN)
  @Delete(':id')
  @ApiOperation({
    summary:
      'Delete a kindergarten and all its associated data (admins, students)',
  })
  @ApiResponse({
    status: 200,
    description: 'Kindergarten deleted successfully',
  })
  @ApiResponse({ status: 403, description: 'Forbidden', type: ErrorResponse })
  @ApiResponse({
    status: 404,
    description: 'Kindergarten not found',
    type: ErrorResponse,
  })
  async remove(@Param('id') id: string) {
    const result: boolean = await this.kindergartenService.delete(id);
    return new ObjectResponse(result);
  }
}
