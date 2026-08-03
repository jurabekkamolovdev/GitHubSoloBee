import { Injectable, Inject, UnauthorizedException } from '@nestjs/common';
import { JwtService } from '@nestjs/jwt';
import { ConfigService } from '@nestjs/config';
import { JwtPayload } from 'src/infrastructure/jwt/jwt.strategy';
import { USER_SERVICE } from 'src/domain/user/service/user.service.interface';
import { type IUserService } from 'src/domain/user/service/user.service.interface';
import { User } from 'src/domain/user/model/user.model';

@Injectable()
export class AuthService {
  constructor(
    @Inject(USER_SERVICE)
    private readonly userService: IUserService,
    private readonly jwtService: JwtService,
    private readonly config: ConfigService,
  ) {}

  async login(
    username: string,
    password: string,
  ): Promise<{ accessToken: string; refreshToken: string }> {
    const user: User | null = await this.userService.findByUsername(username);
    if (!user) throw new Error('Invalid credentials');

    const isMatch = await user.validatePassword(password);

    if (!isMatch) throw new Error('Invalid credentials');

    return this.generateTokens(user);
  }

  async refresh(
    payload: JwtPayload,
  ): Promise<{ accessToken: string; refreshToken: string }> {
    const user = await this.userService.findById(payload.id);
    if (!user) throw new UnauthorizedException('User not found');
    return this.generateTokens(user);
  }

  private async generateTokens(
    user: User,
  ): Promise<{ accessToken: string; refreshToken: string }> {
    const cleanPayload: JwtPayload = {
      id: user.getId(),
      username: user.getUsername(),
      role: user.getRole(),
      kindergartenId: user.getKindergartenId()!,
    };

    return {
      accessToken: await this.jwtService.signAsync(cleanPayload),
      refreshToken: await this.jwtService.signAsync(cleanPayload, {
        secret: this.config.get('JWT_REFRESH_SECRET', '9140656kj'),
        expiresIn: this.config.get('JWT_REFRESH_EXPIRES_IN', '7d'),
      }),
    };
  }
}
