(ns intro.aula4)

(def precos [30 700 1000])

(println (precos 0))
;(println (precos 17))
(println (get precos 0))
(println (get precos 2))
(println (get precos 17))
(println (get precos 17 0))
(println (get precos 1 0))
;Loading
;30
;IndexOutOfBoundsException
;30
;1000
;nil
;0
;700

(println (conj precos 5))
(println precos)
;[30 700 1000 5]
;[30 700 1000]

(println (update precos 0 inc))
;[31 700 1000]

(println (update precos 1 dec))
;[30 699 1000]

(defn soma-5
  [valor]
  (println "Somando 5 ao valor" valor)
  (+ valor 5))

(println (update precos 1 soma-5))
;Somando 5 ao valor 700
;[30 705 1000]

;----------------------------------------------------------------------------------------------------
;CÓDIGO DA AULA ANTERIOR

(defn aplica-desconto?
  [valor-bruto]
  (> valor-bruto 100))

(defn valor-descontado
  [valor-bruto]
  (if (aplica-desconto? valor-bruto)
    (let [taxa-de-desconto 0.1,
          desconto (* valor-bruto taxa-de-desconto)]
      (- valor-bruto desconto))
    valor-bruto))

;----------------------------------------------------------------------------------------------------
; MAP

(println "precos:" precos)
(println "precos com desconto:" (map valor-descontado precos))
;precos: [30 700 1000]
;precos com desconto: (30 630.0 900.0)

;----------------------------------------------------------------------------------------------------
; FILTER

(println (range 10))
(println (filter even? (range 10)))
;(0 1 2 3 4 5 6 7 8 9)
;(0 2 4 6 8)

(println "precos:" precos)
(println "precos que serão aplicados desconto" (filter aplica-desconto? precos))
(println "apenas precos com desconto:" (map valor-descontado (filter aplica-desconto? precos)))
;precos: [30 700 1000]
;precos que serão aplicados desconto (700 1000)
;apenas precos com desconto: (630.0 900.0)

;----------------------------------------------------------------------------------------------------
; REDUCE

(println "precos:" precos)
(println "precos somados:" (reduce + precos))
;precos: [30 700 1000]
;precos somados: 1730

(defn minha-soma
  [valor1 valor2]
  (println "somando" valor1 "e" valor2)
  (+ valor1 valor2))

(println "total:" (reduce minha-soma precos))
;somando 30 e 700
;somando 730 e 1000
;total: 1730

(println "total, com valor extra:" (reduce minha-soma 10 precos))
;somando 10 e 30
;somando 40 e 700
;somando 740 e 1000
;total, com valor extra: 1740

;----------------------------------------------------------------------------------------------------