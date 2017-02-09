package org.mockito.internal.progress;

import org.mockito.listeners.MockitoListener;
import org.mockito.stubbing.OngoingStubbing;

public interface OngoingStubbingListener extends MockitoListener {

	void onOngoingStubbing(OngoingStubbing<?> ongoingStubbing);

}
