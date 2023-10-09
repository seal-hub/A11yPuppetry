package PREFIX.TOOL_NAME.controller;

import android.util.Log;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;

import PREFIX.TOOL_NAME.ActionUtils;
import PREFIX.TOOL_NAME.ActualWidgetInfo;
import PREFIX.TOOL_NAME.ConceivedWidgetInfo;
import PREFIX.TOOL_NAME.Config;
import PREFIX.TOOL_NAME.TOOL_NAMEService;
import PREFIX.TOOL_NAME.UseCase.InfoCommand;
import PREFIX.TOOL_NAME.UseCase.LocatableCommand;
import PREFIX.TOOL_NAME.UseCase.NavigateCommand;
import PREFIX.TOOL_NAME.UseCase.Command;
import PREFIX.TOOL_NAME.Utils;
import PREFIX.TOOL_NAME.WidgetInfo;

public class Controller {
    private final Locator locator;
    private final ActionPerformer actionPerformer;
    public Controller(Locator locator, ActionPerformer actionPerformer){
        this.locator = locator;
        this.actionPerformer = actionPerformer;
    }

    public void clearResult(){
        String dir = TOOL_NAMEService.getInstance().getBaseContext().getFilesDir().getPath();
        new File(dir, Config.v().CONTROLLER_RESULT_FILE_NAME).delete();
    }

    public void interrupt(){
        locator.interrupt();
    }

    public void executeCommand(String stepCommandJson){
        Command command = Command.createCommandFromJSON(stepCommandJson);
        executeCommand(command);
    }

    public void executeCommand(Command command){
        clearResult();
        if(command == null){
            Log.e(TOOL_NAMEService.TAG, "The incoming Command is null!");
            writeResult(null);
            return;
        }

        command.setState(Command.CommandState.RUNNING);
        if(command instanceof LocatableCommand){
            executeLocatableStep((LocatableCommand) command);
        }
        else if (command instanceof NavigateCommand){
            navigate(command, (NavigateCommand) command);
        }
        else if (command instanceof InfoCommand){
            InfoCommand infoCommand = (InfoCommand) command;
            if(infoCommand.getQuestion().equals("a11y_focused")){
                WidgetInfo widgetInfo = ActualWidgetInfo.createFromA11yNode(TOOL_NAMEService.getInstance().getAccessibilityFocusedNode());
                if (widgetInfo != null) {
                    Log.i(TOOL_NAMEService.TAG, "The focused node is: " + widgetInfo + " Xpath: " + widgetInfo.getXpath());
                    JSONObject jsonCommand = widgetInfo.getJSONCommand("xpath", false, "click");
                    infoCommand.setJsonResult(jsonCommand);
                    infoCommand.setState(Command.CommandState.COMPLETED);
                }
                else{
                    Log.i(TOOL_NAMEService.TAG, "The focused node is null! ");
                    infoCommand.setState(Command.CommandState.FAILED);
                }
            }
            else if(infoCommand.getQuestion().equals("is_focused")){
                ConceivedWidgetInfo conceivedWidgetInfo = null;
                try {
                    conceivedWidgetInfo = ConceivedWidgetInfo.createFromJson(infoCommand.getExtra());
                    conceivedWidgetInfo.setLocatedBy("xpath");
                    Log.i(TOOL_NAMEService.TAG, "A   " + infoCommand.getExtra().keySet() );
                    Log.i(TOOL_NAMEService.TAG, "ConceivedWidgetInfo of target is " + conceivedWidgetInfo.getJSONCommand("Q",false,"Z") + " --------- " + infoCommand.getExtra().toJSONString() );
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TOOL_NAMEService.TAG, "ConceivedWidgetInfo cannot be created " + e.getLocalizedMessage());
                }
                ActualWidgetInfo targetWidgetInfo = ActionUtils.findActualWidget(conceivedWidgetInfo);
                if(targetWidgetInfo == null){
                    Log.e(TOOL_NAMEService.TAG, "TargetNode is null!");
                    infoCommand.setState(Command.CommandState.FAILED);
                }
                else {
                    Log.i(TOOL_NAMEService.TAG, "TargetNode is " + targetWidgetInfo);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("result", ActionUtils.isFocusedNodeTarget(targetWidgetInfo.getA11yNodeInfo()));
                    infoCommand.setJsonResult(jsonObject);
                    infoCommand.setState(Command.CommandState.COMPLETED);
                }
            }
            else{
                infoCommand.setState(Command.CommandState.FAILED);
            }
            writeResult(infoCommand);
        }
        else{
            Log.e(TOOL_NAMEService.TAG, "Unrecognizable Command!");
            writeResult(null);
        }
    }

    private void navigate(Command command, NavigateCommand navigateCommand) {
        ActionPerformer.ExecutorCallback callback = new ActionPerformer.ExecutorCallback() {
            @Override
            public void onCompleted() {
                onCompleted(null);
            }

            @Override
            public void onCompleted(ActualWidgetInfo navigatedWidget) {
                command.setState(Command.CommandState.COMPLETED);
                navigateCommand.setNavigatedWidget(navigatedWidget);
                writeResult(navigateCommand);
            }

            @Override
            public void onError(String message) {
                navigateCommand.setState(Command.CommandState.FAILED_PERFORM);
                Log.e(TOOL_NAMEService.TAG, String.format("Error in navigating command %s. Message: %s", navigateCommand, message));
                writeResult(navigateCommand);
            }
        };
        try {
            actionPerformer.navigate(navigateCommand, callback);
        }
        catch (Exception e){
            navigateCommand.setState(Command.CommandState.FAILED);
            Log.e(TOOL_NAMEService.TAG, String.format("An exception happened navigating command %s. Message: %s", navigateCommand, e.getMessage()));
            writeResult(navigateCommand);
        }
    }

    private void executeLocatableStep(LocatableCommand locatableCommand) {
        Locator.LocatorCallback locatorCallback = new Locator.LocatorCallback() {
            @Override
            public void onCompleted(ActualWidgetInfo actualWidgetInfo) {
                locatableCommand.setActedWidget(actualWidgetInfo);
                Log.i(TOOL_NAMEService.TAG, String.format("Performing command %s on Widget %s", locatableCommand, actualWidgetInfo));
                actionPerformer.execute(locatableCommand, actualWidgetInfo, new ActionPerformer.ExecutorCallback() {
                    @Override
                    public void onCompleted() {
                        onCompleted(null);
                    }

                    @Override
                    public void onCompleted(ActualWidgetInfo navigatedWidget) {
                        locatableCommand.setState(Command.CommandState.COMPLETED);
                        writeResult(locatableCommand);
                    }

                    @Override
                    public void onError(String message) {
                        locatableCommand.setState(Command.CommandState.FAILED_PERFORM);
                        Log.e(TOOL_NAMEService.TAG, String.format("Error in performing command %s. Message: %s", locatableCommand, message));
                        writeResult(locatableCommand);
                    }
                });
            }

            @Override
            public void onError(String message) {
                locatableCommand.setState(Command.CommandState.FAILED_LOCATE);
                Log.e(TOOL_NAMEService.TAG, String.format("Error in locating command %s. Message: %s", locatableCommand, message));
                writeResult(locatableCommand);
            }
        };
        try {
            locator.locate(locatableCommand, locatorCallback);
        }
        catch (Exception e){
            locatableCommand.setState(Command.CommandState.FAILED);
            Log.e(TOOL_NAMEService.TAG, String.format("An exception happened executing command %s. Message: %s", locatableCommand, e.getMessage()));
            writeResult(locatableCommand);
        }
    }

    private void writeResult(Command command){
        String jsonCommandStr = command != null ? command.getJSON().toJSONString() : "Error";
        Utils.createFile(Config.v().CONTROLLER_RESULT_FILE_NAME, jsonCommandStr);
    }
}
