import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMedicos } from '../medicos.model';
import { MedicosService } from '../service/medicos.service';
import { MedicosFormGroup, MedicosFormService } from './medicos-form.service';

@Component({
  standalone: true,
  selector: 'jhi-medicos-update',
  templateUrl: './medicos-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MedicosUpdateComponent implements OnInit {
  isSaving = false;
  medicos: IMedicos | null = null;

  protected medicosService = inject(MedicosService);
  protected medicosFormService = inject(MedicosFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MedicosFormGroup = this.medicosFormService.createMedicosFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ medicos }) => {
      this.medicos = medicos;
      if (medicos) {
        this.updateForm(medicos);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const medicos = this.medicosFormService.getMedicos(this.editForm);
    if (medicos.id !== null) {
      this.subscribeToSaveResponse(this.medicosService.update(medicos));
    } else {
      this.subscribeToSaveResponse(this.medicosService.create(medicos));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMedicos>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(medicos: IMedicos): void {
    this.medicos = medicos;
    this.medicosFormService.resetForm(this.editForm, medicos);
  }
}
