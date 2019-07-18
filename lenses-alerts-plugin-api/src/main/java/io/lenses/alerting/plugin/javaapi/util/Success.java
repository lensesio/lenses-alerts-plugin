/*
 * Copyright 2017-2019 Lenses.io Ltd
 */
package io.lenses.alerting.plugin.javaapi.util;

public class Success<T> implements Try<T> {
    public final T value;

    public Success(T value) {
        this.value = value;
    }
}
