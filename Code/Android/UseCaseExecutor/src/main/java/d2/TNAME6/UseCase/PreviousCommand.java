package PREFIX.TOOL_NAME.UseCase;

import android.util.Log;

import org.json.simple.JSONObject;

import PREFIX.TOOL_NAME.TOOL_NAMEService;

public class PreviousCommand extends NavigateCommand {

    PreviousCommand(JSONObject stepJson) {
        super(stepJson);
        Log.i(TOOL_NAMEService.TAG, "Previous Step");
    }

    public static boolean isPreviousAction(String action){
        return action.equals("previous");
    }


    @Override
    public String toString() {
        return "PreviousStep{" +
                "State=" + getState().name() +
                '}';
    }
}
