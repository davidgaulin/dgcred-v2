import { Injectable } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Http, Headers, RequestOptions, Response } from '@angular/http';
import { AuthHttpService } from './auth-http.service';
import { Loan } from '../_models/index';

import { environment } from '../../environments/environment';
import { map } from 'rxjs/operators';

@Injectable()
export class FinancingService {
    constructor(public http: AuthHttpService) { }

    getAllLoans() {
        return this.http.get(environment.apiUrl + '/loan').pipe(map((response: Response) => response.json())); //.catch(handleError);
    }

    getLoanCount() {
        return this.http.get(environment.apiUrl + '/loan/count').pipe(map((response: Response) => response.json())); //.catch(handleError);
    }

    deleteLoanDocument(leid: number, deid: number) {
        return this.http.get(environment.apiUrl + '/loan/deleteDocument/' + leid + '/' + deid).pipe(map((response: Response) => response.json())); //.catch(handleError);
    }

    addLoanDocument(leid: number, deid: number) {
        console.log("peid: " + leid + " deid: " + deid);
        return this.http.get(environment.apiUrl + '/loan/addDocument/' + leid + '/' + deid).pipe(map((response: Response) => response.json())); //.catch(handleError);
    }

    getByEid(eid: number) {
        return this.http.get(environment.apiUrl + '/loan/' + eid).pipe(map((response: Response) => response.json())); //.catch(handleError);
    }

    save(loan: Loan) {
        return this.http.post(environment.apiUrl + '/loan', loan).pipe(map((response: Response) => response.json())); //.catch(handleError);
    }

    delete(id: number) {
        return this.http.delete(environment.apiUrl + '/loan/' + id).pipe(map((response: Response) => response.json())); //.catch(handleError);
    }
}

// function handleError( error: any, caugth: any) {
//   let errorMsg = error.message || 'Wopidoooo!  Something is not rigth with the world.  Check again'
//   return Observable.throw(errorMsg);
// }
