import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { AgendaMedicaDetailComponent } from './agenda-medica-detail.component';

describe('AgendaMedica Management Detail Component', () => {
  let comp: AgendaMedicaDetailComponent;
  let fixture: ComponentFixture<AgendaMedicaDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AgendaMedicaDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./agenda-medica-detail.component').then(m => m.AgendaMedicaDetailComponent),
              resolve: { agendaMedica: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AgendaMedicaDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AgendaMedicaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load agendaMedica on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AgendaMedicaDetailComponent);

      // THEN
      expect(instance.agendaMedica()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
