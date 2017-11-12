import { Injectable } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Observable } from 'rxjs/Rx';
import { Http, Headers, RequestOptions, Response } from '@angular/http';
import { AuthHttpService } from './auth-http.service';

@Injectable()
export class FinancialGraphService {
    constructor(public http: AuthHttpService) { }

    getLoansBalancePrevision(start: number, end: number) {
        return this.http.get('http://localhost:8080/api/fg/loanbalance/previsions/years/' + start + '/' + end).map((response: Response) => response.json()).catch(handleError);
    }
    getLoanValueCapitalPrevision(start: number, end: number) {
        return this.http.get('http://localhost:8080/api/fg/overview/loan/value/capital/previsions/years/' + start + '/' + end).map((response: Response) => response.json()).catch(handleError);
    }
}
function handleError( error: any, caugth: any) {
  let errorMsg = error.message || 'Wopidoooo!  Something is not rigth with the world.  Check again'
  return Observable.throw(errorMsg);
}


