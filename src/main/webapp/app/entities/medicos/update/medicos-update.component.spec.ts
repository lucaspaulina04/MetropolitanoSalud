import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { MedicosService } from '../service/medicos.service';
import { IMedicos } from '../medicos.model';
import { MedicosFormService } from './medicos-form.service';

import { MedicosUpdateComponent } from './medicos-update.component';

describe('Medicos Management Update Component', () => {
  let comp: MedicosUpdateComponent;
  let fixture: ComponentFixture<MedicosUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let medicosFormService: MedicosFormService;
  let medicosService: MedicosService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MedicosUpdateComponent],
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
      .overrideTemplate(MedicosUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MedicosUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    medicosFormService = TestBed.inject(MedicosFormService);
    medicosService = TestBed.inject(MedicosService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const medicos: IMedicos = { id: 456 };

      activatedRoute.data = of({ medicos });
      comp.ngOnInit();

      expect(comp.medicos).toEqual(medicos);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMedicos>>();
      const medicos = { id: 123 };
      jest.spyOn(medicosFormService, 'getMedicos').mockReturnValue(medicos);
      jest.spyOn(medicosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medicos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: medicos }));
      saveSubject.complete();

      // THEN
      expect(medicosFormService.getMedicos).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(medicosService.update).toHaveBeenCalledWith(expect.objectContaining(medicos));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMedicos>>();
      const medicos = { id: 123 };
      jest.spyOn(medicosFormService, 'getMedicos').mockReturnValue({ id: null });
      jest.spyOn(medicosService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medicos: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: medicos }));
      saveSubject.complete();

      // THEN
      expect(medicosFormService.getMedicos).toHaveBeenCalled();
      expect(medicosService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMedicos>>();
      const medicos = { id: 123 };
      jest.spyOn(medicosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medicos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(medicosService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
