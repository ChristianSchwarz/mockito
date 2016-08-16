/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import java.lang.reflect.AccessibleObject;

public class AccessibilityChanger {

    private final boolean wasAccessible;
    private final AccessibleObject accessibleObject;

    private AccessibilityChanger(AccessibleObject accessibleObject, boolean wasAccessible) {
        this.accessibleObject = accessibleObject;
        this.wasAccessible = wasAccessible;

    }

    /**
     * changes the accessibleObject accessibility and returns true if accessibility was changed
     * 
     * @return
     */
    public static AccessibilityChanger enableAccess(AccessibleObject accessibleObject) {
        AccessibilityChanger operation = new AccessibilityChanger(accessibleObject, accessibleObject.isAccessible());
        accessibleObject.setAccessible(true);
        return operation;
    }
    
    public void undo() {
        try {
            accessibleObject.setAccessible(wasAccessible);
        } catch (Throwable t) {
            // ignore
        }
    }
}
