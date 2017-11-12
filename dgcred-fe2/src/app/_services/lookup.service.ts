import { Injectable } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Observable } from 'rxjs/Rx';
import { Http, Headers, RequestOptions, Response } from '@angular/http';
import { AuthHttpService } from './auth-http.service';

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

    getProvinces() : any {
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

    getCountries() : any {
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

    getRentPeriods() : any {
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
    getAreaUnits() : any {
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

    getAll() {
        // ARGS -- Useless but required ---
        let a = this.http.put('http://localhost:8080/api/property/put', { "name" : "test"}).map((response: Response) => response.json()).catch(handleError);
        // ARGS -- Useless but required ---

        if (!this.lookups) {
            this.lookups = this.http.get('http://localhost:8080/api/lookups')
                .map((response: Response) => response.json()).catch(handleError);
        }
        console.log(this.lookups);
        return this.lookups;
    }

}

function handleError( error: any, caugth: any) {
  let errorMsg = error.message || 'Wopidoooo!  Something is not rigth with the world.  Check again'
  return Observable.throw(errorMsg);
}