(ns bling.core
  (:require [reagent.core :as r]))

#_(comment
    TODOS
    =====

    Increase code quality.

    Add function descriptions.

    Comment the code.

    Handle argument "mixins" in function "create-component-class" .

    Decide whether we should use some proper protocol
    stead of the "create-component-class" with lots of arguments.

    Asyncronous controller logic is not handled at all yet (see also next point)

    Make the controller method REALLY pure, even if the controller
    will call some IO stuff (access to web storage, ajax requests etc.)
    => some monadic approach?

    Find a better solution for those "dispatch" and "publish" callbacks.
    )

(def *debug-all* false)

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

                                (let [old-state @state-atom
                                      controller-result (controller @state-atom event)
                                      new-state (retrieve-new-state controller-result)
                                      new-events (retrieve-new-events controller-result)]
                                     ;; Prints out a debug information if we are in debug mode
                                     (if (or debug *debug-all*)
                                       (print (str "\n\n=== STATE TRANSITION ========="
                                                   "\n--- old state ----------------\n"
                                                   old-state
                                                   "\n\n--- new state ----------------\n"
                                                   (if (= old-state new-state)
                                                     "(no changes)"
                                                     new-state)
                                                   "\n\n--- event --------------------\n"
                                                   event
                                                   (when (and (not-empty debug) (not= debug true))
                                                         (str "\n\n--- info ---------------------\n"
                                                              debug))
                                                   "\n===============================\n\n")))
                                     (reset! state-atom new-state)
                                     (publish-events new-events publish)))]
                  dispatch))))

(defn- retrieve-new-state [controller-result]
       (if (vector? controller-result)
         (get controller-result 0)
         controller-result))

(defn- retrieve-new-events [controller-result]
       (if (vector? controller-result)
         (get controller-result 1)
         controller-result))

(defn- publish-events [events publish]
       (let [seq (if (seq? events)
                   events
                   [events])]
            (doseq [event seq]
                   (publish event))))
