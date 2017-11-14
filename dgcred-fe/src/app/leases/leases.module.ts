import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { LeasesRoutingModule } from "./leases-routing.module";
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { ListComponent } from './list/list.component';
import { EditComponent } from './edit/edit.component';
import { TenantComponent } from './tenant/tenant.component';

import { ReactiveFormsModule, FormsModule } from '@angular/forms';

import { FileUploadModule } from "ng2-file-upload";

@NgModule({
  imports: [
    CommonModule,
    NgbModule,
    LeasesRoutingModule,
    ReactiveFormsModule,
    FormsModule,
    FileUploadModule 
  ],
  exports: [],
  declarations: [
    ListComponent, 
    EditComponent,
    TenantComponent 
  ],
  providers: []
})

export class LeasesModule { }
