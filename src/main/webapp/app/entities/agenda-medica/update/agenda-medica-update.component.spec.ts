import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IPaciente } from 'app/entities/paciente/paciente.model';
import { PacienteService } from 'app/entities/paciente/service/paciente.service';
import { IMedicos } from 'app/entities/medicos/medicos.model';
import { MedicosService } from 'app/entities/medicos/service/medicos.service';
import { ICentroSalud } from 'app/entities/centro-salud/centro-salud.model';
import { CentroSaludService } from 'app/entities/centro-salud/service/centro-salud.service';
import { IAgendaMedica } from '../agenda-medica.model';
import { AgendaMedicaService } from '../service/agenda-medica.service';
import { AgendaMedicaFormService } from './agenda-medica-form.service';

import { AgendaMedicaUpdateComponent } from './agenda-medica-update.component';

describe('AgendaMedica Management Update Component', () => {
  let comp: AgendaMedicaUpdateComponent;
  let fixture: ComponentFixture<AgendaMedicaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let agendaMedicaFormService: AgendaMedicaFormService;
  let agendaMedicaService: AgendaMedicaService;
  let pacienteService: PacienteService;
  let medicosService: MedicosService;
  let centroSaludService: CentroSaludService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AgendaMedicaUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(AgendaMedicaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AgendaMedicaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    agendaMedicaFormService = TestBed.inject(AgendaMedicaFormService);
    agendaMedicaService = TestBed.inject(AgendaMedicaService);
    pacienteService = TestBed.inject(PacienteService);
    medicosService = TestBed.inject(MedicosService);
    centroSaludService = TestBed.inject(CentroSaludService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Paciente query and add missing value', () => {
      const agendaMedica: IAgendaMedica = { id: 456 };
      const pacienteHora: IPaciente = { id: 18426 };
      agendaMedica.pacienteHora = pacienteHora;

      const pacienteCollection: IPaciente[] = [{ id: 31084 }];
      jest.spyOn(pacienteService, 'query').mockReturnValue(of(new HttpResponse({ body: pacienteCollection })));
      const additionalPacientes = [pacienteHora];
      const expectedCollection: IPaciente[] = [...additionalPacientes, ...pacienteCollection];
      jest.spyOn(pacienteService, 'addPacienteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ agendaMedica });
      comp.ngOnInit();

      expect(pacienteService.query).toHaveBeenCalled();
      expect(pacienteService.addPacienteToCollectionIfMissing).toHaveBeenCalledWith(
        pacienteCollection,
        ...additionalPacientes.map(expect.objectContaining),
      );
      expect(comp.pacientesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Medicos query and add missing value', () => {
      const agendaMedica: IAgendaMedica = { id: 456 };
      const horasMedicas: IMedicos = { id: 31702 };
      agendaMedica.horasMedicas = horasMedicas;

      const medicosCollection: IMedicos[] = [{ id: 29969 }];
      jest.spyOn(medicosService, 'query').mockReturnValue(of(new HttpResponse({ body: medicosCollection })));
      const additionalMedicos = [horasMedicas];
      const expectedCollection: IMedicos[] = [...additionalMedicos, ...medicosCollection];
      jest.spyOn(medicosService, 'addMedicosToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ agendaMedica });
      comp.ngOnInit();

      expect(medicosService.query).toHaveBeenCalled();
      expect(medicosService.addMedicosToCollectionIfMissing).toHaveBeenCalledWith(
        medicosCollection,
        ...additionalMedicos.map(expect.objectContaining),
      );
      expect(comp.medicosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call CentroSalud query and add missing value', () => {
      const agendaMedica: IAgendaMedica = { id: 456 };
      const horasCentroSalud: ICentroSalud = { id: 5273 };
      agendaMedica.horasCentroSalud = horasCentroSalud;

      const centroSaludCollection: ICentroSalud[] = [{ id: 20327 }];
      jest.spyOn(centroSaludService, 'query').mockReturnValue(of(new HttpResponse({ body: centroSaludCollection })));
      const additionalCentroSaluds = [horasCentroSalud];
      const expectedCollection: ICentroSalud[] = [...additionalCentroSaluds, ...centroSaludCollection];
      jest.spyOn(centroSaludService, 'addCentroSaludToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ agendaMedica });
      comp.ngOnInit();

      expect(centroSaludService.query).toHaveBeenCalled();
      expect(centroSaludService.addCentroSaludToCollectionIfMissing).toHaveBeenCalledWith(
        centroSaludCollection,
        ...additionalCentroSaluds.map(expect.objectContaining),
      );
      expect(comp.centroSaludsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const agendaMedica: IAgendaMedica = { id: 456 };
      const pacienteHora: IPaciente = { id: 14525 };
      agendaMedica.pacienteHora = pacienteHora;
      const horasMedicas: IMedicos = { id: 31525 };
      agendaMedica.horasMedicas = horasMedicas;
      const horasCentroSalud: ICentroSalud = { id: 17429 };
      agendaMedica.horasCentroSalud = horasCentroSalud;

      activatedRoute.data = of({ agendaMedica });
      comp.ngOnInit();

      expect(comp.pacientesSharedCollection).toContain(pacienteHora);
      expect(comp.medicosSharedCollection).toContain(horasMedicas);
      expect(comp.centroSaludsSharedCollection).toContain(horasCentroSalud);
      expect(comp.agendaMedica).toEqual(agendaMedica);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAgendaMedica>>();
      const agendaMedica = { id: 123 };
      jest.spyOn(agendaMedicaFormService, 'getAgendaMedica').mockReturnValue(agendaMedica);
      jest.spyOn(agendaMedicaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ agendaMedica });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: agendaMedica }));
      saveSubject.complete();

      // THEN
      expect(agendaMedicaFormService.getAgendaMedica).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(agendaMedicaService.update).toHaveBeenCalledWith(expect.objectContaining(agendaMedica));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAgendaMedica>>();
      const agendaMedica = { id: 123 };
      jest.spyOn(agendaMedicaFormService, 'getAgendaMedica').mockReturnValue({ id: null });
      jest.spyOn(agendaMedicaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ agendaMedica: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: agendaMedica }));
      saveSubject.complete();

      // THEN
      expect(agendaMedicaFormService.getAgendaMedica).toHaveBeenCalled();
      expect(agendaMedicaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAgendaMedica>>();
      const agendaMedica = { id: 123 };
      jest.spyOn(agendaMedicaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ agendaMedica });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(agendaMedicaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePaciente', () => {
      it('Should forward to pacienteService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(pacienteService, 'comparePaciente');
        comp.comparePaciente(entity, entity2);
        expect(pacienteService.comparePaciente).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareMedicos', () => {
      it('Should forward to medicosService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(medicosService, 'compareMedicos');
        comp.compareMedicos(entity, entity2);
        expect(medicosService.compareMedicos).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCentroSalud', () => {
      it('Should forward to centroSaludService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(centroSaludService, 'compareCentroSalud');
        comp.compareCentroSalud(entity, entity2);
        expect(centroSaludService.compareCentroSalud).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
