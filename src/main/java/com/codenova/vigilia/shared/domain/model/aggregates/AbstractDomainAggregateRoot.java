package com.codenova.vigilia.shared.domain.model.aggregates;

import org.springframework.data.domain.AbstractAggregateRoot;
/**
 * Base class for domain aggregate roots
 */
public abstract class AbstractDomainAggregateRoot<T extends AbstractDomainAggregateRoot<T>>
        extends AbstractAggregateRoot<T> {

    protected void registerDomainEvent(Object event) {
        super.registerEvent(event);
    }
}