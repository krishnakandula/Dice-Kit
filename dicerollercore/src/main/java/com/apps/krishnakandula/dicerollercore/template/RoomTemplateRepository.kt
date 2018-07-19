package com.apps.krishnakandula.dicerollercore.template

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.apps.krishnakandula.dicerollercore.template.Template
import com.apps.krishnakandula.dicerollercore.template.TemplateRepository
import com.apps.krishnakandula.dicerollercore.template.TemplateSchema
import io.reactivex.Flowable

@Dao
interface RoomTemplateRepository : TemplateRepository {

    @Query("SELECT * FROM ${TemplateSchema.TABLE_NAME}")
    override fun getTemplates(): Flowable<List<Template>>

    @Insert
    override fun addTemplate(template: Template)

    @Delete
    override fun deleteTemplate(template: Template)

    @Query("SELECT * FROM ${TemplateSchema.TABLE_NAME} " +
            "WHERE ${TemplateSchema.Cols.ID} = :id")
    override fun getTemplate(id: Long): Flowable<Template>
}


