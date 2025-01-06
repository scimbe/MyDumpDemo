package editor.actions;

import javax.swing.*;

public class SearchActions {
    private final JTextArea textArea;
    private final JLabel statusBar;
    private int lastSearchIndex = 0;

    public SearchActions(JTextArea textArea, JLabel statusBar) {
        this.textArea = textArea;
        this.statusBar = statusBar;
    }

    public void findNext(String searchText, boolean caseSensitive, boolean wholeWords) {
        String content = textArea.getText();
        searchText = caseSensitive ? searchText : searchText.toLowerCase();
        content = caseSensitive ? content : content.toLowerCase();

        if (searchText.isEmpty()) {
            statusBar.setText(" Nothing to search for");
            return;
        }

        int startIndex = lastSearchIndex + 1;
        if (startIndex >= content.length()) {
            startIndex = 0;
        }

        int index = findMatchingPosition(content, searchText, startIndex, wholeWords);
        if (index == -1 && startIndex > 0) {
            index = findMatchingPosition(content, searchText, 0, wholeWords);
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

    public void replace(String searchText, String replaceText, boolean caseSensitive, boolean wholeWords) {
        if (textArea.getSelectedText() == null) {
            findNext(searchText, caseSensitive, wholeWords);
            return;
        }
        textArea.replaceSelection(replaceText);
        lastSearchIndex = textArea.getSelectionStart();
        findNext(searchText, caseSensitive, wholeWords);
    }

    public void replaceAll(String searchText, String replaceText, boolean caseSensitive, boolean wholeWords) {
        if (searchText.isEmpty()) {
            statusBar.setText(" Nothing to replace");
            return;
        }

        String content = textArea.getText();
        String searchPattern = caseSensitive ? searchText : searchText.toLowerCase();
        String workingContent = caseSensitive ? content : content.toLowerCase();

        int count = 0;
        StringBuilder newContent = new StringBuilder();
        int lastIndex = 0;

        for (int i = 0; i < workingContent.length(); i++) {
            if (isMatch(workingContent, searchPattern, i, wholeWords)) {
                newContent.append(content.substring(lastIndex, i));
                newContent.append(replaceText);
                lastIndex = i + searchPattern.length();
                i = lastIndex - 1;
                count++;
            }
        }

        newContent.append(content.substring(lastIndex));
        textArea.setText(newContent.toString());
        statusBar.setText(" Replaced " + count + " occurrences");
    }

    private int findMatchingPosition(String content, String searchText, int startIndex, boolean wholeWords) {
        for (int i = startIndex; i < content.length(); i++) {
            if (isMatch(content, searchText, i, wholeWords)) {
                return i;
            }
        }
        return -1;
    }

    private boolean isMatch(String text, String pattern, int position, boolean wholeWords) {
        if (!text.regionMatches(position, pattern, 0, pattern.length())) {
            return false;
        }

        if (!wholeWords) {
            return true;
        }

        boolean startMatches = position == 0 || !Character.isLetterOrDigit(text.charAt(position - 1));
        boolean endMatches = (position + pattern.length() == text.length()) ||
                           !Character.isLetterOrDigit(text.charAt(position + pattern.length()));

        return startMatches && endMatches;
    }
}