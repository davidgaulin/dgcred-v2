import { Injectable } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Observable } from 'rxjs/Rx';
import { Http, Headers, RequestOptions, Response } from '@angular/http';
import { AuthHttpService } from './auth-http.service';
import { Event } from '../_models/index';

import { environment } from '../../environments/environment';

@Injectable()
export class CalendarService {
    constructor(public http: AuthHttpService) { } 

    getAllEvents(year: number, month: number) {
        return this.http.get(environment.apiUrl + '/calendar/renewals/' + year + '/' + month).map((response: Response) => response.json()).catch(handleError);
    }
}
function handleError( error: any, caugth: any) {
  let errorMsg = error.message || 'Wopidoooo!  Something is not rigth with the world.  Check again'
  return Observable.throw(errorMsg);
}