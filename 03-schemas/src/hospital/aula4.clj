(ns hospital.aula4
  (:use clojure.pprint)
  (:require [schema.core :as s]))

(s/set-fn-validation! true)

; --------------------------------------------------------------------------------------------------
; MAPAS DINÂMICOS

; Chave opcional dentro de um schema
(def PosInt (s/pred pos-int? 'positive-integer))
(def Plano [s/Keyword])
(def Paciente
  {:id                          PosInt,
   :nome                        s/Str
   :plano                       Plano
   (s/optional-key :nascimento) s/Str})

(pprint (s/validate Paciente {:id 15, :nome "Guilherme", :plano [:raio-x, :ultrasom]}))
(pprint (s/validate Paciente {:id 15, :nome "Guilherme", :plano [], :nascimento "18/9/1981"}))


; Utilizar mapa para ter as chaves dinâmicas
(def Pacientes
  {PosInt, Paciente})

(let [guilherme {:id 15, :nome "Guilherme", :plano [:raio-x]},
      daniela {:id 20, :nome "Daniela", :plano []}]
  (pprint (s/validate Pacientes {15, guilherme}))
  (pprint (s/validate Pacientes {20, daniela})))
  ;(pprint (s/validate Pacientes {-15, guilherme})) => (not (positive-integer -15))
  ;(pprint (s/validate Pacientes {15, 15})) => (not (map? 15))
  ;(pprint (s/validate Pacientes {15, {:id 1, :nome "X"}})) => :plano missing-required-key


; Schema Visitas
(def Visitas
  {PosInt [s/Str]})

; --------------------------------------------------------------------------------------------------