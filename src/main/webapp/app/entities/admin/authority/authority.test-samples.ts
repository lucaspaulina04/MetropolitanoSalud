import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '6f5e0616-20ab-4cf5-a12c-ed5ced804a03',
};

export const sampleWithPartialData: IAuthority = {
  name: '21508364-5a69-447a-b57a-59f4e19ddc73',
};

export const sampleWithFullData: IAuthority = {
  name: 'c5635c09-2e6b-4362-a78b-dd93a4ee57cd',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
