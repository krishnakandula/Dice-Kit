package com.apps.krishnakandula.dicerollerui.view.deletetemplate

import com.apps.krishnakandula.common.view.BaseView
import com.apps.krishnakandula.dicerollercore.template.Template
import io.reactivex.Flowable

interface DeleteTemplateDialogView : BaseView {

    interface ViewActions {
        fun dismissDialog()
    }

    interface UserActions {
        fun onClickConfirmDeleteBtn(): Flowable<Template>
    }
}
