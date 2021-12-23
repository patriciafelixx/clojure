(ns hospital.logic-test
  (:use clojure.pprint)
  (:require [clojure.test :refer :all]
            [hospital.logic :refer :all]
            [hospital.model :as h.model]
            [schema.core :as s]
            [clojure.test.check.clojure-test :refer (defspec)]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [schema-generators.generators :as g]
            [hospital.model :as model]))

(s/set-fn-validation! true)

(deftest cabe-na-fila?-test
  (testing "Que cabe na fila vazia"
    (is (cabe-na-fila? {:espera []}, :espera)))

  (testing "Que cabe em filas geradas aleatóriamente"
    (doseq [fila (gen/sample (gen/vector gen/string-alphanumeric 0 4))]
      (is (cabe-na-fila? {:espera fila} :espera))))

  (testing "Que cabe na fila"
    (is (cabe-na-fila? {:espera [1 2]}, :espera))
    (is (cabe-na-fila? {:espera [1 12 3 17]}, :espera)))

  (testing "Que não cabe na fila cheia"
    (is (not (cabe-na-fila? {:espera [3 7 9 15 19]}, :espera))))

  (testing "Que não cabe na fila mais do que cheia"
    (is (not (cabe-na-fila? {:espera [3 7 9 15 19 0]}, :espera))))

  (testing "Que não cabe na fila  quando o departamento não existir"
    (is (not (cabe-na-fila? {:espera [3 7 9 15 19 0]}, :x)))))

; --------------------------------------------------------------------------------------------------------
; defspec e prop/for-all

;(deftest chega-em-test
;  (testing "..."
;    (doseq [fila (gen/sample (gen/vector gen/string-alphanumeric 0 4))
;            pessoa (gen/sample gen/string-alphanumeric)]
;      (println pessoa fila))))

;; doseq n indicado para mais de uma parametro. aqui ele cruzou filas com pessoas, gerando 100 casos
;; vamos utilizar o defspec com o prop/for-all

(defspec explorando-api 10
         (prop/for-all
           [fila (gen/vector gen/string-alphanumeric 0 4)
            pessoa gen/string-alphanumeric]
           ;(println pessoa fila)
           true))

(defspec insere-pessoa-em-filas-menores-que-5 10
         (prop/for-all
           [fila (gen/vector gen/string-alphanumeric 0 4)
            pessoa gen/string-alphanumeric]
           (is (= {:espera (conj fila pessoa)}
                  (chega-em {:espera fila} :espera pessoa)))))

; --------------------------------------------------------------------------------------------------------

(def nome-aleatorio-gen
  (gen/fmap clojure.string/join
            (gen/vector gen/char-alphanumeric 5 10)))

(defn transforma-vetor-em-fila [vetor]
  (reduce conj h.model/fila-vazia vetor))

(def fila-nao-cheia-gen
  (gen/fmap transforma-vetor-em-fila
            (gen/vector nome-aleatorio-gen 0 4)))

;(defn transfere-ignorando-erro [hospital para]
;  (try
;    (transfere hospital :espera para)ł
;    (catch clojure.lang.ExceptionInfo e
;      (cond
;        (= :fila-cheia (:type (ex-data e))) hospital
;        :else (throw e)))))

(defn transfere-ignorando-erro [hospital para]
  (try
    (transfere hospital :espera para)
    (catch IllegalStateException e
      hospital)))

(defspec transfere-tem-que-manter-a-quantidade-de-pessoas 50
         (prop/for-all
           [espera (gen/fmap transforma-vetor-em-fila (gen/vector nome-aleatorio-gen 0 50))
            raio-x fila-nao-cheia-gen
            ultrasom fila-nao-cheia-gen
            vai-para (gen/vector (gen/elements [:raio-x :ultrasom]) 0 50)]
           (let [hospital-inicial {:espera espera, :raio-x raio-x, :ultrasom ultrasom}
                 hospital-final (reduce transfere-ignorando-erro hospital-inicial vai-para)]
             (is (= (total-de-pacientes hospital-inicial)
                    (total-de-pacientes hospital-final))))))

; --------------------------------------------------------------------------------------------------------

(defn adiciona-fila-de-espera [[hospital fila]]
  (assoc hospital :espera fila))

(def hospital-gen
  (gen/fmap
    adiciona-fila-de-espera
    (gen/tuple (gen/not-empty (g/generator h.model/Hospital))
               fila-nao-cheia-gen)))

(def chega-em-gen
  "Gerador de chegadas no hospital"
  (gen/tuple (gen/return chega-em)
             (gen/return :espera)
             nome-aleatorio-gen
             (gen/return 1)))

(defn adiciona-inexistente-ao-departamento [departamento]
  (keyword (str departamento "-inexistente")))

(defn transfere-gen [hospital]
  "Gerados de transferencias no hospital"
  (let [departamentos (keys hospital)
        departamentos-inexistentes (map adiciona-inexistente-ao-departamento departamentos)
        todos-os-departamentos (concat departamentos departamentos-inexistentes)]
    (gen/tuple (gen/return transfere)
               (gen/elements todos-os-departamentos)
               (gen/elements todos-os-departamentos)
               (gen/return 0))))

(defn acao-gen [hospital]
  (gen/one-of [chega-em-gen
               (transfere-gen hospital)]))

(defn acoes-gen [hospital]
  (gen/not-empty (gen/vector (acao-gen hospital) 1 100)))

(defn executa-uma-acao [situacao [funcao param1 param2 diferenca-se-sucesso]]
  (let [hospital (:hospital situacao)
        diferenca-atual (:diferenca situacao)]
    (try
      (let [hospital-novo (funcao hospital param1 param2)]
        {:hospital  hospital-novo
         :diferenca (+ diferenca-se-sucesso diferenca-atual)})
      (catch IllegalStateException e
        situacao)
      (catch AssertionError e
        situacao))))

(defspec simula-um-dia-do-hospital-nao-perde-pessoas 50
         (prop/for-all
           [hospital-inicial hospital-gen]
           (let [acoes (gen/generate (acoes-gen hospital-inicial))
                 situacao-inicial {:hospital hospital-inicial, :diferenca 0}
                 total-de-pacientes-inicial (total-de-pacientes hospital-inicial)
                 situacao-final (reduce executa-uma-acao situacao-inicial acoes)
                 total-de-pacientes-final (total-de-pacientes (:hospital situacao-final))]
             ;(println total-de-pacientes-final total-de-pacientes-inicial (:diferenca situacao-final))
             (is (= (- total-de-pacientes-final (:diferenca situacao-final)) total-de-pacientes-inicial)))))

; --------------------------------------------------------------------------------------------------------