// presentation/avatar/controller/avatar.controller.ts
import {
  Controller,
  Post,
  Get,
  Delete,
  Body,
  Param,
  Inject,
} from '@nestjs/common';
import {
  ApiTags,
  ApiOperation,
  ApiResponse,
  ApiBearerAuth,
  ApiParam,
} from '@nestjs/swagger';
import {
  AVATAR_SERVICE,
  type IAvatarService,
} from 'src/domain/avatar/service/avatar.service.interface';
import { CreateAvatarDto } from '../dto/create-avatar.dto';
import {
  AvatarResponseDto,
  GroupedAvatarResponseDto,
} from '../dto/avatar-response.dto';
import { Roles } from 'src/core/decorators/roles.decorator';
import { Role } from 'src/core/utils/role.enum';
import { Public } from 'src/core/decorators/public.decorator';

@ApiTags('Avatars')
@Controller('avatars')
export class AvatarController {
  constructor(
    @Inject(AVATAR_SERVICE)
    private readonly avatarService: IAvatarService,
  ) {}

  @Roles(Role.SUPER_ADMIN)
  @ApiBearerAuth()
  @Post()
  @ApiOperation({ summary: '[ADMIN] Create a new avatar' })
  @ApiResponse({ status: 201, type: AvatarResponseDto })
  async createAvatar(@Body() dto: CreateAvatarDto): Promise<AvatarResponseDto> {
    return this.avatarService.createAvatar(dto);
  }

  @Public()
  @Get()
  @ApiOperation({
    summary: 'Get all avatars, grouped by gender (boy/girl)',
    description:
      'Returns two fixed lists — `boy` and `girl` — for the avatar picker ' +
      'shown during profile creation. The client asks the parent whether the ' +
      'child is a boy or girl, then renders the matching list and lets them ' +
      'tap one image.',
  })
  @ApiResponse({
    status: 200,
    description: 'Avatars grouped by gender',
    type: GroupedAvatarResponseDto,
  })
  async getAvatars(): Promise<GroupedAvatarResponseDto> {
    const grouped = await this.avatarService.getAllAvatarsGrouped();

    return {
      status: 'success',
      timestamp: new Date().toISOString(),
      data: grouped,
    };
  }

  @Roles(Role.SUPER_ADMIN)
  @ApiBearerAuth()
  @Delete(':id')
  @ApiOperation({ summary: '[ADMIN] Delete an avatar' })
  @ApiParam({ name: 'id' })
  async deleteAvatar(@Param('id') id: string): Promise<void> {
    return this.avatarService.deleteAvatar(id);
  }
}
