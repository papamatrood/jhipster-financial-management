import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { ITransaction } from 'app/entities/transaction/transaction.model';
import { TransactionService } from 'app/entities/transaction/service/transaction.service';
import { TransactionLogService } from '../service/transaction-log.service';
import { ITransactionLog } from '../transaction-log.model';
import { TransactionLogFormService } from './transaction-log-form.service';

import { TransactionLogUpdateComponent } from './transaction-log-update.component';

describe('TransactionLog Management Update Component', () => {
  let comp: TransactionLogUpdateComponent;
  let fixture: ComponentFixture<TransactionLogUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let transactionLogFormService: TransactionLogFormService;
  let transactionLogService: TransactionLogService;
  let transactionService: TransactionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TransactionLogUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(TransactionLogUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TransactionLogUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    transactionLogFormService = TestBed.inject(TransactionLogFormService);
    transactionLogService = TestBed.inject(TransactionLogService);
    transactionService = TestBed.inject(TransactionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Transaction query and add missing value', () => {
      const transactionLog: ITransactionLog = { id: 456 };
      const transaction: ITransaction = { id: 17083 };
      transactionLog.transaction = transaction;

      const transactionCollection: ITransaction[] = [{ id: 21753 }];
      jest.spyOn(transactionService, 'query').mockReturnValue(of(new HttpResponse({ body: transactionCollection })));
      const additionalTransactions = [transaction];
      const expectedCollection: ITransaction[] = [...additionalTransactions, ...transactionCollection];
      jest.spyOn(transactionService, 'addTransactionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ transactionLog });
      comp.ngOnInit();

      expect(transactionService.query).toHaveBeenCalled();
      expect(transactionService.addTransactionToCollectionIfMissing).toHaveBeenCalledWith(
        transactionCollection,
        ...additionalTransactions.map(expect.objectContaining),
      );
      expect(comp.transactionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const transactionLog: ITransactionLog = { id: 456 };
      const transaction: ITransaction = { id: 31616 };
      transactionLog.transaction = transaction;

      activatedRoute.data = of({ transactionLog });
      comp.ngOnInit();

      expect(comp.transactionsSharedCollection).toContain(transaction);
      expect(comp.transactionLog).toEqual(transactionLog);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransactionLog>>();
      const transactionLog = { id: 123 };
      jest.spyOn(transactionLogFormService, 'getTransactionLog').mockReturnValue(transactionLog);
      jest.spyOn(transactionLogService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transactionLog });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: transactionLog }));
      saveSubject.complete();

      // THEN
      expect(transactionLogFormService.getTransactionLog).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(transactionLogService.update).toHaveBeenCalledWith(expect.objectContaining(transactionLog));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransactionLog>>();
      const transactionLog = { id: 123 };
      jest.spyOn(transactionLogFormService, 'getTransactionLog').mockReturnValue({ id: null });
      jest.spyOn(transactionLogService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transactionLog: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: transactionLog }));
      saveSubject.complete();

      // THEN
      expect(transactionLogFormService.getTransactionLog).toHaveBeenCalled();
      expect(transactionLogService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransactionLog>>();
      const transactionLog = { id: 123 };
      jest.spyOn(transactionLogService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transactionLog });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(transactionLogService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTransaction', () => {
      it('Should forward to transactionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(transactionService, 'compareTransaction');
        comp.compareTransaction(entity, entity2);
        expect(transactionService.compareTransaction).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
