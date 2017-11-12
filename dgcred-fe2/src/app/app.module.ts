
import { NgModule } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { AppRoutingModule } from './app-routing.module';
import { SharedModule } from "./shared/shared.module";
import { ToastModule, ToastOptions } from 'ng2-toastr/ng2-toastr';
import { AgmCoreModule } from '@agm/core';
import { HttpModule } from '@angular/http';

import { ReactiveFormsModule, FormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { ContentLayoutComponent } from "./layouts/content/content-layout.component";
import { FullLayoutComponent } from "./layouts/full/full-layout.component";

import { CustomOption } from "./shared/toastr/custom-option";

import { AuthGuard } from './_guards/index';

import {
    AlertService, AuthenticationService,
    UserService, PropertyService, FinancialGraphService, LoanService,
    AuthHttpService, LookupService, CalendarService, RateHubService
} from './_services/index';


import * as $ from 'jquery';


@NgModule({
    declarations: [
        AppComponent,
        FullLayoutComponent,
        ContentLayoutComponent
    ],
    imports: [
        BrowserAnimationsModule,
        AppRoutingModule,
        SharedModule,
        HttpModule,
        ToastModule.forRoot(),
        NgbModule.forRoot(),
        AgmCoreModule.forRoot({
            apiKey: 'AIzaSyBr5_picK8YJK7fFR2CPzTVMj6GG1TtRGo'
        }),
        FormsModule,
        ReactiveFormsModule
    ],
    providers: [
        // Authentication Providers
        AuthGuard,
        AlertService,
        AuthenticationService,
        UserService,
        AuthHttpService,
        // Data Services
        LookupService,
        PropertyService,
        FinancialGraphService,
        LoanService,
        CalendarService,
        RateHubService,
        //Toastr providers
        { provide: ToastOptions, useClass: CustomOption }
    ],
    bootstrap: [AppComponent]
})
export class AppModule { }
