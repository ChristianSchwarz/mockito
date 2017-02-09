package org.mockito.internal.stubbing;

import static org.mockito.AdditionalAnswers.answerVoid;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;

import java.util.List;

import org.mockito.Mockito;
import org.mockito.VoidCall;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.OngoingVoidStubbing;
import org.mockito.stubbing.VoidAnswer;
import org.mockito.stubbing.VoidAnswer0;
import org.mockito.stubbing.VoidAnswer1;
import org.mockito.stubbing.VoidAnswer2;
import org.mockito.stubbing.VoidAnswer3;
import org.mockito.stubbing.VoidAnswer4;
import org.mockito.stubbing.VoidAnswer5;

public class OngoingVoidStubbingImpl implements OngoingVoidStubbing {
    private OngoingStubbing<Object> ongoingStubbing;

    public OngoingVoidStubbingImpl(VoidCall methodCall) {
    	
        ongoingStubbing = execute(methodCall);
        mockingProgress().stubbingStarted();
        checkExactlyOneCallOnMockWasMade(ongoingStubbing);
    }

    @Override
    public OngoingVoidStubbing thenThrow(Throwable... throwables) {
        ongoingStubbing = ongoingStubbing.thenThrow(throwables);
        return this;
    }

    @Override
    public OngoingVoidStubbing thenThrow(Class<? extends Throwable> throwableType) {
        ongoingStubbing = ongoingStubbing.thenThrow(throwableType);
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public OngoingVoidStubbing thenThrow(Class<? extends Throwable> toBeThrown, Class<? extends Throwable>... nextToBeThrown) {
        ongoingStubbing = ongoingStubbing.thenThrow(toBeThrown, nextToBeThrown);
        return this;
    }

    @Override
    public OngoingVoidStubbing thenCallRealMethod() {
        ongoingStubbing = ongoingStubbing.thenCallRealMethod();
        return this;
    }

    @Override
    public OngoingVoidStubbing thenDoNothing() {
        return then(() -> {});
    }

    @Override
    public OngoingVoidStubbing thenAnswer(VoidAnswer answer) {
        return then((InvocationOnMock i)->{answer.answer(i);return null;});

    }

    @Override
    public OngoingVoidStubbing then(VoidAnswer0 answer) {
        return thenAnswer(i -> answer.answer());
    }

    @Override
    public <A> OngoingVoidStubbing then(VoidAnswer1<A> answer) {
        return then(answerVoid(answer));
    }

    @Override
    public <A, B> OngoingVoidStubbing then(VoidAnswer2<A, B> answer) {
        return then(answerVoid(answer));
    }

    @Override
    public <A, B, C> OngoingVoidStubbing then(VoidAnswer3<A, B, C> answer) {
        return then(answerVoid(answer));
    }

    @Override
    public <A, B, C, D> OngoingVoidStubbing then(VoidAnswer4<A, B, C, D> answer) {
        return then(answerVoid(answer));
    }

    @Override
    public <A, B, C, D, E> OngoingVoidStubbing then(VoidAnswer5<A, B, C, D, E> answer) {
        return then(answerVoid(answer));
    }
    
    private OngoingVoidStubbing then(Answer<Void> answer){
        ongoingStubbing = ongoingStubbing.then(answer);
        return this;
    }

    @Override
    public <M> M getMock() {
        return ongoingStubbing.getMock();
    }

    private static void checkExactlyOneCallOnMockWasMade(OngoingStubbing<Object> ongoingStubbing) {
    	if (ongoingStubbing==null){
    		throw missingMockInvocations();
    	}
        OngoingStubbingImpl<?> stubbing = (OngoingStubbingImpl<?>) ongoingStubbing;
		List<Invocation> allInvocations = stubbing.getRegisteredInvocations();
        if (allInvocations.isEmpty()){
        	throw missingMockInvocations();
        }
		
		if (allInvocations.size() > 1) {
        	for (Invocation invocation : allInvocations) {
        		clearInvocations(invocation.getMock());
			}
        	
            throw tooManyMockInvocations(ongoingStubbing, allInvocations);
        }
    }

	private static MockitoException tooManyMockInvocations(OngoingStubbing<Object> ongoingStubbing,
			List<Invocation> allInvocations) {
		return new MockitoException("Too many mock methods were called! Expected exactly one call to mock: " + ongoingStubbing.getMock() + "! Got: " + allInvocations);
	}

	private static MockitoException missingMockInvocations() {
		return new MockitoException("No mock method was called! Expected exactly one call to mock.");
	}

    @SuppressWarnings("unchecked")
    private static OngoingStubbing<Object> execute(VoidCall methodCall) {
    	
        try {
            methodCall.run();
        } catch (Throwable mustNotHappen) {
            throw new MockitoException("Unexpected exception was thrown from: " + methodCall,mustNotHappen);
        }
		return (OngoingStubbing<Object>) mockingProgress.pullOngoingStubbing();
    }
}