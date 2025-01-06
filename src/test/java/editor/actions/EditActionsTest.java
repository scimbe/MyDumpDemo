package editor.actions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.swing.*;
import javax.swing.undo.UndoManager;

import static org.junit.jupiter.api.Assertions.*;

public class EditActionsTest {
    private JTextArea textArea;
    private UndoManager undoManager;
    private EditActions editActions;

    @BeforeEach
    void setUp() {
        textArea = new JTextArea();
        undoManager = new UndoManager();
        textArea.getDocument().addUndoableEditListener(undoManager);
        editActions = new EditActions(textArea, undoManager);
    }

    @Test
    void shouldHandleUndoRedo() {
        textArea.setText("Test text");
        assertEquals("Test text", textArea.getText());

        editActions.undo();
        assertEquals("", textArea.getText());

        editActions.redo();
        assertEquals("Test text", textArea.getText());
    }

    @Test
    void shouldHandleCutCopyPaste() {
        textArea.setText("Test text");
        textArea.selectAll();

        editActions.copy();
        editActions.cut();
        assertEquals("", textArea.getText());

        editActions.paste();
        assertEquals("Test text", textArea.getText());
    }
}