import stainless.lang.*
import stainless.collection.*
import stainless.annotation.*

// Task 3: Verify this property with Stainless
def associativity[T](l1: List[T], l2: List[T], l3: List[T]): Unit = {
}.ensuring(l1 ++ (l2 ++ l3) == (l1 ++ l2) ++ l3)