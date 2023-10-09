package PREFIX.TOOL_NAME.controller;

import android.os.Handler;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import PREFIX.TOOL_NAME.ActualWidgetInfo;
import PREFIX.TOOL_NAME.ConceivedWidgetInfo;
import PREFIX.TOOL_NAME.TOOL_NAMEService;
import PREFIX.TOOL_NAME.UseCase.LocatableCommand;
import PREFIX.TOOL_NAME.Utils;

public abstract class AbstractLocator implements Locator {
    enum LocatorStatus{
        WAITING,
        COMPLETED,
        FAILED
    }
    static class LocatorResult{
        ActualWidgetInfo actualWidgetInfo;
        LocatorStatus status;
        String message;

        LocatorResult(){
            actualWidgetInfo = null;
            status = LocatorStatus.FAILED;
            message = "Initialized";
        }

        LocatorResult(ActualWidgetInfo actualWidgetInfo){
            this();
            if(actualWidgetInfo != null){
                status = LocatorStatus.COMPLETED;
                this.actualWidgetInfo = actualWidgetInfo;
                message = "Completed";
            }
        }
        LocatorResult(LocatorStatus status, String message){
            this();
            this.status = status;
            this.message = message;
        }
        LocatorResult(LocatorStatus status){
            this(status, "S: " + status.name());
        }
    }
    private static long delay = 1000; // TODO: Configurable
    private static final AtomicInteger locatorId = new AtomicInteger(0);

    public final synchronized void locate(LocatableCommand locatableCommand, Locator.LocatorCallback callback){
        if(callback == null)
            callback = new DummyLocatorCallback();
        locatorId.incrementAndGet();
        Log.d(TOOL_NAMEService.TAG, this.getClass().getSimpleName() +":" + locatorId.intValue() + " locating " + locatableCommand);
        Locator.LocatorCallback finalCallback = callback;
        new Handler().post(() -> this.locateTask(locatableCommand, finalCallback, locatorId.intValue()));
    }

    @Override
    public synchronized void interrupt() {
        locatorId.incrementAndGet();
    }

    private synchronized void  locateTask(LocatableCommand locatableCommand, Locator.LocatorCallback callback, int myLocatorId){
        if(locatableCommand == null || locatableCommand.getTargetWidgetInfo() == null)
        {
            Log.e(TOOL_NAMEService.TAG, this.getClass().getSimpleName()+":" + myLocatorId + " locating null!");
            callback.onError("The locatableStep is null");
            return;
        }
        Log.d(TOOL_NAMEService.TAG, this.getClass().getSimpleName() +":" + myLocatorId + " locating " + locatableCommand);
        if(myLocatorId != locatorId.intValue()){
            Log.d(TOOL_NAMEService.TAG, this.getClass().getSimpleName() + " is interrupted!");
            callback.onError("The targetWidget is null");
            return;
        }
        if (locatableCommand.reachedMaxLocatingAttempts()){
            Log.d(TOOL_NAMEService.TAG, "Reached max attempt for " + this.getClass().getSimpleName() + " locating " + locatableCommand);
            callback.onError("Reached Max Locating Attempt");
            return;
        }
        LocatorResult locatorResult = locateAttempt(locatableCommand.getTargetWidgetInfo());
        if (locatorResult == null || locatorResult.status == LocatorStatus.FAILED)
        {
            Log.d(TOOL_NAMEService.TAG, this.getClass().getSimpleName() +":" + myLocatorId + " is FAILED!");
            String errorMessage = locatorResult != null ? locatorResult.message : "No LocatorResult";
            callback.onError(errorMessage);
            return;
        }
        else if (locatorResult.status == LocatorStatus.COMPLETED)
        {
            Log.d(TOOL_NAMEService.TAG, this.getClass().getSimpleName() +":" + myLocatorId+ " is COMPLETED!");
            if (locatorResult.actualWidgetInfo == null)
                callback.onError("Locator is completed but the actualWidgetInfo is null");
            else
                new Handler().post(() -> callback.onCompleted(locatorResult.actualWidgetInfo));
            return;
        }
        else if (locatorResult.status == LocatorStatus.WAITING){
            locatableCommand.increaseLocatingAttempts();
            Log.d(TOOL_NAMEService.TAG, this.getClass().getSimpleName() +":" + myLocatorId + " is WAITING! Attempt: " + locatableCommand.getNumberOfLocatingAttempts());
            new Handler().postDelayed(() -> this.locateTask(locatableCommand, callback, myLocatorId), delay);
            return;
        }
        callback.onError("Unknown Error");
    }

    protected abstract LocatorResult locateAttempt(ConceivedWidgetInfo targetWidget);
}
