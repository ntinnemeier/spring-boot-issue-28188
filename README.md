# Simple program to reproduce Spring Boot issue 28188

For more info see: https://github.com/spring-projects/spring-boot/issues/28188

## Program description

This is a simple Spring Boot application for reproducing a memory leak. To do so, this application:
- continuously sends messages with random `id` to `httpbin.org/response-headers?key={id}` using a `WebClient`. 
  The `WebClient` uses a URI builder for constructing a parameterised URL.
- has a dependency with two micrometer metrics libraries: prometheus and statsd
- has http client metrics turned off (`management.metrics.enable.http.client.requests = false` in application.properties)

## How to reproduce

- Run the application in your favorite IDE and wait for it to be fully started
- After a few seconds put a breakpoint on [this line](https://github.com/micrometer-metrics/micrometer/blob/main/micrometer-core/src/main/java/io/micrometer/core/instrument/MeterRegistry.java#L607) of the `MeterRegistry`

*Expected*: Meters for the http client metrics are not accepted as per the `MeterFilter` for `management.metrics.enable.http.client.requests = false`

*Actual*: Meters for the http client metrics are accepted, because the `MeterFilter` pertaining to http client metrics is ignored.

To see the memory leak, inspect the contents of `MeterRegistry.meterMap` (from a debugging session or a heap dump) and see it clogged with 
http client micrometer timers.

When you exclude either the statsd or prometheus dependency in the `pom.xml` and repeat the steps above, you'll see that the `MeterFilter` that 
disallows http client metrics is applied. Consequenlty, the `MeterRegistry.meterMap` is not filled up with http client micrometer timers.