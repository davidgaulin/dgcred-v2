import { FinancialInstitution } from '../_models/index';
import { Property } from '../_models/index';
import { Document } from '../_models/index';
export class Loan {

	constructor(
    public eid: number,
    public paymentFrequency: string,
    public compounding: string,
    public amount: number,
    public loanCreationDate: string,
    public interestRate: number,
    public amortization: number,
    public balance: number,
    public balanceDate: string,
    public renewalDate: string,
    public termsInMonth: number,
    public financialInstitution?: FinancialInstitution,
    public property?: Property,
    public documents?: Document[]
    ) { }
}
