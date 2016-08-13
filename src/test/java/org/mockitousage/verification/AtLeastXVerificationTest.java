/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.verification;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.exceptions.verification.TooLittleActualInvocations;
import org.mockito.exceptions.verification.TooManyActualInvocations;
import org.mockitoutil.TestBase;

import java.util.List;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class AtLeastXVerificationTest extends TestBase {

    @Mock private List<String> mock;

    @Test
    public void shouldVerifyAtLeastXTimes() throws Exception {
        //when
        mock.clear();
        mock.clear();
        mock.clear();

        //then
        verify(mock, atLeast(2)).clear();
    }

    @Test
    public void shouldFailVerifiationAtLeastXTimes() throws Exception {
        mock.add("one");
        verify(mock, atLeast(1)).add(anyString());

        try {
            verify(mock, atLeast(2)).add(anyString());
            fail();
        } catch (MockitoAssertionError e) {}
    }
    
    @Test
    public void shouldAllowAtLeastZeroForTheSakeOfVerifyNoMoreInteractionsSometimes() throws Exception {
        //when
        mock.add("one");
        mock.clear();

        //then
        verify(mock, atLeast(0)).add("one");
        verify(mock, atLeast(0)).clear();

        verifyNoMoreInteractions(mock);        
    }
    
    @Test
    public void atLeast1AtMost3_actual2() throws Exception {
        mock.add("added");
        mock.add("added");
        verify(mock,atLeast(1).atMost(3)).add("added");
    }
    
    @Test(expected=TooLittleActualInvocations.class)
    public void atLeast1AtMost5_actual0() throws Exception {
      
        verify(mock,atLeast(1).atMost(5)).add("added");
    }
    
    @Test(expected=TooManyActualInvocations.class)
    public void atLeast1AtMost3_actual4() throws Exception {
      
        mock.add("added");
        mock.add("added");
        mock.add("added");
        mock.add("added");
        verify(mock,atLeast(1).atMost(3)).add("added");
    }
    
   
}