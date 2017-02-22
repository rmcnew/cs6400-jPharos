# cs6400-jPharos
jPharos is a simple Java-based Ray Tracer made for CS 6400:  Computer Graphics II

Note:  Normal object-oriented design principles are not followed for many of the jPharos classes.  Typically, class data is private and only accessible through well-defined selector and mutator functions (i.e. "getters" and "setters").  

Most of the jPharos ray tracer classes are read-only value classes that represent geometric primitives and mathematical operations, so the normal use of encapsulation and data hiding is not appropriate.  ([Rich Hickey's "The Value of Values" talk](https://www.youtube.com/watch?v=-6BsiVyC1kM) gives a great overview of values and how the immutability of values enhances the ability to reason about software.)  

The purpose of selector and mutator functions is to hide the class’s internal implementation details.  This is usually not needed for value classes since they are read-only.  Functions that produce a different value do not mutate the value object in place, but rather create a new object for the new value.
