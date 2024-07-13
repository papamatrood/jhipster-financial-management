import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TransactionTypeComponent } from './list/transaction-type.component';
import { TransactionTypeDetailComponent } from './detail/transaction-type-detail.component';
import { TransactionTypeUpdateComponent } from './update/transaction-type-update.component';
import TransactionTypeResolve from './route/transaction-type-routing-resolve.service';

const transactionTypeRoute: Routes = [
  {
    path: '',
    component: TransactionTypeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TransactionTypeDetailComponent,
    resolve: {
      transactionType: TransactionTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TransactionTypeUpdateComponent,
    resolve: {
      transactionType: TransactionTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TransactionTypeUpdateComponent,
    resolve: {
      transactionType: TransactionTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default transactionTypeRoute;
