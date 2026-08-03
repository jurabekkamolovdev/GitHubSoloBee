import type { TypeOrmModuleOptions } from '@nestjs/typeorm';
import type { ConfigService } from '@nestjs/config';

function readBool(value: string | undefined, fallback: boolean): boolean {
  if (value === undefined) return fallback;
  return value.toLowerCase() === 'true' || value === '1';
}

export function buildTypeOrmOptions(
  config: ConfigService,
): TypeOrmModuleOptions {
  return {
    type: 'postgres',
    host: config.get<string>('DB_HOST', 'localhost'),
    port: Number(config.get<string>('DB_PORT', '5432')),
    username: config.get<string>('DB_USERNAME', 'turnerko'),
    password: config.get<string>('DB_PASSWORD', '2002'),
    database: config.get<string>('DB_NAME', 'solobee'),
    autoLoadEntities: readBool(config.get<string>('DB_AUTOLOAD_MODELS'), true),
    synchronize: readBool(config.get<string>('DB_SYNCHRONIZE'), true),
    logging: false,
  };
}
