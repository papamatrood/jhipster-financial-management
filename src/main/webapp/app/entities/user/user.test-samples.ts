import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 14114,
  login: 'p&{&@yI\\t-\\GIFd',
};

export const sampleWithPartialData: IUser = {
  id: 17588,
  login: 'OR@p9i\\3UL3GgK',
};

export const sampleWithFullData: IUser = {
  id: 28139,
  login: 'h^*In@Ctwl\\uGpil7\\{zBw',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
