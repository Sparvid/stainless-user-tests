import stainless.annotation.* 
import stainless.lang.*

// Task 1: Verify this function with Stainless
def factorial(n: BigInt): BigInt = {
        require(n >= 0)
        if n == 0 then BigInt(0)
        else n * factorial(n-1)
}.ensuring(_ >= 0)