(ns blingbling.core
  (:require [reagent.core :as r]))

(enable-console-print!)

;; Sorry - no documention yet :-(

(defn create-component-class [view controller initial-state]
  (do
    (assert (fn? view))
    (assert (fn? controller))

    (fn [] (let
      [state-atom
       (r/atom initial-state)
       dispatch
       #(let
         [
         new-state (controller % @state-atom dispatch)]
         (print "----------")
         (do (print @state-atom)
         (print new-state))
         (reset! state-atom new-state))]
      (fn [props] (view props @state-atom dispatch))))))


(defn mount-component [component target]
  (let [dom-element (if (string? target) (.getElementById js/document target) target)]
    (r/render-component component dom-element)))

;;;; -----------------------------------------------------------------

;;(ns nanoreact.core)

