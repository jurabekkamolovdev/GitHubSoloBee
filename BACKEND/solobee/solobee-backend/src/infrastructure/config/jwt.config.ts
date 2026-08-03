import type { ConfigService } from '@nestjs/config';
import type { JwtModuleOptions } from '@nestjs/jwt';
import type { StringValue } from 'ms';

export function buildJwtOptions(config: ConfigService): JwtModuleOptions {
  return {
    secret: config.get<string>('JWT_SECRET', '5127620d'),
    signOptions: {
      expiresIn: config.get<StringValue>('JWT_EXPIRES_IN', '30m'),
    },
  };
}
