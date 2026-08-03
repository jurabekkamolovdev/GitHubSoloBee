import {
  Entity,
  PrimaryColumn,
  Column,
  CreateDateColumn,
  UpdateDateColumn,
  OneToMany,
} from 'typeorm';
import { SubCategoryEntity } from './sub-category.entity';

@Entity('categories')
export class CategoryEntity {
  @PrimaryColumn('uuid') id: string;
  @Column() name: string;
  @Column({ nullable: true, type: 'text' }) backgroundColor: string | null;
  @Column({ nullable: true, type: 'text' }) foregroundColor: string | null;
  @Column({ default: 0 }) orderIndex: number;
  @OneToMany(() => SubCategoryEntity, (s) => s.category, { cascade: true })
  subCategories: SubCategoryEntity[];
  @CreateDateColumn() createdAt: Date;
  @UpdateDateColumn() updatedAt: Date;
}
