package flhan.de.financemanager.ui.main.expenses.overview

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import flhan.de.financemanager.base.InteractorStatus.Success
import flhan.de.financemanager.common.data.Expense
import flhan.de.financemanager.common.events.Create
import flhan.de.financemanager.common.events.Delete
import flhan.de.financemanager.common.events.Update
import flhan.de.financemanager.common.extensions.cleanUp
import flhan.de.financemanager.common.extensions.toOverviewItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class ExpenseOverviewViewModel(fetchExpensesInteractor: FetchExpensesInteractor) : ViewModel() {

    val listItems = MutableLiveData<List<ExpenseOverviewItem>>()

    private val expenses = mutableListOf<ExpenseOverviewItem>()
    private val disposables = CompositeDisposable()

    init {
        fetchExpensesInteractor.fetchAll()
                .filter { result -> result.status == Success }
                .doOnNext { event ->
                    when (event.result) {
                        is Create -> {
                            val createEvent = event.result as Create<Expense>
                            expenses.add(0, createEvent.obj.toOverviewItem())
                        }
                        is Update -> {
                            val updateEvent = event.result as Update<Expense>
                            val itemIndex = expenses.indexOfFirst { item -> item.id == updateEvent.obj.id }
                            expenses[itemIndex] = updateEvent.obj.toOverviewItem()
                        }
                        is Delete -> {
                            val deleteEvent = event.result as Delete<Expense>
                            val itemIndex = expenses.indexOfFirst { item -> item.id == deleteEvent.id }
                            expenses.removeAt(itemIndex)
                        }
                        else -> {
                            throw IllegalArgumentException("Result Type $event not supported.")
                        }
                    }
                }
                .subscribe { listItems.value = expenses }
                .addTo(disposables)
    }

    override fun onCleared() {
        disposables.cleanUp()
        super.onCleared()
    }
}
