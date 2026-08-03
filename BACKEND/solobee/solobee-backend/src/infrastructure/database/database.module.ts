import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { ConfigModule, ConfigService } from '@nestjs/config';
import { UserDomainModule } from 'src/domain/user/user-domain.module';
import { buildTypeOrmOptions } from '../config/database.config';
import { SeedService } from './seed/seed.service';

@Module({
  imports: [
    UserDomainModule,
    TypeOrmModule.forRootAsync({
      imports: [ConfigModule],
      inject: [ConfigService],
      useFactory: (config: ConfigService) => buildTypeOrmOptions(config),
    }),
  ],
  providers: [SeedService],
})
export class DatabaseModule {}
