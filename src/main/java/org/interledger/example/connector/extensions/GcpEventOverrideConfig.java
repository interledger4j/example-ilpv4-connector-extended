package org.interledger.example.connector.extensions;

import org.interledger.connector.events.PacketFullfillmentEvent;
import org.interledger.connector.events.PacketRejectionEvent;
import org.interledger.connector.gcp.GcpPacketResponseEventPublisher;
import org.interledger.connector.server.spring.settings.SpringConnectorConfig;
import org.interledger.connector.settings.ConnectorSettings;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import java.util.function.Supplier;

/**
 * Example of a Component that gets picked up and overrides a default bean implementation
 */
@Configuration
@ConditionalOnProperty(value = "spring.cloud.gcp.pubsub.enabled", havingValue = "true")
@Import(SpringConnectorConfig.class)
public class GcpEventOverrideConfig {

  @Bean
  @Primary // must be primary to override the default GcpPacketResponseEventPublisher
  GcpPacketResponseEventPublisher myGcpPacketResponseEventPublisher(
    @Value("${example.message:hello world}") String message,
    Supplier<ConnectorSettings> connectorSettingsSupplier
    ) {
    return new GcpPacketResponseEventPublisher() {
      @Override
      public void publish(PacketFullfillmentEvent event) {
        System.out.println("Fulfillment says " + message + " from " + connectorSettingsSupplier.get().operatorAddress());
      }

      @Override
      public void publish(PacketRejectionEvent event) {
        System.out.println("Rejection says " + message + " from " + connectorSettingsSupplier.get().operatorAddress());
      }
    };
  }

}
