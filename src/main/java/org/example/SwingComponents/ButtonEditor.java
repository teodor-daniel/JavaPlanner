package org.example.SwingComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ButtonEditor extends DefaultCellEditor {
    private final java.util.function.Consumer<Integer> action;
    private final JButton button;
    private String label;
    private int cellEditorRow;

    public ButtonEditor(JCheckBox checkBox, String label, java.util.function.Consumer<Integer> action) {
        super(checkBox);
        this.label = label;
        this.action = action;
        this.button = new JButton();
        this.button.setOpaque(true);
        this.button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.accept(cellEditorRow);
                fireEditingStopped();
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int col) {
        this.label = (value == null) ? "" : value.toString();
        button.setText(label);
        cellEditorRow = row;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return label;
    }
}
