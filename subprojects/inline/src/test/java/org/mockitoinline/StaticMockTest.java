package org.mockitoinline;

import org.junit.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

public class StaticMockTest {
    @Test
    public void name() {
        Mockito.mockStatic(StaticUtil.class);



        String r=StaticUtil.foo();
        assertThat(r).isEqualTo("foo");
    }
}
