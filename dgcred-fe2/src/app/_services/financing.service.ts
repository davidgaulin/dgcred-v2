import { Injectable } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Observable } from 'rxjs/Rx';
import { Http, Headers, RequestOptions, Response } from '@angular/http';
import { AuthHttpService } from './auth-http.service';
import { Loan } from '../_models/index';

@Injectable()
export class FinancingService {
    constructor(public http: AuthHttpService) { }

    getLoanCount() {
        return this.http.get('http://localhost:8080/api/financing/count').map((response: Response) => response.json()).catch(handleError);
    }

    deleteLoanDocument(leid: number, deid: number) {
        return this.http.get('http://localhost:8080/api/financing/deleteDocument/' + leid + '/' + deid).map((response: Response) => response.json()).catch(handleError);
    }

    addLoanDocument(leid: number, deid: number) {
        console.log("peid: " + leid + " deid: " + deid);
        return this.http.get('http://localhost:8080/api/financing/addDocument/' + leid + '/' + deid).map((response: Response) => response.json()).catch(handleError);
    }

    getByEid(eid: number) {
        return this.http.get('http://localhost:8080/api/financing/' + eid).map((response: Response) => response.json()).catch(handleError);
    }

    save(loan: Loan) {
        return this.http.post('http://localhost:8080/api/financing', loan).map((response: Response) => response.json()).catch(handleError);
    }

    delete(id: number) {
        return this.http.delete('http://localhost:8080/api/financing/' + id).map((response: Response) => response.json()).catch(handleError);
    }
}

function handleError( error: any, caugth: any) {
  let errorMsg = error.message || 'Wopidoooo!  Something is not rigth with the world.  Check again'
  return Observable.throw(errorMsg);
}
