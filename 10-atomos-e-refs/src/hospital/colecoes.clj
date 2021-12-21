(ns hospital.colecoes)

(defn testa-vetor []
  (let [espera [111 222]]
    (println "TESTA VETOR")
    (println espera)
    (println (conj espera 333))
    (println (conj espera 444))
    (println (pop espera))))
(testa-vetor)

(defn testa-lista []
  (let [espera '(111 222)]
    (println "TESTA LISTA")
    (println espera)
    (println (conj espera 333))
    (println (conj espera 444))
    (println (pop espera))))
(testa-lista)

(defn testa-conjunto []
  (let [espera #{111 222}]
    (println "TESTA CONJUNTO")
    (println espera)
    (println (conj espera 333))
    (println (conj espera 444))
    ;(println (pop espera)) => error
    ))
(testa-conjunto)

(defn testa-fila []
  (let [espera (conj clojure.lang.PersistentQueue/EMPTY 111 222)]
    (println "TESTA FILA")
    (println (seq espera))
    (println (seq (conj espera 333)))
    (println (seq (pop espera)))
    (println (peek espera))))
(testa-fila)



