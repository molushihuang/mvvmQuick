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
    list.slice(IntRange(1, 3)).forEach { println("sliceIntRange>$it") }//（传入的是下标起始点和要截取的元素个数）
    list.slice(listOf(1, 3, 4)).forEach { println("slice>$it") }//（传入的是下标集合）

    list.filter { it % 2 == 0 }.forEach { println("filter>$it") }
    list.filterIndexed { index, i ->
        i % 2 == 0 && index < 5
    }.forEach { println("filterIndex>$it") }

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
    newNumberList.sort()
    newNumberList.forEach { println("filterTo>$it") }

    //按顺序删除集合的n个元素
    val listDrop = listOf(12, 2, 3, 45, 5, 9, 3)
    listDrop.drop(2).forEach { println("drop>$it") }
    listDrop.dropLast(2).forEach { println("dropLast>$it") }
    //从集合的第一项开始去掉满足条件元素，这样操作一直持续到出现第一个不满足条件元素出现为止，返回剩余元素(可能剩余元素有满足条件的元素)
    val listDropWhile = listOf(1, 2, 4, 8, 1, 9)
    listDropWhile.dropWhile { it == 1 }.forEach { println("listDropWhile>$it") }
    listDropWhile.dropLastWhile { it > 1 }.forEach { println("dropLastWhile$it") }

    val listTake = listOf(12, 5, 3, 9, 8, 6)
    listTake.take(3).forEach { println("take>$it") }
    listTake.takeLast(3).forEach { println("takeLast>$it") }
    listTake.takeWhile { it > 3 }.forEach { println("takeWhile>$it") }
    listTake.takeLastWhile { it > 3 }.forEach { println("takeLastWhile>$it") }


}

