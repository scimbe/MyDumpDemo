package editor.actions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

public class SearchActionsTest {
    private JTextArea textArea;
    private JLabel statusBar;
    private SearchActions searchActions;

    @BeforeEach
    void setUp() {
        textArea = new JTextArea();
        statusBar = new JLabel();
        searchActions = new SearchActions(textArea, statusBar);
    }

    @Test
    void shouldFindTextWithCaseInsensitive() {
        textArea.setText("This is a Test text with TEST pattern and test ending.");
        searchActions.findNext("test", false, false);
        assertEquals("test", textArea.getSelectedText().toLowerCase());
    }

    @Test
    void shouldFindTextWithCaseSensitive() {
        textArea.setText("This is a Test text with TEST pattern.");
        searchActions.findNext("TEST", true, false);
        assertEquals("TEST", textArea.getSelectedText());
    }

    @Test
    void shouldFindWholeWords() {
        textArea.setText("testing test tested");
        searchActions.findNext("test", false, true);
        assertEquals("test", textArea.getSelectedText().toLowerCase());
    }

    @Test
    void shouldReplaceSelectedText() {
        textArea.setText("old text");
        textArea.setSelectionStart(0);
        textArea.setSelectionEnd(3);
        searchActions.replace("old", "new", false, false);
        assertEquals("new text", textArea.getText());
    }

    @Test
    void shouldReplaceAllOccurrences() {
        textArea.setText("test text with test pattern and test ending");
        searchActions.replaceAll("test", "check", false, false);
        assertEquals("check text with check pattern and check ending", textArea.getText());
    }
}