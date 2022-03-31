import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DisciplineComponent } from '../list/discipline.component';
import { DisciplineDetailComponent } from '../detail/discipline-detail.component';
import { DisciplineUpdateComponent } from '../update/discipline-update.component';
import { DisciplineRoutingResolveService } from './discipline-routing-resolve.service';

const disciplineRoute: Routes = [
  {
    path: '',
    component: DisciplineComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DisciplineDetailComponent,
    resolve: {
      discipline: DisciplineRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DisciplineUpdateComponent,
    resolve: {
      discipline: DisciplineRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DisciplineUpdateComponent,
    resolve: {
      discipline: DisciplineRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(disciplineRoute)],
  exports: [RouterModule],
})
export class DisciplineRoutingModule {}
