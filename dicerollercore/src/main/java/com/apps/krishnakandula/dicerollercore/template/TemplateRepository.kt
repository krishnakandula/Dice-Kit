package com.apps.krishnakandula.dicerollercore.template

import io.reactivex.Flowable

interface TemplateRepository {

    fun getTemplates(): Flowable<List<Template>>

    fun addTemplate(template: Template)

    fun deleteTemplate(template: Template)
}
