import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IPurchaseOrder } from '../purchase-order.model';

@Component({
  selector: 'jhi-purchase-order-detail',
  templateUrl: './purchase-order-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class PurchaseOrderDetailComponent {
  purchaseOrder = input<IPurchaseOrder | null>(null);

  previousState(): void {
    window.history.back();
  }
}
