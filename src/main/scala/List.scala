import stainless.lang.*
import stainless.collection.*
import stainless.annotation.*

/* Task 3: Locate the bug in the insert function using Stainless, and fix it

  Time limit: 20 minutes

   When running Stainless on the file, all the properties in the summary should be marked green.
   You MAY modify the function body, but NOT the signature or the postcondition.
   You may write auxiliary functions or lemmas, and freely add verification constructs (e.g. require, decreases, invariant, assert, ...)

   The entirety of your solution should be in this file. 

   You have access to the tutorial and Stainless documentation (open in the browser), but no other
   internet resources, programs or extensions, such as Copilot. You may ask clarifying questions to
   the interviewer about the task, but not regarding the solution.

   Please voice your thought process out loud: How you perceive the problem, what solutions you're
   considering, and generally anything that crosses your mind while working on the problem.

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