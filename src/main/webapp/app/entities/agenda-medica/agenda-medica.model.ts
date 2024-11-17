import { IPaciente } from 'app/entities/paciente/paciente.model';
import { IMedicos } from 'app/entities/medicos/medicos.model';
import { ICentroSalud } from 'app/entities/centro-salud/centro-salud.model';

export interface IAgendaMedica {
  id: number;
  fechaHora?: string | null;
  pacienteID?: string | null;
  medicoID?: string | null;
  centroSaludID?: string | null;
  estadoHora?: string | null;
  pacienteHora?: IPaciente | null;
  horasMedicas?: IMedicos | null;
  horasCentroSalud?: ICentroSalud | null;
}

export type NewAgendaMedica = Omit<IAgendaMedica, 'id'> & { id: null };
