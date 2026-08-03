import {
  Controller,
  Post,
  Body,
  Get,
  Param,
  Put,
  Delete,
  Inject,
  Req,
} from '@nestjs/common';
import {
  ApiTags,
  ApiBearerAuth,
  ApiOperation,
  ApiResponse,
  ApiExtraModels,
} from '@nestjs/swagger';
import { COURSES_SERVICE } from 'src/domain/courses/service/courses.service.interface';
import { type ICoursesService } from 'src/domain/courses/service/courses.service.interface';
import { Roles } from '../../../core/decorators/roles.decorator';
import { Role } from '../../../core/utils/role.enum';
import { ErrorResponse } from '../../../core/utils/base-response';
import {
  CreateCategoryDto,
  UpdateCategoryDto,
  CreateSubCategoryDto,
  UpdateSubCategoryDto,
  CreateTopicDto,
  UpdateTopicDto,
  ReorderTopicsDto,
  CreateActivityDto,
  UpdateActivityDto,
} from '../model/request/courses-request.dto';
import {
  CategoryArrayResponseDto,
  SubCategoryArrayResponseDto,
  TopicArrayResponseDto,
  TopicActivitiesResponseDto,
} from '../model/response/courses-response.dto';
import { CoursesWebMapper } from '../mapper/courses-web.mapper';
import { JwtPayload } from 'src/infrastructure/jwt/jwt.strategy';

@ApiTags('Courses')
@ApiBearerAuth()
@ApiExtraModels(
  CategoryArrayResponseDto,
  SubCategoryArrayResponseDto,
  TopicArrayResponseDto,
  TopicActivitiesResponseDto,
  ErrorResponse,
)
@Controller('courses')
export class CoursesController {
  constructor(
    @Inject(COURSES_SERVICE)
    private readonly coursesService: ICoursesService,
    private readonly webMapper: CoursesWebMapper,
  ) {}

  @Get('categories')
  @ApiOperation({
    summary: 'Get all categories with their sub-categories',
    description:
      'Returns categories and their sub-categories (without topics/activities to keep payload small). ' +
      'Each category includes `lessonCount` (total activities across all its topics).',
  })
  @ApiResponse({
    status: 200,
    description: 'List of categories',
    type: CategoryArrayResponseDto,
  })
  @ApiResponse({
    status: 401,
    description: 'Unauthorized',
    type: ErrorResponse,
  })
  async findAllCategories() {
    const data = await this.coursesService.findAllCategories();
    return this.webMapper.toCategoryArrayResponse(data);
  }

  @Get('categories/:categoryId/subcategories')
  @ApiOperation({ summary: 'Get all sub-categories of a specific category' })
  @ApiResponse({
    status: 200,
    description: 'List of subcategories',
    type: SubCategoryArrayResponseDto,
  })
  async getSubCategoriesByCategory(@Param('categoryId') categoryId: string) {
    const data =
      await this.coursesService.findSubCategoriesByCategory(categoryId);
    return this.webMapper.toSubCategoryArrayResponse(data);
  }

  @Get('subcategories/:subCategoryId/topics')
  @ApiOperation({
    summary: 'Get all topics of a specific sub-category',
    description:
      'Returns topics without activities. Each topic includes a `completed` flag ' +
      "derived from the current student's progress.",
  })
  @ApiResponse({
    status: 200,
    description: 'List of topics',
    type: TopicArrayResponseDto,
  })
  @ApiResponse({
    status: 404,
    description: 'SubCategory not found',
    type: ErrorResponse,
  })
  async getTopicsBySubCategory(
    @Param('subCategoryId') subCategoryId: string,
    @Req() req: { user: JwtPayload },
  ) {
    const data = await this.coursesService.findTopicsBySubCategory(
      subCategoryId,
      req.user?.id,
    );
    return this.webMapper.toTopicArrayResponse(data);
  }

  @Get('topics/:topicId/activities')
  @ApiOperation({
    summary: 'Get the activity tabs of a specific topic',
    description:
      "Returns the topic's activity tabs in fixed order: " +
      'Learn → Writing → Wordhunt → PicQuest.',
  })
  @ApiResponse({
    status: 200,
    description: 'Topic activity tabs',
    type: TopicActivitiesResponseDto,
  })
  @ApiResponse({
    status: 404,
    description: 'Topic not found',
    type: ErrorResponse,
  })
  async getActivitiesByTopic(
    @Param('topicId') topicId: string,
    @Req() req: { user: JwtPayload },
  ) {
    const isStudent = req.user?.role === Role.STUDENT;
    const data = await this.coursesService.findActivitiesByTopic(
      topicId,
      isStudent ? req.user?.id : undefined,
      { revealAll: !isStudent },
    );
    return this.webMapper.toTopicActivitiesResponse(data);
  }

  @Roles(Role.SUPER_ADMIN)
  @Post('categories')
  @ApiOperation({ summary: '[ADMIN] Create a new category' })
  @ApiResponse({ status: 201, description: 'Category created' })
  async createCategory(@Body() dto: CreateCategoryDto): Promise<any> {
    return this.coursesService.createCategory(dto);
  }

  @Roles(Role.SUPER_ADMIN)
  @Put('categories/:id')
  @ApiOperation({ summary: '[ADMIN] Update a category' })
  @ApiResponse({ status: 200, description: 'Category updated' })
  async updateCategory(
    @Param('id') id: string,
    @Body() dto: UpdateCategoryDto,
  ): Promise<any> {
    return this.coursesService.updateCategory(id, dto);
  }

  @Roles(Role.SUPER_ADMIN)
  @Delete('categories/:id')
  @ApiOperation({
    summary:
      '[ADMIN] Delete a category (cascades to all sub-categories, topics, activities)',
  })
  @ApiResponse({ status: 200, description: 'Category deleted' })
  async deleteCategory(@Param('id') id: string): Promise<any> {
    return this.coursesService.deleteCategory(id);
  }

  @Get('subcategories/:id')
  @ApiOperation({ summary: 'Get a single sub-category' })
  @ApiResponse({ status: 200, description: 'SubCategory' })
  async getSubCategory(@Param('id') id: string): Promise<any> {
    return this.coursesService.findSubCategoryById(id);
  }

  @Roles(Role.SUPER_ADMIN)
  @Post('subcategories')
  @ApiOperation({ summary: '[ADMIN] Create a new sub-category' })
  @ApiResponse({ status: 201, description: 'SubCategory created' })
  async createSubCategory(@Body() dto: CreateSubCategoryDto): Promise<any> {
    return this.coursesService.createSubCategory(dto);
  }

  @Roles(Role.SUPER_ADMIN)
  @Put('subcategories/:id')
  @ApiOperation({ summary: '[ADMIN] Update a sub-category' })
  @ApiResponse({ status: 200, description: 'SubCategory updated' })
  async updateSubCategory(
    @Param('id') id: string,
    @Body() dto: UpdateSubCategoryDto,
  ): Promise<any> {
    return this.coursesService.updateSubCategory(id, dto);
  }

  @Roles(Role.SUPER_ADMIN)
  @Delete('subcategories/:id')
  @ApiOperation({
    summary:
      '[ADMIN] Delete a sub-category (cascades to topics and activities)',
  })
  @ApiResponse({ status: 200, description: 'SubCategory deleted' })
  async deleteSubCategory(@Param('id') id: string): Promise<any> {
    return this.coursesService.deleteSubCategory(id);
  }

  @Get('topics/:id')
  @ApiOperation({ summary: 'Get a single topic (without activities)' })
  @ApiResponse({ status: 200, description: 'Topic' })
  async getTopic(@Param('id') id: string): Promise<any> {
    return this.coursesService.findTopicById(id);
  }

  @Roles(Role.SUPER_ADMIN)
  @Post('topics')
  @ApiOperation({ summary: '[ADMIN] Create a new topic' })
  @ApiResponse({ status: 201, description: 'Topic created' })
  async createTopic(@Body() dto: CreateTopicDto): Promise<any> {
    return this.coursesService.createTopic(dto);
  }

  @Roles(Role.SUPER_ADMIN)
  @Put('topics/reorder')
  @ApiOperation({
    summary: '[ADMIN] Bulk-reorder topics',
    description:
      'Send the final `orderIndex` for each topic that moved. Executed in two phases to avoid transient duplicate indexes.',
  })
  @ApiResponse({ status: 200, description: 'Topics reordered' })
  async reorderTopics(@Body() dto: ReorderTopicsDto): Promise<any> {
    return this.coursesService.reorderTopics(dto);
  }

  @Roles(Role.SUPER_ADMIN)
  @Put('topics/:id')
  @ApiOperation({ summary: '[ADMIN] Update a topic' })
  @ApiResponse({ status: 200, description: 'Topic updated' })
  async updateTopic(
    @Param('id') id: string,
    @Body() dto: UpdateTopicDto,
  ): Promise<any> {
    return this.coursesService.updateTopic(id, dto);
  }

  @Roles(Role.SUPER_ADMIN)
  @Delete('topics/:id')
  @ApiOperation({ summary: '[ADMIN] Delete a topic (cascades to activities)' })
  @ApiResponse({ status: 200, description: 'Topic deleted' })
  async deleteTopic(@Param('id') id: string) {
    return this.coursesService.deleteTopic(id);
  }

  @Roles(Role.SUPER_ADMIN)
  @Post('activities')
  @ApiOperation({ summary: '[ADMIN] Create a new activity for a topic' })
  @ApiResponse({ status: 201, description: 'Activity created' })
  async createActivity(@Body() dto: CreateActivityDto): Promise<any> {
    return this.coursesService.createActivity(dto);
  }

  @Roles(Role.SUPER_ADMIN)
  @Put('activities/:id')
  @ApiOperation({ summary: '[ADMIN] Update an activity payload or orderIndex' })
  @ApiResponse({ status: 200, description: 'Activity updated' })
  async updateActivity(
    @Param('id') id: string,
    @Body() dto: UpdateActivityDto,
  ): Promise<any> {
    return this.coursesService.updateActivity(id, dto);
  }

  @Roles(Role.SUPER_ADMIN)
  @Delete('activities/:id')
  @ApiOperation({ summary: '[ADMIN] Delete an activity' })
  @ApiResponse({ status: 200, description: 'Activity deleted' })
  async deleteActivity(@Param('id') id: string) {
    return this.coursesService.deleteActivity(id);
  }
}
