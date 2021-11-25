(ns intro.aula3)

;----------------------------------------------------------------------------------------------------
;PREDICATE
(defn aplica-desconto?
  [valor-bruto]
  (if (> valor-bruto 100)
    true
    false))

;----------------------------------------------------------------------------------------------------
;pode-se usar when no lugar do if, sem precisar colocar condição para o else
;ou pode-se colocar a versao direta (mais indicado):
(defn aplica-desconto?
  [valor-bruto]
  (> valor-bruto 100))

(defn valor-descontado
  [valor-bruto]
  ;(if (> valor-bruto 100)
  (if (aplica-desconto? valor-bruto)
    (let [taxa-de-desconto (/ 10 100),
          desconto (* valor-bruto taxa-de-desconto)]
      (- valor-bruto desconto))
    valor-bruto))

;----------------------------------------------------------------------------------------------------
(defn maior-que-cem?
  [valor]
  (> valor 100))

(defn valor-descontado
  [aplica? valor-bruto]
  (if (aplica? valor-bruto)
    (let [taxa-desconto 0.1,
          desconto (* valor-bruto taxa-desconto)]
      (- valor-bruto desconto))))

(println "FUNÇÃO COMO PARÂMETRO")
(println (valor-descontado  maior-que-cem? 1000))
(println (valor-descontado maior-que-cem? 100))

;----------------------------------------------------------------------------------------------------

(defn valor-descontado
  [aplica? valor-bruto]
  (if (aplica? valor-bruto)
    (let [taxa-desconto 0.1,
          desconto (* valor-bruto taxa-desconto)]
      (- valor-bruto desconto))))

(println "FUNÇÃO ANÔNIMA")
(println (valor-descontado  (fn [valor] (> valor 100)) 1000))
(println (valor-descontado  (fn [valor] (> valor 100)) 100))

(println (valor-descontado  (fn [v] (> v 100)) 1000))
(println (valor-descontado  (fn [v] (> v 100)) 100))

(println (valor-descontado #(> %1 100) 1000))
(println (valor-descontado #(> %1 100) 100))

(println (valor-descontado #(> % 100) 1000))
(println (valor-descontado #(> % 100) 100))

;----------------------------------------------------------------------------------------------------