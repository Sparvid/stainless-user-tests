import stainless.annotation.* 
import stainless.lang.*
import stainless.collection.*

// Copy or write code from tutorial

def square(x: BigInt): BigInt = {
  require(x >= 2)
  val res = x * x - x
  assert(res >= 0)
  assert(res > x)
  res
}.ensuring(res => res >= 0 && res > x)