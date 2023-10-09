package PREFIX.TOOL_NAME.controller;

import android.util.Log;
import PREFIX.TOOL_NAME.ActualWidgetInfo;
import PREFIX.TOOL_NAME.TOOL_NAMEService;
import PREFIX.TOOL_NAME.UseCase.BackCommand;
import PREFIX.TOOL_NAME.UseCase.ClickCommand;
import PREFIX.TOOL_NAME.UseCase.FocusCommand;
import PREFIX.TOOL_NAME.UseCase.JumpNextCommand;
import PREFIX.TOOL_NAME.UseCase.JumpPreviousCommand;
import PREFIX.TOOL_NAME.UseCase.LocatableCommand;
import PREFIX.TOOL_NAME.UseCase.NavigateCommand;
import PREFIX.TOOL_NAME.UseCase.NextCommand;
import PREFIX.TOOL_NAME.UseCase.PreviousCommand;
import PREFIX.TOOL_NAME.UseCase.SelectCommand;
import PREFIX.TOOL_NAME.UseCase.TypeCommand;

public abstract class AbstractActionPerformer implements ActionPerformer {
    @Override
    public void navigate(NavigateCommand navigateCommand, ExecutorCallback callback) {
        if(callback == null)
            callback = new DummyExecutorCallback();
        if (navigateCommand instanceof NextCommand)
            navigateNext((NextCommand) navigateCommand, callback);
        else if (navigateCommand instanceof PreviousCommand)
            navigatePrevious((PreviousCommand) navigateCommand, callback);
        else if (navigateCommand instanceof JumpNextCommand)
            navigateJumpNext((JumpNextCommand) navigateCommand, callback);
        else if (navigateCommand instanceof JumpPreviousCommand)
            navigateJumpPrevious((JumpPreviousCommand) navigateCommand, callback);
        else if (navigateCommand instanceof SelectCommand)
            navigateSelect((SelectCommand) navigateCommand, callback);
        else if (navigateCommand instanceof BackCommand)
            navigateBack((BackCommand) navigateCommand, callback);
        else {
            Log.e(TOOL_NAMEService.TAG, "This navigate step is unrecognizable " + navigateCommand);
            callback.onError("Unrecognizable Action");
        }
    }

    @Override
    public final void execute(LocatableCommand locatableCommand, ActualWidgetInfo actualWidgetInfo, ExecutorCallback callback) {
        if(callback == null)
            callback = new DummyExecutorCallback();
        Log.i(TOOL_NAMEService.TAG, this.getClass().getSimpleName() + " executing " + locatableCommand);
        boolean actionResult = false;
        if (locatableCommand == null || actualWidgetInfo == null){
            Log.e(TOOL_NAMEService.TAG, String.format("Problem with locatable step %s or actualWidgetInfo %s", locatableCommand, actualWidgetInfo));
            callback.onError("Error in parameters");
            return;
        }
        if(locatableCommand instanceof ClickCommand) {
            actionResult = executeClick((ClickCommand) locatableCommand, actualWidgetInfo);
        }
        else if(locatableCommand instanceof TypeCommand) {
            actionResult = executeType((TypeCommand) locatableCommand, actualWidgetInfo);
        }
        else if(locatableCommand instanceof FocusCommand){
            actionResult = executeFocus((FocusCommand) locatableCommand, actualWidgetInfo);
        }
        else {
            Log.e(TOOL_NAMEService.TAG, "This locatable step is unrecognizable " + locatableCommand);
            callback.onError("Unrecognizable Action");
            return;
        }
        if(actionResult){
            Log.i(TOOL_NAMEService.TAG, "Action is executed successfully!");
            callback.onCompleted();
        }
        else{
            Log.i(TOOL_NAMEService.TAG, "Action could not be executed!");
            callback.onError("Error on execution!");
        }
    }

    public abstract boolean executeClick(ClickCommand clickStep, ActualWidgetInfo actualWidgetInfo);
    public abstract boolean executeType(TypeCommand typeStep, ActualWidgetInfo actualWidgetInfo);
    public abstract boolean executeFocus(FocusCommand focusStep, ActualWidgetInfo actualWidgetInfo);
    public abstract void navigateNext(NextCommand nextStep, ExecutorCallback callback);
    public abstract void navigatePrevious(PreviousCommand previousStep, ExecutorCallback callback);
    public abstract void navigateJumpNext(JumpNextCommand nextStep, ExecutorCallback callback);
    public abstract void navigateJumpPrevious(JumpPreviousCommand previousStep, ExecutorCallback callback);
    public abstract void navigateSelect(SelectCommand selectCommand, ExecutorCallback callback);
    public abstract void navigateBack(BackCommand selectCommand, ExecutorCallback callback);
}
