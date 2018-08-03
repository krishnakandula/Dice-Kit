package com.apps.krishnakandula.dicerollerui.view.roll

import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apps.krishnakandula.common.Scopes
import com.apps.krishnakandula.dicerollercore.template.Template
import com.apps.krishnakandula.dicerollerui.R
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.template_item.view.*
import javax.inject.Inject

@Scopes.Fragment
class TemplateAdapter @Inject constructor(private val context: Context,
                                          private val templateInteractionsListener: TemplateInteractionsListener)
    : RecyclerView.Adapter<TemplateAdapter.TemplateViewHolder>() {

    private val templates = emptyList<Template>().toMutableList()

    fun setData(data: List<Template>,
                scrollCallback: (lastIndex: Int) -> Unit) {
        updateDataObservable(data)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onNext = { diffResult ->
                    diffResult.dispatchUpdatesTo(this)
                    templates.clear()
                    templates.addAll(data)
                    scrollCallback(templates.lastIndex)
                })
    }

    private fun updateDataObservable(data: List<Template>) = Observable.just(DiffUtil.calculateDiff(TemplateDiffCallback(templates, data)))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TemplateViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.template_item, parent, false)
        return TemplateViewHolder(itemView)
    }

    override fun getItemCount(): Int = templates.size

    override fun onBindViewHolder(holder: TemplateViewHolder, position: Int) {
        if (position < itemCount) holder.bind(templates[position])
    }

    inner class TemplateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(template: Template) {
            itemView.template_item_template_name.text = template.name
            itemView.setOnClickListener { templateInteractionsListener.onClickTemplate(template) }
            itemView.setOnLongClickListener {
                templateInteractionsListener.onLongClickTemplate(template)
                true
            }
        }
    }

    interface TemplateInteractionsListener {

        fun onClickTemplate(template: Template)

        fun onLongClickTemplate(template: Template)
    }

    internal class TemplateDiffCallback(private val oldTemplates: List<Template>,
                                        private val newTemplates: List<Template>) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldTemplates[oldItemPosition] == newTemplates[newItemPosition]
        }

        override fun getOldListSize(): Int = oldTemplates.size

        override fun getNewListSize(): Int = newTemplates.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldTemplates[oldItemPosition] == newTemplates[newItemPosition]
        }
    }
}
