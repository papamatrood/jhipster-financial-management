import dayjs from 'dayjs/esm';
import { ITransaction } from 'app/entities/transaction/transaction.model';

export interface ITransactionLog {
  id: number;
  action?: string | null;
  actionAt?: dayjs.Dayjs | null;
  transaction?: ITransaction | null;
}

export type NewTransactionLog = Omit<ITransactionLog, 'id'> & { id: null };
