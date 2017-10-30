Loop
====

[![Build Status](https://travis-ci.org/marconilanna/loop.svg)](https://travis-ci.org/marconilanna/loop)

A minimalist toy programming language implemented in Scala.
My solution to a job interview problem.

### Language Description

The language has only three constructs:

0. Initialization: `a = 0`
0. Increment: `a = a + 1`
0. Loop `a` times: `loop a { ... }`

Initialization is always to zero.
Increment, despite the syntax, is restricted to the equivalent of `a++`.

Constants can defined on the command line as `var1=n,var2=m,...`

### Running

`sbt "run test/resources/mult.loop a=2,b=3"`

License
-------

Copyright 2017 Marconi Lanna

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
