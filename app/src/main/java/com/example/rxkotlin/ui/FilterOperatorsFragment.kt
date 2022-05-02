package com.example.rxkotlin.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rxkotlin.R
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_filter_operators.*

@AndroidEntryPoint
class FilterOperatorsFragment : Fragment() {

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_filter_operators, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding()
    }

    private fun viewBinding(){
        btnFilter.setOnClickListener{
            onHandleFilter()
        }

        btnDistinct.setOnClickListener{
            onHandleDistinct()
        }

        btnTake.setOnClickListener{
            onHandleTake()
        }

        btnTakeWhile.setOnClickListener{
            onHandleTakeWhile()
        }
    }

    //Filter- even numbers
    private fun onHandleFilter() {
        tvOutput.text = ""
        val list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        tvOperationType.text = getString(R.string.filter)
        tvInput.text = list.toString()
        val disposable = io.reactivex.rxjava3.core.Observable.fromIterable(list)
            .filter { t -> t.rem(2) == 0 }
            .subscribe({ value -> tvOutput.append("$value ") },
                { error -> tvOutput.text = "${error.message}" })
        compositeDisposable.add(disposable)
    }

    //Distinct
    private fun onHandleDistinct() {
        tvOutput.text = ""
        val list = listOf(1, 2, 2, 5, 5, 6, 7, 7, 9, 9)
        tvOperationType.text = getString(R.string.distinct)
        tvInput.text = list.toString()
        val disposable = io.reactivex.rxjava3.core.Observable.fromIterable(list)
            .distinct()
            .subscribe({ value -> tvOutput.append("$value ") },
                { error -> tvOutput.text = "${error.message}" })
        compositeDisposable.add(disposable)
    }

    //Take - 4 items from given list
    private fun onHandleTake() {
        tvOutput.text = ""
        val list = listOf(1, 2, 2, 5, 5, 6, 7, 7, 9, 9)
        tvOperationType.text = getString(R.string.take)
        tvInput.text = list.toString()
        val disposable = io.reactivex.rxjava3.core.Observable.fromIterable(list)
            .take(4)
            .subscribe({ value -> tvOutput.append("$value ") },
                { error -> tvOutput.text = "${error.message}" })
        compositeDisposable.add(disposable)
    }

    //TakeWhile - until odd number occurs
    private fun onHandleTakeWhile() {
        tvOutput.text = ""
        val list = listOf(2, 2, 2, 6, 5, 6, 7, 7, 9, 9)
        tvOperationType.text = getString(R.string.take_while)
        tvInput.text = list.toString()
        val disposable = io.reactivex.rxjava3.core.Observable.fromIterable(list)
            .takeWhile { t -> t.rem(2) == 0 }
            .subscribe({ value -> tvOutput.append("$value ") },
                { error -> tvOutput.text = "${error.message}" })
        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}
