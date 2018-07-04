package com.apps.krishnakandula.diceroller.template

import com.apps.krishnakandula.diceroller.Dice
import io.reactivex.Completable
import io.reactivex.Observable

interface TemplateRepository {

    fun getTemplates(): Observable<List<Template>>

    fun addTemplate(name: String, rolls: List<Dice>): Completable

    fun removeTemplate(id: String): Completable
}
