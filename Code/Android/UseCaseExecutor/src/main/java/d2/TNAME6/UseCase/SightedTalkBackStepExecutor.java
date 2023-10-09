package PREFIX.TOOL_NAME.UseCase;

import android.util.Log;
import android.util.Pair;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

import PREFIX.TOOL_NAME.ActionUtils;
import PREFIX.TOOL_NAME.ActualWidgetInfo;
import PREFIX.TOOL_NAME.TOOL_NAMEService;
import PREFIX.TOOL_NAME.Utils;

@Deprecated
public class SightedTalkBackStepExecutor implements StepExecutor {
    public static boolean apiFocus = false;
    @Override
    public boolean executeStep(Command step) {
        Log.i(TOOL_NAMEService.TAG, "STB Executing Step " + step);
        if(step.getState() != Command.CommandState.RUNNING)
            return false;
        if(step instanceof LocatableCommand){
            LocatableCommand locatableCommand = (LocatableCommand) step;
            locatableCommand.increaseLocatingAttempts();
            if(locatableCommand.reachedMaxLocatingAttempts()){
                Log.i(TOOL_NAMEService.TAG, "Reached Stopping Criteria!");
                locatableCommand.setState(Command.CommandState.FAILED);
                return false;
            }
            List<AccessibilityNodeInfo> similarNodes = Utils.findSimilarNodes(locatableCommand.getTargetWidgetInfo());
            if(similarNodes.size() != 1){
                if(similarNodes.size() == 0) {
                    Log.i(TOOL_NAMEService.TAG, "The target widget could not be found in current screen.");
                    Log.i(TOOL_NAMEService.TAG, "The target XPATH: " + locatableCommand.getTargetWidgetInfo().getXpath());
                    List<AccessibilityNodeInfo> allNodes = Utils.getAllA11yNodeInfo(false);
                    for(AccessibilityNodeInfo nodeInfo : allNodes){
                        ActualWidgetInfo actualWidgetInfo = ActualWidgetInfo.createFromA11yNode(nodeInfo);
                        if (actualWidgetInfo != null)
                            Log.i(TOOL_NAMEService.TAG, "\t" + actualWidgetInfo.getXpath());
                    }
                }
                else{
                    Log.i(TOOL_NAMEService.TAG, "There are more than one candidates for the target.");
                    for(AccessibilityNodeInfo node : similarNodes){
                        Log.i(TOOL_NAMEService.TAG, " Node: " + node);
                    }
                }
                return true;
            }
            else {
                AccessibilityNodeInfo node = similarNodes.get(0);
                if(!ActionUtils.isFocusedNodeTarget(similarNodes)){
                    if(apiFocus){
                        Log.e(TOOL_NAMEService.TAG, String.format("API Focusing on %s", node));
                        ActionUtils.a11yFocusOnNode(node);
                    }
                    else {
                        Pair<Integer, Integer> clickableCoordinate = ActionUtils.getClickableCoordinate(node, false);
                        int x = clickableCoordinate.first, y = clickableCoordinate.second;
                        Log.e(TOOL_NAMEService.TAG, String.format("Physically clicking on (%d, %d)", x, y));
                        ActionUtils.performTap(x, y);
                    }
                    locatableCommand.increaseLocatingAttempts();
                    return false;
                }
                locatableCommand.increaseActingAttempts();
                locatableCommand.setActedWidget(ActualWidgetInfo.createFromA11yNode(TOOL_NAMEService.getInstance().getAccessibilityFocusedNode()));
                if(locatableCommand instanceof ClickCommand){
                    ActionUtils.performDoubleTap();
                    locatableCommand.setState(Command.CommandState.COMPLETED);
                }
                else if(locatableCommand instanceof TypeCommand){
                    ActionUtils.performType(TOOL_NAMEService.getInstance().getAccessibilityFocusedNode(), ((TypeCommand) locatableCommand).getText());
                    locatableCommand.setState(Command.CommandState.COMPLETED);
                }
                else{
                    Log.e(TOOL_NAMEService.TAG, "This locatable step is unrecognizable " + locatableCommand);
                    locatableCommand.setState(Command.CommandState.FAILED);
                    return false;
                }
                return true;
            }
        }
        else {
            Log.e(TOOL_NAMEService.TAG, "This step is unrecognizable " + step);
            step.setState(Command.CommandState.FAILED);
            return false;
        }
    }

    @Override
    public boolean interrupt() {
        // TODO
        return false;
    }
}
