package com.naman.flobizstack.models

data class StackQuestions(
    val has_more: Boolean,
    val items: MutableList<Item>,
    val quota_max: Int,
    val quota_remaining: Int
)