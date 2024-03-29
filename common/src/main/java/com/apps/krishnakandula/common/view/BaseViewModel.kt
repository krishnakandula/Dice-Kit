package com.apps.krishnakandula.common.view

import io.reactivex.disposables.CompositeDisposable

interface BaseViewModel {

    fun bindSources(): CompositeDisposable
}
