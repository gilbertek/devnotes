# Clojure notes

## Operation Forms

```clojure
(op ...)
```

- op can be one of
  - Special operator or macro
  - Expression that yield a function
  - Something invocable

## Literals

```clojure
42            ; Long
6.022e23      ; Double

42N           ; BigInt
1.0M          ;  Decimal
22/7          ; Ratio

"Hello"       ; String
\e            ; Character

true false    ; Booleans
nil           ; Null

+ Fred *bob*  ; Symbols
*             ; Generally means mutable

:alpha :beta  ; Keywords
```

## Data Structures

```
(4 :alpha 3.0) ; List

[2 "hello" 99] ; Vector Similar to array

{:a 1 :b 2} ; Map or Dictionaries

#{Alice Jim Bob} ; Set # on the left hand side followed by {  }

```

## Meta data

Clojure supports meta data, which is the ability to attach any map of data to any object.
Meta data do not affect the object equality semantic.
Clojure use meta data to store function documentation string

```clojure
(with-meta [1 2 3] {:example true})
;; => [1 2 3]
;; The meta data value is {:example true}

;; the meta function output the meta data
(meta (with-meta [1 2 3] {:example true}))
;; => {:example true}
```

## Reader Macros

A Reader macro is a kind of syntactic sugar or shortcut
A reader macro is a pattern of characters that the Clojure reader understand
and knows to expand to some usually large piece of Clojure code.

| Reader Macros     | Expansion                    |
| :---------------- | ---------------------------- |
| 'foo              | (quote foo)                  |
| #'foo             | (var foo)                    |
| @foo              | (deref foo)                  |
| #(+ % 5)          | (fn [x] (+ x 5))             |
| ^{:key val} foo   | (with-meta foo {:key val})   |
| ^:key foo         | (with-meta foo :key true)    |

Quote is a special operator in clojure that return its argument unevaluated.

Anonymous function
```clojure
#(+ % 5)
```

## Leiningen Directory Structure

| Path       | Purpose                   |
| :-----     | -----                     |
| projct.clj | Project/Build config      |
| classes/   | Compiled bytecode         |
| lib/       | Dependent JARs            |
| public/    | HTML/CSS/JS files for web |
| src/       | Clojure source            |
| test/      | Unit test                 |

## Maven Directory Structure

| Path             | Purpose              |
| :-----           | -------------        |
| pom.xml          | Project/build config |
| target/classes   | Compiled bytecode    |
| ~/.m2/repository | Dependent JARs       |
| src/main/clojure | Clojure source       |
| src/test/clojure | Unit tests           |

## Functions

Functions are first-class abstraction in Clojure
  - Can be stored, passed as argument, invoked
  - fn create a function with named parameters and body
  - fn makes an anomymous function
  - Invoke a function with fn itself in function position
  - Store function in a named Var for later use
  - Invoke as list with name in function position

### Functions: Let
  - `let` allow us to bind symbols to immutable values (Values may be literals or expressions)
  - Bound symbols are available in lexical scope
  - local to the let

```clojure
(fn [message] (println message))

; Invocation
((fn [message] (println message)))

; named functions
(def messenger (fn [msg] (println msg)))
;; => #'user/messenger

; named function the first and the second are the same
(defn messenger [msg] (print msg))
;; => #'user/messenger

(messenger "Hello world!")
;; => Hello world!

(defn messenger [msg]
  (let [a 7
        b 5
        c (capitalize msg)]
    (println a b c)
  ) ; end of 'let' scope
) ; end of function
```

### Functions: Function Arity

- Can overload function by arity
  - Arity: number of arguments
- Each arity is a list ([args*] body*)
- One arity can invoke another


```clojure
;; Method overload
;; no args, call self with default msg
(defn messemger
;; no args, call self with default msg
  ([] (messenger "Hello world!"))

  ;; One arg, print it
  ([msg] (println msg)))

(messenger)
;; Hello world!

(messenger "Hello class!")
;; Hello class!

(defn greet
  ([name] greet "Hello" name)
  ([greeting name] (str greeting ", " name "!")))

(defn concat-strings [& strings]
  (apply str string))
;; (concat-strings "one" "two" "three") : => "onetwothree"
```

### Functions: Variadic functions

- Variadic: function of indefinite arity
  - Only one allowed when overloading gon arity
- & symbol is used in params
  - Next param collects all remaining args
  - Collected args represented as sequence

```clojure
(defn messenger [greeting & who]
  (println greeting who))

(messenger "Hello" "world" "class")
;; => Hello (world class)
```

### Functions: Apply

- Invokes function on arguments
- Final argument is a sequence
- "Unpacks" remaining arguments from sequence

```clojure
(let [a 1
      b 2
      more '(3 4)]
  (apply f a b more))

;; this invokes (f 1 2 3 4)
;; Unpack 3 and 4

;; & puts rest of args into sequence
(defn messenger [greeting & who]
  ;; apply gets args out of sequence
  (apply print greeting who))

(messenger "Hello" "world" "class")
;; Hello world class
```

### Closures

- fn 'closes' over surrounding lexical scope
  - creates a closure
- Closed-over references persist beyond lexical scope

```clojure
(defn messenger-builder [greeting]
  (fn [who] (print greeting who))) ; closes over greeting

;; greeting provided here, then goes out of scope
(def hello-er (messenger-builder "Hello"))

;; greeting still available because hello-er is closure
(hello-er "world!")
;; Hello world!
```

## Invoking Java Code

- `dot` special operator provides a way to invoke Java methods and access fields

| Task            | Java              | Clojure          |
| :----           | :------           | :-----           |
| Instantiation   | new Widget("Foo") | Widget."Foo"     |
| Instance method | rnd.nextInt()     | .nextInt rnd     |
| Instance field  | object.field      | (.-field object) |
| Static method   | Math.sqrt(25)     | (Math/sqrt 25)   |
| Static field    | Math.PI           | (Math/PI)        |

## Chaining Access

| Language | Syntax                              |
| : ----   | :--------------------               |
| Java     | `person.getAddress().getZipCode()`  |
| Clojure  | `(.. person getAddress getZipCode)` |

### Java Methods vs Functions

- Java methods are not Clojure functions
- Can't store them, pass them as arguments
- Can wrap them in functions when necessary

```clojure

;; make a function to invoke .length on arg.
(fn [obj] (.length obj))
```

## Terse fn reader macro

- Terse form #() for short fns defined inline
  - Single argument: %
  - Multiple args: %1, %2, %3, ...
  - Variadic: %& for remaining args

```
;; Invoke .length on arg
#(.length %)
```

## Names & Namespaces

### Why Namespaces

- Re-use common names in different contexts
  - e.g clojure.core/replace and clojure.string/replace
- Separate application "layers" or "components"
- Libraries
- Separate "public API" and "internal implementation"

### What is in a Namespace?

- Namespace scope
  - Vars
  - Keywords
  - Java type names
- Local, Lexical scope
  - Function arguments
  - let

### Namespace-Qualified Vars

```clojure
;; In the namespace "foo.bar"
(defn hello [] (println "Hello, world!"))

;; In another namespace
(foo.bar/hello) ; namespace-qualified
```

### Namespace-Qualified Keywords

Keywords can also be namespace-qualified

```clojure
;; In the namespace "foo.bar"
:x ; keyword with no namespace

::x ; Keyword with namespace "foo.bar"

:baz/x ; Keyword with namespace "baz"
```

### Namespaces in the REPL

- `in-ns` switches to namespace
  - Creates namespace if doesn't exist
- Argument is a symbol, must be quoted
- REPL always starts in namespace "user"

```clojure
user=> (in-ns 'foo.bar.baz)
;; nil
foo.bar.baz=>
```

## Namespace Operations

- Load: find source on classpath and eval it
- Alias: make a shorter name for namespace-qualified symbols
- Refer: copy symbol bindings from another namespace into current namespace
- Import: make Java class names available in current namespace

### require

- Loads the namespace if not already loaded
  - Argument is a symbol, must be quoted
- Have to refer to things with full-qualified names

```clojure
(require 'clojure.set)
;; nil

(clojure.set/union #{1 2} #{2 3 4})
;; #{1 2 3 4}
```

### require :as

- Loads the namespace if not already loaded
  - Argument is a vector, must be quoted
- Aliases the namespace to alternate name

```clojure
(require '[clojure.set :as set])
;; nil

;; "set" is an alias "clojure.set"
(set/union #{1 2} #{2 3 4})
;; #{1 2 3 4}
```

### `use`

- Loads the namespace if not already loaded
  - Argument is a symbol, must be quoted
- Refers all symbols into current namespace
- Warns when symbols clash
- Not recommended except for REPL exploration

```clojure
user=> (use 'clojure.string)
;; nil
user=> (reverse "hello")
;; "olleh"
```

### `use :only`

- Loads the namespace if not already loaded
  - Argument is a vector, must be quoted
- Refers only specified symbols into current namespace

```clojure
(use '[clojure.string :only (join)])
;; nil

(join "," [1 2 3])
;; "1, 2, 3"
```

### Reloading Namespaces

By default, namespaces are loaded only once
- use and require take optional flags to force reload

```clojure
;; Reload just the foo.bar namespace
(require 'foo.bar :reload)

;; Reload foo.bar and everything
;; reuired or used by foo.bar:
(require 'foo.bar :reload-all)
```

### `import`

- Makes Java classes available w/o package prefix in current namespace
  - Argument is a list, quoting is optional
- Does not support aliases/renaming
- Does not support Java's `import *`

```clojure
(import (java.io FileReader File))
;; nil

(FileReader. (File. "readme.txt"))
;; => #<FileReader ...>
```

### Namespaces and Files

- For require/use to work, clojure have to find code defining namespace
- Clojure converts namespace name to path and looks on CLASSPATH
  - `Dots` in namespace name become `/`
  - Hyphens become underscores
- Idiomatic to define namespace per file

### `ns` declaration

- Creates namespace and loads, aliases what you need
  - At top of file
- Refers all of `clojure.core`
- import all `java.lang`

```clojure
;; in file foo/bar/baz_quux.clj
(ns foo.bar.bax-quux)
```

### `ns :require`

- Loads other namespace with optional alias
  - Arguments are not quoted

```clojure
(ns my.cool.project
  (:require [some.ns.foo :as foo]))

(foo/function-in-foo)
```

### `ns :use`

- Loads other namespace and refers symbols into namespace
  - Arguments are not required

```clojure
(ns my.cool.project
  (:use [some.ns.foo :only (bar baz)]))

(bar) ; => (some.ns.foo/bar)
```


### `ns :import`

- Loads Java library and refers symbols into namespace
  - Arguments are not required

```clojure
(ns my.cool.project
  (:import (java.io File Writer)))

  (File) ; => java.io.File
```

### Namespaces: Private Vars

- Add `^:private` meta datato a definition
  - `defn-` is shortcut for `defn ^:private`
- Prevents automatic refer with `use`
- Prevents accidental reference by qualified symbol
- Not truly hidden: can work around

### `the-ns`

- Namespaces are first class objects
- But their names are not normal symbols

```clojure
clojure.core
;; ClassNotFoundException: clojure.core

(the-ns 'clojure.core)
;; #<Namespace clojure.core>
```

### Namespace Introspection

- `ns-name` : namespace name, as a symbol
- `ns-map` : map of all symbols
  - `ns-interns`: only def'd Vars
    - `ns-publics`: only public Vars
  - `ns-imports`: Only imported classes
- `ns-aliases`: map of all aliases
- `clojure.repl/dir`: print public Vars

## Collections

- Clojure provide extensive facilities for representing and manipulating data
- Small number of data structures
- `Seq` abstraction common across data structures & more
- Large library of functions across all of them

### Collections: Immutability

- The values of simple types are immutable
  - 4, 0.5, true
- In Clojure, the values of compound data structures are immutable too
  - Key to Clojure's concurrency model
- Code never changes values, it generates a new ones to refer to instead
- Persistent data structures ensure this is efficient in time and space

### Collections: Persistent Data Structures

- New values built from old values + modifications
- New values are not full copies
- Collection maintains its performance guarantees for most operations
- All Clojure data structures are persistent

- Linked List
- Bianary Tree

### Collection: Concrete Data Structures

- Sequential
  - List, Vector
- Associative
  - Map, Vector
- Both types support declarative desctructuring

### Collection: List

- Singly-linked list
- prepend: O(1)
- Lookup: O(1) at head, O(n) anywhere else

```clojure
()                ;=> empty list
(1 2 3)           ;=> error because 1 cannot be a function
(list 1 2 3)      ;=> (1 2 3)
'(1 2 3)          ;=> (1 2 3)
(conj '(2 3) 1)   ;=> (1 2 3)
;; conj add to a data structure, in the case of list
;; conj add to the front of the list (prepend)
```

### Collection: List

- Indexed, random-access, array-like
- prepend: O(1)
- Lookup: O(1)

```clojure
[]              ;=> empty vector
[1 2 3]         ;=> [1 2 3]
(vector 1 2 3)  ;=> [1 2 3]
(vec '(1 2 3))  ;=> [1 2 3]
(nth [1 2 3] 0) ;=> 1
(conj [1 2] 3)  ;=> [1 2 3]

;; Vector
;; conj append at the end of a vector
```

### Collection: Map

- key => value, hash table, dictionary
- Insert & Lookup: O(1)
- Unordered

```clojure
{}                  ;=> Empty map
{:a 1 :b 2}         ;=> {:a 1 :b 2}
(:a {a: 1 :b 2})    ;=> 1
({a: 1 :b 2} :a)    ;=> 1
(assoc {:a 1} :b 2) ;=> {:a 1 :b 2}
(dissoc {:a 1} :a)  ;=> {}
(conj {} [:a 1])    ;=> {:a 1}
```

### Collection: Nested Access

- Helper functions access data via path specified by keys

```clojure
(def jdoe {:name "John Doe" :address {:zip 27705}})

(get jdoe :name)
;;=> "John Doe"

(get jdoe :age 21)
;;=> Retuns default value if not found

(get-in jdoe [:address :zip]) ;=> 27705

(get-in jdoe [:address :stree] "Main St.") ;=> "Main St."

(assoc-in jdoe [:address :zip] 27745)
;;=> {:name "John Doe" :address {:zip 27745}}

(update-in jdoe [:address :zip] inc)
;;=> {:name "John Doe", :address {:zip 27706}}
```

### Collection: Set

- Set of distinct values
- Insert: O(1)
- Member?: O(1)
- Unordered

```clojure
#{}                   ;=> Empty set
#{:a :b}              ;=> {:a :b}
(#{:a :b} :a)         ;=> :a
(conj #{} :a)         ;=> #{:a}
(contains? #{:a} :a)  ;=> true

; clojure.set Examples
(require '[clojure.set :as set])
(set/union #{:a} #{:b})               ;=> #{:a :b}
(set/difference #{:a :b} #{:a})       ;=> {:b}
(set/intersection #{:a :b} #{:b :c})  ;=> #{:b}
```

## Collection: Destructuring

- Declarative way to pull apart compound data
  - vs. explicit, verbose access
- Works both on sequential and associative data structure
- Nests for deep, arbitrary access.
- Destructuring works in `fn` and `defn` params, `let` bindings
  - And anything built on top on them

## Sequential Destructuring

- Provide vector of symbols to bind by position
  - Binds to `nil`if there's no data
- Can get "everything else" with `&`
  - Value is a sequence
- Idiomatic to use `_` for values you don't care about

```clojure
(def stuff [7 8 9 10 11]) ;=> 'user/stuff

;; Bind a, b, c to first 3 values in stuff
(let [[a b c] stuff]
  (list (+ a b) (+ b c)))
;;=> (15 17)

(let [[a b c d e f] stuff]
  (list d e f))
;;=> (10 11 nil)

(let [[a & others] stuff]
  (println a)
  (println others))
;; 7
;; (8 9 10 11)
;; nil

(let [[_ & others] stuff] ; skip the first element
  (println others))
;; (8 9 10 11)
```

## Associative Destructuring

- Provide map of symbols to bind by key
  - Binds to nil if there's no value
- Keys can be infered from vector of symbols to bind
- Use `:or` to provide default values for bound keys

```clojure
(def m {:a 7 :b 4}) ;=> #'user/m

(let [{a :a, b :b} m]
  [a b])
;; [7 4]

(let [{:keys [a b a]} m]
  [a b c])
;; [7 4 nil]

(let [{:keys [a b c]
       :or {c 3}} m]
  ([a b c]))
;; [7 4 3]
```

### Named Argument

- Applying vector of keys to `&` binding emulates named args

```clojure
(defn game [planet & {:keys [human-players computer-players]}]
  (println "Total players: " (+ human-players computer-players)))

(game "Mars" :human-players 1 :computer-players 2)
;; Total players: 3
```

## Sequences

- Abstraction for representing iteration
- Backed by a data struture or a function
  - Can be lazy and/or 'infinite'
- Foundation for large library of functions

###  Sequences: Sequence API

- `(seq coll)`
  - If collection is non-empty, return `seq` object on it, else `nil`
  - Can't recover input from `seq`
- `(first coll)`
  - Returns first element
- `(rest coll)`
  - Returns a sequence of the rest of the elements
- `(cons x coll)`
  - Returns a new sequence: first is x, rest is coll

### Sequences over Structures

- Can treat any Clojure data struture as a seq
  - Associative strutures treated as sequence of pairs

```clojure
(def a-list '(1 2 3)) ;=> #'user/a-list
```

### Sequences over functions

- Can map a generator function to a `seq`
- `Seq` is lazy, can be infinite
  - Can process more than fits in memory

```clojure
(def a-range (range 1 5)) ;=> 'user/a-range
```

### Sequences in the REPL

- REPL always prints sequences with parens
  - But it's not a list
- Infinite sequences take a long time to print

```clojure
(set! *print-lenght* 10) ; only print 10 things
```

### Sequence Libraries

- Generators (Generate lazy sequences)
  - List, vector, map, SQL ResultSet, Stream, Directoty, Iterator, XML, ...
- Operations
  - map, filter, reduce, count, some, replace, ...
- Generators * Operations = Power!

```clojure
;; Creating a Sequence
(seq [1 2 3])                         ;=> (1 2 3) ; not a list
(range)                               ;=> (0 1 2 3 4 ....Infinite)
(range 3)                             ;=> (0 1 2)
(range 1 7 2)                         ;=> (1 2 5)
(iterate #(* 2 %) 2)                  ;=> (2 4 6 16 ...infinite)
(re-seq #"[aeiou]" "clojure")         ;=> ("o" "u" "e") ; Regex matches

;; Seq in, Seq out
(take 3 (range))                      ;=> (0 1 2)
(drop 3 (range))                      ;=> (3 4 5 ... infinite)
(map #(* % %) [0 1 2 3])              ;=> (0 1 4 9) ; vector treated as seq
(filter even? (range))                ;=> (0 2 4 6 ... infinite)
(apply str (interpose "," (range 3))) ;=> "0,1,2"

;; Using a Seq
(reduce + (range 4))                  ;=> 6
(reduce + 10 (range 4))               ;=> 16
(into #{} "hello")                    ;=> #{\e \h \l \o}
(into {} [[:x 1] [:y 2]])             ;=> {:x 1, :y 2}
(some {2 :b 3: c} {1 nil 2 3})        ;=> :b
```

## Flow Control

### Expressions in Clojure

- Everything in Clojure is an expression
  - Always returns a value
  - A block of multiple expressions returns the last value
    - e.g. `let`,`do`, `fn`
  - Expressions exclusively for side-effects returns `nil`

### Flow Control Expressions

- Flow control operators are expressions too
- Composable, can use them anywhere
  - Fewer intermediate variables
- Extensible, via macros
  - e.g `when-let`

### Truthiness


```clojure
(if true :truthy :falsey)
;; :truthy

(if (Object.) :truthy :falsey) ;Objects are true
;; :truthy

(if [] :truthy :falsey) ; empty  collections are true
;; :truthy

(if false :truthy :falsey)
;; :falsey

(if nil :truthy :falsey) ; nil is false
;; falsey

(if (seq []) :truthy :falsey) ; seq on empty coll is nil
;; :falsey

```

### If & If/do

- Multiple expressions per branch
- Last value in branch returned


```clojure
(str "2 is" (if even? 2) "even" "odd")
;; => 2 is even

; else-expression is optional
(if (true? false) "impossible")
;; nil

(if (even? 5)
(do (println "even")
true)
(do (println "odd")
false)) ;=> false
;; odd
```

### If-let

- Often want `let` as `if` branch, instead of `do`
- `if-let` combines the forms
- Only one binding from form allowed, tested for truthyness

```clojure
 (if-let [x (even? 3)]
  (println x)
  (println "some odd value"))
```

### Cond

- Series of tests  and expressions
- `:else` expression is optional

```clojure
(cond
  test1 expression1
  test2 expression2
  ...
  :else else-expression)

(let [x 5]
  (cond
    (< x 2) "x is less than 2"
    (< x 10) "x is less than 10"
    :else "x is greater than or equal to 10"))
;;=> "x is less than 10"

(let [x 11]
  (cond
    (< x 2) "x is less than 2"
    (< x 10) "x is less than 10"
    :else "x is greater than or equal to 10"))
;;=> "x is greather than or equal to 10"

```

### Condp

- `cond` with share prediacte

```clojure
(defn foo [x]
  (condp = x
    5 "x is 5"
    10 "x is 10"
    "x isn't 5 or 10"))

(foo 11) ;=> "x isn't 5 or 10"
```

### `case`

- Predicate always =
- Test-values must be compile-time literals
- Match is O(1)
- Else-expression has no test value

```clojure
(defn foo [x]
  (case x
    5 "x is 5"
    10 "x is 10"
    "x is not 5 or 10"))
(foo 11) ;=> x is not 5 or 10
```

### Recursion and Iteration

- Clojure provides `loop` and the sequence abstraction
- `loop` is "class" recursion
  - Closed to consumers, lower-level
- Sequences represent iteration as values
  - Consumers can partially iterate

### `doseq`

- Iterates over a sequence
  - Similar to Java's foreach loop
- if a lazy sequence, `doseq` forces evalution (not lazy)
- Use primarly for side-effects or operations that disc or memory

```clojure
(doseq [n (range 3)]
(println n))
;; 0
;; 1
;; 3
;; nil
```

### `doseq` with multiple bindings

- Similar to nested foreach loops
- Processes all permutations of sequence content

### `dotimes`

- Evaluate expression n times
- Use for side-effects

```clojure
(dotimes [i 3]
  (println 1))
;; 0
;; 1
;; 2
;; nil
```

### `while`

- Evaluate expression while condition is true

```clojure
(while (.accept socket)
  (handle socket))
```

### Clojure's for

- List comprehension, NOT a for-loop
- Generator function for sequence permutation

```clojure
(for [x [0 1]
      y [0 1]]
  [x y])
;; => ([0 0] [0 1] [1 0] [1 1]) ; seq
```

### loop/recur

- Functional looping construct
  - `loop` defines bindings
  - `recur` re-executes `loop` with new bindings
- Prefer high-order library fns

```clojure
(loop [i 0]
  (if (< i 10)
    (recur (inc i))
    i))
```
;; => 10

### defn/recur

- fn arguments are bindings

```clojure
(defn increase [i]
  (if (< i 10)
    (recur (inc i))
    i))

(increase 1) ;=> 10
```

### `recur` for recursion

- `recur` must be in "tail position"
  - The last expression in a branch
- `recur` must provide values for all bound symbols by position
  - Loop bindings
  - defn/fn args
- Recursion via `recur` does not comsume stack

### Exception handling

- `try/catch/finally`
  - as in Java

```clojure
(try
  (/ 2 1)
  (catch ArithmeticExcption e
    "divide by zero")
    (finally
      (println "cleanup")))
;; cleanup
;; 2
```

### Throwing exception

```clojure
(try
  (throw (Exception. "Something went wrong"))
  (catch Exception e (.getMessage e)))
;; => "Something went wrong"
```

### `with-open`

- JDK7 introduce try-with-resources
- Clojure provides `with-open` for similar purpose
- Automatically close file once operation has completed

```clojure
(require '[clojure.java.io :as io])

(with-open [f (io/writer "/tmp/new")]
  (.write f "some text"))
```


## Miscellaneous

Code inspection

```clojure
(dir clojure.java.io)
;; output public methods in namespace

(keys (ns-publics 'clojure.java.io))
;; output public methods as keys

(doc delete-file)
;; documentation

(source delete-file)
;; Show source from clojure

*ns* ; in REPL display current namespace
```
