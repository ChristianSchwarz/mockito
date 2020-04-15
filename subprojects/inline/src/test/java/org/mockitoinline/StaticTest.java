package org.mockitoinline;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.asm.Advice.OnNonDefaultValue;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;

import static java.lang.System.out;
import static net.bytebuddy.implementation.bytecode.assign.Assigner.Typing.DYNAMIC;
import static net.bytebuddy.matcher.ElementMatchers.*;

public class StaticTest {

    public static void main(String[] args) {
        ByteBuddyAgent.install();
        ByteBuddy byteBuddy = new ByteBuddy();


        byteBuddy
            .redefine(StaticUtil.class)
            .visit(Advice.withCustomMapping()
                .to(ProxyAdvice.class)
                .on(isStatic()
                    .and(not(isTypeInitializer()))
                    .and(not(isConstructor()))
                ))
            .make()
            .load(
                StaticUtil.class.getClassLoader(),
                ClassReloadingStrategy.fromInstalledAgent());


        out.println(StaticUtil.foo());
        out.println(StaticUtil.foo());
    }
}

class ProxyAdvice {
    @Advice.OnMethodEnter(skipOn = OnNonDefaultValue.class)
    static Callable<?> enter(@Advice.Origin final Method method,
                             @Advice.AllArguments final Object[] arguments
                             ) throws Throwable {

        System.out.println(method);
        if (true) {
            return null;
        }

        return new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                System.out.println("-> "+Arrays.toString(arguments));
                return null;
            }
        };
    }

    @Advice.OnMethodExit
    static void exit(@Advice.Return(readOnly = false, typing = DYNAMIC) Object returnValue,
                     @Advice.Enter Callable<?> staticMethod) throws Throwable {
        if (staticMethod != null) {
            returnValue = staticMethod.call();
        }
    }

}


