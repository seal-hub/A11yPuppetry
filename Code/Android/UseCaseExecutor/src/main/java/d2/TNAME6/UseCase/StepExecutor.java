package PREFIX.TOOL_NAME.UseCase;

@Deprecated
public interface StepExecutor {
    boolean executeStep(Command step);
    boolean interrupt();
}
