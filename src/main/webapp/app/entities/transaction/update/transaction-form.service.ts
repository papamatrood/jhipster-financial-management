import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITransaction, NewTransaction } from '../transaction.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITransaction for edit and NewTransactionFormGroupInput for create.
 */
type TransactionFormGroupInput = ITransaction | PartialWithRequiredKeyOf<NewTransaction>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITransaction | NewTransaction> = Omit<T, 'date' | 'createdAt' | 'updatedAt'> & {
  date?: string | null;
  createdAt?: string | null;
  updatedAt?: string | null;
};

type TransactionFormRawValue = FormValueOf<ITransaction>;

type NewTransactionFormRawValue = FormValueOf<NewTransaction>;

type TransactionFormDefaults = Pick<NewTransaction, 'id' | 'date' | 'createdAt' | 'updatedAt'>;

type TransactionFormGroupContent = {
  id: FormControl<TransactionFormRawValue['id'] | NewTransaction['id']>;
  amount: FormControl<TransactionFormRawValue['amount']>;
  description: FormControl<TransactionFormRawValue['description']>;
  date: FormControl<TransactionFormRawValue['date']>;
  createdAt: FormControl<TransactionFormRawValue['createdAt']>;
  updatedAt: FormControl<TransactionFormRawValue['updatedAt']>;
  transactionType: FormControl<TransactionFormRawValue['transactionType']>;
  user: FormControl<TransactionFormRawValue['user']>;
};

export type TransactionFormGroup = FormGroup<TransactionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TransactionFormService {
  createTransactionFormGroup(transaction: TransactionFormGroupInput = { id: null }): TransactionFormGroup {
    const transactionRawValue = this.convertTransactionToTransactionRawValue({
      ...this.getFormDefaults(),
      ...transaction,
    });
    return new FormGroup<TransactionFormGroupContent>({
      id: new FormControl(
        { value: transactionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      amount: new FormControl(transactionRawValue.amount, {
        validators: [Validators.required],
      }),
      description: new FormControl(transactionRawValue.description, {
        validators: [Validators.maxLength(255)],
      }),
      date: new FormControl(transactionRawValue.date, {
        validators: [Validators.required],
      }),
      createdAt: new FormControl(transactionRawValue.createdAt, {
        validators: [Validators.required],
      }),
      updatedAt: new FormControl(transactionRawValue.updatedAt),
      transactionType: new FormControl(transactionRawValue.transactionType),
      user: new FormControl(transactionRawValue.user),
    });
  }

  getTransaction(form: TransactionFormGroup): ITransaction | NewTransaction {
    return this.convertTransactionRawValueToTransaction(form.getRawValue() as TransactionFormRawValue | NewTransactionFormRawValue);
  }

  resetForm(form: TransactionFormGroup, transaction: TransactionFormGroupInput): void {
    const transactionRawValue = this.convertTransactionToTransactionRawValue({ ...this.getFormDefaults(), ...transaction });
    form.reset(
      {
        ...transactionRawValue,
        id: { value: transactionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TransactionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      date: currentTime,
      createdAt: currentTime,
      updatedAt: currentTime,
    };
  }

  private convertTransactionRawValueToTransaction(
    rawTransaction: TransactionFormRawValue | NewTransactionFormRawValue,
  ): ITransaction | NewTransaction {
    return {
      ...rawTransaction,
      date: dayjs(rawTransaction.date, DATE_TIME_FORMAT),
      createdAt: dayjs(rawTransaction.createdAt, DATE_TIME_FORMAT),
      updatedAt: dayjs(rawTransaction.updatedAt, DATE_TIME_FORMAT),
    };
  }

  private convertTransactionToTransactionRawValue(
    transaction: ITransaction | (Partial<NewTransaction> & TransactionFormDefaults),
  ): TransactionFormRawValue | PartialWithRequiredKeyOf<NewTransactionFormRawValue> {
    return {
      ...transaction,
      date: transaction.date ? transaction.date.format(DATE_TIME_FORMAT) : undefined,
      createdAt: transaction.createdAt ? transaction.createdAt.format(DATE_TIME_FORMAT) : undefined,
      updatedAt: transaction.updatedAt ? transaction.updatedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
