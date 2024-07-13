import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITransactionLog, NewTransactionLog } from '../transaction-log.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITransactionLog for edit and NewTransactionLogFormGroupInput for create.
 */
type TransactionLogFormGroupInput = ITransactionLog | PartialWithRequiredKeyOf<NewTransactionLog>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITransactionLog | NewTransactionLog> = Omit<T, 'actionAt'> & {
  actionAt?: string | null;
};

type TransactionLogFormRawValue = FormValueOf<ITransactionLog>;

type NewTransactionLogFormRawValue = FormValueOf<NewTransactionLog>;

type TransactionLogFormDefaults = Pick<NewTransactionLog, 'id' | 'actionAt'>;

type TransactionLogFormGroupContent = {
  id: FormControl<TransactionLogFormRawValue['id'] | NewTransactionLog['id']>;
  action: FormControl<TransactionLogFormRawValue['action']>;
  actionAt: FormControl<TransactionLogFormRawValue['actionAt']>;
  transaction: FormControl<TransactionLogFormRawValue['transaction']>;
};

export type TransactionLogFormGroup = FormGroup<TransactionLogFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TransactionLogFormService {
  createTransactionLogFormGroup(transactionLog: TransactionLogFormGroupInput = { id: null }): TransactionLogFormGroup {
    const transactionLogRawValue = this.convertTransactionLogToTransactionLogRawValue({
      ...this.getFormDefaults(),
      ...transactionLog,
    });
    return new FormGroup<TransactionLogFormGroupContent>({
      id: new FormControl(
        { value: transactionLogRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      action: new FormControl(transactionLogRawValue.action, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      actionAt: new FormControl(transactionLogRawValue.actionAt, {
        validators: [Validators.required],
      }),
      transaction: new FormControl(transactionLogRawValue.transaction),
    });
  }

  getTransactionLog(form: TransactionLogFormGroup): ITransactionLog | NewTransactionLog {
    return this.convertTransactionLogRawValueToTransactionLog(
      form.getRawValue() as TransactionLogFormRawValue | NewTransactionLogFormRawValue,
    );
  }

  resetForm(form: TransactionLogFormGroup, transactionLog: TransactionLogFormGroupInput): void {
    const transactionLogRawValue = this.convertTransactionLogToTransactionLogRawValue({ ...this.getFormDefaults(), ...transactionLog });
    form.reset(
      {
        ...transactionLogRawValue,
        id: { value: transactionLogRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TransactionLogFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      actionAt: currentTime,
    };
  }

  private convertTransactionLogRawValueToTransactionLog(
    rawTransactionLog: TransactionLogFormRawValue | NewTransactionLogFormRawValue,
  ): ITransactionLog | NewTransactionLog {
    return {
      ...rawTransactionLog,
      actionAt: dayjs(rawTransactionLog.actionAt, DATE_TIME_FORMAT),
    };
  }

  private convertTransactionLogToTransactionLogRawValue(
    transactionLog: ITransactionLog | (Partial<NewTransactionLog> & TransactionLogFormDefaults),
  ): TransactionLogFormRawValue | PartialWithRequiredKeyOf<NewTransactionLogFormRawValue> {
    return {
      ...transactionLog,
      actionAt: transactionLog.actionAt ? transactionLog.actionAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
