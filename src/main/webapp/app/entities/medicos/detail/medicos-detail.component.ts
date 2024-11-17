import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IMedicos } from '../medicos.model';

@Component({
  standalone: true,
  selector: 'jhi-medicos-detail',
  templateUrl: './medicos-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class MedicosDetailComponent {
  medicos = input<IMedicos | null>(null);

  previousState(): void {
    window.history.back();
  }
}
