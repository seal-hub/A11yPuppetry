package PREFIX.TOOL_NAME.app;

import android.view.accessibility.AccessibilityEvent;

import PREFIX.TOOL_NAME.TOOL_NAMEService;

public class MyTOOL_NAMEService extends TOOL_NAMEService {
    static String TAG = "TOOL_NAME_SERVICE_APP";
    public MyTOOL_NAMEService() {
    }
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        super.onAccessibilityEvent(event);
    }
}
