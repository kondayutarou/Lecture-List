package com.shoppinglist.data

import androidx.room.*
import com.shoppinglist.data.source.local.ShoppingListItemInterface
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable

@Entity
data class ShoppingListItem(
    @PrimaryKey override val uuid: String,
    @ColumnInfo(name = "name") override val name: String,
    @ColumnInfo(name = "checked") override var checked: Boolean
) : ShoppingListItemInterface
