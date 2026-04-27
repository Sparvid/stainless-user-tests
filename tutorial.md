# Tutorial

This tutorial is meant to prepare participants for the user tasks. It is done in person, with Arvid as help.


## Pre and Post-conditions

Stainless takes Scala programs as input. Properties that you want to verify are expressed as **annotations** in the code. These annotations are converted into runtime assertions when the program is executed, meaning that annotated programs can still be compiled and run like regular Scala programs. Let's see how this works with a simple example. The file `Tutorial.scala` under `src/tutorial/scala` should be opened to right in a split window. We start by defining a function that computes the square of an integer. Copy this code to the Scala file.
```scala
def square(x: BigInt): BigInt = {
  x * x
}
```

The type `BigInt` is of arbitrary size, and we use it to avoid arithmetic overflow.  A property of squaring is that the result is always non-negative. We can express this property as a **postcondition** using an `ensuring` clause at the end of the function definition. This clause specified a property that the function's result must satisfy. This property is written as a lambda (anonymous function) that takes the result of the function as input and returns a boolean.
```scala
def square(x: BigInt): BigInt = {
  x * x
}.ensuring(res => res >= 0)
```

Since the function returns a value, the postcondition can also be written as `ensuring(_ >= 0)` where the underscore is bound to the returned value.

If you run stainless on this file with the command `stainless src/tutorial/scala/Tutorial.scala`, you should see the following output, where most of the text is colored green.

```shell
> stainless src/main/scala/Tutorial.scala
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

The summary shows that the postcondition of the `square` function has been proven valid. You can see other details, such as the time taken to verify the property, the solver used, and the line number where the property is defined.

If verification is not successful, Stainless will report failed properties in the summary, marked red. And also generate warnings, which might contain helpful information for debugging, such as a counter-example. For example, if we introduce a bug into the code by inverting one of the factors.

```scala
def square(x: BigInt): BigInt = {
  x * -x
}.ensuring(res => res >= 0)
```
And then try to verify it with Stainless. Then the output should look like:


```shell
[  Info  ] Finished compiling                                       
[  Info  ] Preprocessing finished                                   
[  Info  ] Finished lowering the symbols                            
[  Info  ] Finished generating VCs                                  
[  Info  ] Starting verification...
[  Info  ]  Verified: 0 / 1
[Warning ]  - Result for 'postcondition' VC for square @6:7:
[Warning ] x * -x >= BigInt("0")
[Warning ] src/main/scala/Tutorial.scala:6:7:  => INVALID
             def square(x: BigInt): BigInt = {
                 ^
[Warning ] Found counter-example:
[Warning ]   x: BigInt -> BigInt("-1")
[  Info  ]  Verified: 0 / 1
[  Info  ] Done in 2.23s
[  Info  ]   ┌───────────────────┐
[  Info  ] ╔═╡ stainless summary ╞═════════════════════════════════════════════════════════════════════════════════╗
[  Info  ] ║ └───────────────────┘                                                                                 ║
[  Info  ] ║ src/main/scala/Tutorial.scala:6:7:           square   postcondition     invalid    U:smt-cvc5    0.1  ║
[  Info  ] ╟┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄╢
[  Info  ] ║ total: 1    valid: 0    (0 from cache, 0 trivial) invalid: 1    unknown: 0    time:    0.11           ║
[  Info  ] ╚═══════════════════════════════════════════════════════════════════════════════════════════════════════╝
[  Info  ] Verification pipeline summary:
[  Info  ]   smt-cvc5, smt-z3, non-batched
[  Info  ] Shutting down executor service.
```

We can look at the warnings above the summary to see what counter-example Stainless has found. In this case, we see that x -> -1 causes the postcondition to fail. 

TODO: assertions

Now, suppose we know that the input to the `square` function is always at least 2. In that case, we could attempt to prove additional properties such as `res > x`.

```scala
// Trust me: x >= 2
def square(x: BigInt): BigInt = {
  x * x
}.ensuring(res => res >= 0 && res > x)
```

Unfortunately, Stainless will not verify this snippet because the property does not hold for all inputs, and Stainless has no way of knowing what you assume about the input. To fix this issue, we can explicitly specify assumptions about the input using **preconditions**, expressed with the `require` keyword. Preconditions define conditions that must hold whenever the function is called.


```scala
def square(x: BigInt): BigInt = {
  require(x >= 2)
  x * x
}.ensuring(res => res >= 0 && res > x)
```

Stainless should now be able to verify the postcondition of the `square` function. If you call the function elsewhere in your code with an input that does not satisfy the precondition, Stainless will complain, stating that the precondition is not met.

```scala
def square(x: BigInt): BigInt = {
  require(x >= 2)
  x * x
}.ensuring(res => res >= 0 && res > x)

// This will not verify
val squareOfOne = square(1)
```

## Termination and invariants

For looping constructs and recursive functions, Stainless will attempt to verify termination. Sometimes Stainless is able to do it successfully without guidance, but sometimes it needs help. Let's try verifying the following function.

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

We get a 'measure missing' failure from Stainless. If the measure is too complex for Stainless to infer automatically, you can specify it manually using the `decreases` annotation.

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
}
```

Now let's say we want to verify that the result is positive by adding an `ensuring` clause.

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
}.ensuring(_ >= 0)
```

Stainless times out on the postcondition, and can be helped along with a loop invariant. The `invariant` annotation needs to be applied to the whole while constructs, to which we add parenthesis. After this Stainless successfully verifies the function.

```scala
def loop(n: BigInt): BigInt = {
   var i: BigInt = n  
   var res: BigInt = 0  
   (while(i > 0) {  
       decreases(i)  
       res = res + 1  
       i = i-1  
   }).invariant(res >= 0)
   res  
}.ensuring(_ >= 0)
```

End of tutorial