import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'student',
        data: { pageTitle: 'myApp.student.home.title' },
        loadChildren: () => import('./student/student.module').then(m => m.StudentModule),
      },
      {
        path: 'discipline',
        data: { pageTitle: 'myApp.discipline.home.title' },
        loadChildren: () => import('./discipline/discipline.module').then(m => m.DisciplineModule),
      },
      {
        path: 'teacher',
        data: { pageTitle: 'myApp.teacher.home.title' },
        loadChildren: () => import('./teacher/teacher.module').then(m => m.TeacherModule),
      },
      {
        path: 'class-room',
        data: { pageTitle: 'myApp.classRoom.home.title' },
        loadChildren: () => import('./class-room/class-room.module').then(m => m.ClassRoomModule),
      },
      {
        path: 'course',
        data: { pageTitle: 'myApp.course.home.title' },
        loadChildren: () => import('./course/course.module').then(m => m.CourseModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
