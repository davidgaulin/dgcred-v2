import { Component, Input } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { LookupService } from '../../_services/index';
@Component({
  selector: 'unit',
  templateUrl: 'unit.component.html',
})
export class UnitComponent {
  @Input('group')
  public unitForm: FormGroup;
  public isLoading: boolean = true;
  public areaUnits: any[];
  public rentPeriods: any[];
  public bedrooms: any;
  public bathrooms: any;

  public bedroomsList: any[] = [ "Studio", "1", "2", "3", "4+" ];
  public bathroomsList: any[] = [ "0.5", "1", "1.5", "2", "2.5", "3", "3.5", "4+" ];
  public errorMessage: string;

  constructor(private lookupService: LookupService) {  }

  ngOnInit(): void {
    this.lookupService.getAll().subscribe(
       p => { this.rentPeriods = p.rentPeriods;
              this.areaUnits = p.areaUnits;
            },
       e => { this.errorMessage = e; console.log(e) },
       () => { this.isLoading = false; } );
  }    
}