package br.com.bytebank.accounts.infrastructure.messaging.event;

import java.util.UUID;


public record AccountOpenedEvent(UUID customerId, UUID accountId) {}