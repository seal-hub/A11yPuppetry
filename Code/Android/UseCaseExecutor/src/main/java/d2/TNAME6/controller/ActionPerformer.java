package PREFIX.TOOL_NAME.controller;

import PREFIX.TOOL_NAME.ActualWidgetInfo;
import PREFIX.TOOL_NAME.UseCase.LocatableCommand;
import PREFIX.TOOL_NAME.UseCase.NavigateCommand;

public interface ActionPerformer {
    public interface ExecutorCallback{
        void onCompleted();
        void onCompleted(ActualWidgetInfo navigatedWidget);
        void onError(String message);
    }
    void execute(LocatableCommand locatableCommand, ActualWidgetInfo actualWidgetInfo, ExecutorCallback callback);
    void navigate(NavigateCommand navigateCommand, ExecutorCallback callback);
}

class DummyExecutorCallback implements ActionPerformer.ExecutorCallback{

    @Override
    public void onCompleted() {

    }

    @Override
    public void onCompleted(ActualWidgetInfo navigatedWidget) {

    }

    @Override
    public void onError(String message) {

    }
}
