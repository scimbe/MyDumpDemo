package editor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
}