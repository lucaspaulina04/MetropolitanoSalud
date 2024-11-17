export interface IMedicos {
  id: number;
  nombre?: string | null;
  apellido?: string | null;
  especialidad?: string | null;
  email?: string | null;
  telefono?: string | null;
}

export type NewMedicos = Omit<IMedicos, 'id'> & { id: null };
