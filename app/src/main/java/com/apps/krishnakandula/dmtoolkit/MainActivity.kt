package com.apps.krishnakandula.dmtoolkit

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.apps.krishnakandula.dicerollerui.view.roll.DiceRollerFragment

class MainActivity : AppCompatActivity() {

    companion object {
        const val DICE_ROLLER_FRAGMENT_TAG = "DICE_ROLLER_FRAGMENT_TAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var fragment = supportFragmentManager.findFragmentByTag(DICE_ROLLER_FRAGMENT_TAG)
        if (fragment == null) fragment = DiceRollerFragment()
        if (!fragment.isAdded) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragment, DICE_ROLLER_FRAGMENT_TAG)
                    .commit()
        }
    }
}
