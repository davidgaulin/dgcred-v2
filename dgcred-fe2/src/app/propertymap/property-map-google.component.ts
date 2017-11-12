import { Component, Input } from '@angular/core';
import { MapMarker } from '../_models/mapmarker';

@Component({
  selector: 'propertymapgoogle',
  templateUrl: 'property-map-google.template.html',
  styles: ['agm-map { height: 300px; }']
})
export class PropertyMapGoogle {
  @Input('lat')
  public lat: number = -37.813179;
  @Input('lng')
  public lng: number = 144.950259;
  @Input('zoom')
  public zoom: number = 13;
  @Input('markers')
  public markers: MapMarker[];
}
