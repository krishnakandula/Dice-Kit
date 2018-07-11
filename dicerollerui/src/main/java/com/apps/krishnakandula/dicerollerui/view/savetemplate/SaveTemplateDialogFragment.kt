package com.apps.krishnakandula.dicerollerui.view.savetemplate

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apps.krishnakandula.dicerollercore.DiceRollerComponentProvider
import com.apps.krishnakandula.dicerollerui.R
import com.apps.krishnakandula.dicerollerui.di.DaggerSaveTemplateUIComponent
import com.apps.krishnakandula.dicerollerui.di.DiceRollerUIComponentProvider
import com.apps.krishnakandula.dicerollerui.di.SaveTemplateUIModule
import com.apps.krishnakandula.dicerollerui.view.roll.TemplateAdapter
import kotlinx.android.synthetic.main.fragment_save_template.*

class SaveTemplateDialogFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerSaveTemplateUIComponent.builder()
                .diceRollerComponent((activity!!.application as DiceRollerComponentProvider).diceRollerComponent())
                .saveTemplateUIModule(SaveTemplateUIModule())
                .build()
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_save_template, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragment_save_template_cancel_btn.setOnClickListener { dialog.dismiss() }
    }
}
