import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IMedicos } from '../medicos.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../medicos.test-samples';

import { MedicosService } from './medicos.service';

const requireRestSample: IMedicos = {
  ...sampleWithRequiredData,
};

describe('Medicos Service', () => {
  let service: MedicosService;
  let httpMock: HttpTestingController;
  let expectedResult: IMedicos | IMedicos[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MedicosService);
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

    it('should create a Medicos', () => {
      const medicos = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(medicos).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Medicos', () => {
      const medicos = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(medicos).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Medicos', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Medicos', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Medicos', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMedicosToCollectionIfMissing', () => {
      it('should add a Medicos to an empty array', () => {
        const medicos: IMedicos = sampleWithRequiredData;
        expectedResult = service.addMedicosToCollectionIfMissing([], medicos);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(medicos);
      });

      it('should not add a Medicos to an array that contains it', () => {
        const medicos: IMedicos = sampleWithRequiredData;
        const medicosCollection: IMedicos[] = [
          {
            ...medicos,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMedicosToCollectionIfMissing(medicosCollection, medicos);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Medicos to an array that doesn't contain it", () => {
        const medicos: IMedicos = sampleWithRequiredData;
        const medicosCollection: IMedicos[] = [sampleWithPartialData];
        expectedResult = service.addMedicosToCollectionIfMissing(medicosCollection, medicos);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(medicos);
      });

      it('should add only unique Medicos to an array', () => {
        const medicosArray: IMedicos[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const medicosCollection: IMedicos[] = [sampleWithRequiredData];
        expectedResult = service.addMedicosToCollectionIfMissing(medicosCollection, ...medicosArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const medicos: IMedicos = sampleWithRequiredData;
        const medicos2: IMedicos = sampleWithPartialData;
        expectedResult = service.addMedicosToCollectionIfMissing([], medicos, medicos2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(medicos);
        expect(expectedResult).toContain(medicos2);
      });

      it('should accept null and undefined values', () => {
        const medicos: IMedicos = sampleWithRequiredData;
        expectedResult = service.addMedicosToCollectionIfMissing([], null, medicos, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(medicos);
      });

      it('should return initial array if no Medicos is added', () => {
        const medicosCollection: IMedicos[] = [sampleWithRequiredData];
        expectedResult = service.addMedicosToCollectionIfMissing(medicosCollection, undefined, null);
        expect(expectedResult).toEqual(medicosCollection);
      });
    });

    describe('compareMedicos', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMedicos(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMedicos(entity1, entity2);
        const compareResult2 = service.compareMedicos(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMedicos(entity1, entity2);
        const compareResult2 = service.compareMedicos(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMedicos(entity1, entity2);
        const compareResult2 = service.compareMedicos(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
