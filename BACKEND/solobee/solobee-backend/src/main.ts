import { NestFactory } from '@nestjs/core';
import { ValidationPipe } from '@nestjs/common';
import { AppModule } from './app.module';
import { setupSwagger } from './core/swagger/swagger.setup';
import { AllExceptionsFilter } from './core/filters/all-exceptions.filter';
import { ResponseInterceptor } from './core/interceptors/response.interceptor';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);

  app.enableCors();

  app.useGlobalPipes(
    new ValidationPipe({
      whitelist: true,
      transform: true,
      forbidNonWhitelisted: true,
    }),
  );

  app.useGlobalFilters(new AllExceptionsFilter());
  app.useGlobalInterceptors(new ResponseInterceptor());

  setupSwagger(app);

  const port = process.env.PORT ?? 3000;
  await app.listen(port);

  console.log(`🚀 Running on port ${port}`);
  console.log(`📱 Mobile Swagger: http://localhost:${port}/api-docs`);
  console.log(`🔧 Admin  Swagger: http://localhost:${port}/api-admin`);
}

void bootstrap();
