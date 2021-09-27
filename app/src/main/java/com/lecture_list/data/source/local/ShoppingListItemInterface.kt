package com.lecture_list.data.source.local

interface ShoppingListItemInterface {
    val uuid: String
    val name: String
    var checked: Boolean
}