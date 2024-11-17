import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IAgendaMedica } from '../agenda-medica.model';

@Component({
  standalone: true,
  selector: 'jhi-agenda-medica-detail',
  templateUrl: './agenda-medica-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class AgendaMedicaDetailComponent {
  agendaMedica = input<IAgendaMedica | null>(null);

  previousState(): void {
    window.history.back();
  }
}
