import { v4 as uuidv4 } from 'uuid';

export class Kindergarten {
  private id: string;
  private name: string;
  private address: string | null;
  private isActive: boolean;
  private createdAt: Date;
  private updatedAt: Date;

  constructor(
    id: string,
    name: string,
    address?: string | null,
    isActive?: boolean | null,
    createdAt?: Date,
    updatedAt?: Date,
  ) {
    this.id = id;
    this.name = name;
    this.address = address ?? null;
    this.isActive = isActive ?? true;
    this.createdAt = createdAt ?? new Date();
    this.updatedAt = updatedAt ?? new Date();
  }

  static create(
    name: string,
    address?: string | null,
    isActive?: boolean | null,
  ) {
    return new Kindergarten(uuidv4(), name, address, isActive);
  }

  getId(): string {
    return this.id;
  }
  getName(): string {
    return this.name;
  }
  getAddress(): string | null {
    return this.address;
  }
  getIsActive(): boolean {
    return this.isActive;
  }
  getCreatedAt(): Date {
    return this.createdAt;
  }
  getUpdatedAt(): Date {
    return this.updatedAt;
  }
}
