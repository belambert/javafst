javafst
=======

This is mostly taken from
[Sphinx](https://github.com/cmusphinx/sphinx4/tree/master/sphinx4-core/src/main/java/edu/cmu/sphinx/fst)
with a small amount of clean-up, making it obey Google style checks, etc.

I've needed to extract this code from the Sphinx4 repo a number of times, so I
decided to finally give it another home here. I dont plan to make any major
modifications, but I might hack on it a little here or there. Please feel free
to make contributions.

You might find [JOpenFst](https://github.com/steveash/jopenfst) to be even more
useful.

TODO
====

The original code could be modernized in a number of ways, especially to take
advantages of Java8 features.  Here is a short list of some of the things that
could be done:

- Lots of the IO statements can be inlined.
- Lots of finals.
- regular "for" loops
- Java8 IO "Files."
- Replace 'Pair' class with Tuple?
- Use null less.
- The mutable/immutable distinction is a little weird.
