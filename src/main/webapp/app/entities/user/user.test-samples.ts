import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 14041,
  login: 'Zt',
};

export const sampleWithPartialData: IUser = {
  id: 26244,
  login: 'M2',
};

export const sampleWithFullData: IUser = {
  id: 2605,
  login: 'p{6@nz0',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
