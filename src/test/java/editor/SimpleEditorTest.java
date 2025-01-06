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
        textArea = getPrivateField("textArea");
    }
    
    private <T> T getPrivateField(String fieldName) {
        try {
            java.lang.reflect.Field field = SimpleEditor.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(editor);
        } catch (Exception e) {
            fail("Could not access " + fieldName + " field: " + e.getMessage());
            return null;
        }
    }

    @Test
    void shouldInitializeWithCorrectDefaults() {
        assertNotNull(editor);
        assertNotNull(textArea);
        assertEquals("Simple Editor", editor.getTitle());
        assertEquals(800, editor.getWidth());
        assertEquals(600, editor.getHeight());
        assertEquals(JFrame.DO_NOTHING_ON_CLOSE, editor.getDefaultCloseOperation());
    }

    @Test
    void shouldSetupCorrectMenuStructure() {
        JMenuBar menuBar = editor.getJMenuBar();
        assertNotNull(menuBar);

        // Check menu count
        assertEquals(3, menuBar.getMenuCount(), "Should have File, Edit, and Search menus");

        // Verify File menu
        JMenu fileMenu = menuBar.getMenu(0);
        assertEquals("File", fileMenu.getText());
        assertEquals(6, fileMenu.getItemCount()); // Including separator

        // Verify Edit menu
        JMenu editMenu = menuBar.getMenu(1);
        assertEquals("Edit", editMenu.getText());
        assertEquals(6, editMenu.getItemCount()); // Including separator

        // Verify Search menu
        JMenu searchMenu = menuBar.getMenu(2);
        assertEquals("Search", searchMenu.getText());
        assertEquals(2, searchMenu.getItemCount());
    }

    @Test
    void shouldUpdateModificationState() {
        assertFalse(editor.isModified());
        
        // Simulate text modification
        textArea.setText("Modified content");
        assertTrue(editor.isModified());
        assertTrue(editor.getTitle().startsWith("*"));
        
        // Set modified to false
        editor.setModified(false);
        assertFalse(editor.isModified());
        assertFalse(editor.getTitle().startsWith("*"));
    }

    @Test
    void shouldHaveCorrectKeyboardShortcuts() {
        JMenuBar menuBar = editor.getJMenuBar();
        
        // Check File menu shortcuts
        JMenu fileMenu = menuBar.getMenu(0);
        assertEquals(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK),
            fileMenu.getItem(0).getAccelerator()); // New
        assertEquals(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK),
            fileMenu.getItem(1).getAccelerator()); // Open
        assertEquals(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK),
            fileMenu.getItem(2).getAccelerator()); // Save
        
        // Check Edit menu shortcuts
        JMenu editMenu = menuBar.getMenu(1);
        assertEquals(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK),
            editMenu.getItem(0).getAccelerator()); // Undo
        assertEquals(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK),
            editMenu.getItem(1).getAccelerator()); // Redo
        
        // Check Search menu shortcuts
        JMenu searchMenu = menuBar.getMenu(2);
        assertEquals(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK),
            searchMenu.getItem(0).getAccelerator()); // Find/Replace
    }
}