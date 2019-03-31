import { Injectable } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Http, Headers, RequestOptions, Response } from '@angular/http';
import { AuthHttpService } from './auth-http.service';

import { environment } from '../../environments/environment';
import { map } from 'rxjs/operators';

@Injectable()
export class FinancialGraphService {
    constructor(public http: AuthHttpService) { }

    getLoansBalancePrevision(start: number, end: number) {
        return this.http.get(environment.apiUrl + '/fg/loanbalance/previsions/years/' + start + '/' + end).pipe(map((response: Response) => response.json()));
        //.catch(handleError);
    }
    getLoanValueCapitalPrevision(start: number, end: number) {
        return this.http.get(environment.apiUrl + '/fg/overview/loan/value/capital/previsions/years/' + start + '/' + end).pipe(map((response: Response) => response.json()));
        //.catch(handleError);
    }
}
// function handleError( error: any, caugth: any) {
//   let errorMsg = error.message || 'Wopidoooo!  Something is not rigth with the world.  Check again'
//   return Observable.throw(errorMsg);
// }


