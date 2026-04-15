import stainless.annotation.* 
import stainless.lang.*

// Task 2: Verify this function with Stainless
def sumN2(n: BigInt): BigInt = {
    var i: BigInt = n
    var sum: BigInt = 0
    while (i > 0) {
        sum += i
        i -= 1
    }
    sum
}.ensuring(_ >= 0)