package org.mockito.internal.util.reflection;

import java.lang.reflect.Method;

import org.mockito.ArgumentMatcher;
import org.mockito.internal.util.reflection.TypeResolver.Unknown;

public class ArgumentMatcherUtil {

	
	/**
     * Returns <code>true</code> if the given <b>argument</b> can be passed to
     * the given <code>argumentMatcher</code> without causing a
     * {@link ClassCastException}.
     */
    public static boolean isCompatible(ArgumentMatcher<?> argumentMatcher, Object argument) {
        if (argument == null)
            return true;

        Class<?> expectedArgumentType = TypeResolver.resolveRawArgument(ArgumentMatcher.class, argumentMatcher.getClass());
        if (expectedArgumentType == Unknown.class){
        	return true;
        }
        return expectedArgumentType.isInstance(argument);
    }
	
	 /**
     * Returns the type of {@link ArgumentMatcher#matches(Object)} of the given
     * {@link ArgumentMatcher} implementation.
     */
    public static Class<?> getArgumentType(ArgumentMatcher<?> argumentMatcher) {
        Method[] methods = argumentMatcher.getClass().getMethods();
        
        for (Method method : methods) {
            if (isMatchesMethod(method)) {
                return method.getParameterTypes()[0];
            }
        }
        throw new NoSuchMethodError("Method 'matches(T)' not found in ArgumentMatcher: " + argumentMatcher + " !\r\n Please file a bug with this stack trace at: https://github.com/mockito/mockito/issues/new ");
    }

    /**
     * Returns <code>true</code> if the given method is
     * {@link ArgumentMatcher#matches(Object)}
     */
    private static boolean isMatchesMethod(Method method) {
        if (method.getParameterTypes().length != 1) {
            return false;
        }
        if (method.isBridge()) {
            return false;
        }
        return method.getName().equals("matches");
    }
}
