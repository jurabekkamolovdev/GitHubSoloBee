import bcrypt from 'bcrypt';
import { v4 as uuidv4 } from 'uuid';
import { Role } from 'src/core/utils/role.enum';
import { ICreateUser } from '../service/user.service.interface';

const SALT_ROUNDS = 10;

export class User {
  private id: string;
  private username: string;
  private passwordHash: string;
  private role: Role;
  private kindergartenId: string | null;
  private isActive: boolean;
  private refreshTokenHash: string | null;
  private tokenVersion: number;
  private createdAt: Date;
  private updatedAt: Date;

  constructor(params: {
    id: string;
    username: string;
    passwordHash: string;
    role: Role;
    kindergartenId?: string | null;
    isActive?: boolean;
    refreshTokenHash?: string | null;
    tokenVersion?: number;
    createdAt?: Date;
    updatedAt?: Date;
  }) {
    this.id = params.id;
    this.username = params.username;
    this.passwordHash = params.passwordHash;
    this.role = params.role;
    this.kindergartenId = params.kindergartenId ?? null;
    this.isActive = params.isActive ?? true;
    this.refreshTokenHash = params.refreshTokenHash ?? null;
    this.tokenVersion = params.tokenVersion ?? 0;
    this.createdAt = params.createdAt ?? new Date();
    this.updatedAt = params.updatedAt ?? new Date();
  }

  static async create(params: ICreateUser): Promise<User> {
    // if (params.role === Role.STUDENT) {
    //   const suffix = uuidv4().replace(/-/g, '').substring(0, 4).toLowerCase();
    //   const cleanName = params.username.toLowerCase().replace(/\s+/g, '');
    //   params.username = `${cleanName}-${suffix}`;
    // }

    const id = uuidv4();
    // const resolvedPassword = params.password || User.generateStrongPassword(10);
    if (!params.password) {
      throw new Error(`Password User`);
    }
    const passwordHash = await bcrypt.hash(params.password, SALT_ROUNDS);

    const user = new User({
      id: id,
      username: params.username,
      passwordHash: passwordHash,
      role: params.role,
      kindergartenId: params.kindergartenId ?? null,
    });
    return user;
  }

  async validatePassword(plainText: string): Promise<boolean> {
    return bcrypt.compare(plainText, this.passwordHash);
  }

  async changePassword(newPassword: string): Promise<void> {
    this.passwordHash = await bcrypt.hash(newPassword, SALT_ROUNDS);
    this.updatedAt = new Date();
  }

  async setRefreshToken(token: string | null): Promise<void> {
    this.refreshTokenHash = token
      ? await bcrypt.hash(token, SALT_ROUNDS)
      : null;
    this.updatedAt = new Date();
  }

  async validateRefreshToken(token: string): Promise<boolean> {
    if (!this.refreshTokenHash) return false;
    return bcrypt.compare(token, this.refreshTokenHash);
  }

  incrementTokenVersion(): void {
    this.tokenVersion += 1;
    this.updatedAt = new Date();
  }

  getId(): string {
    return this.id;
  }
  getUsername(): string {
    return this.username;
  }
  getPasswordHash(): string {
    return this.passwordHash;
  }
  getRole(): Role {
    return this.role;
  }
  getKindergartenId(): string | null {
    return this.kindergartenId;
  }
  getIsActive(): boolean {
    return this.isActive;
  }
  getRefreshTokenHash(): string | null {
    return this.refreshTokenHash;
  }
  getTokenVersion(): number {
    return this.tokenVersion;
  }
  getCreatedAt(): Date {
    return this.createdAt;
  }
  getUpdatedAt(): Date {
    return this.updatedAt;
  }

  deactivate(): void {
    this.isActive = false;
    this.updatedAt = new Date();
  }

  activate(): void {
    this.isActive = true;
    this.updatedAt = new Date();
  }

  private static generateStrongPassword(length: number): string {
    const charset =
      'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+';
    return Array.from(
      { length },
      () => charset[Math.floor(Math.random() * charset.length)],
    ).join('');
  }
}
