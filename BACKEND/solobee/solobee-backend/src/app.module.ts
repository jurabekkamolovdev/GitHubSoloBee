import { Module } from '@nestjs/common';
import { APP_GUARD } from '@nestjs/core';
import { EventEmitterModule } from '@nestjs/event-emitter';
import { InfrastructureModule } from './infrastructure/infrastructure.module';
import { StudentControllerModule } from './web/student/student-web.module';
import { KindergartenControllerModule } from './web/kindergarten/kindergarten-web.module';
import { AuthModule } from './auth/auth.module';
import { AppJwtModule } from './infrastructure/jwt/jwt.module';
import { JwtAuthGuard } from './infrastructure/jwt/jwt-auth.guard';
import { RolesGuard } from './infrastructure/jwt/roles.guard';
import { StorageControllerModule } from './web/storage/storage-web.module';
import { CoursesControllerModule } from './web/courses/courses-web.module';
import { ProgressControllerModule } from './web/progress/progress-web.module';
import { AvatarControllerModule } from './web/avatar/avatar-web.module';

@Module({
  imports: [
    EventEmitterModule.forRoot(),
    InfrastructureModule,
    AuthModule,
    AppJwtModule,
    StudentControllerModule,
    KindergartenControllerModule,
    StorageControllerModule,
    CoursesControllerModule,
    ProgressControllerModule,
    AvatarControllerModule,
  ],
  providers: [
    {
      provide: APP_GUARD,
      useClass: JwtAuthGuard,
    },
    {
      provide: APP_GUARD,
      useClass: RolesGuard,
    },
  ],
})
export class AppModule {}
