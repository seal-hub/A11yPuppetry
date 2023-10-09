package PREFIX.TOOL_NAME.UseCase;

import android.util.Log;

import org.json.simple.JSONObject;

import PREFIX.TOOL_NAME.TOOL_NAMEService;

public class JumpPreviousCommand extends NavigateCommand {

    JumpPreviousCommand(JSONObject stepJson) {
        super(stepJson);
        Log.i(TOOL_NAMEService.TAG, "Jump Previous Step");
    }

    public static boolean isJumpPreviousAction(String action){
        return action.equals("jump_previous");
    }


    @Override
    public String toString() {
        return "JumpPreviousStep{" +
                "State=" + getState().name() +
                '}';
    }
}
