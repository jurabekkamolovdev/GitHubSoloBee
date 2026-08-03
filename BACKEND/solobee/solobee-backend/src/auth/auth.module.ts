import { Module } from '@nestjs/common';
import { AppJwtModule } from 'src/infrastructure/jwt/jwt.module';
import { AuthService } from './auth.service';
import { AuthController } from './auth.controller';
import { UserDomainModule } from 'src/domain/user/user-domain.module';

@Module({
  imports: [AppJwtModule, UserDomainModule],
  providers: [AuthService],
  controllers: [AuthController],
})
export class AuthModule {}
