import { ICentroSalud, NewCentroSalud } from './centro-salud.model';

export const sampleWithRequiredData: ICentroSalud = {
  id: 17225,
};

export const sampleWithPartialData: ICentroSalud = {
  id: 2628,
  nombre: 'traditionalism under how',
};

export const sampleWithFullData: ICentroSalud = {
  id: 27603,
  nombre: 'furthermore subdued',
  direccion: 'burgeon before mysteriously',
  telefono: 'uh-huh up lieu',
  tipo: 'um',
};

export const sampleWithNewData: NewCentroSalud = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
