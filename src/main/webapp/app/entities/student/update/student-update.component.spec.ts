import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { StudentService } from '../service/student.service';
import { IStudent, Student } from '../student.model';
import { IDiscipline } from 'app/entities/discipline/discipline.model';
import { DisciplineService } from 'app/entities/discipline/service/discipline.service';
import { ITeacher } from 'app/entities/teacher/teacher.model';
import { TeacherService } from 'app/entities/teacher/service/teacher.service';
import { IClassRoom } from 'app/entities/class-room/class-room.model';
import { ClassRoomService } from 'app/entities/class-room/service/class-room.service';
import { ICourse } from 'app/entities/course/course.model';
import { CourseService } from 'app/entities/course/service/course.service';

import { StudentUpdateComponent } from './student-update.component';

describe('Student Management Update Component', () => {
  let comp: StudentUpdateComponent;
  let fixture: ComponentFixture<StudentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let studentService: StudentService;
  let disciplineService: DisciplineService;
  let teacherService: TeacherService;
  let classRoomService: ClassRoomService;
  let courseService: CourseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [StudentUpdateComponent],
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
      .overrideTemplate(StudentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(StudentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    studentService = TestBed.inject(StudentService);
    disciplineService = TestBed.inject(DisciplineService);
    teacherService = TestBed.inject(TeacherService);
    classRoomService = TestBed.inject(ClassRoomService);
    courseService = TestBed.inject(CourseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Discipline query and add missing value', () => {
      const student: IStudent = { id: 456 };
      const disciplines: IDiscipline[] = [{ id: 30249 }];
      student.disciplines = disciplines;

      const disciplineCollection: IDiscipline[] = [{ id: 65599 }];
      jest.spyOn(disciplineService, 'query').mockReturnValue(of(new HttpResponse({ body: disciplineCollection })));
      const additionalDisciplines = [...disciplines];
      const expectedCollection: IDiscipline[] = [...additionalDisciplines, ...disciplineCollection];
      jest.spyOn(disciplineService, 'addDisciplineToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ student });
      comp.ngOnInit();

      expect(disciplineService.query).toHaveBeenCalled();
      expect(disciplineService.addDisciplineToCollectionIfMissing).toHaveBeenCalledWith(disciplineCollection, ...additionalDisciplines);
      expect(comp.disciplinesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Teacher query and add missing value', () => {
      const student: IStudent = { id: 456 };
      const teachers: ITeacher[] = [{ id: 11571 }];
      student.teachers = teachers;

      const teacherCollection: ITeacher[] = [{ id: 22083 }];
      jest.spyOn(teacherService, 'query').mockReturnValue(of(new HttpResponse({ body: teacherCollection })));
      const additionalTeachers = [...teachers];
      const expectedCollection: ITeacher[] = [...additionalTeachers, ...teacherCollection];
      jest.spyOn(teacherService, 'addTeacherToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ student });
      comp.ngOnInit();

      expect(teacherService.query).toHaveBeenCalled();
      expect(teacherService.addTeacherToCollectionIfMissing).toHaveBeenCalledWith(teacherCollection, ...additionalTeachers);
      expect(comp.teachersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ClassRoom query and add missing value', () => {
      const student: IStudent = { id: 456 };
      const classRoom: IClassRoom = { id: 32437 };
      student.classRoom = classRoom;

      const classRoomCollection: IClassRoom[] = [{ id: 76854 }];
      jest.spyOn(classRoomService, 'query').mockReturnValue(of(new HttpResponse({ body: classRoomCollection })));
      const additionalClassRooms = [classRoom];
      const expectedCollection: IClassRoom[] = [...additionalClassRooms, ...classRoomCollection];
      jest.spyOn(classRoomService, 'addClassRoomToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ student });
      comp.ngOnInit();

      expect(classRoomService.query).toHaveBeenCalled();
      expect(classRoomService.addClassRoomToCollectionIfMissing).toHaveBeenCalledWith(classRoomCollection, ...additionalClassRooms);
      expect(comp.classRoomsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Course query and add missing value', () => {
      const student: IStudent = { id: 456 };
      const course: ICourse = { id: 41397 };
      student.course = course;

      const courseCollection: ICourse[] = [{ id: 92109 }];
      jest.spyOn(courseService, 'query').mockReturnValue(of(new HttpResponse({ body: courseCollection })));
      const additionalCourses = [course];
      const expectedCollection: ICourse[] = [...additionalCourses, ...courseCollection];
      jest.spyOn(courseService, 'addCourseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ student });
      comp.ngOnInit();

      expect(courseService.query).toHaveBeenCalled();
      expect(courseService.addCourseToCollectionIfMissing).toHaveBeenCalledWith(courseCollection, ...additionalCourses);
      expect(comp.coursesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const student: IStudent = { id: 456 };
      const disciplines: IDiscipline = { id: 80864 };
      student.disciplines = [disciplines];
      const teachers: ITeacher = { id: 62883 };
      student.teachers = [teachers];
      const classRoom: IClassRoom = { id: 29729 };
      student.classRoom = classRoom;
      const course: ICourse = { id: 40043 };
      student.course = course;

      activatedRoute.data = of({ student });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(student));
      expect(comp.disciplinesSharedCollection).toContain(disciplines);
      expect(comp.teachersSharedCollection).toContain(teachers);
      expect(comp.classRoomsSharedCollection).toContain(classRoom);
      expect(comp.coursesSharedCollection).toContain(course);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Student>>();
      const student = { id: 123 };
      jest.spyOn(studentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ student });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: student }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(studentService.update).toHaveBeenCalledWith(student);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Student>>();
      const student = new Student();
      jest.spyOn(studentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ student });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: student }));
      saveSubject.complete();

      // THEN
      expect(studentService.create).toHaveBeenCalledWith(student);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Student>>();
      const student = { id: 123 };
      jest.spyOn(studentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ student });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(studentService.update).toHaveBeenCalledWith(student);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackDisciplineById', () => {
      it('Should return tracked Discipline primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDisciplineById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackTeacherById', () => {
      it('Should return tracked Teacher primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTeacherById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackClassRoomById', () => {
      it('Should return tracked ClassRoom primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackClassRoomById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackCourseById', () => {
      it('Should return tracked Course primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCourseById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedDiscipline', () => {
      it('Should return option if no Discipline is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedDiscipline(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Discipline for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedDiscipline(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Discipline is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedDiscipline(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });

    describe('getSelectedTeacher', () => {
      it('Should return option if no Teacher is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedTeacher(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Teacher for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedTeacher(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Teacher is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedTeacher(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
