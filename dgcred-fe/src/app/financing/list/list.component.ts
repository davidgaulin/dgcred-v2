import { Component, ViewChild, OnInit } from '@angular/core';
import { DatatableComponent } from "@swimlane/ngx-datatable/release";

import { FinancingService } from '../../_services/index';
import { LookupService } from '../../_services/index';
import { CurrencyPipe } from '@angular/common';
import { PercentPipe } from '@angular/common';
import { LocalDataSource } from 'ng2-smart-table';

declare var require: any;

@Component({
    selector: 'app-list',
    templateUrl: './list.component.html',
    styleUrls: ['./list.component.scss']
  })

export class ListComponent implements OnInit {
    rows = [];

    temp = [];
    loans = [];
    links: any[];

    errorMessage: string;
    isLoading: boolean = true;

    constructor(private financingService: FinancingService,
                private lookupService: LookupService) { 
    

        this.getAll();

    }

    ngOnInit() {
        //this.buildForm();
    }


    getAll() {
        console.log("Loading loans");
        this.isLoading = true;
        this.financingService.getAllLoans().subscribe(
        p => {
            console.log(p['_links']);
            if (p['_links']) {
            this.links = p['_links'];
            } else {
            this.loans = p;
            }
        },
        e => { this.errorMessage = e; console.log(e) },
        () => {
            this.isLoading = false;
        });
    }  
    
    delete(eid:number) {
        this.financingService.delete(eid);
    }
}