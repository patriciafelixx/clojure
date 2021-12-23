(ns hospital.core
  (:use clojure.pprint)
  (:require [clojure.test.check.generators :as gen]
            [schema-generators.generators :as g]
            [hospital.model :as model]))

;; --------------------------------------------------------------------------------------------------------
;; clojure.test.check.generators
;(println (gen/sample gen/boolean 3))
;(println (gen/sample gen/small-integer 100))
;(println (gen/sample gen/string))
;(println (gen/sample gen/string-alphanumeric 5))
;
;(println (gen/sample (gen/vector gen/small-integer) 5))
;(println (gen/sample (gen/vector gen/small-integer 3) 5))
;(println (gen/sample (gen/vector gen/small-integer 1 3)))
;
;(println (gen/sample (gen/vector gen/string-alphanumeric 0 4)))
;
;; --------------------------------------------------------------------------------------------------------
;; prismatic/schema-generators
;(pprint (g/sample 5 model/Paciente))
;(pprint (g/sample 5 model/Departamento))
;(pprint (g/sample 5 model/Hospital))
;
;(pprint (g/generate model/Hospital))
;(pprint (g/generator model/Hospital))
;
;; --------------------------------------------------------------------------------------------------------