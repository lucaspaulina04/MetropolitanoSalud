import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPaciente } from 'app/entities/paciente/paciente.model';
import { PacienteService } from 'app/entities/paciente/service/paciente.service';
import { IMedicos } from 'app/entities/medicos/medicos.model';
import { MedicosService } from 'app/entities/medicos/service/medicos.service';
import { ICentroSalud } from 'app/entities/centro-salud/centro-salud.model';
import { CentroSaludService } from 'app/entities/centro-salud/service/centro-salud.service';
import { AgendaMedicaService } from '../service/agenda-medica.service';
import { IAgendaMedica } from '../agenda-medica.model';
import { AgendaMedicaFormGroup, AgendaMedicaFormService } from './agenda-medica-form.service';

@Component({
  standalone: true,
  selector: 'jhi-agenda-medica-update',
  templateUrl: './agenda-medica-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AgendaMedicaUpdateComponent implements OnInit {
  isSaving = false;
  agendaMedica: IAgendaMedica | null = null;

  pacientesSharedCollection: IPaciente[] = [];
  medicosSharedCollection: IMedicos[] = [];
  centroSaludsSharedCollection: ICentroSalud[] = [];

  protected agendaMedicaService = inject(AgendaMedicaService);
  protected agendaMedicaFormService = inject(AgendaMedicaFormService);
  protected pacienteService = inject(PacienteService);
  protected medicosService = inject(MedicosService);
  protected centroSaludService = inject(CentroSaludService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AgendaMedicaFormGroup = this.agendaMedicaFormService.createAgendaMedicaFormGroup();

  comparePaciente = (o1: IPaciente | null, o2: IPaciente | null): boolean => this.pacienteService.comparePaciente(o1, o2);

  compareMedicos = (o1: IMedicos | null, o2: IMedicos | null): boolean => this.medicosService.compareMedicos(o1, o2);

  compareCentroSalud = (o1: ICentroSalud | null, o2: ICentroSalud | null): boolean => this.centroSaludService.compareCentroSalud(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ agendaMedica }) => {
      this.agendaMedica = agendaMedica;
      if (agendaMedica) {
        this.updateForm(agendaMedica);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const agendaMedica = this.agendaMedicaFormService.getAgendaMedica(this.editForm);
    if (agendaMedica.id !== null) {
      this.subscribeToSaveResponse(this.agendaMedicaService.update(agendaMedica));
    } else {
      this.subscribeToSaveResponse(this.agendaMedicaService.create(agendaMedica));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAgendaMedica>>): void {
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

  protected updateForm(agendaMedica: IAgendaMedica): void {
    this.agendaMedica = agendaMedica;
    this.agendaMedicaFormService.resetForm(this.editForm, agendaMedica);

    this.pacientesSharedCollection = this.pacienteService.addPacienteToCollectionIfMissing<IPaciente>(
      this.pacientesSharedCollection,
      agendaMedica.pacienteHora,
    );
    this.medicosSharedCollection = this.medicosService.addMedicosToCollectionIfMissing<IMedicos>(
      this.medicosSharedCollection,
      agendaMedica.horasMedicas,
    );
    this.centroSaludsSharedCollection = this.centroSaludService.addCentroSaludToCollectionIfMissing<ICentroSalud>(
      this.centroSaludsSharedCollection,
      agendaMedica.horasCentroSalud,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.pacienteService
      .query()
      .pipe(map((res: HttpResponse<IPaciente[]>) => res.body ?? []))
      .pipe(
        map((pacientes: IPaciente[]) =>
          this.pacienteService.addPacienteToCollectionIfMissing<IPaciente>(pacientes, this.agendaMedica?.pacienteHora),
        ),
      )
      .subscribe((pacientes: IPaciente[]) => (this.pacientesSharedCollection = pacientes));

    this.medicosService
      .query()
      .pipe(map((res: HttpResponse<IMedicos[]>) => res.body ?? []))
      .pipe(
        map((medicos: IMedicos[]) =>
          this.medicosService.addMedicosToCollectionIfMissing<IMedicos>(medicos, this.agendaMedica?.horasMedicas),
        ),
      )
      .subscribe((medicos: IMedicos[]) => (this.medicosSharedCollection = medicos));

    this.centroSaludService
      .query()
      .pipe(map((res: HttpResponse<ICentroSalud[]>) => res.body ?? []))
      .pipe(
        map((centroSaluds: ICentroSalud[]) =>
          this.centroSaludService.addCentroSaludToCollectionIfMissing<ICentroSalud>(centroSaluds, this.agendaMedica?.horasCentroSalud),
        ),
      )
      .subscribe((centroSaluds: ICentroSalud[]) => (this.centroSaludsSharedCollection = centroSaluds));
  }
}
