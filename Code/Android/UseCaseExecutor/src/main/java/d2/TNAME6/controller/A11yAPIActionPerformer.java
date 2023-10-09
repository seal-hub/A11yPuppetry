package PREFIX.TOOL_NAME.controller;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import PREFIX.TOOL_NAME.ActualWidgetInfo;
import PREFIX.TOOL_NAME.TOOL_NAMEService;
import PREFIX.TOOL_NAME.UseCase.ClickCommand;

public class A11yAPIActionPerformer extends BaseActionPerformer {
    @Override
    public boolean executeClick(ClickCommand clickStep, ActualWidgetInfo actualWidgetInfo) {
        AccessibilityNodeInfo clickableNode = actualWidgetInfo.getA11yNodeInfo();
        while (clickableNode != null && !clickableNode.isClickable())
            clickableNode = clickableNode.getParent();
        if (clickableNode == null) {
            Log.e(TOOL_NAMEService.TAG, "The widget is not clickable.");
            return false;
        }
        return clickableNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
    }
}
