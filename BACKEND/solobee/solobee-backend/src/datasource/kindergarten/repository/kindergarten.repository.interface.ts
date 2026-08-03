import { Kindergarten } from 'src/domain/kindergarten/model/kindergarten.model';

export interface IKindergartenRepository {
  save(user: Kindergarten): Promise<Kindergarten>;
  findAll(): Promise<Kindergarten[]>;
  findById(id: string): Promise<Kindergarten | null>;
  delete(id: string): Promise<boolean>;
}

export const KINDERGARTEN_REPOSITORY = Symbol('KINDERGARTEN_REPOSITORY');
