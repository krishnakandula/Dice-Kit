package com.apps.krishnakandula.common.view

import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

interface BasePresenter {

    companion object {
        const val DEFAULT_ACTIONS_TIMEOUT: Long = 400
        val DEFAULT_TIME_UNIT = TimeUnit.MILLISECONDS
    }

    fun bindActions(): CompositeDisposable
}
