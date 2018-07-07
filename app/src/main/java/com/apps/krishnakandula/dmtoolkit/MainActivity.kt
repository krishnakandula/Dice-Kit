package com.apps.krishnakandula.dmtoolkit

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.apps.krishnakandula.dicerollerui.view.DiceRollerFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragment = DiceRollerFragment()
        supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit()
    }
}
