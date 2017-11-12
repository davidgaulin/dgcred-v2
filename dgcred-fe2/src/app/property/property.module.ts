import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PropertyRoutingModule } from "./property-routing.module";
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { ListComponent } from './list/list.component';
import { EditComponent } from './edit/edit.component';
import { UnitComponent } from './unit/unit.component';

import { ReactiveFormsModule, FormsModule } from '@angular/forms';

import { FileUploadModule } from "ng2-file-upload";

@NgModule({
  imports: [
    CommonModule,
    NgbModule,
    PropertyRoutingModule,
    ReactiveFormsModule,
    FormsModule,
    FileUploadModule
  ],
  exports: [],
  declarations: [
    ListComponent, 
    EditComponent,
    UnitComponent
  ],
  providers: []
})

export class PropertyModule { }

