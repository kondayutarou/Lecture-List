package com.shoppinglist.data.source.local

interface ShoppingListItemInterface {
    val uuid: String
    val name: String
    var checked: Boolean
}