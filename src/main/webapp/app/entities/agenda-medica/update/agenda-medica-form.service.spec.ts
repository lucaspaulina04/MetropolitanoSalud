import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../agenda-medica.test-samples';

import { AgendaMedicaFormService } from './agenda-medica-form.service';

describe('AgendaMedica Form Service', () => {
  let service: AgendaMedicaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AgendaMedicaFormService);
  });

  describe('Service methods', () => {
    describe('createAgendaMedicaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAgendaMedicaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fechaHora: expect.any(Object),
            pacienteID: expect.any(Object),
            medicoID: expect.any(Object),
            centroSaludID: expect.any(Object),
            estadoHora: expect.any(Object),
            pacienteHora: expect.any(Object),
            horasMedicas: expect.any(Object),
            horasCentroSalud: expect.any(Object),
          }),
        );
      });

      it('passing IAgendaMedica should create a new form with FormGroup', () => {
        const formGroup = service.createAgendaMedicaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fechaHora: expect.any(Object),
            pacienteID: expect.any(Object),
            medicoID: expect.any(Object),
            centroSaludID: expect.any(Object),
            estadoHora: expect.any(Object),
            pacienteHora: expect.any(Object),
            horasMedicas: expect.any(Object),
            horasCentroSalud: expect.any(Object),
          }),
        );
      });
    });

    describe('getAgendaMedica', () => {
      it('should return NewAgendaMedica for default AgendaMedica initial value', () => {
        const formGroup = service.createAgendaMedicaFormGroup(sampleWithNewData);

        const agendaMedica = service.getAgendaMedica(formGroup) as any;

        expect(agendaMedica).toMatchObject(sampleWithNewData);
      });

      it('should return NewAgendaMedica for empty AgendaMedica initial value', () => {
        const formGroup = service.createAgendaMedicaFormGroup();

        const agendaMedica = service.getAgendaMedica(formGroup) as any;

        expect(agendaMedica).toMatchObject({});
      });

      it('should return IAgendaMedica', () => {
        const formGroup = service.createAgendaMedicaFormGroup(sampleWithRequiredData);

        const agendaMedica = service.getAgendaMedica(formGroup) as any;

        expect(agendaMedica).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAgendaMedica should not enable id FormControl', () => {
        const formGroup = service.createAgendaMedicaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAgendaMedica should disable id FormControl', () => {
        const formGroup = service.createAgendaMedicaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
