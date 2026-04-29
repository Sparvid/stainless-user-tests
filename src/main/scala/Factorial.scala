import stainless.annotation.* 
import stainless.lang.*

/* Task 1: Verify the postcondition using Stainless
   That means running Stainless on the function and proving the postcondition valid.
   You may not modify the existing code; function body or postcondition.
   You may write auxiliary functions or lemmas, and freely add verification constructs (e.g. require, ensure, decreases, invariant, assert, ...)

   You may only modify this file.
   You may access the tutorial freely, but no other files.
   You may not use other programs or extensions, such as Copilot.
   You may not access the internet.
   
   You have 10 minutes to spend on this task.
   You may ask Arvid clarifying questions about the task, but not questions regarding solutions.
   Please voice your thought process out loud: How you percieve the problem, what solutions you're considering, etc.
   If you are completely stuck for 30 seconds, you will get a hint for how to proceed.

   Enjoy!
 */ 
def factorial(n: BigInt): BigInt = {
        if n == 0 then BigInt(0)
        else n * factorial(n-1)
}.ensuring(_ >= 0)