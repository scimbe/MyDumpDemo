package editor;

import editor.ui.EditorMenuBar;
import editor.ui.FindReplaceDialog;
import editor.listener.EditorDocumentListener;
import editor.listener.EditorWindowListener;
import editor.actions.FileActions;
import editor.actions.EditActions;

import javax.swing.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.io.File;

public class SimpleEditor extends JFrame {
    private JTextArea textArea;
    private JFileChooser fileChooser;
    private File currentFile;
    private boolean isModified;
    private UndoManager undoManager;
    private JLabel statusBar;
    private FindReplaceDialog findReplaceDialog;
    private FileActions fileActions;
    private EditActions editActions;

    public SimpleEditor() {
        initComponents();
        setupUI();
        setupListeners();
    }

    private void initComponents() {
        setTitle("Simple Editor");
        setSize(800, 600);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        
        undoManager = new UndoManager();
        textArea = new JTextArea();
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.getDocument().addUndoableEditListener(undoManager);
        
        fileChooser = new JFileChooser();
        statusBar = new JLabel(" Ready");
        isModified = false;
        
        fileActions = new FileActions(this, textArea, fileChooser, undoManager);
        editActions = new EditActions(textArea, undoManager);
    }

    private void setupUI() {
        setJMenuBar(new EditorMenuBar(this, fileActions, editActions));
        add(new JScrollPane(textArea));
        add(statusBar, BorderLayout.SOUTH);
        
        findReplaceDialog = new FindReplaceDialog(this, textArea, statusBar);
    }

    private void setupListeners() {
        addWindowListener(new EditorWindowListener(this, fileActions));
        textArea.getDocument().addDocumentListener(
            new EditorDocumentListener(this)
        );
    }

    public void setModified(boolean modified) {
        isModified = modified;
        updateTitle();
    }

    public void updateTitle() {
        String title = "Simple Editor";
        if (currentFile != null) {
            title = currentFile.getName() + " - " + title;
        }
        if (isModified) {
            title = "* " + title;
        }
        setTitle(title);
    }

    public boolean isModified() {
        return isModified;
    }

    public void setCurrentFile(File file) {
        this.currentFile = file;
    }

    public File getCurrentFile() {
        return currentFile;
    }

    public void setStatusText(String text) {
        statusBar.setText(text);
    }

    public void showFindReplaceDialog() {
        findReplaceDialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SimpleEditor().setVisible(true);
        });
    }
}
