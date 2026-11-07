package com.codenova.vigilia.shared.application.patterns;

import java.util.function.Function;

/**
 * Outcome of an operation: a success value or a typed failure.
 */
public sealed interface Result<T, E> permits Result.Success, Result.Failure {

    record Success<T, E>(T value) implements Result<T, E> {}

    record Failure<T, E>(E error) implements Result<T, E> {}

    static <T, E> Result<T, E> success(T value) {
        return new Success<>(value);
    }

    static <T, E> Result<T, E> failure(E error) {
        return new Failure<>(error);
    }

    default boolean isSuccess() {
        return this instanceof Success<T, E>;
    }

    default <R> R fold(Function<? super T, ? extends R> onSuccess, Function<? super E, ? extends R> onFailure) {
        return switch (this) {
            case Success<T, E> success -> onSuccess.apply(success.value());
            case Failure<T, E> failure -> onFailure.apply(failure.error());
        };
    }
}