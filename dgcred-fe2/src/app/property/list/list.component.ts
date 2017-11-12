import { Component, OnInit } from '@angular/core';
import { PropertyService } from '../../_services/index';
import { LookupService } from '../../_services/index';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss']
})
export class ListComponent implements OnInit {

  errorMessage: string;
  isLoading: boolean = true;
  properties: any[];

  // lookups
  provinces: any[];
  countries: any[];

  links: any[];

  pagedItems: any[];
  public maxSize: number = 5;
  public totalItems: number = 1000;
  public currentPage: number = 1;
  public itemsPerPage: number = 3;
  public allItems: any[];

  constructor(private propertyService: PropertyService,
    private lookupService: LookupService,
    private route: ActivatedRoute,
    private router: Router) { }

  ngOnInit() {
    console.log("WHAT THE FUCK");
    this.lookupService.getAll().subscribe(
      p => {
        this.provinces = p.provinces;
        this.countries = p.countries;
      },
      e => { this.errorMessage = e; console.log(e) },
      () => { });
    this.getAll();
  }


  getAll() {
    this.isLoading = true;
    let event: any = { page: 1, itemsPerPage: this.itemsPerPage };
    this.propertyService.getAll().subscribe(
      p => {
        console.log(p['_links']);
        if (p['_links']) {
          this.links = p['_links'];
        } else {
          this.properties = p;
          this.allItems = p;
          this.totalItems = this.allItems.length;
        }
      },
      e => { this.errorMessage = e; console.log(e) },
      () => {
        this.pageChanged(event);
        for (var i = 0; i < this.properties.length; i++) {
          if (this.properties[i].units) {
            this.properties[i].units = this.properties[i].units.sort((n1,n2) => n1 - n2);
          }
          //var marker = new MapMarker();
          //if (this.properties[i].latitude > 0) {
          //marker.lat = this.properties[i].latitude;
          //marker.lng = this.properties[i].longitude;
          //marker.label = this.properties[i].name;
          //marker.property = this.properties[i];
          //marker.draggable = false;
          //this.markers.push(marker);
          //}
        }
        this.isLoading = false;
      });
  }
  public pageChanged(event: any): void {

    // get current page of items
    let start: number = ((event.page - 1) * this.itemsPerPage);
    let end: number = ((event.page - 1) * this.itemsPerPage) + this.itemsPerPage;
    if (end > this.totalItems) {
      end = this.totalItems;
    }
    this.pagedItems = this.allItems.slice(start, end);
  }
 
  delete(eid: number) {
    this.isLoading = true;
    this.propertyService.delete(eid).subscribe(
      p => { },
      e => { this.errorMessage = e.message; },
      () => { this.isLoading = false; this.getAll(); }
    );
  }
  edit(eid: number) {
    //this.router.navigate(['/app/property/edit/' + eid]);
  }

}
