import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Loan } from '../../_models/index';
import { FinancialInstitution } from '../../_models/index';
import { Document } from '../../_models/index';
import { DatePipe } from "@angular/common";

import { AuthHttpService } from '../../_services/index';
import { FinancingService } from '../../_services/index';
import { LookupService } from '../../_services/index';
import { PropertyService } from '../../_services/index';
import { environment } from '../../../environments/environment';

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
  public isSaving = false;
  public update = false;

  public loanForm: FormGroup;
  public loan: Loan;

  public hasBaseDropZoneOver: boolean = false;

  public financialInstitutions: any[];
  public paymentFrequencies: any[];
  public properties: any[];

  public fileOverBase(e: any): void {
    console.log("File Over Based");
    this.hasBaseDropZoneOver = e;
  }

  public uploader: FileUploader = new FileUploader({
    url: environment.apiUrl + '/document',
    authToken: 'Bearer ' + this.authService.jwtString()
  });  

  constructor(private financingService: FinancingService,
    private lookupService: LookupService,
    private propertyService: PropertyService,
    private _fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private datePipe: DatePipe,
    private authService: AuthHttpService) {


    this.uploader.onCompleteItem = (item: any, response: any, status: any, headers: any) => {
      var doc: Document = JSON.parse(response);
      this.loan.documents.push(doc);
      this.financingService.addLoanDocument(this.loan.eid, doc.eid).subscribe(
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
    if (eid) {
      this.update = true;
      this.financingService.getByEid(this.route.snapshot.params['eid'])
      .subscribe(
      p => { this.loan = p; },
      e => { this.errorMessage = e; console.log(this.errorMessage); },
      () => {
        console.log(this.loan.eid);
        this.updateLoanForm()
        this.isLoading = false;
      });
    } else {
      this.newLoan();
    }
    this.isLoading = true;
    this.lookupService.getAll().subscribe(
      p => {
        console.log(p);
        this.paymentFrequencies = p.paymentFrequencies;
        this.financialInstitutions = p.financialInstitutions;
      },
      e => { this.errorMessage = e; console.log(this.errorMessage); },
      () => {
        if (!eid) {
          this.newLoan();
          console.log("pathc1");
          this.loanForm.patchValue(this.loan, { onlySelf: true });
        }
        this.isLoading = false;
      }
    );
    this.propertyService.getAll().subscribe(
      p => {
        this.properties = p;
      },
      e => {
        this.errorMessage = e.message;
        console.log(this.errorMessage);
      },
      () => { 
        this.isLoading = false;
      }
    );

  }

  newLoan() {
    this.loan = new Loan(0, '', '', 0, '', 5, 25, 0, '', '', 300);
    this.loan.property = this.propertyService.newBlankProperty();
    this.loan.financialInstitution = new FinancialInstitution();
  }

  updateLoanForm() {
    this.loanForm.patchValue(this.loan, { onlySelf: true });
  }

  buildForm() {
    this.loanForm = this._fb.group({
      eid: [''],
      amount: [0, Validators.required],
      interestRate: [0, Validators.required],
      amortization: [0, Validators.required],
      balance: [0, Validators.required],
      balanceDate: ['', Validators.required],
      renewalDate: ['', Validators.required],
      loanCreationDate: ['', Validators.required],
      paymentFrequency: [''],
      financialInstitution: this._fb.group({
        eid: ['']
      }),
      property: this._fb.group({
        eid: ['']
      })
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

  deleteDocument(index: number, deid: number) {
    this.financingService.deleteLoanDocument(this.loan.eid, deid);
    //(<FormArray>this.propertyForm.controls['documents']).removeAt(index);
    this.loan.documents.splice(index, 1);
  }

  cancel() {
    this.router.navigate(['/financing/list']);
  }

  onSubmit(theForm: FormGroup) {
    this.isSaving = true;
    this.financingService.save(theForm.value).subscribe(
      p => { },
      e => {
        this.errorMessage = e.message;
        console.log(this.errorMessage);
      },
      () => { this.isSaving = false; this.router.navigate(['/financing/list']); });
  }
}
