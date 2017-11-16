import { Injectable } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Observable } from 'rxjs/Rx';
import { Http, Headers, RequestOptions, Response } from '@angular/http';
import { AuthHttpService } from './auth-http.service';
import { Lease } from '../_models/index';

@Injectable()
export class LeaseService {
    constructor(public http: AuthHttpService) { }

    getAllLeases() {
        return this.http.get('http://localhost:8080/api/lease').map((response: Response) => response.json()).catch(handleError);
    }

    getLeaseCount() {
        return this.http.get('http://localhost:8080/api/lease/count').map((response: Response) => response.json()).catch(handleError);
    }

    deleteLeaseDocument(leid: number, deid: number) {
        return this.http.get('http://localhost:8080/api/lease/deleteDocument/' + leid + '/' + deid).map((response: Response) => response.json()).catch(handleError);
    }

    addLeaseDocument(leid: number, deid: number) {
        console.log("peid: " + leid + " deid: " + deid);
        return this.http.get('http://localhost:8080/api/lease/addDocument/' + leid + '/' + deid).map((response: Response) => response.json()).catch(handleError);
    }

    getByEid(eid: number) {
        return this.http.get('http://localhost:8080/api/lease/' + eid).map((response: Response) => response.json()).catch(handleError);
    }

    save(lease: Lease) {
        return this.http.post('http://localhost:8080/api/lease', lease).map((response: Response) => response.json()).catch(handleError);
    }

    delete(id: number) {
        return this.http.delete('http://localhost:8080/api/lease/' + id).map((response: Response) => response.json()).catch(handleError);
    }
}

function handleError( error: any, caugth: any) {
  let errorMsg = error.message || 'Wopidoooo!  Something is not rigth with the world.  Check again'
  return Observable.throw(errorMsg);
}