import stainless.lang.*
import stainless.collection.*
import stainless.annotation.*

/* Task 3: Locate the bug using Stainless, and fix it
   That means that the postcondition should be proven valid in the end.
   You MAY modify the function bodies, but NOT the postcondition.
   You may write auxiliary functions or lemmas, and freely add verification constructs (e.g. require, ensure, decreases, invariant, assert, ...)

   You may only modify this file.
   You may access the tutorial freely, but no other files.
   You may not use other programs or extensions, such as Copilot.
   You may not access the internet.
   
   You have 20 minutes to spend on this task.
   You may ask Arvid clarifying questions about the task, but not questions regarding solutions.
   Please voice your thought process out loud: How you percieve the problem, what solutions you're considering, etc.
   If you are completely stuck for 30 seconds, you will get a hint for how to proceed.

   Enjoy!
 */ 
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