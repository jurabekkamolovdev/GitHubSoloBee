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
import { CategoryEntity } from './category.entity';
import { TopicEntity } from './topic.entity';

@Entity('sub_categories')
export class SubCategoryEntity {
  @PrimaryColumn('uuid') id: string;
  @Column() categoryId: string;
  @ManyToOne(() => CategoryEntity, (c) => c.subCategories, {
    onDelete: 'CASCADE',
  })
  @JoinColumn({ name: 'categoryId' })
  category: CategoryEntity;
  @Column() name: string;
  @Column({ nullable: true, type: 'text' }) thumbnailKey: string | null;
  @Column({ default: 0 }) orderIndex: number;
  @OneToMany(() => TopicEntity, (t) => t.subCategory, { cascade: true })
  topics: TopicEntity[];
  @CreateDateColumn() createdAt: Date;
  @UpdateDateColumn() updatedAt: Date;
}
