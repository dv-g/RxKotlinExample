package com.example.rxkotlin.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rxkotlin.R
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_transformation_operators.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class TransformationOperatorsFragment : Fragment() {

    private val compositeDisposable = CompositeDisposable()
    private val throttleFirstObservable = PublishSubject.create<Int>()
    private val debounceObservable = PublishSubject.create<Int>()
    private var debounceInput = 0
    private var throttleInput = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_transformation_operators, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding()
        setupDebounce()
        setupThrottleFirst()
    }

    private fun viewBinding() {
        btnMap.setOnClickListener {
            onHandleMap()
        }
        btnBuffer.setOnClickListener {
            onHandleBuffer()
        }
        btnDebounce.setOnClickListener {
            onHandleDebounce()
        }
        btnThrottleFirst.setOnClickListener {
            onHandleThrottleFirst()
        }
        btnFlatMap.setOnClickListener {
            onHandleFlatMap()
        }
        btnConcatMap.setOnClickListener {
            onHandleConcatMap()
        }
        btnSwitchMap.setOnClickListener {
            onHandleSwitchMap()
        }
    }

    //Map
    private fun onHandleMap() {
        tvOutput.text = ""
        val list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        tvOperationType.text = getString(R.string.map)
        tvInput.text = list.toString()
        val disposable = Observable.fromIterable(list)
            .map { item -> item * item }
            .subscribe({ value -> tvOutput.append("$value ") },
                { error -> tvOutput.text = "${error.message}" })
        compositeDisposable.add(disposable)
    }

    //Buffer - 3 items at a time
    private fun onHandleBuffer() {
        tvOutput.text = ""
        val list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        tvOperationType.text = getString(R.string.buffer)
        tvInput.text = list.toString()
        val disposable = Observable.fromIterable(list)
            .buffer(3)
            .subscribe({ value -> tvOutput.append("$value ") },
                { error -> tvOutput.text = "${error.message}" })
        compositeDisposable.add(disposable)
    }

    //Debounce
    private fun onHandleDebounce() {
        tvOperationType.text = getString(R.string.debounce)
        tvInput.text = debounceInput.toString()
        debounceObservable.onNext(debounceInput++)
    }

    private fun setupDebounce() {
        debounceObservable
            .debounce(1, TimeUnit.SECONDS)
            .subscribe({ value -> tvOutput.text = value.toString() },
                { error -> tvOutput.text = "${error.message}" })
            .addTo(compositeDisposable)
    }

    private fun setupThrottleFirst() {
        throttleFirstObservable
            .throttleFirst(2, TimeUnit.SECONDS, Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ value -> tvOutput.text = value.toString() },
                { error -> tvOutput.text = "${error.message}" })
            .addTo(compositeDisposable)
    }

    //ThrottleFirst
    private fun onHandleThrottleFirst() {
        tvOperationType.text = getString(R.string.throttle_first)
        tvInput.text = throttleInput.toString()
        debounceObservable.onNext(throttleInput++)
    }

    //FlatMap
    private fun onHandleFlatMap() {
        val builder = StringBuilder()
        tvOutput.text = ""
        val list = listOf(1, 2, 3, 4, 5)
        tvOperationType.text = getString(R.string.flatmap)
        tvInput.text = list.toString()
        val disposable = Observable.fromIterable(list)
            .flatMap { t -> modifyObservable(t) }
            .subscribe({ value -> builder.append("$value ") },
                { error -> tvOutput.text = "${error.message}" },
                { tvOutput.text = builder })
        compositeDisposable.add(disposable)
    }

    private fun modifyObservable(input: Int) =
        Observable.create(ObservableOnSubscribe<Int> { emitter ->
            emitter.onNext(input * input)
            emitter.onComplete()
        }).subscribeOn(Schedulers.io())

    //ConcatMap
    private fun onHandleConcatMap() {
        val builder = StringBuilder()
        tvOutput.text = ""
        val list = listOf(1, 2, 3, 4, 5)
        tvOperationType.text = getString(R.string.flatmap)
        tvInput.text = list.toString()
        val disposable = Observable.fromIterable(list)
            .concatMap { t -> modifyObservable(t) }
            .subscribe({ value -> builder.append("$value ") },
                { error -> tvOutput.text = "${error.message}" },
                { tvOutput.text = builder })
        compositeDisposable.add(disposable)
    }

    //SwitchMap
    private fun onHandleSwitchMap() {
        val builder = StringBuilder()
        tvOutput.text = ""
        val list = listOf(1, 2, 3, 4, 5)
        tvOperationType.text = getString(R.string.switch_map)
        tvInput.text = list.toString()
        val disposable = Observable.fromIterable(list)
            .switchMap { t -> modifyObservable(t) }
            .subscribe({ value -> builder.append("$value ") },
                { error -> tvOutput.text = "${error.message}" },
                { tvOutput.text = builder })
        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}