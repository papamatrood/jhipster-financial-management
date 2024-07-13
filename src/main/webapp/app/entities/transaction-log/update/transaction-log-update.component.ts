import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITransaction } from 'app/entities/transaction/transaction.model';
import { TransactionService } from 'app/entities/transaction/service/transaction.service';
import { ITransactionLog } from '../transaction-log.model';
import { TransactionLogService } from '../service/transaction-log.service';
import { TransactionLogFormService, TransactionLogFormGroup } from './transaction-log-form.service';

@Component({
  standalone: true,
  selector: 'jhi-transaction-log-update',
  templateUrl: './transaction-log-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TransactionLogUpdateComponent implements OnInit {
  isSaving = false;
  transactionLog: ITransactionLog | null = null;

  transactionsSharedCollection: ITransaction[] = [];

  protected transactionLogService = inject(TransactionLogService);
  protected transactionLogFormService = inject(TransactionLogFormService);
  protected transactionService = inject(TransactionService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TransactionLogFormGroup = this.transactionLogFormService.createTransactionLogFormGroup();

  compareTransaction = (o1: ITransaction | null, o2: ITransaction | null): boolean => this.transactionService.compareTransaction(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transactionLog }) => {
      this.transactionLog = transactionLog;
      if (transactionLog) {
        this.updateForm(transactionLog);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const transactionLog = this.transactionLogFormService.getTransactionLog(this.editForm);
    if (transactionLog.id !== null) {
      this.subscribeToSaveResponse(this.transactionLogService.update(transactionLog));
    } else {
      this.subscribeToSaveResponse(this.transactionLogService.create(transactionLog));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITransactionLog>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(transactionLog: ITransactionLog): void {
    this.transactionLog = transactionLog;
    this.transactionLogFormService.resetForm(this.editForm, transactionLog);

    this.transactionsSharedCollection = this.transactionService.addTransactionToCollectionIfMissing<ITransaction>(
      this.transactionsSharedCollection,
      transactionLog.transaction,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.transactionService
      .query()
      .pipe(map((res: HttpResponse<ITransaction[]>) => res.body ?? []))
      .pipe(
        map((transactions: ITransaction[]) =>
          this.transactionService.addTransactionToCollectionIfMissing<ITransaction>(transactions, this.transactionLog?.transaction),
        ),
      )
      .subscribe((transactions: ITransaction[]) => (this.transactionsSharedCollection = transactions));
  }
}
