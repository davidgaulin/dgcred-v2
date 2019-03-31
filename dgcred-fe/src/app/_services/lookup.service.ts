import { Injectable } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Http, Headers, RequestOptions, Response } from '@angular/http';
import { AuthHttpService } from './auth-http.service';

import { environment } from '../../environments/environment';
import { map } from 'rxjs/operators';

@Injectable()
export class LookupService {
    constructor(public http: AuthHttpService) { }

    lookups: any;
    provinces: any;
    countries: any;
    errorMessage: string;
    isLoading: boolean = false;
    areaUnits: any;
    rentPeriods: any;
    propertyTypes: any;
    financialInstitutions: any;
    paymentFrequency: any;

    public PLEX_PROPERTY_TYPE_CODE = '3';

    getProvinces(): any {
        console.log("Getting");
        if (this.provinces) {
            console.log("Getting Existing");
            return this.provinces;
        } else {
            this.getAll();
            console.log("Getting New");
            return this.provinces;
        }
    }

    getPaymentFrequencies(): any {
        console.log("Getting");
        if (this.paymentFrequency) {
            console.log("Getting Existing");
            return this.paymentFrequency;
        } else {
            this.getAll();
            console.log("Getting New");
            return this.paymentFrequency;
        }
    }

    getPropertyTypes(): any {
        console.log("Getting");
        if (this.propertyTypes) {
            console.log("Getting Existing");
            return this.propertyTypes;
        } else {
            this.getAll();
            console.log("Getting New");
            return this.propertyTypes;
        }
    }

    getCountries(): any {
        console.log("Getting");
        if (this.countries) {
            console.log("Getting Existing");
            return this.countries;
        } else {
            this.getAll();
            console.log("Getting New");
            return this.countries;
        }
    }

    getRentPeriods(): any {
        console.log("Getting");
        if (this.rentPeriods) {
            console.log("Getting Existing");
            return this.rentPeriods;
        } else {
            this.getAll();
            console.log("Getting New");
            return this.rentPeriods;
        }
    }
    getAreaUnits(): any {
        console.log("Getting");
        if (this.areaUnits) {
            console.log("Getting Existing");
            return this.areaUnits;
        } else {
            this.getAll();
            console.log("Getting New");
            return this.areaUnits;
        }
    }

    getFinancialInstitutions(): any {
        // ARGS -- Useless but required ---
        let a = this.http.put(environment.apiUrl + '/property/put', { "name": "test" }).pipe(
        map((response: Response) => response.json()))
        //.catch(handleError);
        // ARGS -- Useless but required ---

        if (!this.lookups) {
            this.financialInstitutions = this.http.get(environment.apiUrl + '/lookup/financialInstitutions').pipe(
                map((response: Response) => response.json()));
                //.catch(handleError);
        }
        console.log(this.financialInstitutions);
        return this.financialInstitutions;
    }

    getAll() {
        // ARGS -- Useless but required ---
        let a = this.http.put(environment.apiUrl + '/property/put', { "name": "test" }).pipe(
            map((response: Response) => response.json()));
            //.catch(handleError);
        // ARGS -- Useless but required ---

        if (!this.lookups) {
            this.lookups = this.http.get(environment.apiUrl + '/lookups').pipe(
                map((response: Response) => response.json()));
                //.catch(handleError);
        }
        console.log(this.lookups);
        return this.lookups;
    }

}

// function handleError(error: any, caugth: any) {
//     let errorMsg = error.message || 'Wopidoooo!  Something is not rigth with the world.  Check again'
//     return Observable.throw(errorMsg);
// }