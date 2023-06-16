package com.gatling.loadtest;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.CoreDsl;
import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;

import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.core.CoreDsl.css;
import static io.gatling.javaapi.core.CoreDsl.feed;
import static io.gatling.javaapi.core.CoreDsl.rampUsers;
import static io.gatling.javaapi.core.CoreDsl.repeat;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.core.CoreDsl.ssv;
import static io.gatling.javaapi.core.CoreDsl.tryMax;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;


/**
 * This sample is based on our official tutorials:
 * <ul>
 *   <li><a href="https://gatling.io/docs/gatling/tutorials/quickstart">Gatling quickstart tutorial</a>
 *   <li><a href="https://gatling.io/docs/gatling/tutorials/advanced">Gatling advanced tutorial</a>
 * </ul>
 */
public class LoadTest extends Simulation {

    HttpProtocolBuilder httpProtocol =
        http.baseUrl("http://0.0.0.0:8080")
            .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
            .acceptLanguageHeader("en-US,en;q=0.5")
            .acceptEncodingHeader("gzip, deflate")
            .userAgentHeader(
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0"
            );

    FeederBuilder<String> post_feeder = ssv("com/gatling/resources/load-test-post.csv").random();

    ChainBuilder post =
        tryMax(2).on(
            feed(post_feeder)
                .exec(session -> session.set("bodyVal", "#{BODY}"))
                .exec(
                    http("Post")
                        .post("/#{PATH}")
                        .header(HttpHeaderNames.CONTENT_TYPE, String.valueOf(HttpHeaderValues.APPLICATION_JSON))
                        .body(StringBody("#{BODY}"))
                        .check(
                            status().is(201)
                        )
                )
                .pause(1)
                .exec(
                    http("Validate Post")
                        .get("/#{VALIDATE_PATH}")
                        .check(
                            CoreDsl.bodyString().isEL("#{BODY}")
                        )
                )
                .exitHereIfFailed()
        );

    FeederBuilder<String> get_feeder = ssv("com/gatling/resources/load-test-get.csv").random();

    // repeat is a loop resolved at RUNTIME
    ChainBuilder browse =
        // Note how we force the counter name, so we can reuse it
        repeat(2).on(
            feed(get_feeder)
            .exec(
                http("list employees")
                    .get("/#{PATH}")
                    .check(status().is(200))
            ).pause(1)
        );

    ScenarioBuilder users = scenario("Users").exec(post);
    //ScenarioBuilder admins = scenario("Admins").exec(search, browse, edit);

    {
        setUp(
            users.injectOpen(rampUsers(2).during(4))
            //admins.injectOpen(rampUsers(2).during(10))
        ).protocols(httpProtocol);
    }
}
