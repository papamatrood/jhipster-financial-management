import dayjs from 'dayjs/esm';
import { ITransactionType } from 'app/entities/transaction-type/transaction-type.model';

export interface ITransaction {
  id: number;
  amount?: number | null;
  description?: string | null;
  date?: dayjs.Dayjs | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  transactionType?: ITransactionType | null;
}

export type NewTransaction = Omit<ITransaction, 'id'> & { id: null };
