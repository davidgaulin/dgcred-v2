import { Injectable } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Observable } from 'rxjs/Rx';
import { Http, Headers, RequestOptions, Response } from '@angular/http';
import { AuthHttpService } from './auth-http.service';
import { Property } from '../_models/index';

@Injectable()
export class PropertyService {
    constructor(public http: AuthHttpService) { }

    getCount() {
        return this.http.get('http://localhost:8080/api/property/count').map((response: Response) => response.json()).catch(handleError);
    }

    getMidPoint() {
        return this.http.get('http://localhost:8080/api/property/midPoint').map((response: Response) => response.json()).catch(handleError);
    }

    deleteDocument(peid: number, deid: number) {
        return this.http.get('http://localhost:8080/api/property/deleteDocument/' + peid + '/' + deid).map((response: Response) => response.json()).catch(handleError);
    }

    addDocument(peid: number, deid: number) {
        console.log("peid: " + peid + " deid: " + deid);
        return this.http.get('http://localhost:8080/api/property/addDocument/' + peid + '/' + deid).map((response: Response) => response.json()).catch(handleError);
    }


    getAll() {
        // TODO REMOVE
        let b: Property;
        b = new Property(0, '3', 'put test');
        let a = this.http.put('http://localhost:8080/api/property/put', b).map((response: Response) => response.json()).catch(handleError);
        // TODO END

        return this.http.get('http://localhost:8080/api/property').map((response: Response) => response.json()).catch(handleError);
    }

    getRange(start: number, count: number) {
        return this.http.get('http://localhost:8080/api/property/range/' + start + '/' + count).map((response: Response) => response.json()).catch(handleError);
    }    

    getByEid(eid: number) {
        return this.http.get('http://localhost:8080/api/property/' + eid).map((response: Response) => response.json()).catch(handleError);
    }

    save(property: Property) {
        return this.http.post('http://localhost:8080/api/property', property).map((response: Response) => response.json()).catch(handleError);
    }

    delete(id: number) {
        return this.http.delete('http://localhost:8080/api/property/' + id).map((response: Response) => response.json()).catch(handleError);
    }
}

function handleError( error: any, caugth: any) {
  let errorMsg = error.message || 'Wopidoooo!  Something is not rigth with the world.  Check again'
  return Observable.throw(errorMsg);
}