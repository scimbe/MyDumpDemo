package editor.listener;

import editor.SimpleEditor;
import editor.actions.FileActions;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class EditorWindowListener extends WindowAdapter {
    private final SimpleEditor editor;
    private final FileActions fileActions;

    public EditorWindowListener(SimpleEditor editor, FileActions fileActions) {
        this.editor = editor;
        this.fileActions = fileActions;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        fileActions.exit();
    }
}