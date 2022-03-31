import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DisciplineService } from '../service/discipline.service';

import { DisciplineComponent } from './discipline.component';

describe('Discipline Management Component', () => {
  let comp: DisciplineComponent;
  let fixture: ComponentFixture<DisciplineComponent>;
  let service: DisciplineService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DisciplineComponent],
    })
      .overrideTemplate(DisciplineComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DisciplineComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DisciplineService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.disciplines?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
