import {
  Entity,
  PrimaryColumn,
  Column,
  ManyToOne,
  JoinColumn,
  CreateDateColumn,
  UpdateDateColumn,
  Index,
} from 'typeorm';
import { UserEntity } from 'src/datasource/user/model/user.entity';
import { TopicEntity } from 'src/datasource/courses/model/topic.entity';
import { ProgressStatus } from 'src/domain/progress/model/topic-progress.model';

@Entity('topic_progress')
@Index(['studentUserId', 'topicId'], { unique: true })
export class TopicProgressEntity {
  @PrimaryColumn({ type: 'uuid' })
  id: string;

  @Column()
  studentUserId: string;

  @ManyToOne(() => UserEntity, { onDelete: 'CASCADE' })
  @JoinColumn({ name: 'studentUserId' })
  student: UserEntity;

  @Column()
  topicId: string;

  @ManyToOne(() => TopicEntity, { onDelete: 'CASCADE' })
  @JoinColumn({ name: 'topicId' })
  topic: TopicEntity;

  @Column({
    type: 'enum',
    enum: ProgressStatus,
    default: ProgressStatus.LOCKED,
  })
  status: ProgressStatus;

  @Column({ default: 0 })
  starsEarned: number;

  @CreateDateColumn()
  createdAt: Date;

  @UpdateDateColumn()
  updatedAt: Date;
}
