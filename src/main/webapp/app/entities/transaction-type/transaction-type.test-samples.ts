import { ITransactionType, NewTransactionType } from './transaction-type.model';

export const sampleWithRequiredData: ITransactionType = {
  id: 12574,
  name: 'à la merci smack',
};

export const sampleWithPartialData: ITransactionType = {
  id: 28205,
  name: 'biathlète',
};

export const sampleWithFullData: ITransactionType = {
  id: 23277,
  name: 'broum',
};

export const sampleWithNewData: NewTransactionType = {
  name: 'meuh',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
