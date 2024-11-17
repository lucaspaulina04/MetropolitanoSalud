import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import MedicosResolve from './route/medicos-routing-resolve.service';

const medicosRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/medicos.component').then(m => m.MedicosComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/medicos-detail.component').then(m => m.MedicosDetailComponent),
    resolve: {
      medicos: MedicosResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/medicos-update.component').then(m => m.MedicosUpdateComponent),
    resolve: {
      medicos: MedicosResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/medicos-update.component').then(m => m.MedicosUpdateComponent),
    resolve: {
      medicos: MedicosResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default medicosRoute;
