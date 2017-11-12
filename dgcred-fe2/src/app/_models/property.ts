import { Address } from '../_models/index';
import { Unit } from '../_models/index';
import { Document } from '../_models/index';
export class Property {
  constructor(
    public eid: number,
    public name: string,
    public latitude?: number,
    public longitude?: number,
    public address?: Address,
    public financed?: boolean,
    public evaluation: number = 0,
    public evaluationDate: string = '1900-01-01',
    public purchasePrice: number = 0,
    public purchaseDate: string = '1900-01-01',
    public constructionYear: number = 1900,
    public units?: Unit[],
    public documents?: Document[]
  ) {  }
}