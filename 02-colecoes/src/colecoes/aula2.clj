(ns colecoes.aula2)

(defn conta [total-ate-agora elementos]
  (println total-ate-agora elementos)
  (conta (inc total-ate-agora) (rest elementos)))

;(println (conta 0 ["daniela", "guilherme", "carlos", "paulo", "lucia", "ana"]))
;0 [daniela guilherme carlos paulo lucia ana]
;1 (guilherme carlos paulo lucia ana)
;2 (carlos paulo lucia ana)
;3 (paulo lucia ana)
;4 (lucia ana)
;5 (ana)
;6 ()
;7 ()
;(...)
; -> ERRO! Faltou acrescentar o else para quando finalizar a contagem

(defn conta [total-ate-agora elementos]
  (println total-ate-agora elementos)
  (if (next elementos)
    (recur (inc total-ate-agora) (next elementos))
    total-ate-agora))

(println (conta 0 ["daniela", "guilherme", "carlos", "paulo", "lucia", "ana"]))
;0 [daniela guilherme carlos paulo lucia ana]
;1 (guilherme carlos paulo lucia ana)
;2 (carlos paulo lucia ana)
;3 (paulo lucia ana)
;4 (lucia ana)
;5 (ana)
;5
; -> ERRO! a contagem final deveria retornar 6

(defn conta [total-ate-agora elementos]
  (println total-ate-agora elementos)
  (if (seq elementos)
    (recur (inc total-ate-agora) (next elementos))
    total-ate-agora))

;(println (conta 0 ["daniela", "guilherme", "carlos", "paulo", "lucia", "ana"]))
;0 [daniela guilherme carlos paulo lucia ana]
;1 (guilherme carlos paulo lucia ana)
;2 (carlos paulo lucia ana)
;3 (paulo lucia ana)
;4 (lucia ana)
;5 (ana)
;6 nil
;6 -> OK, AGORA SIM!

(println (conta 0 []))
;0 []
;0 -> OK!

;----------------------------------------------------------------------------------------------------
;Múltiplas variações de uma mesma função por aridade distinta
(defn conta
  ([elemetos]
   (conta 0 elemetos))

  ([total-ate-agora elementos]
   (println total-ate-agora elementos)
   (if (seq elementos)
     (recur (inc total-ate-agora) (next elementos))
     total-ate-agora)))

(println (conta ["X", "Y", "Z"]))
;0 [X Y Z]
;1 (Y Z)
;2 (Z)
;3 nil
;3 -> OK!

;----------------------------------------------------------------------------------------------------
; Loop

(defn conta
  [elementos]
  (loop [total-ate-agora 0, elementos-restantes, elementos]
    (if (seq elementos-restantes)
      (recur (inc total-ate-agora) (next elementos-restantes))
      total-ate-agora)))

(println (conta ["um", "dois", "tres"]))
; 3

;----------------------------------------------------------------------------------------------------