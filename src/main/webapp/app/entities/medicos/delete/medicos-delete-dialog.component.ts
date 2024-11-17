import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMedicos } from '../medicos.model';
import { MedicosService } from '../service/medicos.service';

@Component({
  standalone: true,
  templateUrl: './medicos-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MedicosDeleteDialogComponent {
  medicos?: IMedicos;

  protected medicosService = inject(MedicosService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.medicosService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
