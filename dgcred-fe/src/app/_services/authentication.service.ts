import { Injectable } from '@angular/core';
import { Http, Headers, Response } from '@angular/http';
import 'rxjs/add/operator/map'

import { environment } from '../../environments/environment';
import { map } from 'rxjs/operators';
import { pipe } from '@angular/core/src/render3/pipe';

@Injectable()
export class AuthenticationService {
    constructor(private http: Http) {  console.log("Authenticate Service constructor"); }

    login(username: string, password: string) {
        return this.http.post(environment.registerApiUrl + '/login', JSON.stringify({ username: username, password: password })).pipe(
            map((response: Response) => {
                // login successful if there's a jwt token in the response
                let user = response.json();
                if (user && user.token) {
                    // store user details and jwt token in local storage to keep user logged in between page refreshes
                    localStorage.setItem('currentUser', JSON.stringify(user));
                }
            }));
    }

   register(username: string, password: string, fullname: string) {
        return this.http.post(environment.registerApiUrl + '/register', JSON.stringify({ username: username, password: password, fullname: fullname })).pipe(
            map((response: Response) => {
                // login successful if there's a jwt token in the response
                let user = response.json();
                if (user && user.token) {
                    // store user details and jwt token in local storage to keep user logged in between page refreshes
                    localStorage.setItem('currentUser', JSON.stringify(user));
                }
            }));
    }    

    logout() {
        // remove user from local storage to log user out
        localStorage.removeItem('currentUser');
    }
}