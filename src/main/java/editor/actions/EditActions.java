package editor.actions;

import javax.swing.*;
import javax.swing.undo.UndoManager;

public class EditActions {
    private final JTextArea textArea;
    private final UndoManager undoManager;

    public EditActions(JTextArea textArea, UndoManager undoManager) {
        this.textArea = textArea;
        this.undoManager = undoManager;
    }

    public void undo() {
        if (undoManager.canUndo()) {
            undoManager.undo();
        }
    }

    public void redo() {
        if (undoManager.canRedo()) {
            undoManager.redo();
        }
    }

    public void cut() {
        textArea.cut();
    }

    public void copy() {
        textArea.copy();
    }

    public void paste() {
        textArea.paste();
    }
}