import { Component, OnInit } from '@angular/core';

import { LeaseService } from '../../_services/index';
import { LookupService } from '../../_services/index';


@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss']
})
export class ListComponent implements OnInit {

  errorMessage: string;
  isLoading: boolean = true;
  leases: any[];

  constructor(private lookupService: LookupService, private leaseService: LeaseService) { }

  ngOnInit() {
    this.lookupService.getAll().subscribe(
      p => {
        this.getAll();
      },
      e => { this.errorMessage = e; console.log(e) },
      () => { });
    
  }

  getAll() {
    this.isLoading = true;
    this.leaseService.getAllLeases().subscribe(
      p => {
        this.leases = p;
        debugger
      },
      e => { this.errorMessage = e; console.log(e) },
      () => {
        this.isLoading = false;
      });
  }

}
