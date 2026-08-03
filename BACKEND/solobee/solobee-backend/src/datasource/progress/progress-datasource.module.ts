import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { ActivityProgressEntity } from './model/activity-progress.entity';
import { TopicProgressEntity } from './model/topic-progress.entity';
import { ActivityProgressMapper } from './mapper/activity-progress.mapper';
import { TopicProgressMapper } from './mapper/topic-progress.mapper';
import { ACTIVITY_PROGRESS_REPOSITORY } from './repository/activity-progress.repository.interface';
import { ActivityProgressRepositoryImpl } from './repository/activity-progress.repository.impl';
import { TOPIC_PROGRESS_REPOSITORY } from './repository/topic-progress.repository.interface';
import { TopicProgressRepositoryImpl } from './repository/topic-progress.repository.impl';
// import { ACTIVITY_REF_REPOSITORY } from './repository/activity-ref.repository.interface';
// import { ActivityRefRepositoryImpl } from './repository/activity-ref.repository.impl';
// import { TOPIC_REF_REPOSITORY } from './repository/topic-ref.repository.interface';
// import { TopicRefRepositoryImpl } from './repository/topic-ref.repository.impl';
// import { ActivityEntity } from 'src/datasource/course/model/activity.entity';
// import { TopicEntity } from 'src/datasource/course/model/topic.entity';

@Module({
  imports: [
    TypeOrmModule.forFeature([
      ActivityProgressEntity,
      TopicProgressEntity,
      // ActivityEntity, // activity-ref va snapshot query uchun
      // TopicEntity, // topic-ref uchun
    ]),
  ],
  providers: [
    ActivityProgressMapper,
    TopicProgressMapper,
    {
      provide: ACTIVITY_PROGRESS_REPOSITORY,
      useClass: ActivityProgressRepositoryImpl,
    },
    {
      provide: TOPIC_PROGRESS_REPOSITORY,
      useClass: TopicProgressRepositoryImpl,
    },
    // { provide: ACTIVITY_REF_REPOSITORY, useClass: ActivityRefRepositoryImpl },
    // { provide: TOPIC_REF_REPOSITORY, useClass: TopicRefRepositoryImpl },
  ],
  exports: [
    ACTIVITY_PROGRESS_REPOSITORY,
    TOPIC_PROGRESS_REPOSITORY,
    // ACTIVITY_REF_REPOSITORY,
    // TOPIC_REF_REPOSITORY,
  ],
})
export class ProgressDatasourceModule {}
