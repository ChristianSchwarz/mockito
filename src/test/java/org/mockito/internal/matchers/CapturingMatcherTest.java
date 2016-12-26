/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.LinkedList;
import java.util.List;
import org.junit.Test;
import org.mockitoutil.TestBase;

public class CapturingMatcherTest extends TestBase {

  
    @Test
    public void should_capture_arguments()   {
        List<String> arguments = new LinkedList<String>();
        //given
        CapturingMatcher<String> m = new CapturingMatcher<String>(arguments);
        
        //when
        m.captureFrom("foo");
        m.captureFrom("bar");
        
        //then
        assertThat(arguments).containsSequence("foo", "bar");
    }
    
}