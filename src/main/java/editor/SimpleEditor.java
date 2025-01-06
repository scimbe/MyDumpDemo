package editor;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class SimpleEditor extends JFrame {
    private JTextArea textArea;
    private JFileChooser fileChooser;
    private File currentFile;
    private boolean isModified;
    private UndoManager undoManager;
    private JLabel statusBar;
    private JDialog findReplaceDialog;
    private JTextField findField;
    private JTextField replaceField;
    private int lastSearchIndex = 0;
    private JCheckBox caseSensitiveCheckbox;
    private JCheckBox wholeWordsCheckbox;

    public SimpleEditor() {
        setTitle("Simple Editor");
        setSize(800, 600);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        
        // Initialize components
        undoManager = new UndoManager();
        textArea = new JTextArea();
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.getDocument().addUndoableEditListener(undoManager);
        
        fileChooser = new JFileChooser();
        statusBar = new JLabel(" Ready");
        isModified = false;
        
        // Initialize search components
        caseSensitiveCheckbox = new JCheckBox("Case sensitive");
        wholeWordsCheckbox = new JCheckBox("Whole words only");
        
        // Setup menu
        setupMenuBar();
        
        // Setup document listener
        setupDocumentListener();
        
        // Setup window listener
        setupWindowListener();
        
        // Add components to frame
        add(new JScrollPane(textArea));
        add(statusBar, BorderLayout.SOUTH);
        
        // Setup key bindings
        setupKeyBindings();
        
        // Create find/replace dialog
        createFindReplaceDialog();
    }

    // [Previous methods remain unchanged...]

    private void findNext() {
        String searchText = findField.getText();
        String content = textArea.getText();
        
        if (!caseSensitiveCheckbox.isSelected()) {
            content = content.toLowerCase();
            searchText = searchText.toLowerCase();
        }
        
        if (searchText.isEmpty()) {
            statusBar.setText(" Nothing to search for");
            return;
        }
        
        int startIndex = lastSearchIndex + 1;
        if (startIndex >= content.length()) {
            startIndex = 0;
        }
        
        int index = -1;
        for (int i = startIndex; i < content.length(); i++) {
            if (matchesSearchCriteria(content, searchText, i)) {
                index = i;
                break;
            }
        }
        
        if (index == -1 && startIndex > 0) {
            // Try again from the beginning
            for (int i = 0; i < startIndex; i++) {
                if (matchesSearchCriteria(content, searchText, i)) {
                    index = i;
                    break;
                }
            }
        }
        
        if (index == -1) {
            statusBar.setText(" Text not found: " + searchText);
            return;
        }
        
        textArea.setSelectionStart(index);
        textArea.setSelectionEnd(index + searchText.length());
        textArea.requestFocus();
        lastSearchIndex = index;
        statusBar.setText(" Found at position: " + (index + 1));
    }

    private void replace() {
        if (textArea.getSelectedText() == null) {
            findNext();
            return;
        }
        
        String replaceText = replaceField.getText();
        textArea.replaceSelection(replaceText);
        lastSearchIndex = textArea.getSelectionStart();
        findNext();
    }

    private void replaceAll() {
        String searchText = findField.getText();
        String replaceText = replaceField.getText();
        String content = textArea.getText();
        
        if (searchText.isEmpty()) {
            statusBar.setText(" Nothing to replace");
            return;
        }
        
        int count = 0;
        StringBuilder newContent = new StringBuilder();
        int lastIndex = 0;
        
        if (!caseSensitiveCheckbox.isSelected()) {
            searchText = searchText.toLowerCase();
            content = content.toLowerCase();
        }
        
        for (int i = 0; i < content.length(); i++) {
            if (matchesSearchCriteria(content, searchText, i)) {
                newContent.append(textArea.getText().substring(lastIndex, i));
                newContent.append(replaceText);
                lastIndex = i + searchText.length();
                i = lastIndex - 1;
                count++;
            }
        }
        
        newContent.append(textArea.getText().substring(lastIndex));
        textArea.setText(newContent.toString());
        statusBar.setText(" Replaced " + count + " occurrences");
    }

    private void setModified(boolean modified) {
        isModified = modified;
        updateTitle();
    }

    private void updateTitle() {
        String title = "Simple Editor";
        if (currentFile != null) {
            title = currentFile.getName() + " - " + title;
        }
        if (isModified) {
            title = "* " + title;
        }
        setTitle(title);
    }

    public String getText() {
        return textArea.getText();
    }
    
    public void setText(String text) {
        textArea.setText(text);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SimpleEditor().setVisible(true);
        });
    }
}