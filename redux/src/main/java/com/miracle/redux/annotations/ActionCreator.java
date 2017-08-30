package com.miracle.redux.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate interface with actions creators.
 * <p>
 * <p>
 * Example:
 * <pre><code>
 * &#64;ActionCreator
 * interface CounterActions {
 *     String ACTION_ADD = "ACTION_ADD";
 *     String ACTION_INCREMENT = "ACTION_INCREMENT";
 *
 *     &#64;ActionCreator.Action(ACTION_ADD)
 *     Action add(int valueToAdd);
 *
 *     &#64;ActionCreator.Action(ACTION_INCREMENT)
 *     Action increment();
 * }
 * </code></pre>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ActionCreator {
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface Action {
        String value();
    }
}
