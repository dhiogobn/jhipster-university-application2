import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DisciplineDetailComponent } from './discipline-detail.component';

describe('Discipline Management Detail Component', () => {
  let comp: DisciplineDetailComponent;
  let fixture: ComponentFixture<DisciplineDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DisciplineDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ discipline: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DisciplineDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DisciplineDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load discipline on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.discipline).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
