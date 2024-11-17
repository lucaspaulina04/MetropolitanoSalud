import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAgendaMedica } from '../agenda-medica.model';
import { AgendaMedicaService } from '../service/agenda-medica.service';

const agendaMedicaResolve = (route: ActivatedRouteSnapshot): Observable<null | IAgendaMedica> => {
  const id = route.params.id;
  if (id) {
    return inject(AgendaMedicaService)
      .find(id)
      .pipe(
        mergeMap((agendaMedica: HttpResponse<IAgendaMedica>) => {
          if (agendaMedica.body) {
            return of(agendaMedica.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default agendaMedicaResolve;
