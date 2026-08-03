import { Controller, Post, Param, Req, Body, Inject } from '@nestjs/common';
import {
  ApiTags,
  ApiBearerAuth,
  ApiOperation,
  ApiResponse,
  ApiParam,
} from '@nestjs/swagger';
import {
  PROGRESS_SERVICE,
  type IProgressService,
} from 'src/domain/progress/service/progress.service.interface';
import { Role } from 'src/core/utils/role.enum';
import { Roles } from 'src/core/decorators/roles.decorator';
import { ErrorResponse } from 'src/core/utils/base-response';
import { JwtPayload } from 'src/infrastructure/jwt/jwt.strategy';
import { ReportAttemptDto } from '../model/request/progress-request.dto';

@ApiTags('Progress')
@ApiBearerAuth()
@Controller('progress')
export class ProgressController {
  constructor(
    @Inject(PROGRESS_SERVICE)
    private readonly progressService: IProgressService,
  ) {}

  @Roles(Role.STUDENT)
  @Post('activity/:id')
  @ApiOperation({
    summary: 'Report a successful attempt on an activity',
    description:
      'Call this endpoint every time the student performs a successful attempt ' +
      'on an activity. Each call counts as one attempt. ' +
      'Thresholds (server-side):\n\n' +
      '- **LEARN**: 4 attempts — one per full audio playback.\n' +
      '- **WRITING**: 1 — once the word is spelled correctly.\n' +
      '- **WORDHUNT**: 1 — once the correct audio option is picked.\n' +
      '- **PICQUEST**: 1 — once the correct image is picked.\n\n' +
      'Wrong picks must NOT be reported. When the attempt count reaches the ' +
      'threshold the activity flips to completed. When all activities in a topic ' +
      'are completed, the topic itself is marked completed and the next topic is unlocked.',
  })
  @ApiParam({
    name: 'id',
    description: 'Activity UUID',
    example: 'a1b2c3d4-e5f6-7890-abcd-ef1234567890',
  })
  @ApiResponse({
    status: 201,
    description: 'Attempt recorded',
    schema: {
      example: {
        status: 'success',
        timestamp: '2026-04-15T10:00:00.000Z',
        data: { attemptCount: 2, threshold: 4, completed: false },
      },
    },
  })
  @ApiResponse({
    status: 401,
    description: 'Unauthorized',
    type: ErrorResponse,
  })
  @ApiResponse({
    status: 404,
    description: 'Activity not found',
    type: ErrorResponse,
  })
  reportAttempt(
    @Param('id') activityId: string,
    @Req() req: { user: JwtPayload },
    @Body() body: ReportAttemptDto,
  ) {
    return this.progressService.reportActivityAttempt(
      req.user.id,
      activityId,
      body.result,
    );
  }
}
