import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ITransactionLog } from '../transaction-log.model';

@Component({
  standalone: true,
  selector: 'jhi-transaction-log-detail',
  templateUrl: './transaction-log-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class TransactionLogDetailComponent {
  transactionLog = input<ITransactionLog | null>(null);

  previousState(): void {
    window.history.back();
  }
}
