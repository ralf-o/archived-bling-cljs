(ns blingbling.core
  (:require [reagent.core :as r]))

(defn create-component-class
  [view controller initial-state mixins]
  {:pre
   [(fn? view)
    ((or nil? fn?) controller)
    ((or nil? associative?) mixins)]}

  (fn [& args]
    (let
      [children (if (associative? (first args)) (rest args) args)
       state (if (fn? initial-state) (initial-state) initial-state)
       state-atom (r/atom state)
       dispatcher-factory (obtain-dispatcher-factory controller state-atom)]
      (print stat)
      (fn [props]
        (vec
          (concat
            (view props @state-atom (dispatcher-factory props))
            children))))))


(defn mount-component [component target]
  (let [dom-element (if (string? target) (.getElementById js/document target) target)]
    (r/render-component component dom-element)))

;; --- private --------------------------------------------

(defn- obtain-publisher [props]
  (let [publish (:=> props)]
    (if (fn? publish) publish (fn [_] nil))))

(defn- obtain-dispatcher-factory [controller state-atom]
  (fn [props]
    (if
      (nil? controller)
      (fn [_] nil)
      (let [publish (obtain-publisher props)
            ;; This is just for debugging purposes (activates the "debug mode")
            debug (:!!! props)
            dispatch (fn [event]
                       (let [new-state (controller event @state-atom dispatch publish)]
                         ;; Prints out a debug information if you are in debug mode
                         (if debug
                           (print (str "\n\n=== STATE TRANSITION ========="
                                       "\n--- old state ----------------\n"
                                       @state-atom
                                       "\n\n--- new state ----------------\n"
                                       (if (= @state-atom new-state)
                                         "(no changes)"
                                         new-state)
                                       "\n\n--- event --------------------\n"
                                       event
                                       (when (not= debug true)
                                         (str "\n\n--- info ---------------------\n"
                                              debug "\n"))
                                       "===============================\n\n")))
                         (reset! state-atom new-state)))]
        dispatch))))
