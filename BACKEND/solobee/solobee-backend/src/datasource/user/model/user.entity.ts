import { Entity, PrimaryGeneratedColumn, Column } from 'typeorm';
import { Role } from '../../../core/utils/role.enum';

@Entity('users')
export class UserEntity {
  @PrimaryGeneratedColumn('uuid')
  id: string;

  @Column({ unique: true })
  username: string;

  @Column()
  passwordHash: string;

  @Column({ type: 'enum', enum: Role, default: Role.STUDENT })
  role: Role;

  @Column({ nullable: true, type: 'uuid' })
  kindergartenId: string | null;

  @Column({ default: true })
  isActive: boolean;

  @Column({ nullable: true, type: 'text' })
  refreshTokenHash: string | null;

  @Column({ default: 0 })
  tokenVersion: number = 0;

  @Column()
  createdAt: Date;

  @Column()
  updatedAt: Date;
}
