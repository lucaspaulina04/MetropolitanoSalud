export interface IPaciente {
  id: number;
  nombre?: string | null;
  apellido?: string | null;
  fechanacimiento?: string | null;
  edad?: string | null;
  direccion?: string | null;
  email?: string | null;
  numero?: string | null;
}

export type NewPaciente = Omit<IPaciente, 'id'> & { id: null };
