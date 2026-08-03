import {
  Entity,
  PrimaryColumn,
  Column,
  OneToOne,
  ManyToOne,
  JoinColumn,
  CreateDateColumn,
  UpdateDateColumn,
} from 'typeorm';
import { UserEntity } from 'src/datasource/user/model/user.entity';
import { AvatarEntity } from 'src/datasource/avatar/model/avatar.entity';

@Entity('students')
export class StudentEntity {
  @PrimaryColumn({ type: 'uuid' })
  id: string;

  @PrimaryColumn('uuid')
  userId: string;

  @OneToOne(() => UserEntity, { onDelete: 'CASCADE' })
  @JoinColumn({ name: 'userId' })
  user: UserEntity;

  @Column()
  username: string;

  @Column()
  firstName: string;

  @Column()
  lastName: string;

  @Column()
  age: number;

  @Column({ type: 'date', nullable: true })
  birthDate: Date | null;

  @Column({ type: 'varchar', nullable: true })
  address: string | null;

  @Column({ type: 'varchar', nullable: true })
  parentPhone: string | null;

  @Column({ default: 12000 })
  score: number;

  @Column({ type: 'uuid', nullable: true })
  avatarId: string;

  @ManyToOne(() => AvatarEntity, { nullable: true, onDelete: 'SET NULL' })
  @JoinColumn({ name: 'avatarId' })
  avatar: AvatarEntity;

  @Column({ type: 'varchar', nullable: true })
  currentTopicId: string | null;

  @Column({ type: 'varchar', nullable: true })
  currentCategoryId: string | null;

  @CreateDateColumn()
  createdAt: Date;

  @UpdateDateColumn()
  updatedAt: Date;
}
