/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.invocation;


import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.LocalizedMatcher;
import org.mockito.internal.progress.ArgumentMatcherStorage;
import org.mockito.invocation.Invocation;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.internal.exceptions.Reporter.invalidUseOfMatchers;
import static org.mockito.internal.invocation.ArgumentsProcessor.equalsMatcherOf;
import static org.mockito.internal.invocation.ArgumentsProcessor.equalsMatchersOf;
import static org.mockito.internal.invocation.MatchersBinder.MatcherMarkerValues.isMarkerValue;
import static org.mockito.internal.util.Primitives.primitiveTypeOf;

@SuppressWarnings("unchecked")
public class MatchersBinder implements Serializable {

    public InvocationMatcher bindMatchers(ArgumentMatcherStorage argumentMatcherStorage, Invocation invocation) {
        List<LocalizedMatcher> lastMatchers = argumentMatcherStorage.pullLocalizedMatchers();

        List<ArgumentMatcher> matchers = toMatchers(invocation, lastMatchers);

        return new InvocationMatcher(invocation, matchers);
    }

    private List<ArgumentMatcher> toMatchers(Invocation invocation, List<LocalizedMatcher> localizedMatchers) {
        LinkedList<ArgumentMatcher> matchers = toMatchers(localizedMatchers);


        int matcherCount = matchers.size();
        int argumentCount = invocation.getArguments().length;

        if (matcherCount == 0) {
            return equalsMatchersOf(invocation.getArguments());
        }

        if (matcherCount == argumentCount) {
            return matchers;
        }

        return completeMatchers(invocation, matchers, localizedMatchers);


    }

    private List<ArgumentMatcher> completeMatchers(Invocation invocation, LinkedList<ArgumentMatcher> matchers, List<LocalizedMatcher> localizedMatchers) {
        List<ArgumentMatcher> completedMatchers = new LinkedList<ArgumentMatcher>();
        Class<?>[] paramTypes = invocation.getMethod().getParameterTypes();
        Object[] paramValues = invocation.getRawArguments();

        ArgumentMatcher<?> currentMatcher = matchers.pollFirst();

        for (int i = 0; i < paramTypes.length; i++) {


            Class<?> currentParamType = paramTypes[i];
            Object currentParamValue = paramValues[i];

            if (isMarkerValue(currentParamType, currentParamValue)) {
                if (currentMatcher == null) {
                    //more arguments that matchers found
                    throw invalidUseOfMatchers(invocation.getArguments().length, localizedMatchers);
                }
                completedMatchers.add(currentMatcher);
                currentMatcher = matchers.pollFirst();
            } else {
                //
                completedMatchers.add(equalsMatcherOf(currentParamValue));

            }

        }

        if (!matchers.isEmpty()) {
            //more matchers that arguments were specified
            throw invalidUseOfMatchers(invocation.getArguments().length, localizedMatchers);
        }

        return completedMatchers;
    }


    private LinkedList<ArgumentMatcher> toMatchers(List<LocalizedMatcher> lastMatchers) {
        LinkedList<ArgumentMatcher> matchers = new LinkedList<ArgumentMatcher>();
        for (LocalizedMatcher m : lastMatchers) {
            matchers.add(m.getMatcher());
        }
        return matchers;
    }


    public static class MatcherMarkerValues {
        public final static Integer INT_MARKER = 0;
        public final static Byte BYTE_MARKER = 0;
        public final static Boolean BOOL_MARKER = false;
        public final static Short SHORT_MARKER = 0;
        public final static Long LONG_MARKER = 0L;
        public final static Double DOUBLE_MARKER = 0D;
        public final static Float FLOAT_MARKER = 0F;
        public final static Character CHAR_MARKER = '\u0000';

        public static <T> T objectMarker() {
            return null;
        }

        public static boolean isMarkerValue(Class<?> type, Object value) {
            Object markerValue = markerValueOf(type);
            if (markerValue == objectMarker()) {
                return value == objectMarker();
            }

            return markerValue.equals(value);
        }

        public static <T> T markerValueOf(T value) {
            if (value == null) {
               return objectMarker();
            }
            return (T)markerValueOf(value.getClass());
        }

        public static <T> T markerValueOf(Class<T> type) {

            Class<?> primitiveType = primitiveTypeOf(type);
            if (primitiveType == int.class) {
                return (T) INT_MARKER;
            }
            if (primitiveType == byte.class) {
                return (T) BYTE_MARKER;
            }
            if (primitiveType == boolean.class) {
                return (T) BOOL_MARKER;
            }
            if (primitiveType == short.class) {
                return (T) SHORT_MARKER;
            }
            if (primitiveType == long.class) {
                return (T) LONG_MARKER;
            }
            if (primitiveType == double.class) {
                return (T) DOUBLE_MARKER;
            }
            if (primitiveType == float.class) {
                return (T) FLOAT_MARKER;
            }
            if (primitiveType == char.class) {
                return (T) CHAR_MARKER;
            }

            return objectMarker();
        }
    }

}
