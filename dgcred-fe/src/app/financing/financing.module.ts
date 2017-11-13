import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FinancingRoutingModule } from "./financing-routing.module";
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { ListComponent } from './list/list.component';
import { EditComponent } from './edit/edit.component';

import { ReactiveFormsModule, FormsModule } from '@angular/forms';

import { FileUploadModule } from "ng2-file-upload";

@NgModule({
  imports: [
    CommonModule,
    NgbModule,
    FinancingRoutingModule,
    ReactiveFormsModule,
    FormsModule,
    FileUploadModule
  ],
  exports: [],
  declarations: [
    ListComponent, 
    EditComponent
  ],
  providers: []
})

export class FinancingModule { }
