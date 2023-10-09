package PREFIX.TOOL_NAME.UseCase;

import android.util.Log;

import org.json.simple.JSONObject;

import PREFIX.TOOL_NAME.TOOL_NAMEService;

public class BackCommand extends NavigateCommand {

    BackCommand(JSONObject stepJson) {
        super(stepJson);
        Log.i(TOOL_NAMEService.TAG, "Back Step");
    }

    public static boolean isBackAction(String action){
        return action.equals("back");
    }


    @Override
    public String toString() {
        return "BackStep{" +
                "State=" + getState().name() +
                '}';
    }
}
