import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { AgmCoreModule } from '@agm/core';
import { AgmSnazzyInfoWindowModule } from '@agm/snazzy-info-window';

import { PropertyMapGoogle } from './property-map-google.component';

   
@NgModule({
  declarations: [
    PropertyMapGoogle
  ],
  exports: [ PropertyMapGoogle ],
  imports: [
    CommonModule,
    FormsModule,
    AgmCoreModule.forRoot({
      apiKey: 'AIzaSyCKQ4AdrfylnvDVL4U-_nDcI04YejtzbTI'
    }),
    AgmSnazzyInfoWindowModule
  ],
  bootstrap: [ PropertyMapGoogle ]
})
 
export class PropertyMapModule {
}