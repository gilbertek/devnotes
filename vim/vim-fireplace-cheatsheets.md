# VIM Fireplace cheatsheets

Normal mode commands to help navigating/evaluating Clojure code in the repl

- `cpp` - Evaluate the s-expression and print the results
- `K`   - Print the Clojure documentation for the symbol under the cursor
- `cqc` - Open a mini-repl for executing arbitrary Clojure code
- `cqq` - Open a mini-repl, with the current S-expression pre-entered
- `[d`  - Print the Clojure source for the symbol under the cursor
- `cmm` - Expand a macro
- `:Eval` <clojure code> - Executes arbitrary Clojure code and prints the result
- gf - gf command go to file, works on namespaces.

## Combine with the [vim salve plugin](https://github.com/tpope/vim-salve)

- Autoconnect fireplace.vim to the REPL, or autostart it with `:Console`
- `:Console` command to start a REPL or focus an existing instance if already running using dispatch.vim.
- `:A` - Opens the test file for the given source file in the current buffer (and vice versa). AV/AS/AT are similar but are vsplit, split, and tab respectively.
- `:Apropos` <keyword> - Searches the current class path for symbols which match the keyword
