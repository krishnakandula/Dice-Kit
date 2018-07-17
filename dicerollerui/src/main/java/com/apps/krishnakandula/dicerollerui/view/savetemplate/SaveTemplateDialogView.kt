package com.apps.krishnakandula.dicerollerui.view.savetemplate

import io.reactivex.Flowable

interface SaveTemplateDialogView {

    fun setupActions()

    fun setupListeners()

    interface ViewActions {
        fun dismissDialog()

    }

    interface UserActions {
        fun onClickConfirmBtn(): Flowable<String>

    }
}
