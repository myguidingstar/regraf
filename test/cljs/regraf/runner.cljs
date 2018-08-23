(ns regraf.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [regraf.core-test]))

(doo-tests 'regraf.core-test)
