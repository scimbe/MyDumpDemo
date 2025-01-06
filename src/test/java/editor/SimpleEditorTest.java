package editor;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SimpleEditorTest {
    @Test
    void editorShouldInitialize() {
        SimpleEditor editor = new SimpleEditor();
        assertNotNull(editor);
    }
}