export interface ITransactionType {
  id: number;
  name?: string | null;
}

export type NewTransactionType = Omit<ITransactionType, 'id'> & { id: null };
