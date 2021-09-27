package com.lecture_list.data

import androidx.room.*
import com.lecture_list.data.source.local.ShoppingListItemInterface

@Entity
data class ShoppingListItem(
    @PrimaryKey override val uuid: String,
    @ColumnInfo(name = "name") override val name: String,
    @ColumnInfo(name = "checked") override var checked: Boolean
) : ShoppingListItemInterface
