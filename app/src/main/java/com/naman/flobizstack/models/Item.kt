package com.naman.flobizstack.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "questions")
data class Item(
    @PrimaryKey(autoGenerate = true)
    var id:Int?=null,
    val accepted_answer_id: Int?,
    val answer_count: Int?,
    val content_license: String?,
    val creation_date: Int?,
    val is_answered: Boolean?,
    val last_activity_date: Int?,
    val last_edit_date: Int?,
    val link: String?,
    val owner: Owner?,
    val question_id: Int?,
    val score: Int?,
    val title: String?,
    val tags:List<String>,
    val view_count: Int?
): Serializable