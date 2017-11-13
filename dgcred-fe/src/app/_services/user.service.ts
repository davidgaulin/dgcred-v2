import { Injectable } from '@angular/core';
import { Http, Headers, RequestOptions, Response } from '@angular/http';
import { AuthHttpService } from './auth-http.service';
import { User } from '../_models/index';

@Injectable()
export class UserService {
    constructor(public http: AuthHttpService) { }

    getAll() {
        return this.http.get('http://localhost:8080/api/users', this.jwt()).map((response: Response) => response.json());
    }

    getByEid(eid: number) {
        return this.http.get('http://localhost:8080/api/users/' + eid, this.jwt()).map((response: Response) => response.json());
    }

    create(user: User) {
        return this.http.post('http://localhost:8080/api/users', user, this.jwt()).map((response: Response) => response.json());
    }
 
    update(user: User) {
        let currentUser = JSON.parse(localStorage.getItem('currentUser'));
        currentUser.user = user;
        console.log("Update local storage with: " + JSON.stringify(currentUser.user.preferences));
        localStorage.setItem('currentUser', JSON.stringify(currentUser));
        return this.http.post('http://localhost:8080/api/users/' + user.eid, user, this.jwt()).map((response: Response) => response.json());
    }

    updatePreferences(user: User) {
        let currentUser = JSON.parse(localStorage.getItem('currentUser'));
        currentUser.user = user;
        console.log("Update local storage with: " + JSON.stringify(currentUser.user.preferences));
        localStorage.setItem('currentUser', JSON.stringify(currentUser));
        return this.http.post('http://localhost:8080/api/users/preferences/' + user.eid, user, this.jwt()).map((response: Response) => response.json());
    }


    delete(eid: number) {
        return this.http.delete('http://localhost:8080/api/users/' + eid, this.jwt()).map((response: Response) => response.json());
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