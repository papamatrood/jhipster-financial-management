import dayjs from 'dayjs/esm';

import { ITransaction, NewTransaction } from './transaction.model';

export const sampleWithRequiredData: ITransaction = {
  id: 30059,
  amount: 5267.22,
  date: dayjs('2024-07-12T19:08'),
  createdAt: dayjs('2024-07-12T14:48'),
};

export const sampleWithPartialData: ITransaction = {
  id: 27622,
  amount: 2813.46,
  date: dayjs('2024-07-12T23:44'),
  createdAt: dayjs('2024-07-12T12:07'),
};

export const sampleWithFullData: ITransaction = {
  id: 30593,
  amount: 5862.27,
  description: 'sans de par',
  date: dayjs('2024-07-12T14:41'),
  createdAt: dayjs('2024-07-13T09:20'),
  updatedAt: dayjs('2024-07-13T06:14'),
};

export const sampleWithNewData: NewTransaction = {
  amount: 14716.43,
  date: dayjs('2024-07-13T05:50'),
  createdAt: dayjs('2024-07-12T22:52'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
