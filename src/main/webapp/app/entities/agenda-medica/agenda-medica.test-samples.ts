import { IAgendaMedica, NewAgendaMedica } from './agenda-medica.model';

export const sampleWithRequiredData: IAgendaMedica = {
  id: 32748,
};

export const sampleWithPartialData: IAgendaMedica = {
  id: 19382,
  fechaHora: 'blindly blah capitalize',
};

export const sampleWithFullData: IAgendaMedica = {
  id: 24027,
  fechaHora: 'orientate reborn to',
  pacienteID: 'consequently confiscate',
  medicoID: 'against',
  centroSaludID: 'secondary unique',
  estadoHora: 'outlandish out why',
};

export const sampleWithNewData: NewAgendaMedica = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
