import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ClassRoomService } from '../service/class-room.service';
import { IClassRoom, ClassRoom } from '../class-room.model';

import { ClassRoomUpdateComponent } from './class-room-update.component';

describe('ClassRoom Management Update Component', () => {
  let comp: ClassRoomUpdateComponent;
  let fixture: ComponentFixture<ClassRoomUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let classRoomService: ClassRoomService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ClassRoomUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ClassRoomUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ClassRoomUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    classRoomService = TestBed.inject(ClassRoomService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const classRoom: IClassRoom = { id: 456 };

      activatedRoute.data = of({ classRoom });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(classRoom));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ClassRoom>>();
      const classRoom = { id: 123 };
      jest.spyOn(classRoomService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ classRoom });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: classRoom }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(classRoomService.update).toHaveBeenCalledWith(classRoom);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ClassRoom>>();
      const classRoom = new ClassRoom();
      jest.spyOn(classRoomService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ classRoom });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: classRoom }));
      saveSubject.complete();

      // THEN
      expect(classRoomService.create).toHaveBeenCalledWith(classRoom);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ClassRoom>>();
      const classRoom = { id: 123 };
      jest.spyOn(classRoomService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ classRoom });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(classRoomService.update).toHaveBeenCalledWith(classRoom);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
