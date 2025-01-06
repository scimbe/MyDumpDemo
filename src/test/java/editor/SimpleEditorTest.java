package editor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.swing.*;
import java.awt.event.KeyEvent;
import static org.junit.jupiter.api.Assertions.*;

public class SimpleEditorTest {
    private SimpleEditor editor;

    @BeforeEach
    void setUp() {
        editor = new SimpleEditor();
    }

    @Test
    void editorShouldInitialize() {
        assertNotNull(editor);
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
        editor.setText(testText);
        assertEquals(testText, editor.getText());
    }

    @Test
    void editorShouldUpdateTitleOnModification() {
        editor.setText("Some text");
        assertTrue(editor.getTitle().startsWith("*"));
    }
    
    @Test
    void menuBarShouldContainSearchMenu() {
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
        
        editor.setText(initialText);
        assertEquals(initialText, editor.getText());
        
        editor.setText(initialText + appendText);
        assertEquals(initialText + appendText, editor.getText());
    }
    
    @Test
    void editorShouldClearTextOnNew() {
        editor.setText("Some test text");
        assertFalse(editor.getText().isEmpty());
        
        // Simuliere "New File" Operation
        editor.setText("");
        assertTrue(editor.getText().isEmpty());
    }
    
    @Test
    void editorShouldMaintainModificationState() {
        // Initial state
        assertFalse(editor.getTitle().startsWith("*"));
        
        // After modification
        editor.setText("Modified content");
        assertTrue(editor.getTitle().startsWith("*"));
        
        // After clearing
        editor.setText("");
        assertTrue(editor.getTitle().startsWith("*"));
    }
}