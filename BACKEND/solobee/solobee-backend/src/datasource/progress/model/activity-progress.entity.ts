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
import { ActivityEntity } from 'src/datasource/courses/model/activity.entity';

@Entity('activity_progress')
@Index(['studentUserId', 'activityId'], { unique: true })
export class ActivityProgressEntity {
  @PrimaryColumn({ type: 'uuid' })
  id: string;

  @Column()
  studentUserId: string;

  @ManyToOne(() => UserEntity, { onDelete: 'CASCADE' })
  @JoinColumn({ name: 'studentUserId' })
  student: UserEntity;

  @Column()
  activityId: string;

  @ManyToOne(() => ActivityEntity, { onDelete: 'CASCADE' })
  @JoinColumn({ name: 'activityId' })
  activity: ActivityEntity;

  @Column({ default: 0 })
  attemptCount: number;

  @Column({ default: false })
  isCompleted: boolean;

  @CreateDateColumn()
  createdAt: Date;

  @UpdateDateColumn()
  updatedAt: Date;
}
