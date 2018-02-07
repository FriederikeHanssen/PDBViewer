package command;
/**
 * command pattern
 * Daniel Huson, 12.2017
 */
abstract class ACommand {
    private String name;
    private String description;

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    abstract public void execute() throws Exception;

    abstract public void undo() throws Exception;

    abstract public void redo() throws Exception;

    public boolean isExecutable() {
        return true;
    }

    public boolean isUndoable() {
        return true;
    }

    public boolean isRedoable() {
        return true;
    }
}
