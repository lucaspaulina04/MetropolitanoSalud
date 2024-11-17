import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMedicos } from '../medicos.model';
import { MedicosService } from '../service/medicos.service';

const medicosResolve = (route: ActivatedRouteSnapshot): Observable<null | IMedicos> => {
  const id = route.params.id;
  if (id) {
    return inject(MedicosService)
      .find(id)
      .pipe(
        mergeMap((medicos: HttpResponse<IMedicos>) => {
          if (medicos.body) {
            return of(medicos.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default medicosResolve;
