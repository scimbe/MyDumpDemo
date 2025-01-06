package editor;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class SimpleEditor extends JFrame {

    // [Previous code remains unchanged...]

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
