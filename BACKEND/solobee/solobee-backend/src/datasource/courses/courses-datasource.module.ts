import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { CategoryEntity } from './model/category.entity';
import { SubCategoryEntity } from './model/sub-category.entity';
import { TopicEntity } from './model/topic.entity';
import { ActivityEntity } from './model/activity.entity';
import { ActivityDataMapper } from './mapper/activity.data.mapper';
import { TopicDataMapper } from './mapper/topic.data.mapper';
import { SubCategoryDataMapper } from './mapper/sub-category.data.mapper';
import { CategoryDataMapper } from './mapper/category.data.mapper';
import { ACTIVITY_REPOSITORY } from './repository/interface/activity.repository.interface';
import { TOPIC_REPOSITORY } from './repository/interface/topic.repository.interface';
import { SUB_CATEGORY_REPOSITORY } from './repository/interface/sub-category.repository.interface';
import { CATEGORY_REPOSITORY } from './repository/interface/category.repository.interface';
import { ActivityRepositoryImpl } from './repository/impl/activity.repository.impl';
import { TopicRepositoryImpl } from './repository/impl/topic.repository.impl';
import { SubCategoryRepositoryImpl } from './repository/impl/sub-category.repository.impl';
import { CategoryRepositoryImpl } from './repository/impl/category.repository.impl';

@Module({
  imports: [
    TypeOrmModule.forFeature([
      CategoryEntity,
      SubCategoryEntity,
      TopicEntity,
      ActivityEntity,
    ]),
  ],
  providers: [
    ActivityDataMapper,
    TopicDataMapper,
    SubCategoryDataMapper,
    CategoryDataMapper,
    { provide: ACTIVITY_REPOSITORY, useClass: ActivityRepositoryImpl },
    { provide: TOPIC_REPOSITORY, useClass: TopicRepositoryImpl },
    { provide: SUB_CATEGORY_REPOSITORY, useClass: SubCategoryRepositoryImpl },
    { provide: CATEGORY_REPOSITORY, useClass: CategoryRepositoryImpl },
  ],
  exports: [
    ACTIVITY_REPOSITORY,
    TOPIC_REPOSITORY,
    SUB_CATEGORY_REPOSITORY,
    CATEGORY_REPOSITORY,
  ],
})
export class CoursesDatasourceModule {}
