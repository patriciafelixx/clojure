(ns hospital.aula2
  (:use clojure.pprint)
  (:require [schema.core :as s]))

(s/set-fn-validation! true)

; --------------------------------------------------------------------------------------------------
; SCHEMAS

(def Paciente
  "Schema de um paciente"
  {:id s/Num, :nome s/Str})

(pprint (s/explain Paciente))
(pprint (s/validate Paciente {:id 15, :nome "guilherme"}))

; Typo é pego pelo schema...
; mas poderiamos argumentar que esse tipo de erro seria pego em testes automatizados com cobertura boa;

; mas.... entra a questão de QUERER ser forward compatible OU NÃO. Entender esse trade-off
; sistemas externos não quebrarão o meu sistema ao adicionar campos novos (foward compatible)
; no nosso validate não estamos sendo forward compatible (pode ser interessante quando quero analisar mudanças)

; keywords em schemas são por padrão OBRIGATÓRIAS!

; tipo de retorno com schema força a validação na saída da função.

(s/defn novo-paciente :- Paciente
  [id :- s/Num, nome :- s/Str]
  {:id id, :nome nome})

(pprint (novo-paciente 15 "Guilherme"))

;------------------------------------------------------------------------------------------

; função pura, simples, facil de testar
(defn estritamente-positivo? [x]
  (> x 0))

;(def EstritamentePositivo (s/pred estritamente-positivo?))
(def EstritamentePositivo (s/pred estritamente-positivo? 'estritamente-positivo))

(pprint (s/validate EstritamentePositivo 15))

;(pprint (s/validate EstritamentePositivo 0))
;(pprint (s/validate EstritamentePositivo -15))
;=> error (not (estritamente-positivo 0))

;------------------------------------------------------------------------------------------

(def Paciente
  "Schema de um paciente"
  (pprint (s/validate Paciente {:id 15, :nome "guilherme"}))

  ; Um caminho não recomendado: lambdas dentro dos schemas.
  ; Os nomes ficam confusos ou a legibilidade do schema se perde.
  ; Além da perda da facilidade de testar o lambda isoladamente

  (def Paciente
    "Schema de um paciente"
    {:id   (s/constrained s/Int #(> % 0) 'inteiro-estritamente-positivo),
     :nome s/Str})
  (pprint (s/validate Paciente {:id 15, :nome "guilherme"}))

  ;(pprint (s/validate Paciente {:id -15, :nome "guilherme"}))
  ;(pprint (s/validate Paciente {:id 0, :nome "guilherme"}))

  {:id (s/constrained s/Int pos?), :nome s/Str})
; já existe "pos?" e já existe "pos-int?" Dica: sempre debulhar documentação!

;------------------------------------------------------------------------------------------