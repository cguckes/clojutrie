# clojutrie

A Clojure library implementing a simple keyword search trie.

[![Clojars Project](https://img.shields.io/clojars/v/ctg/clojutrie.svg)](https://clojars.org/ctg/clojutrie)

https://cljdoc.org/d/ctg/clojutrie/1.0.0/doc/readme

## Disclaimer

I don't claim this library to be more efficient or better than anyone else could achieve. It just does what I need it to do. If you see some code that you think could be improved, please feel free to submit a pull request.

## Usage

```clojure
(ns core
  (:require [clojutrie.core :as ct]))

; Insert into an empty trie
(def trie1 (ct/insert (ct/empty-trie) "hi there" :any-value-you-want))
=> {:value #{},
    \h {:value #{},
        \i {:value #{},
            \space {:value #{},
                    \t {:value #{},
                        \h {:value #{},
                            \e {:value #{},
                                \r {:value #{}, \e {:value #{:any-value-you-want}}}}}}}}}}

; Add second key
(def trie2 (ct/insert trie1 "hi yourself" :any-other-value))
=> {:value #{},
    \h {:value #{},
        \i {:value #{},
            \space {:value #{},
                    \t {:value #{},
                        \h {:value #{},
                            \e {:value #{},
                                \r {:value #{}, \e {:value #{:any-value-you-want}}}}}},
                    \y {:value #{},
                        \o {:value #{},
                            \u {:value #{},
                                \r {:value #{},
                                    \s {:value #{},
                                        \e {:value #{},
                                            \l {:value #{},
                                                \f {:value #{:any-other-value}}}}}}}}}}}}}

; Add second value to existing key
(def trie3 (ct/insert trie2 "hi there" 12345))
=> {:value #{},
    \h {:value #{},
        \i {:value #{},
            \space {:value #{},
                    \t {:value #{},
                        \h {:value #{},
                            \e {:value #{},
                                \r {:value #{},
                                    \e {:value #{:any-value-you-want 12345}}}}}},
                    \y {:value #{},
                        \o {:value #{},
                            \u {:value #{},
                                \r {:value #{},
                                    \s {:value #{},
                                        \e {:value #{},
                                            \l {:value #{},
                                                \f {:value #{:any-other-value}}}}}}}}}}}}}

; Search trie for values
(ct/search trie3 "hi there")
=> #{:any-value-you-want 12345}

; Search for non-existing values:
(ct/search trie3 "hi man")
=> #{}

; Get prefix based suggestions for search terms
(ct/prefix-search trie3 "hi")
=> #{"hi there" "hi yourself"}

```

## License

Copyright © 2020 Christopher Thonfeld-Guckes

This program and the accompanying materials are made available under the terms
of the MIT License which is available at
http://opensource.org/licenses/MIT.
