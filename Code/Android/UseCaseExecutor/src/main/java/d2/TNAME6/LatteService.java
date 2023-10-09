package PREFIX.TOOL_NAME;

import android.accessibilityservice.AccessibilityService;
import android.content.IntentFilter;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;

import org.json.simple.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import PREFIX.TOOL_NAME.UseCase.RegularStepExecutor;
import PREFIX.TOOL_NAME.UseCase.SightedTalkBackStepExecutor;
import PREFIX.TOOL_NAME.UseCase.StepExecutor;
import PREFIX.TOOL_NAME.UseCase.TalkBackStepExecutor;
import PREFIX.TOOL_NAME.controller.A11yAPIActionPerformer;
import PREFIX.TOOL_NAME.controller.BaseLocator;
import PREFIX.TOOL_NAME.controller.Controller;
import PREFIX.TOOL_NAME.controller.TalkBackAPILocator;
import PREFIX.TOOL_NAME.controller.TalkBackActionPerformer;
import PREFIX.TOOL_NAME.controller.TalkBackTouchLocator;
import PREFIX.TOOL_NAME.controller.TouchActionPerformer;
import PREFIX.TOOL_NAME.controller.TouchLocator;

public class TOOL_NAMEService extends AccessibilityService {
    private static TOOL_NAMEService instance;

    public AccessibilityNodeInfo getAccessibilityFocusedNode() {
        return accessibilityFocusedNode;
    }

    public AccessibilityNodeInfo getFocusedNode() {
        return focusedNode;
    }

    private AccessibilityNodeInfo focusedNode;
    private AccessibilityNodeInfo accessibilityFocusedNode;
    MessageReceiver receiver;
    public boolean isConnected() {
        return connected;
    }

    private boolean connected = false;
    public static String TAG = "TOOL_NAME_SERVICE";
    private String A11Y_EVENT_TAG = "TOOL_NAME_A11Y_EVENT_TAG";
    public static boolean considerInvisibleNodes = true;

    public static TOOL_NAMEService getInstance() {
        return instance;
    }
    public TOOL_NAMEService() {
    }

    private Map<String, StepExecutor> stepExecutorsMap = new HashMap<>();

    public boolean addStepExecutor(String key, StepExecutor stepExecutor){
        if(stepExecutorsMap.containsKey(key))
            return false;
        stepExecutorsMap.put(key, stepExecutor);
        return true;
    }

    public StepExecutor getStepExecutor(String key){
        return stepExecutorsMap.getOrDefault(key, null);
    }

    private Map<String, Controller> controllerMap = new HashMap<>();

    public Controller getSelectedController() {
        return selectedController;
    }

    public void setSelectedController(Controller selectedController) {
        this.selectedController = selectedController;
    }

    private Controller selectedController = null;

    public boolean addController(String key, Controller controller){
        if(controllerMap.containsKey(key))
            return false;
        controllerMap.put(key, controller);
        return true;
    }

    public Controller getController(String key){
        return controllerMap.getOrDefault(key, null);
    }

    @Override
    protected void onServiceConnected() {
        Log.i(TAG, "TOOL_NAME Service has started!");
        File dir = new File(getBaseContext().getFilesDir().getPath());
        for(File file : dir.listFiles())
            if(!file.isDirectory())
                file.delete();
        receiver = new MessageReceiver();
        registerReceiver(receiver, new IntentFilter(MessageReceiver.MESSAGE_INTENT));
        instance = this;
        connected = true;
        addStepExecutor("regular", new RegularStepExecutor());
        addStepExecutor("talkback", new TalkBackStepExecutor());
        addStepExecutor("sighted_tb", new SightedTalkBackStepExecutor());

        addController("touch", new Controller(new TouchLocator(), new TouchActionPerformer()));
        addController("a11y_api", new Controller(new BaseLocator(), new A11yAPIActionPerformer()));
        addController("tb_api", new Controller(new TalkBackAPILocator(), new TalkBackActionPerformer()));
        addController("tb_touch", new Controller(new TalkBackTouchLocator(), new TalkBackActionPerformer()));
        selectedController = getController("touch");
    }

    @Override
    public void onDestroy() {
        connected = false;
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if(event == null){
            Log.i(TAG, "Incomming event is null!");
            return;
        }

        AccessibilityNodeInfo nodeInfo = event.getSource();
        ActualWidgetInfo widgetInfo = ActualWidgetInfo.createFromA11yNode(nodeInfo, false);
        JSONObject jsonEelement = null;
        if (widgetInfo != null)
            jsonEelement = widgetInfo.getJSONCommand("", false, "");
        JSONObject jsonEvent = new JSONObject();
        jsonEvent.put("Element", jsonEelement);
        Log.i(A11Y_EVENT_TAG, "Event: " + AccessibilityEvent.eventTypeToString(event.getEventType()) + " " + jsonEvent.toJSONString());

        if(event.getEventType() == AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED) {
            accessibilityFocusedNode = event.getSource();
        }
        else if(event.getEventType() == AccessibilityEvent.TYPE_VIEW_FOCUSED) {
            focusedNode = event.getSource();
        }
        else if(event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            AccessibilityWindowInfo changedWindow = null;
            for(AccessibilityWindowInfo windowInfo : getWindows())
                if(windowInfo.getId() == event.getWindowId()){
                    changedWindow = windowInfo;
                    break;
                }
            AccessibilityWindowInfo activeWindow = null;
            AccessibilityNodeInfo rootInActiveWindow = getRootInActiveWindow();
            if(rootInActiveWindow != null)
                activeWindow = rootInActiveWindow.getWindow();
            JSONObject jsonWindowContentChange = null;
            int activeWindowId = activeWindow != null ? activeWindow.getId() : -1;
            String activeWindowTitle = activeWindow != null && activeWindow.getTitle() != null ? activeWindow.getTitle().toString() : "null";
            String changedWindowTitle = changedWindow != null && changedWindow.getTitle() != null ? changedWindow.getTitle().toString() : "null";
            jsonWindowContentChange = new JSONObject();
            jsonWindowContentChange.put("changedWindowId", event.getWindowId());
            jsonWindowContentChange.put("changedWindowTitle", changedWindowTitle);
            jsonWindowContentChange.put("activeWindowId", activeWindowId);
            jsonWindowContentChange.put("activeWindowTitle", activeWindowTitle);
            jsonWindowContentChange.put("Element", jsonEelement);
            Log.i(A11Y_EVENT_TAG, "WindowContentChange: " + jsonWindowContentChange.toJSONString());
        }
    }

    @Override
    public void onInterrupt() {

    }
}
