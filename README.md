# obj-case [![CircleCI](https://circleci.com/gh/hden/obj-case.svg?style=svg)](https://circleci.com/gh/hden/obj-case)

Work with objects of different cased keys.

Inspired by [segmentio/obj-case](https://github.com/segmentio/obj-case)

## Installation

```project.clj
[hden/obj-case "0.1.0"]
```

```deps.edn
{hden/obj-case {:mvn/version "0.1.0"}}
```

## Usage

### get-in

```clojure
(require '[obj-case.core :as obj])

(def input
  {:my {"super_cool" {"climbingShoes" "x"}}})

(obj/get-in input ["my" "superCool" "CLIMBING-SHOES"]) ;; "x"
```

## Ideas

* `assoc-in`
* `dissoc-in`
* `update-in`

## License

Copyright © 2021 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
