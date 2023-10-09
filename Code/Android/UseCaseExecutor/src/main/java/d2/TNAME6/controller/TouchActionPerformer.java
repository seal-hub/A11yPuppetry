package PREFIX.TOOL_NAME.controller;
import android.util.Pair;
import PREFIX.TOOL_NAME.ActionUtils;
import PREFIX.TOOL_NAME.ActualWidgetInfo;
import PREFIX.TOOL_NAME.UseCase.ClickCommand;

public class TouchActionPerformer extends BaseActionPerformer {
    @Override
    public boolean executeClick(ClickCommand clickStep, ActualWidgetInfo actualWidgetInfo) {
        Pair<Integer, Integer> clickableCoordinate = ActionUtils.getClickableCoordinate(actualWidgetInfo.getA11yNodeInfo(), true);
        return ActionUtils.performTap(clickableCoordinate);
    }
}
