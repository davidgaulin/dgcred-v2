import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ListComponent } from './list/list.component';
import { EditComponent } from './edit/edit.component';

@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [ListComponent, EditComponent]
})
export class LeasesModule { }
