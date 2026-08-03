import { DocumentBuilder, SwaggerModule, OpenAPIObject } from '@nestjs/swagger';
import { INestApplication } from '@nestjs/common';

const MOBILE_ALLOWED_OPS: Array<[string, string]> = [
  ['post', '/auth/login'],
  ['post', '/auth/refresh'],
  ['get', '/students/profile'],
  ['delete', '/students'],
  ['get', '/courses/categories'],
  ['get', '/courses/categories/{categoryId}/subcategories'],
  ['get', '/courses/subcategories/{subCategoryId}/topics'],
  ['get', '/courses/topics/{topicId}/activities'],
  ['get', '/avatars'],
  ['post', '/students/register'],
  ['get', '/students/profile'],
  ['get', '/students/statistics'],
  ['get', '/students/statistics/weekly'],
  ['post', '/progress/activity/{id}'],
];

function buildBaseDocument(app: INestApplication): OpenAPIObject {
  const config = new DocumentBuilder()
    .setVersion('1.0')
    .addBearerAuth({
      type: 'http',
      scheme: 'bearer',
      bearerFormat: 'JWT',
      description: 'Paste your accessToken here. Get it from POST /auth/login',
    })
    .build();

  return SwaggerModule.createDocument(app, config);
}

function buildMobileDocument(full: OpenAPIObject): OpenAPIObject {
  const paths: OpenAPIObject['paths'] = {};

  for (const [method, path] of MOBILE_ALLOWED_OPS) {
    const entry = full.paths[path];
    if (entry?.[method]) {
      paths[path] = { ...(paths[path] ?? {}), [method]: entry[method] };
    }
  }

  return {
    ...full,
    info: {
      title: 'Savodxon — Mobile API',
      description:
        '**Mobile developer swagger.** Contains only student-facing endpoints.\n\n' +
        '### How to use\n' +
        '1. Call **POST /auth/login** to get `accessToken`\n' +
        '2. Click **Authorize 🔒** and paste the token\n\n' +
        '### Response format\n```json\n{ "status": "success", "timestamp": "...", "data": { ... } }\n```',
      version: '1.0',
    },
    paths,
    tags: [
      { name: 'Auth', description: 'Login and token refresh' },
      { name: 'Students', description: 'Student profile' },
      { name: 'Courses', description: 'Categories, topics, activities' },
      { name: 'Progress', description: 'Track learning progress' },
    ],
  };
}

function buildAdminDocument(full: OpenAPIObject): OpenAPIObject {
  return {
    ...full,
    info: {
      title: 'Savodxon — Admin API',
      description:
        '**Full API documentation** including all admin endpoints.\n\n' +
        'Roles: `SUPER_ADMIN` | `KINDERGARTEN_ADMIN` | `STUDENT`',
      version: '1.0',
    },
  };
}

export { buildBaseDocument, buildMobileDocument, buildAdminDocument };
