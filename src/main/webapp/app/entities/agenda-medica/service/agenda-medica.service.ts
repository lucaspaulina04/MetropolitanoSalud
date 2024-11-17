import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAgendaMedica, NewAgendaMedica } from '../agenda-medica.model';

export type PartialUpdateAgendaMedica = Partial<IAgendaMedica> & Pick<IAgendaMedica, 'id'>;

export type EntityResponseType = HttpResponse<IAgendaMedica>;
export type EntityArrayResponseType = HttpResponse<IAgendaMedica[]>;

@Injectable({ providedIn: 'root' })
export class AgendaMedicaService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/agenda-medicas');

  create(agendaMedica: NewAgendaMedica): Observable<EntityResponseType> {
    return this.http.post<IAgendaMedica>(this.resourceUrl, agendaMedica, { observe: 'response' });
  }

  update(agendaMedica: IAgendaMedica): Observable<EntityResponseType> {
    return this.http.put<IAgendaMedica>(`${this.resourceUrl}/${this.getAgendaMedicaIdentifier(agendaMedica)}`, agendaMedica, {
      observe: 'response',
    });
  }

  partialUpdate(agendaMedica: PartialUpdateAgendaMedica): Observable<EntityResponseType> {
    return this.http.patch<IAgendaMedica>(`${this.resourceUrl}/${this.getAgendaMedicaIdentifier(agendaMedica)}`, agendaMedica, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAgendaMedica>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAgendaMedica[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAgendaMedicaIdentifier(agendaMedica: Pick<IAgendaMedica, 'id'>): number {
    return agendaMedica.id;
  }

  compareAgendaMedica(o1: Pick<IAgendaMedica, 'id'> | null, o2: Pick<IAgendaMedica, 'id'> | null): boolean {
    return o1 && o2 ? this.getAgendaMedicaIdentifier(o1) === this.getAgendaMedicaIdentifier(o2) : o1 === o2;
  }

  addAgendaMedicaToCollectionIfMissing<Type extends Pick<IAgendaMedica, 'id'>>(
    agendaMedicaCollection: Type[],
    ...agendaMedicasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const agendaMedicas: Type[] = agendaMedicasToCheck.filter(isPresent);
    if (agendaMedicas.length > 0) {
      const agendaMedicaCollectionIdentifiers = agendaMedicaCollection.map(agendaMedicaItem =>
        this.getAgendaMedicaIdentifier(agendaMedicaItem),
      );
      const agendaMedicasToAdd = agendaMedicas.filter(agendaMedicaItem => {
        const agendaMedicaIdentifier = this.getAgendaMedicaIdentifier(agendaMedicaItem);
        if (agendaMedicaCollectionIdentifiers.includes(agendaMedicaIdentifier)) {
          return false;
        }
        agendaMedicaCollectionIdentifiers.push(agendaMedicaIdentifier);
        return true;
      });
      return [...agendaMedicasToAdd, ...agendaMedicaCollection];
    }
    return agendaMedicaCollection;
  }
}
