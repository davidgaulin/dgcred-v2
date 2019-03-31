import { Injectable } from '@angular/core';
import { CommonModule } from '@angular/common';
import { map } from "rxjs/operators";
import { Http, Headers, RequestOptions, Response } from '@angular/http';
import { AuthHttpService } from './auth-http.service';
import { Lease } from '../_models/index';

import { environment } from '../../environments/environment';

@Injectable()
export class LeaseService {
    constructor(public http: AuthHttpService) { }

    getAllLeases() {
        return this.http.get(environment.apiUrl + '/lease').pipe(map((response: Response) => response.json())); //(handleError);
    }

    getLeaseCount() {
        return this.http.get(environment.apiUrl + '/lease/count').pipe(map((response: Response) => response.json())); //(handleError);
    }

    deleteLeaseDocument(leid: number, deid: number) {
        return this.http.get(environment.apiUrl + '/lease/deleteDocument/' + leid + '/' + deid).pipe(map((response: Response) => response.json())); //(handleError);
    }

    addLeaseDocument(leid: number, deid: number) {
        console.log("peid: " + leid + " deid: " + deid);
        return this.http.get(environment.apiUrl + '/lease/addDocument/' + leid + '/' + deid).pipe(map((response: Response) => response.json())); //(handleError);
    }

    getByEid(eid: number) {
        return this.http.get(environment.apiUrl + '/lease/' + eid).pipe(map((response: Response) => response.json())); //(handleError);
    }

    save(lease: Lease) {
        return this.http.post(environment.apiUrl + '/lease', lease).pipe(map((response: Response) => response.json())); //(handleError);
    }

    delete(id: number) {
        return this.http.delete(environment.apiUrl + '/lease/' + id).pipe(map((response: Response) => response.json())); //(handleError);
    }
}

// function handleError( error: any, caugth: any) {
//   let errorMsg = error.message || 'Wopidoooo!  Something is not rigth with the world.  Check again'
//   return Observable.throw(errorMsg);
// }
