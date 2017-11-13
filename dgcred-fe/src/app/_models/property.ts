import { Address } from '../_models/index';
import { Unit } from '../_models/index';
import { Document } from '../_models/index';
export class Property {
  constructor(
    public eid: number,
    public name: string,
    public type: string,
    public latitude?: number,
    public longitude?: number,
    public address?: Address,
    public financed?: boolean,
    public evaluationDate?: string,
    public evaluation?: number,
    public purchaseDate?: string,
    public constructionYear?: number,
    public purchasePrice?: number,    
    public units?: Unit[],
    public documents?: Document[]
  ) {  }
}