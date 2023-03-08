(ns metabase.snowplow-eastwood-issue
  (:gen-class)
  (:import
   (com.snowplowanalytics.snowplow.tracker Snowplow)
   (com.snowplowanalytics.snowplow.tracker.configuration EmitterConfiguration NetworkConfiguration TrackerConfiguration)
   (com.snowplowanalytics.snowplow.tracker.http ApacheHttpClientAdapter)
   (org.apache.http.client.config CookieSpecs RequestConfig)
   (org.apache.http.impl.client HttpClients)
   (org.apache.http.impl.conn PoolingHttpClientConnectionManager)))

(set! *warn-on-reflection* true)

(defonce tracker
         (let [request-config      (-> (RequestConfig/custom)
                                       ;; Set cookie spec to `STANDARD` to avoid warnings about an invalid cookie
                                       ;; header in request response (PR #24579)
                                       (.setCookieSpec CookieSpecs/STANDARD)
                                       (.build))
               client              (-> (HttpClients/custom)
                                       (.setConnectionManager (PoolingHttpClientConnectionManager.))
                                       (.setDefaultRequestConfig request-config)
                                       (.build))
               http-client-adapter (ApacheHttpClientAdapter. "http://localhost:9090" client)
               network-config      (NetworkConfiguration. http-client-adapter)
               tracker-config      (TrackerConfiguration. "my-namespace" "snowplow-eastwood-app-id")
               emitter-config      (-> (EmitterConfiguration.) (.batchSize 1))]
           (Snowplow/createTracker
            ^TrackerConfiguration tracker-config
            ^NetworkConfiguration network-config
            ^EmitterConfiguration emitter-config)))

(defn -main
  [& _args]
  (print tracker))
