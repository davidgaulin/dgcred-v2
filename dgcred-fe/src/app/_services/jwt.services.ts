import { Injectable } from '@angular/core';
import { Http, Headers, RequestOptions, Response } from '@angular/http';

import { User } from '../_models/index';

import { environment } from '../../environments/environment';

@Injectable()
export class JWTService {

    constructor(public http: Http) { }


    public jwt() {
        // create authorization header with jwt token
        let currentUser = JSON.parse(localStorage.getItem('currentUser'));
        if (currentUser && currentUser.token) {
            let headers = new Headers({ 'Authorization': 'Bearer ' + currentUser.token });
            // Remove when in prod
            console.log('Authorization' + 'Bearer ' + currentUser.token);
            return new RequestOptions({ headers: headers });
        } else {
            return null;
        }
    }
    public jwtHeader() {
        // create authorization header with jwt token
        let currentUser = JSON.parse(localStorage.getItem('currentUser'));
        if (currentUser && currentUser.token) {
            let headers = new Headers({ 'Authorization': 'Bearer ' + currentUser.token });
            return headers;
        } else {
            return null;
        }
    }
    public jwtString() {
     // create authorization header with jwt token
        let currentUser = JSON.parse(localStorage.getItem('currentUser'));
        if (currentUser && currentUser.token) {
            return currentUser.token;
        } else {
            return null;
        }   
    }
}