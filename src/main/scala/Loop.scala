import stainless.annotation.* 
import stainless.lang.*

/* Task 2: Add helper specification in order to verify the existing postcondition of the sumN
function using Stainless

  Time limit: 20 minutes

   When running Stainless on the function, all the properties in the summary should be marked green.
   You may not modify the function body, signature or the postcondition, but you may write auxiliary functions or lemmas, and freely add verification constructs (e.g. require, decreases, invariant, assert, ...)

   The entirety of your solution should be in this file. 

   You have access to the tutorial and Stainless documentation (open in the browser), but no other
   internet resources, programs or extensions, such as Copilot. You may ask clarifying questions to
   the interviewer about the task, but not regarding the solution.

   Please voice your thought process out loud: How you perceive the problem, what solutions you're
   considering, and generally anything that crosses your mind while working on the problem.

   Enjoy!
 */ 
def sumN(n: BigInt): BigInt = {
    var i: BigInt = 0
    var sum: BigInt = 0
    while (i <= n) {
        sum += i
        i += 1
    }
    sum
}.ensuring(res => res >= 0)