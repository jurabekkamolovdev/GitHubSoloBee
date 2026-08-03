import {
  Entity,
  PrimaryColumn,
  Column,
  CreateDateColumn,
  UpdateDateColumn,
} from 'typeorm';

export enum AvatarGender {
  BOY = 'BOY',
  GIRL = 'GIRL',
}

@Entity('avatar')
export class AvatarEntity {
  @PrimaryColumn({ type: 'uuid' })
  id: string;

  @Column({ type: 'enum', enum: AvatarGender })
  gender: AvatarGender;

  @Column({ type: 'text' })
  thumbnailKey: string;

  @Column({ type: 'int', default: 0 })
  orderIndex: number;

  @CreateDateColumn()
  createdAt: Date;

  @UpdateDateColumn()
  updatedAt: Date;
}
