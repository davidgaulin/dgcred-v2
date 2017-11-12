import { NgModule } from '@angular/core';
import { CommonModule } from "@angular/common";
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { DashboardRoutingModule } from "./dashboard-routing.module";
import { ChartistModule } from 'ng-chartist';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { MatchHeightModule } from "../shared/directives/match-height.directive";

import { Dashboard1Component } from "./dashboard1/dashboard1.component";
import { Dashboard2Component } from "./dashboard2/dashboard2.component";
import { PropertyMapModule } from '../propertymap/property-map.module';
import { MapsModule } from '../maps/maps.module';



@NgModule({
    imports: [
        CommonModule,
        DashboardRoutingModule,
        ChartistModule,
        NgbModule,
        MatchHeightModule,
        MapsModule,
        PropertyMapModule,
        FormsModule
    ],
    exports: [],
    declarations: [
        Dashboard1Component,
        Dashboard2Component
    ],
    providers: [],
})
export class DashboardModule { }
