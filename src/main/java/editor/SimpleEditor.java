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
    
    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // File Menu
        JMenu fileMenu = new JMenu("File");
        addMenuItem(fileMenu, "New", KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK), e -> newFile());
        addMenuItem(fileMenu, "Open", KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK), e -> openFile());
        addMenuItem(fileMenu, "Save", KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK), e -> saveFile());
        addMenuItem(fileMenu, "Save As", null, e -> saveFileAs());
        fileMenu.addSeparator();
        addMenuItem(fileMenu, "Exit", KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK), e -> exit());
        
        // Edit Menu
        JMenu editMenu = new JMenu("Edit");
        addMenuItem(editMenu, "Undo", KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK), e -> undo());
        addMenuItem(editMenu, "Redo", KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK), e -> redo());
        editMenu.addSeparator();
        addMenuItem(editMenu, "Cut", KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK), e -> cut());
        addMenuItem(editMenu, "Copy", KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK), e -> copy());
        addMenuItem(editMenu, "Paste", KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK), e -> paste());
        
        // Search Menu
        JMenu searchMenu = new JMenu("Search");
        addMenuItem(searchMenu, "Find/Replace", KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK), e -> showFindReplace());
        addMenuItem(searchMenu, "Find Next", KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0), e -> findNext());
        
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(searchMenu);
        setJMenuBar(menuBar);
    }
    
    private void createFindReplaceDialog() {
        findReplaceDialog = new JDialog(this, "Find/Replace", false);
        findReplaceDialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Find field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(5, 5, 5, 5);
        findReplaceDialog.add(new JLabel("Find:"), gbc);
        
        findField = new JTextField(20);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        findReplaceDialog.add(findField, gbc);
        
        // Replace field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        findReplaceDialog.add(new JLabel("Replace:"), gbc);
        
        replaceField = new JTextField(20);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        findReplaceDialog.add(replaceField, gbc);
        
        // Options panel
        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        caseSensitiveCheckbox = new JCheckBox("Case sensitive");
        wholeWordsCheckbox = new JCheckBox("Whole words only");
        optionsPanel.add(caseSensitiveCheckbox);
        optionsPanel.add(wholeWordsCheckbox);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        findReplaceDialog.add(optionsPanel, gbc);
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel();
        JButton findNextButton = new JButton("Find Next");
        JButton replaceButton = new JButton("Replace");
        JButton replaceAllButton = new JButton("Replace All");
        JButton closeButton = new JButton("Close");
        
        findNextButton.addActionListener(e -> findNext());
        replaceButton.addActionListener(e -> replace());
        replaceAllButton.addActionListener(e -> replaceAll());
        closeButton.addActionListener(e -> findReplaceDialog.setVisible(false));
        
        buttonsPanel.add(findNextButton);
        buttonsPanel.add(replaceButton);
        buttonsPanel.add(replaceAllButton);
        buttonsPanel.add(closeButton);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        findReplaceDialog.add(buttonsPanel, gbc);
        
        findReplaceDialog.pack();
        findReplaceDialog.setLocationRelativeTo(this);
    }
    
    private void showFindReplace() {
        findReplaceDialog.setVisible(true);
        findField.requestFocus();
    }
    
    private boolean matchesSearchCriteria(String text, String pattern, int position) {
        if (!caseSensitiveCheckbox.isSelected()) {
            text = text.toLowerCase();
            pattern = pattern.toLowerCase();
        }
        
        if (!wholeWordsCheckbox.isSelected()) {
            return text.indexOf(pattern, position) == position;
        }
        
        boolean startMatches = position == 0 || !Character.isLetterOrDigit(text.charAt(position - 1));
        boolean endMatches = (position + pattern.length() == text.length()) || 
                           !Character.isLetterOrDigit(text.charAt(position + pattern.length()));
        
        return startMatches && endMatches && text.indexOf(pattern, position) == position;
    }
    
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
    
    private void redo() {
        if (undoManager.canRedo()) {
            undoManager.redo();
            statusBar.setText(" Redo");
        }
    }
    
    private void cut() {
        textArea.cut();
        statusBar.setText(" Cut");
    }
    
    private void copy() {
        textArea.copy();
        statusBar.setText(" Copy");
    }
    
    private void paste() {
        textArea.paste();
        statusBar.setText(" Paste");
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