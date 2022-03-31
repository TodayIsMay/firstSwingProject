package ui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Validator that prevents user from entering more than <b>limit</b> characters
 */

public class JTextFieldLimit extends PlainDocument {

    private final int limit;

    public JTextFieldLimit(int limit) {
        this.limit = limit;
    }

    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if (str == null)
            return;
        if ((getLength() + str.length()) <= limit) {
            super.insertString(offset, str, attr);
        }
    }
}
