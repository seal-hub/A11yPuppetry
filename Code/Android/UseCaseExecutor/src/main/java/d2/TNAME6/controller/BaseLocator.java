package PREFIX.TOOL_NAME.controller;

import android.util.Log;

import PREFIX.TOOL_NAME.ActionUtils;
import PREFIX.TOOL_NAME.ActualWidgetInfo;
import PREFIX.TOOL_NAME.ConceivedWidgetInfo;
import PREFIX.TOOL_NAME.TOOL_NAMEService;

public class BaseLocator extends AbstractLocator {
    @Override
    protected LocatorResult locateAttempt(ConceivedWidgetInfo targetWidget)
    {
        ActualWidgetInfo actualWidgetInfo = ActionUtils.findActualWidget(targetWidget);
        if(actualWidgetInfo == null){
            Log.i(TOOL_NAMEService.TAG, "The target widget could not be found at this moment!");
            return new LocatorResult(LocatorStatus.WAITING);
        }
        return new LocatorResult(actualWidgetInfo);
    }
}
