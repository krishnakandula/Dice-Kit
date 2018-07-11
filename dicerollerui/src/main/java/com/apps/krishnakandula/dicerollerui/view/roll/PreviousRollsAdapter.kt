package com.apps.krishnakandula.dicerollerui.view.roll

import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apps.krishnakandula.common.Scopes
import com.apps.krishnakandula.dicerollercore.roller.DiceRollResult
import com.apps.krishnakandula.dicerollerui.R
import com.jakewharton.rxrelay2.BehaviorRelay
import kotlinx.android.synthetic.main.previous_rolls_itemview.view.*
import javax.inject.Inject

@Scopes.Feature
class PreviousRollsAdapter @Inject constructor(private val context: Context)
    : RecyclerView.Adapter<PreviousRollsAdapter.PreviousRollsViewHolder>() {

    private val previousRolls = BehaviorRelay.createDefault<List<DiceRollResult>>(emptyList())

    fun setData(rolls: List<DiceRollResult>) {
        val diffResult = DiffUtil.calculateDiff(PreviousRollsDiffCallback(previousRolls.value, rolls))
        this.previousRolls.accept(rolls)
        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviousRollsViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.previous_rolls_itemview, parent, false)
        return PreviousRollsViewHolder(itemView)
    }

    override fun getItemCount(): Int = previousRolls.value.size

    override fun onBindViewHolder(holder: PreviousRollsViewHolder, position: Int) {
        if (position < itemCount) holder.bind(previousRolls.value[position])
    }

    inner class PreviousRollsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(result: DiceRollResult) {
            val adapter = DiceEquationAdapter(context)
            itemView.previous_rolls_itemview_recycler_view.adapter = adapter
            itemView.previous_rolls_itemview_recycler_view.layoutManager = LinearLayoutManager(context,
                    LinearLayoutManager.HORIZONTAL,
                    false)
            // TODO: Fix adapter
//            adapter.setData(result.dice.map {  })
            itemView.previous_rolls_itemview_result_text_view.text = "${result.result}"
        }
    }
}
