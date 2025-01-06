package editor.ui;

import editor.SimpleEditor;
import editor.actions.FileActions;
import editor.actions.EditActions;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class EditorMenuBar extends JMenuBar {
    private final SimpleEditor editor;
    private final FileActions fileActions;
    private final EditActions editActions;

    public EditorMenuBar(SimpleEditor editor, FileActions fileActions, EditActions editActions) {
        this.editor = editor;
        this.fileActions = fileActions;
        this.editActions = editActions;
        
        createFileMenu();
        createEditMenu();
        createSearchMenu();
    }

    private void createFileMenu() {
        JMenu fileMenu = new JMenu("File");
        addMenuItem(fileMenu, "New", KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK), e -> fileActions.newFile());
        addMenuItem(fileMenu, "Open", KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK), e -> fileActions.openFile());
        addMenuItem(fileMenu, "Save", KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK), e -> fileActions.saveFile());
        addMenuItem(fileMenu, "Save As", null, e -> fileActions.saveFileAs());
        fileMenu.addSeparator();
        addMenuItem(fileMenu, "Exit", KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK), e -> fileActions.exit());
        add(fileMenu);
    }

    private void createEditMenu() {
        JMenu editMenu = new JMenu("Edit");
        addMenuItem(editMenu, "Undo", KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK), e -> editActions.undo());
        addMenuItem(editMenu, "Redo", KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK), e -> editActions.redo());
        editMenu.addSeparator();
        addMenuItem(editMenu, "Cut", KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK), e -> editActions.cut());
        addMenuItem(editMenu, "Copy", KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK), e -> editActions.copy());
        addMenuItem(editMenu, "Paste", KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK), e -> editActions.paste());
        add(editMenu);
    }

    private void createSearchMenu() {
        JMenu searchMenu = new JMenu("Search");
        addMenuItem(searchMenu, "Find/Replace", KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK), 
            e -> editor.showFindReplaceDialog());
        add(searchMenu);
    }

    private void addMenuItem(JMenu menu, String title, KeyStroke accelerator, java.awt.event.ActionListener listener) {
        JMenuItem item = new JMenuItem(title);
        if (accelerator != null) item.setAccelerator(accelerator);
        item.addActionListener(listener);
        menu.add(item);
    }
}
