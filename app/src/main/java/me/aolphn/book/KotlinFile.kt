package me.aolphn.book

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ThreadLocalObject{
    init {
        GlobalScope.launch {

        }
    }
    val x = "x"
    val y = "y"
}