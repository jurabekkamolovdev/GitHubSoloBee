import { Kindergarten } from '../model/kindergarten.model';

export interface IKindergartenService {
  create(params: ICreateKindergarten): Promise<Kindergarten>;
  findAll(): Promise<Kindergarten[]>;
  delete(id: string): Promise<boolean>;
}

export const KINDERGARTEN_SERVICE = Symbol('KINDERGARTEN_SERVICE');

export interface ICreateKindergarten {
  name: string;
  adminUsername: string;
  adminPassword: string;
  address?: string;
}
