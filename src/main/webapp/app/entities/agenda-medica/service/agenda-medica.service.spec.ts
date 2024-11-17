import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IAgendaMedica } from '../agenda-medica.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../agenda-medica.test-samples';

import { AgendaMedicaService } from './agenda-medica.service';

const requireRestSample: IAgendaMedica = {
  ...sampleWithRequiredData,
};

describe('AgendaMedica Service', () => {
  let service: AgendaMedicaService;
  let httpMock: HttpTestingController;
  let expectedResult: IAgendaMedica | IAgendaMedica[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AgendaMedicaService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a AgendaMedica', () => {
      const agendaMedica = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(agendaMedica).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AgendaMedica', () => {
      const agendaMedica = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(agendaMedica).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AgendaMedica', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AgendaMedica', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AgendaMedica', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAgendaMedicaToCollectionIfMissing', () => {
      it('should add a AgendaMedica to an empty array', () => {
        const agendaMedica: IAgendaMedica = sampleWithRequiredData;
        expectedResult = service.addAgendaMedicaToCollectionIfMissing([], agendaMedica);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(agendaMedica);
      });

      it('should not add a AgendaMedica to an array that contains it', () => {
        const agendaMedica: IAgendaMedica = sampleWithRequiredData;
        const agendaMedicaCollection: IAgendaMedica[] = [
          {
            ...agendaMedica,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAgendaMedicaToCollectionIfMissing(agendaMedicaCollection, agendaMedica);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AgendaMedica to an array that doesn't contain it", () => {
        const agendaMedica: IAgendaMedica = sampleWithRequiredData;
        const agendaMedicaCollection: IAgendaMedica[] = [sampleWithPartialData];
        expectedResult = service.addAgendaMedicaToCollectionIfMissing(agendaMedicaCollection, agendaMedica);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(agendaMedica);
      });

      it('should add only unique AgendaMedica to an array', () => {
        const agendaMedicaArray: IAgendaMedica[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const agendaMedicaCollection: IAgendaMedica[] = [sampleWithRequiredData];
        expectedResult = service.addAgendaMedicaToCollectionIfMissing(agendaMedicaCollection, ...agendaMedicaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const agendaMedica: IAgendaMedica = sampleWithRequiredData;
        const agendaMedica2: IAgendaMedica = sampleWithPartialData;
        expectedResult = service.addAgendaMedicaToCollectionIfMissing([], agendaMedica, agendaMedica2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(agendaMedica);
        expect(expectedResult).toContain(agendaMedica2);
      });

      it('should accept null and undefined values', () => {
        const agendaMedica: IAgendaMedica = sampleWithRequiredData;
        expectedResult = service.addAgendaMedicaToCollectionIfMissing([], null, agendaMedica, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(agendaMedica);
      });

      it('should return initial array if no AgendaMedica is added', () => {
        const agendaMedicaCollection: IAgendaMedica[] = [sampleWithRequiredData];
        expectedResult = service.addAgendaMedicaToCollectionIfMissing(agendaMedicaCollection, undefined, null);
        expect(expectedResult).toEqual(agendaMedicaCollection);
      });
    });

    describe('compareAgendaMedica', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAgendaMedica(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAgendaMedica(entity1, entity2);
        const compareResult2 = service.compareAgendaMedica(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAgendaMedica(entity1, entity2);
        const compareResult2 = service.compareAgendaMedica(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAgendaMedica(entity1, entity2);
        const compareResult2 = service.compareAgendaMedica(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
