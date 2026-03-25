import stainless.annotation.* 
import stainless.lang.*

@extern
def main(args: Array[String]): Unit = {
        println(fib(10))
}
// 0,1,1,2,3,5,8,13,21,34,55

// One correct solution using BigInt
def fib(n: BigInt): BigInt = {
        require(n >= 0)
        decreases(n)
        if n == 0 then BigInt(0)
        else if n == 1 then BigInt(1)
        else fib(n-2) + fib(n-1)
}.ensuring(n >= 0)

// Another correct solution?? LEADS TO TIMEOUT, WHY?
def fib2(n: Int): Int = {
        require(n >= 0 && n < Int.MaxValue)
        decreases(n)
        if n == 0 then 0
        else if n == 1 then 1
        else fib2(n-2) + fib2(n-1)
}.ensuring(n >= 0)

// Task skeleton code
def fib3(n: Int): Int = {
        if n == 0 then 0
        else if n == 1 then 1
        else fib3(n-2) + fib3(n-1)
}.ensuring(n >= 0) // Task 1: prove the postcondition