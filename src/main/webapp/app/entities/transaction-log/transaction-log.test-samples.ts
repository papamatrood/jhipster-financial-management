import dayjs from 'dayjs/esm';

import { ITransactionLog, NewTransactionLog } from './transaction-log.model';

export const sampleWithRequiredData: ITransactionLog = {
  id: 4273,
  action: 'dring confondre',
  actionAt: dayjs('2024-07-12T23:26'),
};

export const sampleWithPartialData: ITransactionLog = {
  id: 2263,
  action: 'horrible collègue oups',
  actionAt: dayjs('2024-07-12T14:29'),
};

export const sampleWithFullData: ITransactionLog = {
  id: 8126,
  action: 'adepte pff',
  actionAt: dayjs('2024-07-13T03:36'),
};

export const sampleWithNewData: NewTransactionLog = {
  action: 'délectable concurrence égoïste',
  actionAt: dayjs('2024-07-13T01:07'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
