import { CommonModule } from '@angular/common';
import { Http, Headers, RequestOptions, Response } from '@angular/http';
import { AuthHttpService } from './auth-http.service';
import { Property } from '../_models/index';
import { Address } from '../_models/index';
import { Unit } from '../_models/index';
import { map } from "rxjs/operators";
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable()
export class PropertyService {
    constructor(public http: AuthHttpService) { }

    getCount() {
        return this.http.get(environment.apiUrl + '/property/count').pipe(map((response: Response) => response.json()));
        //, catchError());
    }

    getMidPoint() {
        return this.http.get(environment.apiUrl + '/property/midPoint').pipe(map((response: Response) => response.json()));
        //.catchError((err: HttpErrorResponse) => { }));
    }

    deleteDocument(peid: number, deid: number) {
        return this.http.get(environment.apiUrl + '/property/deleteDocument/' + peid + '/' + deid).pipe(map((response: Response) => response.json()));
        //.catchError((err: HttpErrorResponse) => { }));
    }

    addDocument(peid: number, deid: number) {
        console.log("peid: " + peid + " deid: " + deid);
        return this.http.get(environment.apiUrl + '/property/addDocument/' + peid + '/' + deid).pipe(map((response: Response) => response.json()));
        //.catchError((err: HttpErrorResponse) => { }));
    }

    getAll() {
        // TODO REMOVE
        let b: Property;
        b = new Property(0, '3', 'put test');
        let a = this.http.put(environment.apiUrl + '/property/put', b).pipe(map((response: Response) => response.json()));
        //.catchError((err: HttpErrorResponse) => { }));
        // TODO END

        return this.http.get(environment.apiUrl + '/property').pipe(map((response: Response) => response.json()));
        //.catchError((err: HttpErrorResponse) => { }));
    }

    getRange(start: number, count: number) {
        return this.http.get(environment.apiUrl + '/property/range/' + start + '/' + count).pipe(map((response: Response) => response.json()));
        //.catchError((err: HttpErrorResponse) => { }));
    }

    getByEid(eid: number) {
        return this.http.get(environment.apiUrl + '/property/' + eid).pipe(map((response: Response) => response.json()));
        //.catchError((err: HttpErrorResponse) => { }));
    }

    save(property: Property) {
        return this.http.post(environment.apiUrl + '/property', property).pipe(map((response: Response) => response.json()));
        //.catchError((err: HttpErrorResponse) => { }));
    }

    delete(id: number) {
        0
        return this.http.delete(environment.apiUrl + '/property/' + id).pipe(map((response: Response) => response.json()));
        //.catchError((err: HttpErrorResponse) => { }));
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

// function (err: HttpErrorResponse) => { }) (error: any, caugth: any) {
//     let errorMsg = error.message || 'Wopidoooo!  Something is not rigth with the world.  Check again'
//     return Observable.throw(errorMsg);
// }


