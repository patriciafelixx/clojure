(ns hospital.aula3
  (:use clojure.pprint)
  (:require [schema.core :as s]))

(s/set-fn-validation! true)

; --------------------------------------------------------------------------------------------------
; SCHEMAS COMPOSTOS

; Validação para "positivo inteiro"
(def PosInt (s/pred pos-int? 'positive-integer))

; Schema Paciente
(def Paciente
  {:id   PosInt,
   :nome s/Str})

; Função que cria novo paciente
(s/defn novo-paciente :- Paciente
  [id :- PosInt, nome :- s/Str]
  {:id id, :nome nome})

(pprint (novo-paciente 15 "Guilherme"))
;=> {:id 15, :nome "Guilherme"}

;(pprint (novo-paciente -5 "Guilherme"))
;=> error [(named (not (positive-integer -5)) id) nil]

; ------------------------------------------------------
; Função de verificação - true/false
(defn maior-ou-igual-a-zero? [x] (>= x 0))

; Validação para a verificação de "maior ou igual a zero"
;(def MaiorOuIgualAZero (s/pred maior-ou-igual-a-zero?))

; Validação para valor financeiro
(def ValorFinanceiro (s/constrained s/Num maior-ou-igual-a-zero?))

; Schema Pedido
(def Pedido
  {:paciente     Paciente,
   :valor        ValorFinanceiro,
   :procedimento s/Keyword})

; será que faz sentido "mini-schema" como aliases?
;(def Procedimento s/Keyword)

; Função que cria novo pedido
(s/defn novo-pedido :- Pedido
  [paciente :- Paciente, valor :- ValorFinanceiro, procedimento :- s/Keyword]
  {:paciente     paciente,
   :valor        valor,
   :procedimento procedimento})

(pprint (novo-pedido (novo-paciente 15, "Guilherme"), 175.00, :raio-x))
;{:paciente {:id 15, :nome "Guilherme"},
; :valor 175.0,
; :procedimento :raio-x}

; --------------------------------------------------------------------------------------------------
; SCHEMAS DE SEQUÊNCIAS

; Validação para os tipos dos dados dentro de vetores
(def Numeros [s/Num])
(pprint (s/validate Numeros [15, 3, 0, -1, 5.5]))
(pprint (s/validate Numeros []))
(pprint (s/validate Numeros nil))

;(pprint (s/validate Numeros ["15"]))
;=> error [(not (instance? java.lang.Number "15"))]

; ------------------------------------------------------
; Validação para os procedimentos dentro do vetor
(def Plano [s/Keyword])

; Schema Paciente redefinido
(def Paciente
  {:id    PosInt,
   :nome  s/Str
   :plano Plano})

(pprint (s/validate Paciente {:id 15, :nome "Vanessa", :plano [:raio-x, :ultrasom]}))
(pprint (s/validate Paciente {:id 16, :nome "José", :plano []}))
(pprint (s/validate Paciente {:id 17, :nome "Maria", :plano nil}))

;(pprint (s/validate Paciente {:id 18, :nome "João"}))
;=> error {:plano missing-required-key}

; --------------------------------------------------------------------------------------------------