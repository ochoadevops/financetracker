package flhan.de.financemanager.ui.main.expenses.overview

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import flhan.de.financemanager.base.InteractorStatus.Success
import flhan.de.financemanager.common.extensions.cleanUp
import flhan.de.financemanager.common.extensions.toOverviewItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class ExpenseOverviewViewModel(private val expenseOverviewInteractor: ExpenseOverviewInteractor) : ViewModel() {

    val listItems = MutableLiveData<List<ExpenseOverviewItem>>()
    val paymentSums = MutableLiveData<List<ExpensePaymentItem>>()

    private val disposables = CompositeDisposable()

    init {
        expenseOverviewInteractor.fetchAll()
                .filter { result -> result.status == Success }
                .map { expenses ->
                    expenses.result?.map { it.toOverviewItem() }?.reversed() ?: listOf()
                }
                .subscribe { listItems.value = it }
                .addTo(disposables)

        expenseOverviewInteractor
                .calculatePaymentItems()
                .subscribe { paymentSums.value = it }
    }

    override fun onCleared() {
        disposables.cleanUp()
        super.onCleared()
    }

    fun billAll() {
        expenseOverviewInteractor.billAll()
                .subscribe { println(it) }
    }
}

