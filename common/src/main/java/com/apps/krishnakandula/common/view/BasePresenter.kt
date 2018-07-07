package com.apps.krishnakandula.common.view

import io.reactivex.disposables.CompositeDisposable

interface BasePresenter {

    fun bindActions(): CompositeDisposable
}
