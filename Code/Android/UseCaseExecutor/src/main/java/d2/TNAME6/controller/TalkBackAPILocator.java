package PREFIX.TOOL_NAME.controller;

import android.util.Log;

import PREFIX.TOOL_NAME.ActionUtils;
import PREFIX.TOOL_NAME.ActualWidgetInfo;
import PREFIX.TOOL_NAME.ConceivedWidgetInfo;
import PREFIX.TOOL_NAME.TOOL_NAMEService;

public class TalkBackAPILocator extends BaseLocator{
    @Override
    protected LocatorResult locateAttempt(ConceivedWidgetInfo targetWidget) {
        LocatorResult result = super.locateAttempt(targetWidget);
        if(result.status != LocatorStatus.COMPLETED)
            return result;
        ActualWidgetInfo actualWidgetInfo = result.actualWidgetInfo;
        if(!ActionUtils.isFocusedNodeTarget(actualWidgetInfo.getA11yNodeInfo())){
            Log.e(TOOL_NAMEService.TAG, String.format("API Focusing on %s", actualWidgetInfo.getA11yNodeInfo()));
            ActionUtils.a11yFocusOnNode(actualWidgetInfo.getA11yNodeInfo());
            return new LocatorResult(LocatorStatus.WAITING);
        }
        return new LocatorResult(actualWidgetInfo);
    }
}
