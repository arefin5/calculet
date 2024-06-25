(ns luminapp.routes.login
  (:require
    [luminapp.layout :as layout]
    [luminapp.db.core :as db]
    [luminapp.middleware :as middleware]
    [ring.util.response :refer [redirect]]
    [ring.util.http-response :as response]
    [struct.core :as st]
    [ring.middleware.json :refer [wrap-json-body wrap-json-response]]))

(defn- login-page [request]
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body {:message "Login API"}})

(defn- authenticate [{:keys [params]}]
  (let [user (db/get-user params)]
    (if (= (:password params) (:password user))
      {:status 200
       :headers {"Content-Type" "application/json"}
       :body {:message "Login successful"
              :userid (:userid params)
              :name (:name user)}}
      {:status 401
       :headers {"Content-Type" "application/json"}
       :body {:message "Invalid username or password"}})))

(defn- register-page [request]
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body {:message "Register API"}})

(defn- register [{:keys [params]}]
  (db/create-user! params)
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body {:message "Registration successful"}})

(defn login-routes []
  [""
   ["/login" {:post authenticate}]
   ["/register" {:get register-page
                 :post register}]
   ["/logout" {:get (fn [_] {:status 200
                             :headers {"Content-Type" "application/json"}
                             :body {:message "Logged out successfully"}})}]])

(def app
  (-> (login-routes)
      (wrap-json-body)
      (wrap-json-response)))
