# Clojure CLI Tools notes

`deps.edn` is a configuration file using extensible data notation (edn),
the language that is used to define the structure of Clojure itself.

Configuration is defined using a map with top-level keys for `:deps`, `:paths`, and `:aliases`
and any provider-specific keys for configuring dependency sources
(e.g. GitHub, GitLab, Bitbucket).

`~/.clojure/deps.edn` is used for global configurations that you wish to
apply to all the projects you work with

`project-directory/deps.edn` is for project specific settings

## Show configuration settings

Display which config file is used in current context

`clojure -Sdescribe`

## Show paths

```bash
clj -Spath | tr ':' '\n' | sort

clj -A:test -Spath | tr ':' '\n' | sort

clj -A:test -R:runner -Spath | tr ':' '\n' | sort
```
### Run Liquid and Rebl together

`clj -A:liquid -R:rebel`

## Launch clojure with verbose information

Show the version of Clojure CLI tools used before it runs a REPL.
`clj -Sverbose`

## Sample project deps.edn

```clojure
;; deps.edn
{:paths   ["src/main/clojure"]
 :deps    {org.clojure/clojure {:mvn/version "1.10.1"}}
 :aliases {:test     {:extra-paths ["test/main/clojure"]
                      :extra-deps  {lambdaisland/kaocha {:mvn/version "0.0-529"}}
                      :main-opts   ["-m" "kaocha.runner"]}
           :outdated {:extra-deps {olical/depot {:mvn/version "1.8.4"}}
                      :main-opts  ["-m" "depot.outdated.main" "-a" "outdated"]}
           :uberjar  {:extra-deps {uberdeps {:mvn/version "0.1.4"}}
                      :main-opts  ["-m" "uberdeps.uberjar" "--target" "target/cdeps-0.1.0.jar"]}}}
```

## Common commands

`clj -Aoutdated`

`clj -Atest`

## Using clj-new library

[clj-new](https://github.com/seancorfield/clj-new) is a tool to generate new projects from its own small set of templates.
You can also create your own clj-new templates.
It is also possible to generate projects from Leiningen or Boot templates, however, this does not create a deps.edn file for Clojure CLI tools, it just creates the project as it would from either Leiningen or Boot.


`clj-new` currently has the following built-in templates:

`app` – a deps.edn project with sample code, tests and the congnitect test runner, clj -A:test:runner.
	This project includes `:gensys` directive, so can be run as an application on the command line via `clj -m`

`lib` – the same as the app template, but without the `:gensys` directive as this is mean to be a library.

`template` – the basis for creating your own templates.


Add `clj-new` as an alias in your `~/.clojure/deps.edn` like this:

```clojure
{
:aliases
 {:new {:extra-deps {seancorfield/clj-new
                     {:mvn/version "0.7.6"}}
        :main-opts ["-m" "clj-new.create"]}}
}
```

Create a Clojure CLI tools project using the clj-new app template

```bash
clj -A:new app myname/myapp
cd myapp
clj -m myname.myapp
```

The app template creates a couple of tests to go along with the sample code.
We can use the cognitec test runner to run these tests using the `:test` alias

`clj -A:test:runner`


## figwheel-main
Use the [figwheel-main](https://github.com/bhauman/figwheel-main-template) template to create a project for a simple Clojurescript project, optionally with one or reagent, rum or om libraries.

```bash
clj -A:new figwheel-main hello-world.core -- --reagent # or --rum, --om, --react or nothing
```
or

```bash
lein new figwheel-main hello-world.core -- --reagent # or --rum, --om, --react or nothing
```
## How to Effectively Use Deps & CLI?

In a clojure project, these are the common commands during development

```bash
cd <path>/api
clj -A:test:runner
clj -A:uberjar api.jar
clj -A:deploy api.jar <user>@<server>
```
