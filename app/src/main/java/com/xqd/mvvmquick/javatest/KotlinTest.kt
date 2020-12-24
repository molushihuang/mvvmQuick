package com.xqd.mvvmquick.javatest

/**
 * Created by 谢邱东 on 2020/4/28 10:14.
 * NO bug
 */
fun main(args: Array<String>) {
//        println("求和" + sum(7, 8));
//    println("求和" + sum2(3, 7))
//    box3()

    operatorTest()
}

fun sum(a: Int, b: Int): Int {
    return a + b;
}

fun sum2(a: Int, b: Int): Int = a + b;

//都没装箱
fun box1() {
    var a: Int = 1000;
    println(a == a)
    var b: Int = a;
    var c: Int = a;
    println(b == c)
    println(b === c)
}

//全部装箱
fun box2() {
    var a: Int? = 1000;
    println(a == a)
    var b: Int? = a;
    var c: Int? = a;
    println(b == c)
    println(b === c)
}

//a不装箱，BC装箱
fun box3() {
    var a: Int = 1000;
    println(a == a)
    var b: Int? = a
    var c: Int? = a;
    println(b == c)//比较的是数值
    println(b === c)//比较的是地址

    when (a) {
        1 -> println("value 1")
        100 -> println("value 100")
        1000 -> println("value 1000")
        else -> println("no value")
    }
}

//函数操作符测试
private fun operatorTest() {
    //函数式操作符
    val list = listOf(1, 2, 3, 4, 5, 6, 7)
//        val listSlice = list.slice(IntRange(1, 3))//2.3.4（传入的是下标起始点和要截取的元素个数）
    val listSlice = list.slice(listOf(1, 3, 4))//2.4.5（传入的是下标集合）
    listSlice.forEach { println("slice>$it") }

    val listFilter = list.filter { it % 2 == 0 }
    listFilter.forEach { println("filter>$it") }
    val listFilterIndex = list.filterIndexed { index, i ->
        i % 2 == 0 && index < 5
    }
    listFilterIndex.forEach { println("filterIndex>$it") }

    val numberList1 = listOf(23, 65, 14, 57, 99, 123, 26, 15, 88, 37, 56)
    val numberList2 = listOf(13, 55, 24, 67, 93, 137, 216, 115, 828, 317, 16)
    val numberList3 = listOf(20, 45, 19, 7, 9, 3, 26, 5, 38, 75, 46)
    //需要注意一点的是，我们从源码看到filterTo第一个参数destination是一个可变集合类型，所以这里使用的mutableListOf初始化
    val newNumberList = mutableListOf<Int>().apply {
        numberList1.filterTo(this) {
            it % 2 == 0
        }
        numberList2.filterTo(this) {
            it % 2 == 0
        }
        numberList3.filterTo(this) {
            it % 2 == 0
        }
    }
//        newNumberList.sort()
    newNumberList.forEach { println("filterTo>$it") }
}

