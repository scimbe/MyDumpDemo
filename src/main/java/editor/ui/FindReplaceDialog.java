package editor.ui;

import editor.actions.SearchActions;

import javax.swing.*;
import java.awt.*;

public class FindReplaceDialog extends JDialog {
    private JTextField findField;
    private JTextField replaceField;
    private JCheckBox caseSensitiveCheckbox;
    private JCheckBox wholeWordsCheckbox;
    private SearchActions searchActions;

    public FindReplaceDialog(JFrame parent, JTextArea textArea, JLabel statusBar) {
        super(parent, "Find/Replace", false);
        searchActions = new SearchActions(textArea, statusBar);
        initComponents();
        setupLayout();
        pack();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        findField = new JTextField(20);
        replaceField = new JTextField(20);
        caseSensitiveCheckbox = new JCheckBox("Case sensitive");
        wholeWordsCheckbox = new JCheckBox("Whole words only");
    }

    private void setupLayout() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Find field
        addLabel("Find:", gbc, 0, 0);
        addTextField(findField, gbc, 1, 0);

        // Replace field
        addLabel("Replace:", gbc, 0, 1);
        addTextField(replaceField, gbc, 1, 1);

        // Options panel
        addOptionsPanel(gbc, 0, 2);

        // Buttons panel
        addButtonsPanel(gbc, 0, 3);
    }

    private void addLabel(String text, GridBagConstraints gbc, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        add(new JLabel(text), gbc);
    }

    private void addTextField(JTextField field, GridBagConstraints gbc, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(field, gbc);
    }

    private void addOptionsPanel(GridBagConstraints gbc, int x, int y) {
        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        optionsPanel.add(caseSensitiveCheckbox);
        optionsPanel.add(wholeWordsCheckbox);

        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(optionsPanel, gbc);
    }

    private void addButtonsPanel(GridBagConstraints gbc, int x, int y) {
        JPanel buttonsPanel = new JPanel();
        
        JButton findNextButton = new JButton("Find Next");
        JButton replaceButton = new JButton("Replace");
        JButton replaceAllButton = new JButton("Replace All");
        JButton closeButton = new JButton("Close");

        findNextButton.addActionListener(e -> searchActions.findNext(
            findField.getText(),
            caseSensitiveCheckbox.isSelected(),
            wholeWordsCheckbox.isSelected()
        ));

        replaceButton.addActionListener(e -> searchActions.replace(
            findField.getText(),
            replaceField.getText(),
            caseSensitiveCheckbox.isSelected(),
            wholeWordsCheckbox.isSelected()
        ));

        replaceAllButton.addActionListener(e -> searchActions.replaceAll(
            findField.getText(),
            replaceField.getText(),
            caseSensitiveCheckbox.isSelected(),
            wholeWordsCheckbox.isSelected()
        ));

        closeButton.addActionListener(e -> setVisible(false));

        buttonsPanel.add(findNextButton);
        buttonsPanel.add(replaceButton);
        buttonsPanel.add(replaceAllButton);
        buttonsPanel.add(closeButton);

        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(buttonsPanel, gbc);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            findField.requestFocus();
        }
    }
}