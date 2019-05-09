package io.lenses.alerting.plugin.util;

public class Failure<R> implements Try<R> {
    public final Throwable exception;

    public Failure(Throwable exception) {
        this.exception = exception;
    }
}
