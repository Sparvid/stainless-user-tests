# Tutorial

This tutorial is meant to prepare you for the verification tasks you will carry out during this study.
Please ask questions to the interviewer if any part of the tutorial is unclear. 


## Postconditions

Stainless takes Scala programs as input. Properties that you want to verify are expressed as **annotations** in the code. These annotations are written in Scala syntax, meaning that annotated programs can still be compiled and run like regular Scala programs. Let's see how this works with a simple example. The file `Tutorial.scala` under `src/main/scala` should be opened to right in a split window, where you can reproduce the examples covered in the tutorial. We start by defining a function that computes the square of an integer.
```scala
def square(x: BigInt): BigInt = {
  x * x
}
```

The type `BigInt` represents integers of arbitrary size and cannot overflow.  A property of squaring is that the result is always non-negative. We can express this property as a **postcondition** using an `ensuring` clause at the end of the function definition. This clause specifies a property that the function's result must satisfy. It is written as an anonymous function that takes the result as argument and returns a boolean.
```scala
def square(x: BigInt): BigInt = {
  x * x
}.ensuring(res => res >= 0) // <-- postcondition
```

You can verify this function with Stainless by executing the command `stainless src/tutorial/scala/Tutorial.scala` in the terminal. You should obtain the following output, where every item in the summary is colored green.

```shell
$ stainless src/main/scala/Tutorial.scala

[  Info  ] Finished compiling
[  Info  ] Preprocessing finished
[  Info  ] Finished lowering the symbols
[  Info  ] Finished generating VCs
[  Info  ] Starting verification...
[  Info  ]  Verified: 1 / 1
[  Info  ]   ┌───────────────────┐
[  Info  ] ╔═╡ stainless summary ╞═════════════════════════════════════════════════════════════════════════════════╗
[  Info  ] ║ └───────────────────┘                                                                                 ║
[  Info  ] ║ src/main/scala/Tutorial.scala:1:5:            square   postcondition      valid   U:smt-cvc5     0.1  ║
[  Info  ] ╟┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄╢
[  Info  ] ║ total: 1    valid: 1    (0 from cache, 0 trivial) invalid: 0    unknown: 0    time:    0.08           ║
[  Info  ] ╚═══════════════════════════════════════════════════════════════════════════════════════════════════════╝
```

By looking at the summary you can see that Stainless proved the postcondition of the `square` function. You can see other details, such as the time taken to verify the postcondition, the line number where it is defined and the solver Stainless used to verify it.

## Preconditions

Now, suppose we know that the input to the `square` function is always at least 2. In that case, we could attempt to prove additional properties such as `res > x`.

```scala
// Trust me: x >= 2
def square(x: BigInt): BigInt = {
  x * x
}.ensuring(res => res >= 0 && res > x) // <-- new postcondition
```

Unfortunately, Stainless will not verify this snippet because the property does not hold for all inputs, and Stainless has no way of knowing what you assume about the input. To fix this issue, we can explicitly specify assumptions about the input using **preconditions**, expressed with the `require` keyword. Preconditions define conditions that must hold whenever the function is called.


```scala
def square(x: BigInt): BigInt = {
  require(x >= 2) // <-- precondition
  x * x
}.ensuring(res => res >= 0 && res > x)
```

Stainless should now be able to verify the postcondition of the `square` function. If you call the function elsewhere in your code with an input that does not satisfy the precondition, Stainless will report an error, indicating that the precondition is violated.

```scala
def square(x: BigInt): BigInt = {
  require(x >= 2)
  x * x
}.ensuring(res => res >= 0 && res > x)

// This will not verify
val squareOfOne = square(1)
```


## Debugging

If the verification is not successful, Stainless reports failed properties in the summary, marked red if it finds a counter-example, or yellow if it times out and cannot find one. In particular, counter-examples are helpful for debugging, as they provide concrete inputs that cause the property to fail.
For example, let us introduce a bug into the code of the `square` function,

```scala
def square(x: BigInt): BigInt = {
  require(x >= 2)
  x * x - x // <-- we introduce a bug here
}.ensuring(res => res >= 0 && res > x)
```
and then try to verify it with Stainless. The output should look like:


```shell
$ stainless src/main/scala/Tutorial.scala

[  Info  ] Finished compiling                                       
[  Info  ] Preprocessing finished                                   
[  Info  ] Finished lowering the symbols                            
[  Info  ] Finished generating VCs                                  
[  Info  ] Starting verification...
[  Info  ]  Verified: 0 / 1
[Warning ]  - Result for 'postcondition' VC for square @7:5:
[Warning ] x < BigInt("2") || {
[Warning ]   val res: BigInt = x * x - x
[Warning ]   res >= BigInt("0") && res > x
[Warning ] }
[Warning ] src/main/scala/Tutorial.scala:7:5:  => INVALID
           def square(x: BigInt): BigInt = {
               ^
[Warning ] Found counter-example:
[Warning ]   x: BigInt -> BigInt("2")
[  Info  ]  Verified: 0 / 1
[  Info  ] Done in 0,88s
[  Info  ]   ┌───────────────────┐
[  Info  ] ╔═╡ stainless summary ╞═════════════════════════════════════════════════════════════════════════════════╗
[  Info  ] ║ └───────────────────┘                                                                                 ║
[  Info  ] ║ src/main/scala/Tutorial.scala:7:5:            square   postcondition      invalid    U:smt-z3    0,1  ║
[  Info  ] ╟┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄╢
[  Info  ] ║ total: 1    valid: 0    (0 from cache, 0 trivial) invalid: 1    unknown: 0    time:    0,05           ║
[  Info  ] ╚═══════════════════════════════════════════════════════════════════════════════════════════════════════╝
[  Info  ] Verification pipeline summary:
[  Info  ]   smt-cvc5, smt-z3, non-batched
[  Info  ] Shutting down executor service.
```

In the warnings above the summary, you can see that Stainless found the counter-example `x -> BigInt("2")`, which causes the postcondition to fail.
However, it does not tell you which part of the postcondition fails. 
To check if a property holds at some point in the program, you can use assertions, by writing `assert(condition)`. The success or failure of the assertion will be reported like other properties by Stainless. In this example, we will add assertions to check which of the two parts of the postcondition (`res >= 0` or `res > x`) fails.

```scala
def square(x: BigInt): BigInt = {
  require(x >= 2)
  val res = x * x - x
  assert(res >= 0) // <--- Check the first part of the postcondition
  assert(res > x) // <--- Check the second part of the postcondition
  res
}.ensuring(res => res >= 0 && res > x)
```

If you run Stainless on this code, you should see that the first assertion holds, while the second assertion fails, which means that the part of the postcondition that fails is `res > x`.
This information can be helpful to pinpoint the bug in the code, which in this case is the subtraction of `x` from `x * x`.
If you fix the bug and run Stainless again, you should see that both assertions hold, and the postcondition is verified successfully.




## Termination and invariants

When encountering a looping constructs or a recursive function, Stainless always attempts to verify that it terminates. 
Stainless can verify termination automatically when it is straightforward.
Let's run Stainless on the following function to check if it is the case here:

```scala
def loop(n: BigInt): BigInt = {  
   var i: BigInt = n  
   var res: BigInt = 0  
   while(i > 0) {  
       res = res + 1  
       i = i-1  
   }
   res  
}
```

Stainless reports a `measure missing` failure. A measure is a nonnegative expression that decreases with every iteration of the loop, and is used by Stainless to verify termination. In this case `i` is a good candidate for a measure, as it is nonnegative and decreases with every iteration.

Stainless could not infer a measure automatically for this loop, which thus needs to be provided by the user. You can specify a measure manually using the `decreases` annotation.

```scala
def loop(n: BigInt): BigInt = {  
   var i: BigInt = n  
   var res: BigInt = 0  
   while(i > 0) {  
       decreases(i)  // <-- measure provided by the user
       res = res + 1  
       i = i-1  
   } 
   res  
}
```

If you run Stainless on this code, it should verify successfully.

Now let's say we want to verify that the result is positive.

```scala
def loop(n: BigInt): BigInt = {
   var i: BigInt = n  
   var res: BigInt = 0  
   while(i > 0) {  
       decreases(i)  
       res = res + 1  
       i = i-1  
   } 
   res  
}.ensuring(_ >= 0) // <-- additional postcondition
```

Stainless times out on the postcondition. To help Stainless verify the postcondition, we need to provide information about what holds after every iteration of the loop. 
This information is called a loop invariant and is expressed with the `invariant` annotation. In this example `res >= 0` is a good candidate for a loop invariant, as it holds after every iteration of the loop, and at the end of the loop it implies the postcondition.
Verifying the function with the loop invariant should now succeed.

```scala
def loop(n: BigInt): BigInt = {
   var i: BigInt = n  
   var res: BigInt = 0  
   (while(i > 0) {  
       decreases(i)  
       res = res + 1  
       i = i-1  
   }).invariant(res >= 0) // <-- loop invariant
   res  
}.ensuring(res => res >= 0)
```

Stainless has an official documentation where you can find more details about the features covered in this tutorial: https://epfl-lara.github.io/stainless/.

If you have any questions about the tutorial or about the documentation, please ask the interviewer.
We will now move on to the verification tasks, where you will have the opportunity to apply what you have learned in this tutorial!
