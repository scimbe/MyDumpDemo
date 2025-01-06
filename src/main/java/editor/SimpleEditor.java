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
        
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        setJMenuBar(menuBar);
    }
    
    private void addMenuItem(JMenu menu, String title, KeyStroke accelerator, ActionListener listener) {
        JMenuItem item = new JMenuItem(title);
        if (accelerator != null) item.setAccelerator(accelerator);
        item.addActionListener(listener);
        menu.add(item);
    }
    
    private void setupDocumentListener() {
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { setModified(true); }
            public void removeUpdate(DocumentEvent e) { setModified(true); }
            public void changedUpdate(DocumentEvent e) { setModified(true); }
        });
    }
    
    private void setupWindowListener() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });
    }
    
    private void setupKeyBindings() {
        InputMap inputMap = textArea.getInputMap();
        ActionMap actionMap = textArea.getActionMap();
        
        // Add custom key bindings here if needed
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
    
    private void newFile() {
        if (checkSave()) {
            textArea.setText("");
            currentFile = null;
            setModified(false);
            statusBar.setText(" New file");
        }
    }
    
    private void openFile() {
        if (checkSave() && fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                textArea.read(reader, null);
                currentFile = file;
                setModified(false);
                statusBar.setText(" File opened: " + file.getName());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, 
                    "Error reading file: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private boolean saveFile() {
        if (currentFile == null) {
            return saveFileAs();
        }
        return saveToFile(currentFile);
    }
    
    private boolean saveFileAs() {
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            return saveToFile(fileChooser.getSelectedFile());
        }
        return false;
    }
    
    private boolean saveToFile(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            textArea.write(writer);
            currentFile = file;
            setModified(false);
            statusBar.setText(" File saved: " + file.getName());
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                "Error saving file: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    private boolean checkSave() {
        if (isModified) {
            int response = JOptionPane.showConfirmDialog(this,
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
    
    private void exit() {
        if (checkSave()) {
            dispose();
            System.exit(0);
        }
    }
    
    // Edit functions
    private void undo() {
        if (undoManager.canUndo()) {
            undoManager.undo();
            statusBar.setText(" Undo");
        }
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