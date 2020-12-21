# clojutrie

A Clojure library implementing a simple keyword search trie.

## Usage

```clojure
(ns core
  (:require [clojutrie.core :as ct]))

; Insert into an empty trie
(def trie1 (ct/insert nil "hi there" :any-value-you-want))
=> {\h {\i {\space {\t {\h {\e {\r {\e {:value #{:any-value-you-want}}}}}}}}}}

; Add second key
(def trie2 (ct/insert trie1 "hi yourself" :any-other-value))
=> {\h
    {\i
     {\space
      {\t {\h {\e {\r {\e {:value #{:any-value-you-want}}}}}},
       \y
          {\o {\u {\r {\s {\e {\l {\f {:value #{:any-other-value}}}}}}}}}}}}}

; Add second value to existing key
(def trie3 (ct/insert trie2 "hi there" 12345))
=> {\h
    {\i
     {\space
      {\t {\h {\e {\r {\e {:value #{:any-value-you-want 12345}}}}}},
       \y
          {\o {\u {\r {\s {\e {\l {\f {:value #{:any-other-value}}}}}}}}}}}}}

; Search trie for values
(ct/search trie3 "hi there")
=> #{:any-value-you-want 12345}

; Search for non-existing values:
(ct/search trie3 "hi man")
=> nil

; Get prefix based suggestions for search terms
(ct/prefix-search trie "hi")
=> #{"hi there"
     "hi yourself"}

```

## License

Copyright © 2020 Christopher Thonfeld-Guckes

This program and the accompanying materials are made available under the terms
of the MIT License which is available at
http://opensource.org/licenses/MIT.