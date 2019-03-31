import { Injectable } from '@angular/core';
import { Http, Headers, RequestOptions, Response } from '@angular/http';
import { AuthHttpService } from './auth-http.service';
import { User } from '../_models/index';

import { environment } from '../../environments/environment';
import { map } from 'rxjs/operators';

@Injectable()
export class UserService {
    constructor(public http: AuthHttpService) { }

    getAll() {
        return this.http.get(environment.apiUrl + '/users', this.jwt()).pipe(map((response: Response) => response.json()));
    }

    getByEid(eid: number) {
        return this.http.get(environment.apiUrl + '/users/' + eid, this.jwt()).pipe(map((response: Response) => response.json()));
    }

    create(user: User) {
        return this.http.post(environment.apiUrl + '/users', user, this.jwt()).pipe(map((response: Response) => response.json()));
    }
 
    update(user: User) {
        let currentUser = JSON.parse(localStorage.getItem('currentUser'));
        currentUser.user = user;
        console.log("Update local storage with: " + JSON.stringify(currentUser.user.preferences));
        localStorage.setItem('currentUser', JSON.stringify(currentUser));
        return this.http.post(environment.apiUrl + '/users/' + user.eid, user, this.jwt()).pipe(map((response: Response) => response.json()));
    }

    updatePreferences(user: User) {
        let currentUser = JSON.parse(localStorage.getItem('currentUser'));
        currentUser.user = user;
        console.log("Update local storage with: " + JSON.stringify(currentUser.user.preferences));
        localStorage.setItem('currentUser', JSON.stringify(currentUser));
        return this.http.post(environment.apiUrl + '/users/preferences/' + user.eid, user, this.jwt()).pipe(map((response: Response) => response.json()));
    }


    delete(eid: number) {
        return this.http.delete(environment.apiUrl + '/users/' + eid, this.jwt()).pipe(map((response: Response) => response.json()));
    }

    // private helper methods

    private jwt() {
        // create authorization header with jwt token
        let currentUser = JSON.parse(localStorage.getItem('currentUser'));
        if (currentUser && currentUser.token) {
            let headers = new Headers({ 'Authorization': 'Bearer ' + currentUser.token });
            return new RequestOptions({ headers: headers });
        }
    }
}