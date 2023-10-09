package PREFIX.TOOL_NAME.UseCase;

import android.util.Log;

import org.json.simple.JSONObject;

import PREFIX.TOOL_NAME.TOOL_NAMEService;

public class JumpNextCommand extends NavigateCommand {

    JumpNextCommand(JSONObject stepJson) {
        super(stepJson);
        Log.i(TOOL_NAMEService.TAG, "Jump Next Step");
    }

    public static boolean isJumpNextAction(String action){
        return action.equals("jump_next");
    }


    @Override
    public String toString() {
        return "JumpNextStep{" +
                "State=" + getState().name() +
                '}';
    }
}
