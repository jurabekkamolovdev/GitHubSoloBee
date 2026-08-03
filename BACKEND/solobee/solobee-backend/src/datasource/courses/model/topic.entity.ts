import {
  Entity,
  PrimaryColumn,
  Column,
  CreateDateColumn,
  UpdateDateColumn,
  ManyToOne,
  OneToMany,
  JoinColumn,
} from 'typeorm';
import { SubCategoryEntity } from './sub-category.entity';
import { ActivityEntity } from './activity.entity';

@Entity('topics')
export class TopicEntity {
  @PrimaryColumn('uuid') id: string;
  @Column() subCategoryId: string;
  @ManyToOne(() => SubCategoryEntity, (s) => s.topics, { onDelete: 'CASCADE' })
  @JoinColumn({ name: 'subCategoryId' })
  subCategory: SubCategoryEntity;
  @Column({ nullable: true, type: 'text' }) thumbnailKey: string | null;
  @Column({ default: 0 }) orderIndex: number;
  @OneToMany(() => ActivityEntity, (a) => a.topic, { cascade: true })
  activities: ActivityEntity[];
  @CreateDateColumn() createdAt: Date;
  @UpdateDateColumn() updatedAt: Date;
}
