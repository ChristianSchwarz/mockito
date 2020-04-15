/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import java.util.Collections;
import java.util.Set;

import org.mockito.mock.SerializableMode;

class MockFeatures<T> {

    final Class<T> mockedType;
    final Set<Class<?>> interfaces;
    final SerializableMode serializableMode;
    final boolean stripAnnotations;
    final boolean isStaticMock;

    private MockFeatures(Class<T> mockedType,
                         Set<Class<?>> interfaces,
                         SerializableMode serializableMode,
                         boolean stripAnnotations,
                         boolean isStaticMock) {
        this.mockedType = mockedType;
        this.interfaces = Collections.unmodifiableSet(interfaces);
        this.serializableMode = serializableMode;
        this.stripAnnotations = stripAnnotations;
        this.isStaticMock = isStaticMock;
    }

    public static <T> MockFeatures<T> withMockFeatures(Class<T> mockedType,
                                                       Set<Class<?>> interfaces,
                                                       SerializableMode serializableMode,
                                                       boolean stripAnnotations,
                                                       boolean isStaticMock) {

        return new MockFeatures<T>(mockedType, interfaces, serializableMode, stripAnnotations,isStaticMock);
    }
}
