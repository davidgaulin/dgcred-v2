import { Injectable } from '@angular/core';
import { Http, Headers, Response } from '@angular/http';
import { AuthHttpService } from './auth-http.service';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map'

import { environment } from '../../environments/environment';

@Injectable()
export class RateHubService {
    constructor(public http: AuthHttpService) { }

    getRate(years: number) {
        return this.http.get(environment.apiUrl + '/rates/fixed/' + years + '/years')
            .map((response: Response) => response.json()).catch(handleError);
    }
}

function handleError( error: any, caugth: any) {
  let errorMsg = error.message || 'Wopidoooo!  Something is not rigth with the world.  Check again'
  return Observable.throw(errorMsg);
}