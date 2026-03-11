import stainless.annotation.* 
import stainless.lang.*

@extern
def main(args: Array[String]): Unit = {
        println(converge())
}

def converge(): Float = {
  var a: Float = 0
  var d: Float = 1
  decreases(d)
  while
      d > 1/100
  do
      a = a + d/2
      d = 1 - a
  .invariant(a + d == 1 && a >= 0 && d >= 0)
  a
}.ensuring(res => 99 / 100 <= res && res <= 1)