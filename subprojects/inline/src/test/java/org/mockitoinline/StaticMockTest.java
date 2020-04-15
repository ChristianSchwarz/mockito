package org.mockitoinline;

import org.junit.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StaticMockTest {
    @Test
    public void name() {
        Mockito.mockStatic(StaticUtil.class);

        when(StaticUtil.foo()).thenReturn("yea");


        String r = StaticUtil.foo();

        assertThat(r).isEqualTo("yea");
    }
}
