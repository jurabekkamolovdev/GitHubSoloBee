// presentation/avatar/dto/avatar-response.dto.ts
import { ApiProperty } from '@nestjs/swagger';
import { BaseResponse } from '../../../core/utils/base-response';
import { AvatarGender } from 'src/datasource/avatar/model/avatar.entity';

export class AvatarResponseDto {
  @ApiProperty({
    example: 'a1b2c3d4-e5f6-7890-abcd-ef1234567890',
    description: 'Avatar identifier',
  })
  id: string;

  @ApiProperty({
    enum: AvatarGender,
    example: 'BOY',
    description: 'Which selection group this avatar belongs to',
  })
  gender: AvatarGender;

  @ApiProperty({
    example: 'https://s3.solobee.uz/solobee-media/avatars/xxx.png',
    nullable: true,
    description: 'Resolved public URL for the avatar image',
  })
  thumbnailUrl: string | null;

  @ApiProperty({
    example: 0,
    description: 'Display order within the gender group',
  })
  orderIndex: number;
}

export class GroupedAvatarDataDto {
  @ApiProperty({
    type: [AvatarResponseDto],
    description: 'Avatars available for boy profiles',
  })
  boy: AvatarResponseDto[];

  @ApiProperty({
    type: [AvatarResponseDto],
    description: 'Avatars available for girl profiles',
  })
  girl: AvatarResponseDto[];
}

export class GroupedAvatarResponseDto extends BaseResponse {
  @ApiProperty({
    type: GroupedAvatarDataDto,
    description:
      'Avatars split into two fixed groups. Mobile shows the group ' +
      'matching the profile being created, letting the user pick one image ' +
      'from the list.',
  })
  data: GroupedAvatarDataDto;
}
