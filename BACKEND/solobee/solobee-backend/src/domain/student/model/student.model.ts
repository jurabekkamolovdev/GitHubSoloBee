import { v4 as uuidv4 } from 'uuid';

export class Student {
  private id: string;
  private userId: string;
  private username: string;
  private firstName: string;
  private lastName: string;
  private age: number;
  private avatarId: string;
  private birthDate: Date | null;
  private address: string | null;
  private parentPhone: string | null;
  private score: number;
  private currentTopicId: string | null;
  private currentCategoryId: string | null;
  private createdAt: Date;
  private updatedAt: Date;

  constructor(params: {
    id: string;
    userId: string;
    username: string;
    firstName: string;
    lastName: string;
    age: number;
    avatarId: string;
    birthDate?: string | Date | null;
    address?: string | null;
    parentPhone?: string | null;
    score?: number;
    currentTopicId?: string | null;
    currentCategoryId?: string | null;
    createdAt?: Date;
    updatedAt?: Date;
  }) {
    this.id = params.id;
    this.userId = params.userId;
    this.username = params.username;
    this.firstName = params.firstName;
    this.lastName = params.lastName;
    this.age = params.age;
    this.avatarId = params.avatarId;
    this.birthDate = params.birthDate ? new Date(params.birthDate) : null;
    this.address = params.address ?? null;
    this.parentPhone = params.parentPhone ?? null;
    this.score = params.score ?? 12000;
    this.currentTopicId = params.currentTopicId ?? null;
    this.currentCategoryId = params.currentCategoryId ?? null;
    this.createdAt = params.createdAt ?? new Date();
    this.updatedAt = params.updatedAt ?? new Date();
  }

  static create(
    userId: string,
    username: string,
    firstName: string,
    lastName: string,
    age: number,
    avatarId: string,
    birthDate?: string | null,
    address?: string | null,
    parentPhone?: string | null,
  ): Student {
    return new Student({
      id: uuidv4(),
      userId: userId,
      username: username,
      firstName: firstName,
      lastName: lastName,
      age: age,
      avatarId: avatarId,
      birthDate: birthDate,
      address: address,
      parentPhone: parentPhone,
    });
  }

  addScore(points: number): void {
    if (points < 0) throw new Error('Score manfiy bolishi mumkin emas');
    this.score += points;
    this.updatedAt = new Date();
  }

  setCurrentTopic(topicId: string, categoryId: string): void {
    this.currentTopicId = topicId;
    this.currentCategoryId = categoryId;
    this.updatedAt = new Date();
  }

  updateProfile(params: {
    firstName?: string;
    lastName?: string;
    birthDate?: string | null;
    address?: string | null;
    parentPhone?: string | null;
  }): void {
    if (params.firstName) this.firstName = params.firstName;
    if (params.lastName) this.lastName = params.lastName;
    if (params.birthDate !== undefined)
      this.birthDate = params.birthDate ? new Date(params.birthDate) : null;
    if (params.address !== undefined) this.address = params.address;
    if (params.parentPhone !== undefined) this.parentPhone = params.parentPhone;
    this.updatedAt = new Date();
  }

  getId(): string {
    return this.id;
  }
  getUserId(): string {
    return this.userId;
  }

  getUsername(): string {
    return this.username;
  }

  getFirstName(): string {
    return this.firstName;
  }
  getLastName(): string {
    return this.lastName;
  }
  getAge(): number {
    return this.age;
  }
  getAvatarId(): string {
    return this.avatarId;
  }
  getFullName(): string {
    return `${this.firstName} ${this.lastName}`;
  }
  getBirthDate(): Date | null {
    return this.birthDate;
  }
  getAddress(): string | null {
    return this.address;
  }
  getParentPhone(): string | null {
    return this.parentPhone;
  }
  getScore(): number {
    return this.score;
  }
  getCurrentTopicId(): string | null {
    return this.currentTopicId;
  }
  getCurrentCategoryId(): string | null {
    return this.currentCategoryId;
  }
  getCreatedAt(): Date {
    return this.createdAt;
  }
  getUpdatedAt(): Date {
    return this.updatedAt;
  }
}
