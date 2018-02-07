package command;

import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Command manager
 * Daniel Huson, 12.2017
 */
@SuppressWarnings("ALL")
public class CommandManager {
    private final ObservableList<ACommand> undoStack = FXCollections.observableArrayList();
    private final ObservableList<ACommand> redoStack = FXCollections.observableArrayList();

    private final StringProperty undoName = new SimpleStringProperty("Undo");
    private final StringProperty redoName = new SimpleStringProperty("Redo");

    /**
     * default constructor
     */
    public CommandManager() {
        undoStack.addListener((InvalidationListener) (e) -> undoName.set(undoStack.size() > 0 ? "Undo " + undoStack.get(undoStack.size() - 1).getName() : "Undo"));
        redoStack.addListener((InvalidationListener) (e) -> redoName.set(redoStack.size() > 0 ? "Redo " + redoStack.get(redoStack.size() - 1).getName() : "Redo"));
    }

    /**
     * clear
     */
    public void clear() {
        undoStack.clear();
        redoStack.clear();
    }

    /**
     * add a command to the undoable stack
     *
     * @param command
     */
    public void add(ACommand command) {
        if (command.isExecutable()) {
            if (command.isUndoable())
                undoStack.add(command);
            else
                undoStack.clear();
            redoStack.clear();
        }
    }

    /**
     * execute the command and then add to undoable stack
     *
     * @param command
     * @throws Exception
     */
    public void executeAndAdd(ACommand command) throws Exception {
        if (command.isExecutable()) {
            command.execute();
            add(command);
        }
    }

    /**
     * undo the current undoable command
     *
     * @throws IllegalStateException if no current undoable command
     * @throws Exception
     */
    public void undo() throws Exception {
        if (undoStack.size() == 0)
            throw new IllegalStateException("Undo stack empty");
        final ACommand command = undoStack.remove(undoStack.size() - 1);
        if (command.isRedoable())
            redoStack.add(command);
        else
            redoStack.clear();
        command.undo();
    }

    /**
     * redo the current redoable event
     *
     * @throws IllegalStateException if no current redoable command
     * @throws Exception
     */
    public void redo() throws Exception {
        if (redoStack.size() == 0)
            throw new IllegalStateException("Redo stack empty");
        final ACommand command = redoStack.remove(redoStack.size() - 1);

        if (command.isUndoable())
            undoStack.add(command);
        else
            undoStack.clear();
        command.redo();
    }

    public ReadOnlyBooleanProperty canUndoProperty() {
        final BooleanProperty canUndo = new SimpleBooleanProperty();
        canUndo.bind(Bindings.isNotEmpty(undoStack));
        return canUndo;
    }

    public ReadOnlyBooleanProperty canRedoProperty() {
        final BooleanProperty canRedo = new SimpleBooleanProperty();
        canRedo.bind(Bindings.isNotEmpty(redoStack));
        return canRedo;
    }

    /**
     * get the name of current undoable command
     *
     * @return name property
     */
    public ReadOnlyStringProperty undoNameProperty() {

        return undoName;
    }

    /**
     * get the name of current redoable command
     *
     * @return name property
     */
    public ReadOnlyStringProperty redoNameProperty() {
        return redoName;
    }
}

