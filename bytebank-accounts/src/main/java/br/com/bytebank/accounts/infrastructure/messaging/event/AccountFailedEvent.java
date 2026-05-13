package br.com.bytebank.accounts.infrastructure.messaging.event;

import java.util.UUID;

public record AccountFailedEvent(
        UUID customerId) {
}
