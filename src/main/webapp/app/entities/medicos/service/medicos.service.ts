import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMedicos, NewMedicos } from '../medicos.model';

export type PartialUpdateMedicos = Partial<IMedicos> & Pick<IMedicos, 'id'>;

export type EntityResponseType = HttpResponse<IMedicos>;
export type EntityArrayResponseType = HttpResponse<IMedicos[]>;

@Injectable({ providedIn: 'root' })
export class MedicosService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/medicos');

  create(medicos: NewMedicos): Observable<EntityResponseType> {
    return this.http.post<IMedicos>(this.resourceUrl, medicos, { observe: 'response' });
  }

  update(medicos: IMedicos): Observable<EntityResponseType> {
    return this.http.put<IMedicos>(`${this.resourceUrl}/${this.getMedicosIdentifier(medicos)}`, medicos, { observe: 'response' });
  }

  partialUpdate(medicos: PartialUpdateMedicos): Observable<EntityResponseType> {
    return this.http.patch<IMedicos>(`${this.resourceUrl}/${this.getMedicosIdentifier(medicos)}`, medicos, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMedicos>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMedicos[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMedicosIdentifier(medicos: Pick<IMedicos, 'id'>): number {
    return medicos.id;
  }

  compareMedicos(o1: Pick<IMedicos, 'id'> | null, o2: Pick<IMedicos, 'id'> | null): boolean {
    return o1 && o2 ? this.getMedicosIdentifier(o1) === this.getMedicosIdentifier(o2) : o1 === o2;
  }

  addMedicosToCollectionIfMissing<Type extends Pick<IMedicos, 'id'>>(
    medicosCollection: Type[],
    ...medicosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const medicos: Type[] = medicosToCheck.filter(isPresent);
    if (medicos.length > 0) {
      const medicosCollectionIdentifiers = medicosCollection.map(medicosItem => this.getMedicosIdentifier(medicosItem));
      const medicosToAdd = medicos.filter(medicosItem => {
        const medicosIdentifier = this.getMedicosIdentifier(medicosItem);
        if (medicosCollectionIdentifiers.includes(medicosIdentifier)) {
          return false;
        }
        medicosCollectionIdentifiers.push(medicosIdentifier);
        return true;
      });
      return [...medicosToAdd, ...medicosCollection];
    }
    return medicosCollection;
  }
}
