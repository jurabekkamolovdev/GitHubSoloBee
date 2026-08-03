import {
  Injectable,
  OnApplicationBootstrap,
  Logger,
  Inject,
} from '@nestjs/common';
import { Role } from 'src/core/utils/role.enum';
import { ConfigService } from '@nestjs/config';
import { USER_SERVICE } from 'src/domain/user/service/user.service.interface';
import { type IUserService } from 'src/domain/user/service/user.service.interface';

@Injectable()
export class SeedService implements OnApplicationBootstrap {
  private readonly logger = new Logger(SeedService.name);

  constructor(
    @Inject(USER_SERVICE)
    private readonly userService: IUserService,
    private readonly configService: ConfigService,
  ) {}

  async onApplicationBootstrap() {
    await this.seedSuperAdmin();
  }

  async seedSuperAdmin() {
    try {
      const defaultUsername = this.configService.get<string>(
        'SUPER_ADMIN_USERNAME',
      );
      const defaultPassword = this.configService.get<string>(
        'SUPER_ADMIN_PASSWORD',
      );

      if (!defaultUsername || !defaultPassword) {
        this.logger.warn(
          '⚠️ SUPER_ADMIN_USERNAME or SUPER_ADMIN_PASSWORD not found in .env. Skipping.',
        );
        return;
      }
      await this.userService.create({
        username: defaultUsername,
        role: Role.SUPER_ADMIN,
        kindergartenId: null,
        password: defaultPassword,
      });

      this.logger.log('==============================================');
      this.logger.log('      🚀 Default Super Admin Created!         ');
      this.logger.log(`      Username: ${defaultUsername}            `);
      this.logger.log(`      Password: ${defaultPassword}            `);
      this.logger.log('==============================================');
    } catch (error) {
      this.logger.error('Error seeding Super Admin...', error.stack);
    }
  }
}
