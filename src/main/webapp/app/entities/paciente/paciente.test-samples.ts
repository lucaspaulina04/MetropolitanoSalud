import { IPaciente, NewPaciente } from './paciente.model';

export const sampleWithRequiredData: IPaciente = {
  id: 24410,
};

export const sampleWithPartialData: IPaciente = {
  id: 14453,
  direccion: 'omelet um um',
  numero: 'interestingly',
};

export const sampleWithFullData: IPaciente = {
  id: 3349,
  nombre: 'yahoo',
  apellido: 'an',
  fechanacimiento: 'mmm',
  edad: 'concerned mythology newsstand',
  direccion: 'amidst overcook aged',
  email: 'Raquel.LebronPina99@yahoo.com',
  numero: 'gazebo',
};

export const sampleWithNewData: NewPaciente = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
