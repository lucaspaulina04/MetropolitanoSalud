import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import AgendaMedicaResolve from './route/agenda-medica-routing-resolve.service';

const agendaMedicaRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/agenda-medica.component').then(m => m.AgendaMedicaComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/agenda-medica-detail.component').then(m => m.AgendaMedicaDetailComponent),
    resolve: {
      agendaMedica: AgendaMedicaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/agenda-medica-update.component').then(m => m.AgendaMedicaUpdateComponent),
    resolve: {
      agendaMedica: AgendaMedicaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/agenda-medica-update.component').then(m => m.AgendaMedicaUpdateComponent),
    resolve: {
      agendaMedica: AgendaMedicaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default agendaMedicaRoute;
