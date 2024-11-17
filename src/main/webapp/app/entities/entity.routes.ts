import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'Authorities' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'paciente',
    data: { pageTitle: 'Pacientes' },
    loadChildren: () => import('./paciente/paciente.routes'),
  },
  {
    path: 'medicos',
    data: { pageTitle: 'Medicos' },
    loadChildren: () => import('./medicos/medicos.routes'),
  },
  {
    path: 'centro-salud',
    data: { pageTitle: 'CentroSaluds' },
    loadChildren: () => import('./centro-salud/centro-salud.routes'),
  },
  {
    path: 'agenda-medica',
    data: { pageTitle: 'AgendaMedicas' },
    loadChildren: () => import('./agenda-medica/agenda-medica.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
