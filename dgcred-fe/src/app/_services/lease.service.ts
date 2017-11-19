import { Injectable } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Observable } from 'rxjs/Rx';
import { Http, Headers, RequestOptions, Response } from '@angular/http';
import { AuthHttpService } from './auth-http.service';
import { Lease } from '../_models/index';

import { environment } from '../../environments/environment';

@Injectable()
export class LeaseService {
    constructor(public http: AuthHttpService) { }

    getAllLeases() {
        return this.http.get(environment.apiUrl + '/lease').map((response: Response) => response.json()).catch(handleError);
    }

    getLeaseCount() {
        return this.http.get(environment.apiUrl + '/lease/count').map((response: Response) => response.json()).catch(handleError);
    }

    deleteLeaseDocument(leid: number, deid: number) {
        return this.http.get(environment.apiUrl + '/lease/deleteDocument/' + leid + '/' + deid).map((response: Response) => response.json()).catch(handleError);
    }

    addLeaseDocument(leid: number, deid: number) {
        console.log("peid: " + leid + " deid: " + deid);
        return this.http.get(environment.apiUrl + '/lease/addDocument/' + leid + '/' + deid).map((response: Response) => response.json()).catch(handleError);
    }

    getByEid(eid: number) {
        return this.http.get(environment.apiUrl + '/lease/' + eid).map((response: Response) => response.json()).catch(handleError);
    }

    save(lease: Lease) {
        return this.http.post(environment.apiUrl + '/lease', lease).map((response: Response) => response.json()).catch(handleError);
    }

    delete(id: number) {
        return this.http.delete(environment.apiUrl + '/lease/' + id).map((response: Response) => response.json()).catch(handleError);
    }
}

function handleError( error: any, caugth: any) {
  let errorMsg = error.message || 'Wopidoooo!  Something is not rigth with the world.  Check again'
  return Observable.throw(errorMsg);
}
