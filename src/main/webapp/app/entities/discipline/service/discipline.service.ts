import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDiscipline, getDisciplineIdentifier } from '../discipline.model';

export type EntityResponseType = HttpResponse<IDiscipline>;
export type EntityArrayResponseType = HttpResponse<IDiscipline[]>;

@Injectable({ providedIn: 'root' })
export class DisciplineService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/disciplines');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(discipline: IDiscipline): Observable<EntityResponseType> {
    return this.http.post<IDiscipline>(this.resourceUrl, discipline, { observe: 'response' });
  }

  update(discipline: IDiscipline): Observable<EntityResponseType> {
    return this.http.put<IDiscipline>(`${this.resourceUrl}/${getDisciplineIdentifier(discipline) as number}`, discipline, {
      observe: 'response',
    });
  }

  partialUpdate(discipline: IDiscipline): Observable<EntityResponseType> {
    return this.http.patch<IDiscipline>(`${this.resourceUrl}/${getDisciplineIdentifier(discipline) as number}`, discipline, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDiscipline>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDiscipline[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDisciplineToCollectionIfMissing(
    disciplineCollection: IDiscipline[],
    ...disciplinesToCheck: (IDiscipline | null | undefined)[]
  ): IDiscipline[] {
    const disciplines: IDiscipline[] = disciplinesToCheck.filter(isPresent);
    if (disciplines.length > 0) {
      const disciplineCollectionIdentifiers = disciplineCollection.map(disciplineItem => getDisciplineIdentifier(disciplineItem)!);
      const disciplinesToAdd = disciplines.filter(disciplineItem => {
        const disciplineIdentifier = getDisciplineIdentifier(disciplineItem);
        if (disciplineIdentifier == null || disciplineCollectionIdentifiers.includes(disciplineIdentifier)) {
          return false;
        }
        disciplineCollectionIdentifiers.push(disciplineIdentifier);
        return true;
      });
      return [...disciplinesToAdd, ...disciplineCollection];
    }
    return disciplineCollection;
  }
}
