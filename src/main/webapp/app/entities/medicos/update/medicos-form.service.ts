import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IMedicos, NewMedicos } from '../medicos.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMedicos for edit and NewMedicosFormGroupInput for create.
 */
type MedicosFormGroupInput = IMedicos | PartialWithRequiredKeyOf<NewMedicos>;

type MedicosFormDefaults = Pick<NewMedicos, 'id'>;

type MedicosFormGroupContent = {
  id: FormControl<IMedicos['id'] | NewMedicos['id']>;
  nombre: FormControl<IMedicos['nombre']>;
  apellido: FormControl<IMedicos['apellido']>;
  especialidad: FormControl<IMedicos['especialidad']>;
  email: FormControl<IMedicos['email']>;
  telefono: FormControl<IMedicos['telefono']>;
};

export type MedicosFormGroup = FormGroup<MedicosFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MedicosFormService {
  createMedicosFormGroup(medicos: MedicosFormGroupInput = { id: null }): MedicosFormGroup {
    const medicosRawValue = {
      ...this.getFormDefaults(),
      ...medicos,
    };
    return new FormGroup<MedicosFormGroupContent>({
      id: new FormControl(
        { value: medicosRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nombre: new FormControl(medicosRawValue.nombre),
      apellido: new FormControl(medicosRawValue.apellido),
      especialidad: new FormControl(medicosRawValue.especialidad),
      email: new FormControl(medicosRawValue.email),
      telefono: new FormControl(medicosRawValue.telefono),
    });
  }

  getMedicos(form: MedicosFormGroup): IMedicos | NewMedicos {
    return form.getRawValue() as IMedicos | NewMedicos;
  }

  resetForm(form: MedicosFormGroup, medicos: MedicosFormGroupInput): void {
    const medicosRawValue = { ...this.getFormDefaults(), ...medicos };
    form.reset(
      {
        ...medicosRawValue,
        id: { value: medicosRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MedicosFormDefaults {
    return {
      id: null,
    };
  }
}
