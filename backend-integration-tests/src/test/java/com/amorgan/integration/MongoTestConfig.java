package com.amorgan.integration;

import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.mongo.transitions.Mongod;
import de.flapdoodle.embed.mongo.transitions.RunningMongodProcess;
import de.flapdoodle.reverse.TransitionWalker;
import de.flapdoodle.reverse.transitions.ImmutableStart;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoTestConfig {

    private TransitionWalker.ReachedState<RunningMongodProcess> running;

    @PostConstruct
    public void startMongo() {
        final Net network = Net.of("127.0.0.1", 27017, false);

        this.running = Mongod.instance()
                .withNet(ImmutableStart.to(Net.class)
                        .initializedWith(network))
                .start(Version.Main.V5_0);
    }

    @PreDestroy
    public void stopMongo() {
        if (this.running != null) {
            this.running.close();
        }
    }
}