package editor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.swing.*;
import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleEditorTest {
    private SimpleEditor editor;
    private JTextArea textArea;

    @BeforeEach
    void setUp() {
        editor = new SimpleEditor();
        // Get the textArea from the editor using reflection
        try {
            java.lang.reflect.Field field = SimpleEditor.class.getDeclaredField("textArea");
            field.setAccessible(true);
            textArea = (JTextArea) field.get(editor);
        } catch (Exception e) {
            fail("Could not access textArea field");
        }
    }

    @Test
    void editorShouldInitialize() {
        assertNotNull(editor);
        assertNotNull(textArea);
    }

    @Test
    void editorShouldHaveTitle() {
        assertEquals("Simple Editor", editor.getTitle());
    }

    @Test
    void editorShouldHaveCorrectSize() {
        assertEquals(800, editor.getWidth());
        assertEquals(600, editor.getHeight());
    }

    @Test
    void editorShouldHandleTextOperations() {
        String testText = "Hello, World!";
        textArea.setText(testText);
        assertEquals(testText, textArea.getText());
    }

    @Test
    void editorShouldUpdateTitleOnModification() {
        textArea.setText("Some text");
        assertTrue(editor.getTitle().startsWith("*"));
    }
    
    @Test
    void editorShouldHaveSearchMenu() {
        JMenuBar menuBar = editor.getJMenuBar();
        boolean hasSearchMenu = false;
        
        for (int i = 0; i < menuBar.getMenuCount(); i++) {
            if (menuBar.getMenu(i).getText().equals("Search")) {
                hasSearchMenu = true;
                break;
            }
        }
        
        assertTrue(hasSearchMenu, "Menu bar should contain Search menu");
    }
    
    @Test
    void searchMenuShouldHaveFindReplaceItem() {
        JMenuBar menuBar = editor.getJMenuBar();
        JMenu searchMenu = null;
        
        for (int i = 0; i < menuBar.getMenuCount(); i++) {
            if (menuBar.getMenu(i).getText().equals("Search")) {
                searchMenu = menuBar.getMenu(i);
                break;
            }
        }
        
        assertNotNull(searchMenu, "Search menu should exist");
        
        boolean hasFindReplace = false;
        for (int i = 0; i < searchMenu.getItemCount(); i++) {
            if (searchMenu.getItem(i).getText().equals("Find/Replace")) {
                hasFindReplace = true;
                KeyStroke shortcut = searchMenu.getItem(i).getAccelerator();
                assertEquals(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK), shortcut);
                break;
            }
        }
        
        assertTrue(hasFindReplace, "Search menu should contain Find/Replace item");
    }
    
    @Test
    void editorShouldHandleMultipleTextOperations() {
        String initialText = "Initial text";
        String appendText = " with more content";
        
        textArea.setText(initialText);
        assertEquals(initialText, textArea.getText());
        
        textArea.setText(initialText + appendText);
        assertEquals(initialText + appendText, textArea.getText());
    }
    
    @Test
    void editorShouldClearTextOnNewFile() {
        textArea.setText("Some test text");
        assertFalse(textArea.getText().isEmpty());
        
        // Simulate "New File" operation
        textArea.setText("");
        assertTrue(textArea.getText().isEmpty());
    }
    
    @Test
    void editorShouldMaintainModificationState() {
        // Initial state
        assertFalse(editor.getTitle().startsWith("*"));
        
        // After modification
        textArea.setText("Modified content");
        assertTrue(editor.getTitle().startsWith("*"));
        
        // After clearing
        textArea.setText("");
        assertTrue(editor.getTitle().startsWith("*"));
    }
}