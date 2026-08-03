import { INestApplication } from '@nestjs/common';
import { SwaggerModule } from '@nestjs/swagger';
import {
  buildBaseDocument,
  buildMobileDocument,
  buildAdminDocument,
} from './swagger.documents';

const SWAGGER_OPTIONS = { swaggerOptions: { persistAuthorization: true } };

export function setupSwagger(app: INestApplication): void {
  const full = buildBaseDocument(app);
  const mobile = buildMobileDocument(full);
  const admin = buildAdminDocument(full);

  SwaggerModule.setup('api-docs', app, mobile, {
    ...SWAGGER_OPTIONS,
    customSiteTitle: 'Savodxon Mobile API',
  });

  SwaggerModule.setup('api-admin', app, admin, {
    ...SWAGGER_OPTIONS,
    customSiteTitle: 'Savodxon Admin API',
  });
}
