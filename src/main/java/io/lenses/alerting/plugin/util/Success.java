package io.lenses.alerting.plugin.util;

public class Success<R> implements Try<R> {
    public final R value;

    public Success(R value) {
        this.value = value;
    }
}
