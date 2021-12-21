(ns hospital.core
  (:use [clojure.pprint])
  (:require [hospital.model :as model]))

(let [hospital-core (model/novo-hospital)]
  (pprint hospital-core))