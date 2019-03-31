import { Injectable } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Http, Headers, RequestOptions, Response } from '@angular/http';
import { AuthHttpService } from './auth-http.service';
import { Property } from '../_models/index';

import { environment } from '../../environments/environment';
import { map } from 'rxjs/operators';

@Injectable()
export class LoanService {
    constructor(public http: AuthHttpService) { }

    getLoanForPropertyEid(propertyEid: number) {
        return this.http.get(environment.apiUrl + '/loan/property/' + propertyEid).pipe(
          map((response: Response) => response.json()));
          // TODO Catch error.catch(handleError);
    }
}
