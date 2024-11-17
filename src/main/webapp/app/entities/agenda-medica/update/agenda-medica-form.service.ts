import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IAgendaMedica, NewAgendaMedica } from '../agenda-medica.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAgendaMedica for edit and NewAgendaMedicaFormGroupInput for create.
 */
type AgendaMedicaFormGroupInput = IAgendaMedica | PartialWithRequiredKeyOf<NewAgendaMedica>;

type AgendaMedicaFormDefaults = Pick<NewAgendaMedica, 'id'>;

type AgendaMedicaFormGroupContent = {
  id: FormControl<IAgendaMedica['id'] | NewAgendaMedica['id']>;
  fechaHora: FormControl<IAgendaMedica['fechaHora']>;
  pacienteID: FormControl<IAgendaMedica['pacienteID']>;
  medicoID: FormControl<IAgendaMedica['medicoID']>;
  centroSaludID: FormControl<IAgendaMedica['centroSaludID']>;
  estadoHora: FormControl<IAgendaMedica['estadoHora']>;
  pacienteHora: FormControl<IAgendaMedica['pacienteHora']>;
  horasMedicas: FormControl<IAgendaMedica['horasMedicas']>;
  horasCentroSalud: FormControl<IAgendaMedica['horasCentroSalud']>;
};

export type AgendaMedicaFormGroup = FormGroup<AgendaMedicaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AgendaMedicaFormService {
  createAgendaMedicaFormGroup(agendaMedica: AgendaMedicaFormGroupInput = { id: null }): AgendaMedicaFormGroup {
    const agendaMedicaRawValue = {
      ...this.getFormDefaults(),
      ...agendaMedica,
    };
    return new FormGroup<AgendaMedicaFormGroupContent>({
      id: new FormControl(
        { value: agendaMedicaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      fechaHora: new FormControl(agendaMedicaRawValue.fechaHora),
      pacienteID: new FormControl(agendaMedicaRawValue.pacienteID),
      medicoID: new FormControl(agendaMedicaRawValue.medicoID),
      centroSaludID: new FormControl(agendaMedicaRawValue.centroSaludID),
      estadoHora: new FormControl(agendaMedicaRawValue.estadoHora),
      pacienteHora: new FormControl(agendaMedicaRawValue.pacienteHora),
      horasMedicas: new FormControl(agendaMedicaRawValue.horasMedicas),
      horasCentroSalud: new FormControl(agendaMedicaRawValue.horasCentroSalud),
    });
  }

  getAgendaMedica(form: AgendaMedicaFormGroup): IAgendaMedica | NewAgendaMedica {
    return form.getRawValue() as IAgendaMedica | NewAgendaMedica;
  }

  resetForm(form: AgendaMedicaFormGroup, agendaMedica: AgendaMedicaFormGroupInput): void {
    const agendaMedicaRawValue = { ...this.getFormDefaults(), ...agendaMedica };
    form.reset(
      {
        ...agendaMedicaRawValue,
        id: { value: agendaMedicaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AgendaMedicaFormDefaults {
    return {
      id: null,
    };
  }
}
