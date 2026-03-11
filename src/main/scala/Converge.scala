import stainless.annotation.* 
import stainless.lang.*
import stainless.math.*

@extern
def main(args: Array[String]): Unit = {
        println(converge())
}

def converge(): Rational = {
  var a: Rational = Rational(0)
  var d: Rational = Rational(1)
  decreases(d)

  while (d > Rational(1,100)) {
    a = a + d/Rational(2)
    d = Rational(1) - a
  }.invariant(
    a + d == Rational(1) &&
    a >= Rational(0) &&
    d >= Rational(0)
  )
  a
}.ensuring(res => Rational(99,100) <= res && res <= Rational(1))