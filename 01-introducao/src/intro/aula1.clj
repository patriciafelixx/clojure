(ns intro.aula1)

(println "Bem vindo ao sistema de estoque!")

(def total-de-produtos 15)

(println total-de-produtos)

(println "Total: " total-de-produtos)

(def total-de-produtos 13)

(+ total-de-produtos 3)

(def total-de-produtos (+ total-de-produtos 3))

(def estoque ["Mochila", "Camiseta" ])

(println estoque)

(estoque 0)

(estoque 1)

(count estoque)

(conj estoque "Cadeira")

(def estoque (conj estoque "Cadeira"))

;----------------------------------------------------------------------------------------------------
(defn imprime-mensagem []
  (println "-----------------------")
  (println "Bem vindo!"))

(imprime-mensagem)

;----------------------------------------------------------------------------------------------------
(defn aplica-desconto [valor-bruto]
  (* valor-bruto 0.9))

(aplica-desconto 100.00)

;----------------------------------------------------------------------------------------------------
(defn valor-descontado
  "Retorna o valor com desconto de 10%"
  [valor-bruto]
  (* valor-bruto (- 1 0.1)))
; (* valor-bruto 0.9)


;----------------------------------------------------------------------------------------------------