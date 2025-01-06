package editor.listener;

import editor.SimpleEditor;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class EditorDocumentListener implements DocumentListener {
    private final SimpleEditor editor;

    public EditorDocumentListener(SimpleEditor editor) {
        this.editor = editor;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        editor.setModified(true);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        editor.setModified(true);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        editor.setModified(true);
    }
}