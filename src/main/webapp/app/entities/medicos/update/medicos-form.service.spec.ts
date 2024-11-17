import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../medicos.test-samples';

import { MedicosFormService } from './medicos-form.service';

describe('Medicos Form Service', () => {
  let service: MedicosFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MedicosFormService);
  });

  describe('Service methods', () => {
    describe('createMedicosFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMedicosFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            apellido: expect.any(Object),
            especialidad: expect.any(Object),
            email: expect.any(Object),
            telefono: expect.any(Object),
          }),
        );
      });

      it('passing IMedicos should create a new form with FormGroup', () => {
        const formGroup = service.createMedicosFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            apellido: expect.any(Object),
            especialidad: expect.any(Object),
            email: expect.any(Object),
            telefono: expect.any(Object),
          }),
        );
      });
    });

    describe('getMedicos', () => {
      it('should return NewMedicos for default Medicos initial value', () => {
        const formGroup = service.createMedicosFormGroup(sampleWithNewData);

        const medicos = service.getMedicos(formGroup) as any;

        expect(medicos).toMatchObject(sampleWithNewData);
      });

      it('should return NewMedicos for empty Medicos initial value', () => {
        const formGroup = service.createMedicosFormGroup();

        const medicos = service.getMedicos(formGroup) as any;

        expect(medicos).toMatchObject({});
      });

      it('should return IMedicos', () => {
        const formGroup = service.createMedicosFormGroup(sampleWithRequiredData);

        const medicos = service.getMedicos(formGroup) as any;

        expect(medicos).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMedicos should not enable id FormControl', () => {
        const formGroup = service.createMedicosFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMedicos should disable id FormControl', () => {
        const formGroup = service.createMedicosFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
