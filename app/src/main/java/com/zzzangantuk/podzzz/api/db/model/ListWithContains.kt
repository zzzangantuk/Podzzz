package com.zzzangantuk.podzzz.api.db.model

import androidx.room.Embedded

data class ListWithContains(
    @Embedded val list: ListModel,
    val contains: Boolean
)