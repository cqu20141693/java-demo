### [spring graphql](http://docs.spring.io/spring-graphql/)

#### Schema

1. schema definition language (called SDL)

``` 
By default, GraphQL schema files are expected to be in src/main/resources/graphql 
and have the extension ".graphqls", ".graphql", ".gql", or ".gqls". You can customize
 the schema locations to check as follows:

spring.graphql.schema.locations=classpath:graphql/

The GraphQL schema can be viewed over HTTP at "/graphql/schema". This is not enabled by default:

spring.graphql.schema.printer.enabled=false
```

2. coding （比如 Java Code）

```
GraphQLObjectType fooType = newObject()
    .name("Foo")
    .field(newFieldDefinition()
            .name("bar")
            .type(GraphQLString))
    .build();
```

#### Web EndPoints

``` 
The GraphQL HTTP endpoint is at HTTP POST "/graphql" by default. The path can be customized:

spring.graphql.path=/graphql

The GraphQL WebSocket endpoint supports WebSocket handshakes at "/graphql" by default. The below
 shows the properties that apply for WebSocket handling:

spring.graphql.websocket.path=/graphql

# Time within which a "CONNECTION_INIT" message must be received from the client
spring.graphql.websocket.connection-init-timeout=60s

The GraphQL WebSocket endpoint is off by default. To enable it:

For a Servlet application, add the WebSocket starter spring-boot-starter-websocket.

For a WebFlux application, set the spring.graphql.websocket.path application property.

Declare a WebInterceptor bean to have it registered in theWeb Interception for GraphQL over HTTP and WebSocket requests.

Declare a ThreadLocalAccessor bean to assist with the propagation of ThreadLocal values of interest in WebMvc.

```

#### GraphiQL

``` 
The Spring Boot starter includes a GraphiQL page that is exposed at "/graphiql" by default.
 You can configure this as follows:

spring.graphql.graphiql.enabled=true
spring.graphql.graphiql.path=/graphiql
```

#### Metrics

``` 
When the starter spring-boot-starter-actuator is present on the classpath, metrics for GraphQL
requests are collected. You can disable metrics collection as follows:

management.metrics.graphql.autotime.enabled=false

Metrics can be exposed with an Actuator web endpoint. The following sections assume that its
exposure is enabled in your application configuration, as follows:

management.endpoints.web.exposure.include=health,metrics,info
```