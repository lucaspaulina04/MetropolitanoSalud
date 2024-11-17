import { IMedicos, NewMedicos } from './medicos.model';

export const sampleWithRequiredData: IMedicos = {
  id: 19661,
};

export const sampleWithPartialData: IMedicos = {
  id: 3162,
  apellido: 'pile keenly meadow',
  especialidad: 'following',
  email: 'MariaLuisa.PereaPonce20@hotmail.com',
  telefono: 'trusty doubtfully hastily',
};

export const sampleWithFullData: IMedicos = {
  id: 22911,
  nombre: 'where wearily so',
  apellido: 'needy',
  especialidad: 'spellcheck rich',
  email: 'Manuel.DelacruzDelacruz@yahoo.com',
  telefono: 'eek napkin follower',
};

export const sampleWithNewData: NewMedicos = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
