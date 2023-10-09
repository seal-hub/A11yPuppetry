package PREFIX.TOOL_NAME.UseCase;

import org.json.simple.JSONObject;

import PREFIX.TOOL_NAME.ActualWidgetInfo;

public abstract class NavigateCommand extends Command {
    NavigateCommand(JSONObject stepJson) {
        super(stepJson);
    }
    @Override
    public JSONObject getJSON() {
        JSONObject jsonObject = super.getJSON();
        jsonObject.put("navigatedWidget", navigatedWidget == null ? null : navigatedWidget.getJSONCommand("", false, ""));
        return jsonObject;
    }

    public ActualWidgetInfo getNavigatedWidget() {
        return navigatedWidget;
    }

    public void setNavigatedWidget(ActualWidgetInfo navigatedWidget) {
        this.navigatedWidget = navigatedWidget;
    }

    private ActualWidgetInfo navigatedWidget = null;
}
