package PREFIX.TOOL_NAME.controller;

import android.util.Log;
import android.util.Pair;

import PREFIX.TOOL_NAME.ActionUtils;
import PREFIX.TOOL_NAME.ActualWidgetInfo;
import PREFIX.TOOL_NAME.ConceivedWidgetInfo;
import PREFIX.TOOL_NAME.TOOL_NAMEService;

public class TalkBackTouchLocator extends BaseLocator{
    @Override
    protected LocatorResult locateAttempt(ConceivedWidgetInfo targetWidget) {
        LocatorResult result = super.locateAttempt(targetWidget);
        if(result.status != LocatorStatus.COMPLETED)
            return result;
        ActualWidgetInfo actualWidgetInfo = result.actualWidgetInfo;
        if(!ActionUtils.isFocusedNodeTarget(actualWidgetInfo.getA11yNodeInfo())){
            Pair<Integer, Integer> clickableCoordinate = ActionUtils.getClickableCoordinate(actualWidgetInfo.getA11yNodeInfo(), true);
            Log.i(TOOL_NAMEService.TAG, "TalkBackTouch taps on the coordinates " + clickableCoordinate.first + " " + clickableCoordinate.second + " to locate!");
            ActionUtils.performTap(clickableCoordinate);
            return new LocatorResult(LocatorStatus.WAITING);
        }
        return new LocatorResult(actualWidgetInfo);
    }
}
