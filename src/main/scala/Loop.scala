import stainless.annotation.* 
import stainless.lang.*

// Task 2: Verify this function with Stainless
def sumN(n: BigInt): BigInt = {
    var i: BigInt = 0
    var sum: BigInt = 0
    while (i <= n) {
        sum += i
        i += 1
    }
    sum
}.ensuring(_ >= 0)