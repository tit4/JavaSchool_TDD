package com.db.javaschool.tdd.mockito;


import org.junit.Test;
import org.mockito.InOrder;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.*;

//Let's import Mockito statically so that the code looks clearer

public class MockitoExample {

    @Test
    public void verifySomeBehavior() {

        //mock creation
        @SuppressWarnings("unchecked")
        List<String> mockedList = mock(List.class);
        //using mock object
        mockedList.add("one");
        mockedList.clear();

        //verification
        verify(mockedList).add("one");
//        verify(mockedList).add("two");
        verify(mockedList).clear();

    }

    @Test
    public void stubbing() {
        //You can mock concrete classes, not only interfaces
        List<String> mockedList = mock(LinkedList.class);

        //stubbing
        when(mockedList.get(0)).thenReturn("first");
        when(mockedList.get(1)).thenThrow(new RuntimeException());

        //following prints "first"
        System.out.println(mockedList.get(0));

        //following throws runtime exception
        System.out.println(mockedList.get(1));

        //following prints "null" because get(999) was not stubbed
        System.out.println(mockedList.get(999));

        //Although it is possible to verify a stubbed invocation, usually it's just redundant
        //If your code cares what get(0) returns then something else breaks (often before even verify() gets executed).
        //If your code doesn't care what get(0) returns then it should not be stubbed.
        verify(mockedList).get(0);
    }

    @Test
    public void argumentMatching() {
        //You can mock concrete classes, not only interfaces
        List<String> mockedList = mock(LinkedList.class);
        //stubbing using built-in anyInt() argument matcher
        when(mockedList.get(anyInt())).thenReturn("element");

        //following prints "element"
        System.out.println(mockedList.get(999));

        //you can also verify using an argument matcher
        verify(mockedList).get(anyInt());
        verify(mockedList).get(999);
    }

    public interface IExample {
        void someMethod(Integer first, String second, String third);

        String someOtherMethod();
    }

    @Test
    public void argMatcherPitfall() {
        IExample mock = mock(IExample.class);
        mock.someMethod(1, "", "third argument");

        verify(mock).someMethod(anyInt(), anyString(), eq("third argument"));
        verify(mock).someMethod(1, "", "third argument");
        //above is correct - eq() is also an argument matcher

//        verify(mock).someMethod(anyInt(), anyString(), "third argument");
        //above is incorrect - exception will be thrown because third argument is given without an argument matcher.
    }

    @Test
    public void numberOfInvocations() {
        List<String> mockedList = mock(LinkedList.class);
        //using mock
        mockedList.add("once");

        mockedList.add("twice");
        mockedList.add("twice");

        mockedList.add("three times");
        mockedList.add("three times");
        mockedList.add("three times");

        mockedList.add("five times");
        mockedList.add("five times");
        mockedList.add("five times");
        mockedList.add("five times");
        mockedList.add("five times");
        mockedList.add("five times");

        //following two verifications work exactly the same - times(1) is used by default
        verify(mockedList).add("once");
        verify(mockedList, times(1)).add("once");

        //exact number of invocations verification
        verify(mockedList, times(2)).add("twice");
        verify(mockedList, times(3)).add("three times");

        //verification using never(). never() is an alias to times(0)
        verify(mockedList, never()).add("never happened");

        //verification using atLeast()/atMost()
        verify(mockedList, atLeastOnce()).add("three times");
        verify(mockedList, atLeast(10)).add("five times");
        verify(mockedList, atMost(5)).add("three times");
    }

    @Test(expected = RuntimeException.class)
    public void stubExceptions() {
        List<String> mockedList = mock(LinkedList.class);
        doThrow(new RuntimeException()).when(mockedList).clear();

        //following throws RuntimeException:
        mockedList.clear();

    }

    @Test
    public void inOrderInvocation() {

        // A. Single mock whose methods must be invoked in a particular order
        List<String> singleMock = mock(List.class);

        //using a single mock
        singleMock.add("was added first");
        singleMock.add("was added second");

        //create an inOrder verifier for a single mock
        InOrder inOrder = inOrder(singleMock);

        //following will make sure that add is first called with "was added first, then with "was added second"
        inOrder.verify(singleMock).add("was added first");
        inOrder.verify(singleMock).add("was added second");

        // B. Multiple mocks that must be used in a particular order
        List<String> firstMock = mock(List.class);
        List<String> secondMock = mock(List.class);

        //using mocks
        firstMock.add("was called first");
        secondMock.add("was called second");

        //create inOrder object passing any mocks that need to be verified in order
        inOrder = inOrder(firstMock, secondMock);

        //following will make sure that firstMock was called before secondMock
        inOrder.verify(firstMock).add("was called first");
        inOrder.verify(secondMock).add("was called second");

        // Oh, and A + B can be mixed together at will

    }

    @Test
    public void neverInvoked() {
        List<String> mockOne = mock(List.class);
        List<String> mockTwo = mock(List.class);
        List<String> mockThree = mock(List.class);

        mockThree.add("three");
        //using mocks - only mockOne is interacted
        mockOne.add("one");

        //ordinary verification
        verify(mockOne).add("one");

        //verify that method was never called on a mock
        verify(mockOne, never()).add("two");

        //verify that other mocks were not interacted
        verifyZeroInteractions(mockTwo, mockThree);

    }

    @Test
    public void redundantInvocatios() {
        List<String> mockedList = mock(LinkedList.class);
        //using mocks
        mockedList.add("one");
        mockedList.add("two");

        verify(mockedList).add("one");
        verify(mockedList).add("two");

        //following verification will fail
        verifyNoMoreInteractions(mockedList);

    }

    @Test
    public void consecutiveCalls() {
        LinkedList mockedList = mock(LinkedList.class);
        doThrow(RuntimeException.class).doReturn(1).doReturn(2).when(mockedList).size();
        doReturn(3).when(mockedList).get(2);

        //First call: throws runtime exception:
        try {
            mockedList.size();
        } catch (RuntimeException e) {
            System.out.println("First call: throws runtime exception");
        }

        //Second call: prints "1"
        System.out.println(mockedList.size());

        //Any consecutive call: prints "1" as well (last stubbing wins).
        System.out.println(mockedList.size());
        System.out.println(mockedList.get(2));

        //OR

        IExample mock = mock(IExample.class);
        when(mock.someOtherMethod()).thenReturn("one", "two", "three");
        //one
        System.out.println(mock.someOtherMethod());
        //two
        System.out.println(mock.someOtherMethod());
        //three
        System.out.println(mock.someOtherMethod());
        //three
        System.out.println(mock.someOtherMethod());
    }

    @Test
    public void spying() {

        List<String> list = new LinkedList<>();
        List<String> spy = spy(list);

        //optionally, you can stub out some methods:
        when(spy.size()).thenReturn(100);

        //using the spy calls *real* methods
        spy.add("one");
        spy.add("two");

        //prints "one" - the first element of a list
        System.out.println(spy.get(0));

        //size() method was stubbed - 100 is printed
        System.out.println(spy.size());

        //optionally, you can verify
        verify(spy).add("one");
        verify(spy).add("two");

    }
}
