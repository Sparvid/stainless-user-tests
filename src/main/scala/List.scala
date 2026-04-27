import stainless.lang.*
import stainless.collection.*
import stainless.annotation.*

// Task 3: Verify the function with Stainless
object SortedInsert {
    
  /** Returns true iff the list is sorted in increasing order. */
  def isSorted(l: List[BigInt]): Boolean = l match {
    case Nil()                    => true
    case Cons(_, Nil())           => true
    case Cons(h, t @ Cons(h2, _)) => h <= h2 && isSorted(t)
  }

  // Inserts `x` into the sorted list `l`, returning a new sorted list.
  def insert(x: BigInt, l: List[BigInt]): List[BigInt] = {
    require(isSorted(l))
    l match {
      case Nil() =>
        Cons(x, Nil())
      case Cons(h, t) =>
        if (x <= h) Cons(h, Cons(x, t))
        else        Cons(h, insert(x, t))
    }
  }.ensuring{res => isSorted(res)}
}