package PREFIX.TOOL_NAME.UseCase;

import android.util.Log;

import org.json.simple.JSONObject;

import PREFIX.TOOL_NAME.TOOL_NAMEService;

public class NextCommand extends NavigateCommand {

    NextCommand(JSONObject stepJson) {
        super(stepJson);
        Log.i(TOOL_NAMEService.TAG, "Next Step");
    }

    public static boolean isNextAction(String action){
        return action.equals("next");
    }


    @Override
    public String toString() {
        return "NextStep{" +
                "State=" + getState().name() +
                '}';
    }
}
