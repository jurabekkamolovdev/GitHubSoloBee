import {
  Controller,
  Post,
  Body,
  HttpCode,
  HttpStatus,
  Req,
  UseGuards,
  SetMetadata,
} from '@nestjs/common';
import { AuthService } from './auth.service';
import { ErrorResponse } from 'src/core/utils/base-response';
import { LoginResponseDto } from './dto/login-response.dto';
import { LoginDto } from './dto/login.dto';
import { Public } from 'src/core/decorators/public.decorator';
import {
  ApiTags,
  ApiOperation,
  ApiResponse,
  ApiExtraModels,
  ApiBearerAuth,
} from '@nestjs/swagger';
import { JwtPayload } from 'src/infrastructure/jwt/jwt.strategy';
import {
  JwtRefreshGuard,
  IS_REFRESH_KEY,
} from 'src/infrastructure/jwt/jwt-auth.guard';

@ApiTags('Auth')
@ApiExtraModels(LoginResponseDto, ErrorResponse)
@Controller('auth')
export class AuthController {
  constructor(private readonly authService: AuthService) {}

  @Public()
  @Post('login')
  @HttpCode(HttpStatus.OK)
  @ApiOperation({ summary: 'Login — returns accessToken and refreshToken' })
  @ApiResponse({
    status: 200,
    description: 'Successful login',
    type: LoginResponseDto,
  })
  @ApiResponse({
    status: 401,
    description: 'Invalid credentials',
    type: ErrorResponse,
  })
  async login(@Body() loginDto: LoginDto) {
    const result = await this.authService.login(
      loginDto.username,
      loginDto.password,
    );
    const response = new LoginResponseDto();
    response.data = result;
    return response;
  }

  @SetMetadata(IS_REFRESH_KEY, true)
  @ApiBearerAuth()
  @UseGuards(JwtRefreshGuard)
  @Post('refresh')
  @HttpCode(HttpStatus.OK)
  @ApiOperation({
    summary: 'Refresh tokens',
    description: 'Get new access and refresh tokens using the current token',
  })
  @ApiResponse({
    status: 200,
    description: 'Tokens refreshed successfully',
    type: LoginResponseDto,
  })
  @ApiResponse({
    status: 401,
    description: 'Token is invalid or expired',
    type: ErrorResponse,
  })
  async refresh(@Req() request: { user: JwtPayload }) {
    const result = await this.authService.refresh(request.user);
    const response = new LoginResponseDto();
    response.data = result;
    return response;
  }
}
