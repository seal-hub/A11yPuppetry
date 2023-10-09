package PREFIX.TOOL_NAME.controller;

import android.os.Bundle;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import PREFIX.TOOL_NAME.ActionUtils;
import PREFIX.TOOL_NAME.ActualWidgetInfo;
import PREFIX.TOOL_NAME.TOOL_NAMEService;
import PREFIX.TOOL_NAME.UseCase.BackCommand;
import PREFIX.TOOL_NAME.UseCase.ClickCommand;
import PREFIX.TOOL_NAME.UseCase.FocusCommand;
import PREFIX.TOOL_NAME.UseCase.JumpNextCommand;
import PREFIX.TOOL_NAME.UseCase.JumpPreviousCommand;
import PREFIX.TOOL_NAME.UseCase.NextCommand;
import PREFIX.TOOL_NAME.UseCase.PreviousCommand;
import PREFIX.TOOL_NAME.UseCase.SelectCommand;
import PREFIX.TOOL_NAME.UseCase.TypeCommand;

public class BaseActionPerformer extends AbstractActionPerformer {

    @Override
    public boolean executeClick(ClickCommand clickStep, ActualWidgetInfo actualWidgetInfo) {
        return actualWidgetInfo.getA11yNodeInfo().performAction(AccessibilityNodeInfo.ACTION_CLICK);
    }

    @Override
    public boolean executeType(TypeCommand typeStep, ActualWidgetInfo actualWidgetInfo) {
        Bundle arguments = new Bundle();
        arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, typeStep.getText());
        return actualWidgetInfo.getA11yNodeInfo().performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
    }

    @Override
    public boolean executeFocus(FocusCommand focusStep, ActualWidgetInfo actualWidgetInfo) {
        return ActionUtils.focusOnNode(actualWidgetInfo.getA11yNodeInfo());
    }

    @Override
    public void navigateNext(NextCommand nextStep, ExecutorCallback callback) {

    }

    @Override
    public void navigatePrevious(PreviousCommand previousStep, ExecutorCallback callback) {

    }

    @Override
    public void navigateJumpNext(JumpNextCommand nextStep, ExecutorCallback callback) {

    }

    @Override
    public void navigateJumpPrevious(JumpPreviousCommand previousStep, ExecutorCallback callback) {

    }

    @Override
    public void navigateSelect(SelectCommand selectCommand, ExecutorCallback callback) {
        
    }

    @Override
    public void navigateBack(BackCommand selectCommand, ExecutorCallback callback) {
        boolean result = ActionUtils.performBack();
        if(callback != null){
            if(result)
                callback.onCompleted();
            else
                callback.onError("The back action could not be performed!");
        }
    }
}
