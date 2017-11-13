import { Address } from '../_models/index';
export class Unit {
  constructor(
    public eid: number,
    public number: string,
    public area: number,
    public areaUnit: string,
    public projectedRent: number,
    public rentPeriod: string,
    public bedrooms: string,
    public bathrooms: string, 
    public description: string,
  ) {  }
}