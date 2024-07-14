import dayjs from 'dayjs/esm';
import { ITransactionType } from 'app/entities/transaction-type/transaction-type.model';
import { IUser } from 'app/entities/user/user.model';

export interface ITransaction {
  id: number;
  amount?: number | null;
  description?: string | null;
  date?: dayjs.Dayjs | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  transactionType?: ITransactionType | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewTransaction = Omit<ITransaction, 'id'> & { id: null };
