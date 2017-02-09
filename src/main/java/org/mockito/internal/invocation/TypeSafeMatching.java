/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import static org.mockito.internal.util.reflection.ArgumentMatcherUtil.isCompatible;

import org.mockito.ArgumentMatcher;

@SuppressWarnings({"unchecked","rawtypes"})
public class TypeSafeMatching implements ArgumentMatcherAction {

    private final static ArgumentMatcherAction TYPE_SAFE_MATCHING_ACTION = new TypeSafeMatching();
        
    private TypeSafeMatching() {}

    
    public static ArgumentMatcherAction matchesTypeSafe(){
        return TYPE_SAFE_MATCHING_ACTION;
    }
    
    @Override
    public boolean apply(ArgumentMatcher matcher, Object argument) {
        return isCompatible(matcher, argument) && matcher.matches(argument);
    }

    
    

    
}