A simple repo to demonstrate the issue discussed [here](https://clojurians.slack.com/archives/C03S1KBA2/p1667925853699669)

This can be deleted whenever it is convenient.

To reproduce:

- `clojure -X:run-x` - Runs OK.
- Launch a REPL and load the one and only ns. Works OK.
- `clojure -M:eastwood` - Fails with message stating `okhttp3.CookieJar` class
  not found.

It looks like there is a code path in snowplow that isn't used when you use the
Apache http client, but is still picked up by eastwood when it loads classes for
inspection.