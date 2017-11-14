import { Component, Input } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { LookupService } from '../../_services/index';
@Component({
  selector: 'tenant',
  templateUrl: 'tenant.component.html',
})
export class TenantComponent {
  @Input('group')
  public tenantForm: FormGroup;
  public isLoading = false;
  

  public errorMessage: string;

  constructor(private lookupService: LookupService) {  }

  ngOnInit(): void {
    this.lookupService.getAll().subscribe(
       p => { 
            },
       e => { this.errorMessage = e; console.log(e) },
       () => { this.isLoading = false; } );
  }    
}