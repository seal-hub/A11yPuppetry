package PREFIX.TOOL_NAME.UseCase;

import android.util.Log;

import org.json.simple.JSONObject;

import PREFIX.TOOL_NAME.TOOL_NAMEService;

public class SelectCommand extends NavigateCommand {

    SelectCommand(JSONObject stepJson) {
        super(stepJson);
        Log.i(TOOL_NAMEService.TAG, "Select Command");
    }

    public static boolean isSelectCommand(String action){
        return action.equals("select");
    }


    @Override
    public String toString() {
        return "SelectStep{" +
                "State=" + getState().name() +
                '}';
    }
}
