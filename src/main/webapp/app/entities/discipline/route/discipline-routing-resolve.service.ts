import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDiscipline, Discipline } from '../discipline.model';
import { DisciplineService } from '../service/discipline.service';

@Injectable({ providedIn: 'root' })
export class DisciplineRoutingResolveService implements Resolve<IDiscipline> {
  constructor(protected service: DisciplineService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDiscipline> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((discipline: HttpResponse<Discipline>) => {
          if (discipline.body) {
            return of(discipline.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Discipline());
  }
}
