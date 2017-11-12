import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Property } from '../../_models/index';
import { Unit } from '../../_models/index';
import { Address } from '../../_models/index';
import { Document } from '../../_models/index';
import { DatePipe } from "@angular/common";

import { AuthHttpService } from '../../_services/index';
import { PropertyService } from '../../_services/index';
import { LookupService } from '../../_services/index';

import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';

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

  public property: Property;
  public propertyForm: FormGroup;

  public hasBaseDropZoneOver: boolean = false;

  public isLoading: boolean = true;
  public isSaving: boolean = true;
  public propertyIsLoading = false;
  public properties: Observable<any>;


  public purchaseDate: NgbDateStruct;
  public evaluationDate: NgbDateStruct;

  // lookups
  public provinces: any[];
  public countries: any[];
  public areaUnits: any[];
  public rentPeriods: any[];
  public update: boolean = false;


  public uploader: FileUploader = new FileUploader({
    url: 'http://localhost:8080/api/document',
    authToken: 'Bearer ' + this.authService.jwtString()
  });


  public fileOverBase(e: any): void {
    console.log("File Over Based");
    this.hasBaseDropZoneOver = e;
  }

  constructor(private propertyService: PropertyService,
    private lookupService: LookupService,
    private _fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private datePipe: DatePipe,
    private authService: AuthHttpService) {

    console.log("Property EDIT");

    this.uploader.onCompleteItem = (item: any, response: any, status: any, headers: any) => {
      var doc: Document = JSON.parse(response);
      this.property.documents.push(doc);
      console.log(this.property.documents);
      this.propertyService.addDocument(this.property.eid, doc.eid).subscribe(
        p => { },
        e => {
          this.errorMessage = e.message;
          console.log("ERROR ADDING");
          console.log(this.errorMessage);
        },
        () => { });
      console.log("Document Added!!!!!!!!!!");
    }
  }

  ngOnInit() {

    this.buildForm();
    let eid: string = this.route.snapshot.params['eid'];
    if (eid) {
      this.update = true;
      this.propertyIsLoading = true;
      this.propertyService.getByEid(this.route.snapshot.params['eid'])
        .subscribe(
        p => { this.property = p; },
        e => { this.errorMessage = e; console.log("DAAAAAA"); console.log(this.errorMessage); },
        () => {
          console.log(this.property.eid);
          for (var i = this.property.units.length - 2; i >= 0; i--) {
            this.addUnit();
          }
          this.updatePropertyForm()
          this.propertyIsLoading = false;
        });
    }

    //jQuery('.select2').select2();

    this.lookupService.getAll().subscribe(
      p => {
      this.provinces = p.provinces;
        this.countries = p.countries;
        this.rentPeriods = p.rentPeriods;
        this.areaUnits = p.areaUnits;
      },
      e => { this.errorMessage = e; console.log(this.errorMessage); },
      () => {
        if (!eid) {
          this.newProperty();
          this.propertyForm.patchValue(this.property, { onlySelf: true });
        }
        this.isLoading = false;
      }
    );
  }

  updatePropertyForm() {
    this.propertyForm.patchValue(this.property, { onlySelf: true });
    console.log("Date to isoString:");

    var pdate: Date = new Date(this.property.purchaseDate);
    this.purchaseDate = { day: pdate.getUTCDay(), month: pdate.getUTCMonth() + 1, year: pdate.getUTCFullYear() };
    var edate: Date = new Date(this.property.purchaseDate);
    this.evaluationDate = { day: edate.getUTCDay(), month: edate.getUTCMonth() + 1, year: edate.getUTCFullYear() };
  }

  buildForm() {
    this.propertyForm = this._fb.group({
      eid: [''],
      evaluation: [0],
      evaluationDate: [''],
      name: ['', Validators.required],
      purchaseDate: [''],
      purchasePrice: [0],
      constructionYear: [0],
      financed: [true],
      address: this._fb.group({
        eid: [''],
        address1: ['', Validators.required],
        address2: '',
        city: ['', Validators.required],
        provinceState: ['', Validators.required],
        country: ['', Validators.required],
        postalZipCode: ['', Validators.required],
      }),
      units: this._fb.array([])
      // documents: this._fb.array([])
    });
    this.addUnit();
  }

  newProperty() {
    let address: Address = new Address(0, '', '', '', '', '', '');
    let unit: Unit = new Unit(0, '', 0, '', 0, '', '', '', '');
    unit.number = '';
    unit.area = 0;
    unit.projectedRent = 0;
    unit.areaUnit = this.areaUnits[0].code;
    unit.rentPeriod = this.rentPeriods[0].code;
    unit.bathrooms = '1';
    unit.bedrooms = '2';
    unit.description = ''
    this.property = new Property(0, '', 0, 0, address);
    this.property.evaluation = 0;
    this.property.units = [unit];
  }
  addUnit() {
    // add address to the list
    const control = <FormArray>this.propertyForm.controls['units'];
    const addCtrl = this.initUnit();
    control.push(addCtrl);
  }

  removeUnit(i: number) {
    // remove address from the list
    console.log("TATABABABABAB" + i);
    const control = <FormArray>this.propertyForm.controls['units'];
    control.removeAt(i);
  }

  initUnit() {
    // initialize our address
    return this._fb.group({
      eid: [''],
      number: ['', Validators.required],
      area: [''],
      areaUnit: [''],
      projectedRent: [''],
      rentPeriod: [''],
      bedrooms: ['2'],
      bathrooms: ['1'],
      description: [''],
    });
  }

  initDocument() {
    // initialize our address
    return this._fb.group({
      eid: [''],
      fileName: [''],
      contentType: ['']
    });
  }

  onSubmit(theForm: FormGroup) {
    this.isSaving = true;
    // update purchase date since it is standalone
    console.log("WTFGFFFFFFFF");
    theForm.patchValue({ purchaseDate: new Date(this.purchaseDate['year'], this.purchaseDate['month'] - 1, this.purchaseDate['day']).getTime() / 1000 });
    theForm.patchValue({ evaluationDate: new Date(this.evaluationDate['year'], this.evaluationDate['month'] - 1, this.evaluationDate['day']).getTime() / 1000 });
    this.propertyService.save(theForm.value).subscribe(
      p => { },
      e => {
        this.errorMessage = e.message;
        console.log(this.errorMessage);
      },
      () => { this.isSaving = false; this.router.navigate(['/property/list']); });
  }
  cancel() {
    this.router.navigate(['/property/list']);
  }

  deleteDocument(index: number, deid: number) {
    this.propertyService.deleteDocument(this.property.eid, deid);
    //(<FormArray>this.propertyForm.controls['documents']).removeAt(index);
    this.property.documents.splice(index, 1);
  }

}
