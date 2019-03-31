import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Lease } from '../../_models/index';
import { FinancialInstitution } from '../../_models/index';
import { Document } from '../../_models/index';
import { Property } from '../../_models/index';
import { Unit } from '../../_models/index';
import { DatePipe } from "@angular/common";

import { environment } from '../../../environments/environment';
import { AuthHttpService } from '../../_services/index';
import { LeaseService } from '../../_services/index';
import { LookupService } from '../../_services/index';
import { PropertyService } from '../../_services/index';

import { FileSelectDirective, FileDropDirective, FileUploader } from 'ng2-file-upload';

import { FormBuilder, FormGroup, FormControl, Validators, FormArray, ReactiveFormsModule } from '@angular/forms';
import { NgForm } from '@angular/forms';

import { Observable } from 'rxjs/Observable';

declare var jQuery: any;

@Component({
  selector: 'app-edit',
  templateUrl: './edit.component.html',
  styleUrls: ['./edit.component.scss'],
  providers: [DatePipe]
})

export class EditComponent implements OnInit {

  public errorMessage: string;
  public isLoading = false;
  public unitLoading = true;
  public isSaving = false;
  public update = false;
  public units: Unit[];

  public leaseForm: FormGroup;
  public lease: Lease;

  public hasBaseDropZoneOver: boolean = false;

  public financialInstitutions: any[];
  public paymentFrequencies: any[];
  public properties: Property[];
  public rentPeriods: any[];

  public fileOverBase(e: any): void {
    console.log("File Over Based");
    this.hasBaseDropZoneOver = e;
  }

  public uploader: FileUploader = new FileUploader({
    url: environment.apiUrl + '/document',
    authToken: 'Bearer ' + this.authService.jwtString()
  });

  constructor(private leaseService: LeaseService,
    private lookupService: LookupService,
    private propertyService: PropertyService,
    private _fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private datePipe: DatePipe,
    private authService: AuthHttpService) {


    this.uploader.onCompleteItem = (item: any, response: any, status: any, headers: any) => {
      var doc: Document = JSON.parse(response);
      this.lease.documents.push(doc);
      this.leaseService.addLeaseDocument(this.lease.eid, doc.eid).subscribe(
        p => { },
        e => {
          this.errorMessage = e.message;
          console.log(this.errorMessage);
        },
        () => { });
    }
  }

  ngOnInit() {
    this.buildForm();
    let eid: string = this.route.snapshot.params['eid'];
    this.isLoading = true;
    this.propertyService.getAll().subscribe(
      p => {
        
        this.properties = p;
        // TODO Find a way to chain this, this looks wrong
        if (eid) {
          this.update = true;
          this.leaseService.getByEid(this.route.snapshot.params['eid'])
            .subscribe(
            p => { 
              this.lease = p; 
              this.doUnitCheck(this.lease.property.eid);
              this.isLoading = false;
            },
            e => { this.errorMessage = e; console.log(this.errorMessage); },
            () => {
              console.log(this.lease.eid);
              this.updateLeaseForm();
              this.isLoading = false;
            });
        }
      },
      e => {
        this.errorMessage = e.message;
        console.log(this.errorMessage);
      },
      () => {
        this.isLoading = false;
      }
    );    
    if (eid == null) {
      this.newLease();
    }
    this.isLoading = true;
    this.lookupService.getAll().subscribe(
      p => {
        console.log(p);
        this.paymentFrequencies = p.paymentFrequencies;
        this.financialInstitutions = p.financialInstitutions;
        this.rentPeriods = p.rentPeriods;
      },
      e => { this.errorMessage = e; console.log(this.errorMessage); },
      () => {
        if (!eid) {
          this.newLease();
          this.leaseForm.patchValue(this.lease, { onlySelf: true });
        }
        this.isLoading = false;
      }
    );
  }

  newLease() {
    this.lease = new Lease(0, '2017-01-01', 0, '1', true);
    this.lease.property = this.propertyService.newBlankProperty();
    this.lease.unit = new Unit(0, '0', 0, '0', 0, '0', '1', '1', '');
  }

  updateLeaseForm() {
    this.leaseForm.patchValue(this.lease, { onlySelf: true });
  }

  doUnitCheck(eid: number) {
    if (eid != null && this.properties != null) {
      this.unitLoading = true;
      let tp: any[] = this.properties.filter(property => property.eid === eid);
      if (tp != null && tp.length > 0 && tp[0].units != null) {
        this.units = this.properties.filter(property => property.eid === eid)[0].units;
      } 
      this.unitLoading = false;
    }
  }

  buildForm() {
    this.leaseForm = this._fb.group({
      eid: [''],
      startDate: ['', Validators.required],
      rent: [0, Validators.required],
      rentPeriod: ['M', Validators.required],
      autoRenew: true,
      length: ['12', Validators.required],
      lengthPeriod: ['M', Validators.required],
      property: this._fb.group({
        eid: ['']
      }),
      unit: this._fb.group({
        eid: ['']
      }),
      tenants: this._fb.array([])
    });
    this.addTenant();
  }
  addTenant() {
    // add address to the list
    const control = <FormArray>this.leaseForm.controls['tenants'];
    const addCtrl = this.initTenant();
    control.push(addCtrl);
  }

  initTenant() {
    // initialize our unit
    return this._fb.group({
      eid: [''],
      lastName: ['', Validators.required],
      firstName: ['', Validators.required],
      initial: [''],
      businessNumber: [''],
      ssn: [''],
      driverLicense: [''],
      phoneNumber: this._fb.group({
        type: [''],
        str: [''],
      }),
      email: [''],
      companyName: [''],
      address: this._fb.group({
        eid: [''],
        address1: [''],
        address2: '',
        city: [''],
        provinceState: [''],
        country: [''],
        postalZipCode: [''],
      }),
    });
  }

  removeTenant(i: number) {
    // remove address from the list
    const control = <FormArray>this.leaseForm.controls['tenants'];
    control.removeAt(i);
  }

  cancel() {
    this.router.navigate(['/leases/list']);
  }

  initDocument() {
    // initialize our address
    return this._fb.group({
      eid: [''],
      fileName: [''],
      contentType: ['']
    });
  }
  deleteDocument(index: number, deid: number) {
    this.leaseService.deleteLeaseDocument(this.lease.eid, deid);
    //(<FormArray>this.propertyForm.controls['documents']).removeAt(index);
    this.lease.documents.splice(index, 1);
  }

  onSubmit(theForm: FormGroup) {
    this.isSaving = true;
    this.leaseService.save(theForm.value).subscribe(
      p => { },
      e => {
        this.errorMessage = e.message;
        console.log(this.errorMessage);
      },
      () => { this.isSaving = false; this.router.navigate(['/leases/list']); });
  }
}













