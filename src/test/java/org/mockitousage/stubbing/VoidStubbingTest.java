package org.mockitousage.stubbing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;
import static org.mockito.junit.MockitoJUnit.rule;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.misusing.UnfinishedStubbingException;
import org.mockito.exceptions.misusing.UnfinishedVerificationException;
import org.mockito.junit.MockitoRule;
import org.mockitousage.IMethods;

@SuppressWarnings("serial")
public class VoidStubbingTest {
//@formatter:off
	@Rule
	public MockitoRule mockito = rule();

	@Mock
	private IMethods mock;

	private List<Integer> result;

	@Before
	public void init() {
		result = new ArrayList<>();
	}

	@After
	public void reset() {
		mockingProgress().reset();
		mockingProgress().resetOngoingStubbing();
	}

	@Test
	public void instanceReferenceStubbing() {
		when(mock::voidMethod).then(() -> result.add(1));

		mock.voidMethod();

		assertThat(result).contains(1);
	}

	@Test
	public void consecutiveStubbing() throws Exception {
		when(() -> mock.intArgumentMethod(5))
		.then((Integer a) -> result.add(a))
		.then((Integer a) -> result.add(100 * a));

		mock.intArgumentMethod(5);
		mock.intArgumentMethod(5);

		assertThat(result).containsExactly(5, 500);
	}

	@Test
	public void replaceStubbing() {
		when(() -> mock.voidMethod()).thenDoNothing();
		when(() -> mock.voidMethod()).thenThrow(new Exception1());

		assertThatThrownBy(mock::voidMethod).isInstanceOf(Exception1.class);
	}

	@Test
	public void mixedStubbings() {
		when(mock::voidMethod).thenThrow(new Exception1());
		doThrow(new Exception2()).when(mock).differentMethod();

		assertThatThrownBy(mock::voidMethod).isInstanceOf(Exception1.class);
		assertThatThrownBy(mock::differentMethod).isInstanceOf(Exception2.class);
	}

	@Test
	public void captureArgument() {
		ArgumentCaptor<Integer> captor = forClass(int.class);

		when(() -> mock.intArgumentMethod(captor.capture())).thenDoNothing();

		mock.intArgumentMethod(123);

		assertThat(captor.getValue()).isEqualTo(123);
	}

	@Test
	public void tooManyCallsOnMock() {
		assertThatThrownBy(() -> {

			when(() -> {
				mock.intArgumentMethod(5);
				mock.intArgumentMethod(2);
			});

		}).isInstanceOf(MockitoException.class).hasMessageContaining("Too many mock methods were called")
				.hasMessageContaining("Expected exactly one call to a mock");

	}

	@Test
	public void noCallOnMock() {
		assertThatThrownBy(() -> 

			when(() -> {})

		).isInstanceOf(MockitoException.class)
		.hasMessageContaining("No mock method was called")
		.hasMessageContaining("Expected exactly one call to a mock");

	}

	@Test
	public void noCallOnMock_afterTooManyCallsWereDetected() {
		try {
			when(() -> {
				mock.intArgumentMethod(5);
				mock.intArgumentMethod(2);
			});
		} catch (MockitoException expected) {}

		assertThatThrownBy(() -> 
			
			when(() -> {})
			
		).isInstanceOf(MockitoException.class)
		.hasMessageContaining("No mock method was called")
		.hasMessageContaining("Expected exactly one call to a mock");

	}

	@Test
	public void stubbingAfterMockCall() {
		mock.intArgumentMethod(5);

		when(mock::voidMethod).thenDoNothing();
	}
	
	@Test
	public void stubbingAfterVerify() {
		verify(mock,never()).intArgumentMethod(5);
		
		when(mock::voidMethod).thenDoNothing();
	}
	
	@Test
	public void detectUnfinishedVerify() {
		verify(mock,never()); //unfinished verify, the method call is missing
		
		assertThatThrownBy(()->
		
			when(mock::voidMethod).thenDoNothing()
		
		).isInstanceOf(UnfinishedVerificationException.class);
		
	}
	
	@Test
	public void unfinishedVoidStubbing() throws Exception {
		//unfinished void stubbing, the then-answer call is missing
		when(mock::voidMethod); 
		
		assertThatThrownBy(()->
		
			when(mock::voidMethod).thenDoNothing()
			
		).isInstanceOf(UnfinishedStubbingException.class);
	}
	
	@Test
	public void unfinishedStubbing() throws Exception {
		//unfinished stubbing, the then-answer call is missing
		when(mock.simpleMethod()); 
		
		assertThatThrownBy(()->
		
			when(mock::voidMethod).thenDoNothing()
			
		).isInstanceOf(UnfinishedStubbingException.class);
	}
	
	
	static class Exception1 extends RuntimeException{}
	static class Exception2 extends RuntimeException{}
}