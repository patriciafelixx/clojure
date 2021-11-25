(ns intro.aula2)

(defn valor-descontado
  "Retorna o valor com desconto de 10%"
  [valor-bruto]
  (let [desconto 0.10]
    (* valor-bruto (- 1 desconto))))

(class 90.0)  ; => java.lang.Double
(class 90N)   ; => clojure.lang.BigInt
(class 90M)   ; => java.math.BigDecimal

;----------------------------------------------------------------------------------------------------
(defn valor-descontado
  "Retorna o valor com desconto de 10%"
  [valor-bruto]
  (let [taxa-de-desconto (/ 10 100)
        desconto (* valor-bruto taxa-de-desconto)]
    (println "Calculando desconto de" desconto)
    (- valor-bruto desconto)))

;----------------------------------------------------------------------------------------------------
(defn valor-descontado
  "Retorna o valor com desconto de 10%"
  [valor-bruto]
  (if (> valor-bruto 100)
    (let [taxa-de-desconto 0.1,
           desconto (* valor-bruto taxa-de-desconto)]
      (println "Calculando desconto de" desconto)
      (- valor-bruto desconto))
    valor-bruto))

(valor-descontado 900)

;----------------------------------------------------------------------------------------------------