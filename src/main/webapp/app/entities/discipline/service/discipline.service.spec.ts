import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDiscipline, Discipline } from '../discipline.model';

import { DisciplineService } from './discipline.service';

describe('Discipline Service', () => {
  let service: DisciplineService;
  let httpMock: HttpTestingController;
  let elemDefault: IDiscipline;
  let expectedResult: IDiscipline | IDiscipline[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DisciplineService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      credits: 'AAAAAAA',
      phone: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Discipline', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Discipline()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Discipline', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          credits: 'BBBBBB',
          phone: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Discipline', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
        },
        new Discipline()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Discipline', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          credits: 'BBBBBB',
          phone: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Discipline', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDisciplineToCollectionIfMissing', () => {
      it('should add a Discipline to an empty array', () => {
        const discipline: IDiscipline = { id: 123 };
        expectedResult = service.addDisciplineToCollectionIfMissing([], discipline);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(discipline);
      });

      it('should not add a Discipline to an array that contains it', () => {
        const discipline: IDiscipline = { id: 123 };
        const disciplineCollection: IDiscipline[] = [
          {
            ...discipline,
          },
          { id: 456 },
        ];
        expectedResult = service.addDisciplineToCollectionIfMissing(disciplineCollection, discipline);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Discipline to an array that doesn't contain it", () => {
        const discipline: IDiscipline = { id: 123 };
        const disciplineCollection: IDiscipline[] = [{ id: 456 }];
        expectedResult = service.addDisciplineToCollectionIfMissing(disciplineCollection, discipline);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(discipline);
      });

      it('should add only unique Discipline to an array', () => {
        const disciplineArray: IDiscipline[] = [{ id: 123 }, { id: 456 }, { id: 28070 }];
        const disciplineCollection: IDiscipline[] = [{ id: 123 }];
        expectedResult = service.addDisciplineToCollectionIfMissing(disciplineCollection, ...disciplineArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const discipline: IDiscipline = { id: 123 };
        const discipline2: IDiscipline = { id: 456 };
        expectedResult = service.addDisciplineToCollectionIfMissing([], discipline, discipline2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(discipline);
        expect(expectedResult).toContain(discipline2);
      });

      it('should accept null and undefined values', () => {
        const discipline: IDiscipline = { id: 123 };
        expectedResult = service.addDisciplineToCollectionIfMissing([], null, discipline, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(discipline);
      });

      it('should return initial array if no Discipline is added', () => {
        const disciplineCollection: IDiscipline[] = [{ id: 123 }];
        expectedResult = service.addDisciplineToCollectionIfMissing(disciplineCollection, undefined, null);
        expect(expectedResult).toEqual(disciplineCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
