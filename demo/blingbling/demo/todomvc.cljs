(ns blingbling.demo.todomvc
  (:require [blingbling.core :as bling]))


(defrecord ^:eport Todo
  [id text completed?])

(def ^:eport filters
  {:all (fn [] true)
   :active (complement :completed?)
   :completed :completed?})


(defn todomvc-controller [event  state]
  (let [[event-key arg arg2] event
        find-index-by (fn [coll id f] (first (keep-indexed #(if (= id (f %2)) %1) coll))) ;; TODO
        inc-next-id #(update % :next-id inc)]
    ;; (print (str "-----> "  arg " : " (:todos state) "|" (keep-indexed #(if (= arg (:id %2)) %1) (:todos state)) "|")); (find-index-by-id (:todos state) 2)))
    (case
      event-key

      :set-filter
      (assoc  state :active-filter arg)

      :clear-completed-todos
      (update  state :todos
               #(vec (filter (complement :completed?) %)))

      :set-todo-completed
      (assoc-in  state [:todos (+ 0(find-index-by-id (:todos state) arg)) :completed?] arg2) ;; TODO!!!!

      :set-todo-completed-for-all
      (update  state :todos
               #(mapv
                 (fn [todo]
                   (assoc todo :completed? arg)) %))

      :add-todo
      (inc-next-id (update state :todos
                           #(conj % (->Todo (:next-id state) arg false))))

      :set-todo-text
      (assoc-in  state [:todos arg :text] arg2)

      :remove-todo
      (update  state :todos
               #(vec
                 (filter (fn [todo] (not= (:id todo)  arg)) %))))))

(defn todomvc-view [props state dispatch]
  (let [todos (:todos state)
        active-filter (:active-filter state)
        visible-todos (filter (active-filter filters) todos)
        total-todo-count (count todos)
        active-todo-count (->> todos (filter (complement :completed?)) count)]

    [:section.todoapp
     [:header.header
      [:h1 "todos"]
      [:input.new-todo {:placeholder "What needs to be done?" :autofocus true :on-key-down #(if (= (.-keyCode %) 13)
                                                                                             (let [value (.-target.value %)]
                                                                                               (set! (.-target.value %) "")
                                                                                               (dispatch [:add-todo value])))}]]
     [:section.main
      [:input.toggle-all {:type "checkbox" :checked (zero? active-todo-count) :on-change #(dispatch [:set-todo-completed-for-all (.-target.checked %)])}]
      [:label {:for "toggle-all"} "Mark all as complete" ]
      [:ul.todo-list
       (map-indexed
         (fn [index todo] ^{:key index} [todomvc-item {:todo todo :emit dispatch}])
         visible-todos)]]
     [:footer.footer
      [:span.todo-count
       [:strong active-todo-count] " item left"]
      [:ul.filters
       [:li
        [(if (= active-filter :all) :a.selected :a) {:on-click #(dispatch [:set-filter :all]) :href "#"} "All"]
        [(if (= active-filter :active) :a.selected :a) {:on-click #(dispatch [:set-filter :active]) :href "#"} "Active"]
        [(if (= active-filter :completed) :a.selected :a) {:on-click #(dispatch [:set-filter :completed]) :href "#"} "Completed"]]]
      (if (< active-todo-count total-todo-count)
        [:button.clear-completed {:on-click #(dispatch [:clear-completed-todos])} "Clear completed"])
      [:footer.info
       [:p " Double-click to edit a todo"]
       [:p " Created by " [:a {:href "http://www.google.com"} "Ralf"]]
       [:p " Part of " [:a {:href "http://todomvc.com"} "TodoMVC"]]]]]))


(def todomvc (bling/create-component-class todomvc-view todomvc-controller {:todos [(->Todo 0 "Todo 1", false), (->Todo 1 "Todo 2", true),(->Todo 2 "Todo 3", false)] :active-filter :all :next-id 3}))

;; --------------------------------------

(defn todomvc-item-view [props state dispatch]
  (let [todo (:todo props)
        id (:id todo)
        emit (:emit props)
        class (str (if (:completed? todo) "completed" "active") (if (:in-edit-mode? state) "-editing editing"))
        toggle-edit-mode #(dispatch [:toggle-edit-mode])
        update-todo-text #(emit [:update-todo-text id (.-target.value %)])]

    [:li {:key id :class class}
     [:div.view
      [:input.toggle {:type "checkbox" :checked (:completed? todo) :on-change #(emit [:set-todo-completed id (.-checked (.-target %))])}]
      [:label {:on-double-click toggle-edit-mode} (:text todo)]
      [:button.destroy {:on-click #(emit [:remove-todo id])}]]
     [:input.edit {:type "text"
                   :value (:text todo)
                   :on-change update-todo-text
                   :on-key-down #(if (= (.-keyCode %) 13) (toggle-edit-mode))
                   :on-blur toggle-edit-mode}]]))

(defn todomvc-item-controller [event state dispatch]
  (let [[event-key arg] event]
    (case event-key
      :toggle-edit-mode
      (update state :in-edit-mode? #(not %))

      :update-todo-text
      (dispatch [:set-todo-text id arg]))))

(def todomvc-item (bling/create-component-class todomvc-item-view todomvc-item-controller {:in-edit-mode? false}))

;; --------------------------------------

(enable-console-print!)

(def initial-state
  {:todos
                  [(->Todo 0 "Todo 1" false)
                   (->Todo 1 "Todo 2" true)
                   (->Todo 2 "Todo 3" false)]
   :active-filter :active
   :next-id 3})

(defn ^:export start []
  (bling/mount-component [:div [todomvc :state initial-state][todomvc :state initial-state][todomvc :state initial-state][todomvc :state initial-state][todomvc :state initial-state][todomvc :state initial-state][todomvc :state initial-state][todomvc :state initial-state][todomvc :state initial-state][todomvc :state initial-state][todomvc :state initial-state][todomvc :state initial-state][todomvc :state initial-state][todomvc :state initial-state][todomvc :state initial-state][todomvc :state initial-state][todomvc :state initial-state][todomvc :state initial-state][todomvc :state initial-state][todomvc :state initial-state][todomvc :state initial-state][todomvc :state initial-state][todomvc :state initial-state][todomvc :state initial-state][todomvc :state initial-state][todomvc :state initial-state][todomvc :state initial-state][todomvc :state initial-state] [todomvc :state initial-state]] "root"))
