package com.apps.krishnakandula.dicerollerui.view.roll

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apps.krishnakandula.common.Scopes
import com.apps.krishnakandula.dicerollercore.template.Template
import com.apps.krishnakandula.dicerollerui.R
import com.jakewharton.rxrelay2.BehaviorRelay
import kotlinx.android.synthetic.main.template_item.view.*
import javax.inject.Inject

@Scopes.Feature
class TemplateAdapter @Inject constructor(private val context: Context)
    : RecyclerView.Adapter<TemplateAdapter.TemplateViewHolder>() {

    private val templates = BehaviorRelay.createDefault<List<Template>>(emptyList())

    fun setData(data: List<Template>) {
        this.templates.accept(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TemplateViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.template_item, parent, false)
        return TemplateViewHolder(itemView)
    }

    override fun getItemCount(): Int = templates.value.size

    override fun onBindViewHolder(holder: TemplateViewHolder, position: Int) {
        if (position < itemCount) holder.bind(templates.value[position])
    }

    inner class TemplateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(template: Template) {
            itemView.template_item_template_name.text = template.name
        }
    }
}
