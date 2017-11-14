import { FinancialInstitution } from '../_models/index';
import { Property } from '../_models/index';
import { Address } from '../_models/index';
import { Telephone } from '../_models/index';
export class Tenant {

	constructor(
    public eid: number,
    public address?: Address,
    public firstName?: string,
    public lastName?: string,
    public initial?: string,
    public businessNumber?: string,
    public ssn?: string,
    public driverLicense?: string,
    // TODO just keep telephones
    public telephone?: string,
    public telephones?: Telephone[],
    public email?: string,
    public companyName?: string,
    public documents?: Document[]
    ) { }

}

