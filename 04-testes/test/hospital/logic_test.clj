(ns hospital.logic-test
  (:use clojure.pprint)
  (:require [clojure.test :refer :all]
            [hospital.logic :refer :all]
            [hospital.model :as h.model]
            [schema.core :as s]))

(s/set-fn-validation! true)

; --------------------------------------------------------------------------------
; Boundary tests (testes exatamente nas bordas e one off. -1, +1, <=, >=, =)

(deftest cabe-na-fila?-test
  (testing "Que cabe na fila vazia"
    (is (cabe-na-fila? {:espera []}, :espera)))

  (testing "Que cabe na fila"
    (is (cabe-na-fila? {:espera [1 2]}, :espera))
    (is (cabe-na-fila? {:espera [1 12 3 17]}, :espera)))

  (testing "Que não cabe na fila cheia"
    (is (not (cabe-na-fila? {:espera [3 7 9 15 19]}, :espera))))

  (testing "Que não cabe na fila mais do que cheia"
    (is (not (cabe-na-fila? {:espera [3 7 9 15 19 0]}, :espera))))

  (testing "Que não cabe na fila  quando o departamento não existir"
    (is (not (cabe-na-fila? {:espera [3 7 9 15 19 0]}, :x)))))

; --------------------------------------------------------------------------------
;(deftest chega-em-test
;  (testing "Aceita pessoas enquanto cabem na fila"
;    (is (= {:espera [1 2 3 4 5]} (chega-em {:espera [1 2 3 4]}, :espera, 5)))
;    (is (= {:espera [1 2 5]} (chega-em {:espera [1 2]}, :espera, 5))))
;
;  (testing "Não aceita pessoas quando não cabem na fila"
;
;    ; código clássico horrível, com uma exception jogada, genérica.
;    (is (thrown? clojure.lang.ExceptionInfo) (chega-em {:espera [1 2 3 4 5]}, :espera, 6))
;
;    ; mesmo escolhendo um exception do genero, ainda perigoso!
;    (is (thrown? IllegalStateException (chega-em {:espera [1 2 3 4 5]}, :espera, 6)))
;
;    ; outra abordagem do nil! mas o perigo do swap teríamos que trabalhar a condição de erro em algum outro ponto.
;    (is (nil? (chega-em {:espera [1 2 3 4 5]}, :espera, 6)))
;
;    ; outra maneira de testar, com try catch, onde usa-se os dados da exception para entender o erro.
;    ; menos sensivel que a mensagem de erro (mesmo que usasse regex), mas ainda ehf uma validacao trabalhosa
;    (is (try
;    (chega-em {:espera [1 2 3 4 5]}, :espera, 76)
;    false
;    (catch clojure.lang.ExceptionInfo e
;      (= :impossivel-colocar-pessoa-na-fila (:tipo (ex-data e))))))))
;
; --------------------------------------------------------------------------------
;(deftest chega-em-test
;  (let [hospital-cheio
;        {:espera [1 35 42 64 21]}]
;
;    (testing "Aceita pessoas enquanto cabem  na fila"
;      (is (= {:hospital {:espera [1 2 3 4 5]}, :resultado :sucesso} (chega-em {:espera [1 2 3 4]}, :espera, 5)))
;      (is (= {:hospital {:espera [1 2 5]}, :resultado :sucesso} (chega-em {:espera [1 2]}, :espera, 5))))
;
;    (testing "Não aceita pessoas quando não cabem na fila"
;      ;(is (= {:hospital {:espera [1 2 3 4 5]}, .....}
;      (is (= {:hospital hospital-cheio, :resultado :impossivel-colocar-pessoa-na-fila}
;             (chega-em hospital-cheio, :espera 76))))))
;
; --------------------------------------------------------------------------------
(deftest chega-em-test
  (let [hospital-cheio
        {:espera [1 35 42 64 21]}]

    (testing "Aceita pessoas enquanto cabem  na fila"
      (is (= {:espera [1 2 3 4 5]} (chega-em {:espera [1 2 3 4]}, :espera, 5)))
      (is (= {:espera [1 2 5]} (chega-em {:espera [1 2]}, :espera, 5))))

    (testing "Não aceita pessoas quando não cabem na fila"
      (is (thrown? clojure.lang.ExceptionInfo (chega-em hospital-cheio, :espera, 76))))))

; --------------------------------------------------------------------------------
(deftest transfere-test

  (testing "Quando é possível fazer a transferência"

    ;(let [hospital {:espera [51 5], :raio-x [13]}]
    ;  (is (= {:espera [5], :raio-x [13, 51]} (transfere hospital :espera :raio-x))))
    ;=> bug devido comportamento de pop em vetores - ideal usar listas ↓

    (let [hospital {:espera (conj h.model/fila-vazia "51" "5"), :raio-x (conj h.model/fila-vazia "13")}]
      (is (= {:espera ["5"],
              :raio-x ["13", "51"]}
             (transfere hospital :espera :raio-x))))

    (let [hospital {:espera (conj h.model/fila-vazia "5"), :raio-x h.model/fila-vazia}]
      (is (= {:espera [],
              :raio-x ["5"]}
             (transfere hospital :espera :raio-x)))))

  (testing "Quando NÂO é possível fazer a transferência"
    (let [hospital {:espera (conj h.model/fila-vazia "12"), :raio-x (conj h.model/fila-vazia "2" "23" "57" "14" "20")}]
      (is (thrown? clojure.lang.ExceptionInfo
                   (transfere hospital :espera :raio-x)))))

  (testing "condições obrigatórias"
    (let [hospital {:espera (conj h.model/fila-vazia "5"), :raio-x (conj h.model/fila-vazia "1" "54" "43" "12")}]
      (is (thrown? AssertionError
                   (transfere hospital :nao-existe :raio-x)))
      (is (thrown? AssertionError
                   (transfere hospital :raio-x :nao-existe))))))

; ----------------------------------------------------------------------------