package editor.actions;

import editor.SimpleEditor;

import javax.swing.*;
import javax.swing.undo.UndoManager;
import java.io.*;

public class FileActions {
    private final SimpleEditor editor;
    private final JTextArea textArea;
    private final JFileChooser fileChooser;
    private final UndoManager undoManager;

    public FileActions(SimpleEditor editor, JTextArea textArea, JFileChooser fileChooser, UndoManager undoManager) {
        this.editor = editor;
        this.textArea = textArea;
        this.fileChooser = fileChooser;
        this.undoManager = undoManager;
    }

    public void newFile() {
        if (checkSave()) {
            textArea.setText("");
            editor.setCurrentFile(null);
            editor.setModified(false);
            editor.setStatusText(" New file");
        }
    }

    public void openFile() {
        if (checkSave() && fileChooser.showOpenDialog(editor) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                textArea.read(reader, null);
                editor.setCurrentFile(file);
                editor.setModified(false);
                editor.setStatusText(" File opened: " + file.getName());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(editor, 
                    "Error reading file: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public boolean saveFile() {
        if (editor.getCurrentFile() == null) {
            return saveFileAs();
        }
        return saveToFile(editor.getCurrentFile());
    }

    public boolean saveFileAs() {
        if (fileChooser.showSaveDialog(editor) == JFileChooser.APPROVE_OPTION) {
            return saveToFile(fileChooser.getSelectedFile());
        }
        return false;
    }

    private boolean saveToFile(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            textArea.write(writer);
            editor.setCurrentFile(file);
            editor.setModified(false);
            editor.setStatusText(" File saved: " + file.getName());
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(editor, 
                "Error saving file: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean checkSave() {
        if (editor.isModified()) {
            int response = JOptionPane.showConfirmDialog(editor,
                "The current file has been modified. Save changes?",
                "Save Changes?",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE);
            
            if (response == JOptionPane.YES_OPTION) {
                return saveFile();
            }
            return response != JOptionPane.CANCEL_OPTION;
        }
        return true;
    }

    public void exit() {
        if (checkSave()) {
            editor.dispose();
            System.exit(0);
        }
    }
}