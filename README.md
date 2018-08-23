# regraf

A Graphql subscription example with re-graph on the client,
lacinia-pedestal on the server.

## Development Mode

### Run server:

```
lein repl
(start)
```
A server with Graphiql should start at http://localhost:8888

### Build client:

```
lein figwheel dev
```

Figwheel will automatically push cljs changes to the browser.

Wait a bit, then browse to [http://localhost:3449](http://localhost:3449).
