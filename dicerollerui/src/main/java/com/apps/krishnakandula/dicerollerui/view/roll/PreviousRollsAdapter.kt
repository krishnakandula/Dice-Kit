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
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.previous_rolls_itemview.view.*
import javax.inject.Inject

@Scopes.Fragment
class PreviousRollsAdapter @Inject constructor(private val context: Context,
                                               private val sharedViewPool: RecyclerView.RecycledViewPool)
    : RecyclerView.Adapter<PreviousRollsAdapter.PreviousRollsViewHolder>() {

    private val previousRolls: MutableList<DiceRollResult> = emptyList<DiceRollResult>().toMutableList()

    fun setData(rolls: List<DiceRollResult>,
                scrollCallback: (lastIndex: Int) -> Unit) {
        updateDataObservable(rolls)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onNext = { diffResult ->
                    diffResult.dispatchUpdatesTo(this)
                    previousRolls.clear()
                    previousRolls.addAll(rolls)
                    scrollCallback(previousRolls.lastIndex)
                })
    }

    private fun updateDataObservable(rolls: List<DiceRollResult>) = Observable.just(DiffUtil.calculateDiff(PreviousRollsDiffCallback(previousRolls, rolls)))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviousRollsViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.previous_rolls_itemview, parent, false)
        return PreviousRollsViewHolder(itemView)
    }

    override fun getItemCount(): Int = previousRolls.size

    override fun onBindViewHolder(holder: PreviousRollsViewHolder, position: Int) {
        if (position < itemCount) holder.bind(previousRolls[position])
    }

    inner class PreviousRollsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(result: DiceRollResult) {
            val adapter = PreviousRollsResultAdapter(context)
            itemView.previous_rolls_itemview_recycler_view.adapter = adapter
            itemView.previous_rolls_itemview_recycler_view.layoutManager = LinearLayoutManager(context,
                    LinearLayoutManager.HORIZONTAL,
                    false)
            itemView.previous_rolls_itemview_recycler_view.recycledViewPool = sharedViewPool
            itemView.previous_rolls_itemview_recycler_view.setItemViewCacheSize(15)

            adapter.setData(result.dice)
            itemView.previous_rolls_itemview_result_text_view.text = "${result.result}"
        }
    }
}
