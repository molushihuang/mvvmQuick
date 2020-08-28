package com.xqd.mylibrary.inyterface

/**
 * Created by 谢邱东 on 2020/8/9 16:13.
 * NO bug
 */
interface IOnItemClick<T> {
    fun onItemClick(position: Int, type: Int, t: T)
}