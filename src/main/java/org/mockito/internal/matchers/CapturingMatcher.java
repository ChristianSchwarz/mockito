/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import java.io.Serializable;
import java.util.List;
import org.mockito.ArgumentMatcher;

@SuppressWarnings({ "unchecked", "serial" })
public class CapturingMatcher<T> implements ArgumentMatcher<T>, CapturesArguments, VarargMatcher, Serializable {
    
    private final List<T> arguments;

    public CapturingMatcher(List<T> arguments  ) {
        this.arguments = arguments;
    }
    
    @Override
    public boolean matches(Object argument) {
        return true;
    }    

    @Override
    public String toString() {
        return "<Capturing argument>";
    }

    @Override
    public void captureFrom(Object argument) {
        this.arguments.add((T) argument);
    }
}
