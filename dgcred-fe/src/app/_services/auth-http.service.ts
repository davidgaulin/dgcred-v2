import { Injectable } from '@angular/core';
import { Request, XHRBackend, RequestMethod, RequestOptions, Response, Http, RequestOptionsArgs, Headers } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { Router } from '@angular/router';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';

import { environment } from '../../environments/environment';

@Injectable()
export class AuthHttpService extends Http {

	constructor(backend: XHRBackend, defaultOptions: RequestOptions, http: Http, private router: Router) {
        super(backend, defaultOptions);
        console.log("AuthHttpService");
  	}

    public jwtString() {
     // create authorization header with jwt token
     console.log("create authorization header with jwt token");
        let currentUser = JSON.parse(localStorage.getItem('currentUser'));
        if (currentUser && currentUser.token) {
            return currentUser.token;
        } else {
            return null;
        }   
    }    
    public jwt() {
        // create authorization header with jwt token
        let token = this.jwtString();
        console.log("create authorization header with jwt token");
        if (token) {
            let headers = new Headers({ 'Authorization': 'Bearer ' + token });
            return new RequestOptions( { headers: headers } );
        } else {
            return null;
        }
    }    

    public jwtHeader() {
        // create authorization header with jwt token
        console.log("jwtHeader");
        let token = this.jwtString();
        if (token) {
            let headers = new Headers({ 'Authorization': 'Bearer ' + token });
            return headers;
        } else {
            return null;
        }
    }    

    request(url: string | Request, options?: RequestOptionsArgs): Observable<Response> {
        //console.log(this._defaultOptions.headers.has('Authorization'));
	//if (!this._defaultOptions.headers.has('Authorization')) {
		//this._defaultOptions.headers.append('Authorization', 'Bearer ' + this.jwtString());
	//this._defaultOptions.withCredentials = true;
	//}
	this._defaultOptions.headers.delete('Authorization');
	this._defaultOptions.headers.set('Authorization', 'Bearer ' + this.jwtString());
    this._defaultOptions.headers.set('X-PINGOVER', 'pingpong');
    this._defaultOptions.withCredentials = true;
		return super.request(url, options).catch((error: Response) => {
            if ((error.status === 401 || error.status === 403) && !(url == '/login' || url == '/register' || url == '/logout')) {
                console.log('The authentication session expires or the user is not authorised. Force refresh of the current page.');
                this.router.navigate(['logout']);
            }
            return Observable.throw(error);
        });
    }
}
