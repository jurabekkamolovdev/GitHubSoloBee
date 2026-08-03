import {
  Entity,
  PrimaryColumn,
  Column,
  CreateDateColumn,
  UpdateDateColumn,
  ManyToOne,
  JoinColumn,
} from 'typeorm';
import { TopicEntity } from './topic.entity';
import { ActivityType } from '../../../domain/courses/model/activity.model';

@Entity('activities')
export class ActivityEntity {
  @PrimaryColumn('uuid') id: string;
  @Column() topicId: string;
  @ManyToOne(() => TopicEntity, (t) => t.activities, { onDelete: 'CASCADE' })
  @JoinColumn({ name: 'topicId' })
  topic: TopicEntity;
  @Column({ type: 'enum', enum: ActivityType }) type: ActivityType;
  @Column({ default: 0 }) orderIndex: number;
  @Column({ type: 'jsonb' }) payload: Record<string, any>;
  @CreateDateColumn() createdAt: Date;
  @UpdateDateColumn() updatedAt: Date;
}
