package com.apps.krishnakandula.diceroller.template.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.apps.krishnakandula.diceroller.template.Template
import com.apps.krishnakandula.diceroller.template.TemplateRepository
import com.apps.krishnakandula.diceroller.template.TemplateSchema
import io.reactivex.Flowable

@Dao
interface RoomTemplateRepository : TemplateRepository {

    @Query("SELECT * FROM ${TemplateSchema.TABLE_NAME}")
    override fun getTemplates(): Flowable<List<Template>>

    @Insert
    override fun addTemplate(template: Template)

    @Delete
    override fun deleteTemplate(template: Template)

}


