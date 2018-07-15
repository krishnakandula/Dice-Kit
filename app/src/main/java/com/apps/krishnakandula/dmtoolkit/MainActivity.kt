package com.apps.krishnakandula.dmtoolkit

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.apps.krishnakandula.common.OnBackPressedListener
import com.apps.krishnakandula.dicerollerui.view.roll.DiceRollerFragment

class MainActivity : AppCompatActivity() {

    companion object {
        const val DICE_ROLLER_FRAGMENT_TAG = "DICE_ROLLER_FRAGMENT_TAG"
    }

    private var diceRollerFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        diceRollerFragment = supportFragmentManager.findFragmentByTag(DICE_ROLLER_FRAGMENT_TAG)
        if (diceRollerFragment == null) diceRollerFragment = DiceRollerFragment()
        if (!(diceRollerFragment as Fragment).isAdded) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, diceRollerFragment, DICE_ROLLER_FRAGMENT_TAG)
                    .commit()
        }
    }

    override fun onBackPressed() {
        if (diceRollerFragment != null) {
            (diceRollerFragment as OnBackPressedListener).onBackPressed { super.onBackPressed() }
        } else {
            super.onBackPressed()
        }
    }
}
