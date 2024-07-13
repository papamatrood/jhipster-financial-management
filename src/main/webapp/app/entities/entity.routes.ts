import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'jhipsterFinancialManagementApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'transaction-type',
    data: { pageTitle: 'jhipsterFinancialManagementApp.transactionType.home.title' },
    loadChildren: () => import('./transaction-type/transaction-type.routes'),
  },
  {
    path: 'transaction',
    data: { pageTitle: 'jhipsterFinancialManagementApp.transaction.home.title' },
    loadChildren: () => import('./transaction/transaction.routes'),
  },
  {
    path: 'transaction-log',
    data: { pageTitle: 'jhipsterFinancialManagementApp.transactionLog.home.title' },
    loadChildren: () => import('./transaction-log/transaction-log.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
