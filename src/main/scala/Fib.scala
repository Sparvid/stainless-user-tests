import stainless.annotation.* 
import stainless.lang.*

@extern
def main(args: Array[String]): Unit = {
        println(fib(10))
}
// 0,1,1,2,3,5,8,13,21,34,55

// TODO: remove verification code, write specification (or not)
def fib(n: BigInt): BigInt = {
        require(n >= 0)
        decreases(n)
        if n == 0 then BigInt(0)
        else if n == 1 then BigInt(1)
        else fib(n-2) + fib(n-1)
}.ensuring(n >= 0)