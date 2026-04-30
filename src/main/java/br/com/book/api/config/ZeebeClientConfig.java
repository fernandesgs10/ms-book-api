package br.com.book.api.config;

import io.camunda.zeebe.client.ZeebeClient;
import io.quarkus.arc.Unremovable;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

import java.util.logging.Logger;

@ApplicationScoped
public class ZeebeClientConfig {

    private static final Logger LOG = Logger.getLogger(ZeebeClientConfig.class.getName());

    @Produces
    @ApplicationScoped
    @Unremovable
    public ZeebeClient zeebeClient() {

        String gatewayAddress = System.getenv().getOrDefault("ZEEBE_ADDRESS", "zeebe:26500");

        LOG.info("🔥 Creating ZeebeClient with address: " + gatewayAddress);

        return ZeebeClient.newClientBuilder()
                .gatewayAddress(gatewayAddress)
                .usePlaintext() // importante pra docker/local
                .build();
    }
}