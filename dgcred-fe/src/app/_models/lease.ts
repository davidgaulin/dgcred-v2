import { FinancialInstitution } from '../_models/index';
import { Property } from '../_models/index';
import { Unit } from '../_models/index';
import { Tenant } from '../_models/index';
import { Document } from '../_models/index';
export class Lease {

	constructor(
    public eid: number,
    public startDate: string,
    public rent: number,
    public rentPeriod: string,
    public autoRenew: boolean,
    public length?: string,
    public lengthPeriod?: string,
    public tenant?: Tenant[],
    public property?: Property,
    public unit?: Unit,
    public endDate?: string,
    public leaseRenewalNoticationDate?: string,
    public documents?: Document[]
    ) { } 
}

