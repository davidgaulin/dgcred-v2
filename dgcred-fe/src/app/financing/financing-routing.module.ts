import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { ListComponent } from "./list/list.component";
import { EditComponent } from "./edit/edit.component";

const routes: Routes = [
  {
    path: '',
    children: [
      {
        path: '',
        redirectTo: 'list'
      },
      {
        path: 'list',
        component: ListComponent,
        data: {
          title: 'List'
        }
      },
      {
        path: 'edit',
        component: EditComponent,
        data: {
          title: 'Create'
        }
      },
      {
        path: 'edit/:eid',
        component: EditComponent,
        data: {
          title: 'Edit'
        }
      },      
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})

export class FinancingRoutingModule { }
