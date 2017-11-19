import { Injectable } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Observable } from 'rxjs/Rx';
import { Http, Headers, RequestOptions, Response } from '@angular/http';
import { AuthHttpService } from './auth-http.service';
import { Property } from '../_models/index';
import { Address } from '../_models/index';
import { Unit } from '../_models/index';

import { environment } from '../../environments/environment';

@Injectable()
export class PropertyService {
    constructor(public http: AuthHttpService) { }

    getCount() {
        return this.http.get(environment.apiUrl + '/property/count').map((response: Response) => response.json()).catch(handleError);
    }

    getMidPoint() {
        return this.http.get(environment.apiUrl + '/property/midPoint').map((response: Response) => response.json()).catch(handleError);
    }

    deleteDocument(peid: number, deid: number) {
        return this.http.get(environment.apiUrl + '/property/deleteDocument/' + peid + '/' + deid).map((response: Response) => response.json()).catch(handleError);
    }

    addDocument(peid: number, deid: number) {
        console.log("peid: " + peid + " deid: " + deid);
        return this.http.get(environment.apiUrl + '/property/addDocument/' + peid + '/' + deid).map((response: Response) => response.json()).catch(handleError);
    }


    getAll() {
        // TODO REMOVE
        let b: Property;
        b = new Property(0, '3', 'put test');
        let a = this.http.put(environment.apiUrl + '/property/put', b).map((response: Response) => response.json()).catch(handleError);
        // TODO END

        return this.http.get(environment.apiUrl + '/property').map((response: Response) => response.json()).catch(handleError);
    }

    getRange(start: number, count: number) {
        return this.http.get(environment.apiUrl + '/property/range/' + start + '/' + count).map((response: Response) => response.json()).catch(handleError);
    }    

    getByEid(eid: number) {
        return this.http.get(environment.apiUrl + '/property/' + eid).map((response: Response) => response.json()).catch(handleError);
    }

    save(property: Property) {
        return this.http.post(environment.apiUrl + '/property', property).map((response: Response) => response.json()).catch(handleError);
    }

    delete(id: number) {
        return this.http.delete(environment.apiUrl + '/property/' + id).map((response: Response) => response.json()).catch(handleError);
    }

    newBlankProperty() {
        let address: Address = new Address(0, '', '', '', '', '', '');
        let unit: Unit = new Unit(0, '', 0, '', 0, '', '', '', '');
        unit.number = '';
        unit.area = 0;
        unit.projectedRent = 0;
        unit.areaUnit = '0';
        unit.rentPeriod = '0';
        unit.bathrooms = '1';
        unit.bedrooms = '2';
        unit.description = ''
        let property: Property = new Property(0, '', '', 0, 0, address);
        property.evaluation = 0;
        property.units = [unit];
        return property;
    }
}

function handleError( error: any, caugth: any) {
  let errorMsg = error.message || 'Wopidoooo!  Something is not rigth with the world.  Check again'
  return Observable.throw(errorMsg);
}


