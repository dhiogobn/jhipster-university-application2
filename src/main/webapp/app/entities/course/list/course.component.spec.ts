import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CourseService } from '../service/course.service';

import { CourseComponent } from './course.component';

describe('Course Management Component', () => {
  let comp: CourseComponent;
  let fixture: ComponentFixture<CourseComponent>;
  let service: CourseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CourseComponent],
    })
      .overrideTemplate(CourseComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CourseComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CourseService);

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
    expect(comp.courses?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
