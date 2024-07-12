import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '2d2b7e70-2841-4abb-b957-90e77bdde8fc',
};

export const sampleWithPartialData: IAuthority = {
  name: '5162ed9b-f2a5-4ef1-9349-9d6d51732cff',
};

export const sampleWithFullData: IAuthority = {
  name: 'd8b2ea56-9aa7-4711-897c-1cdd9b7bad10',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
