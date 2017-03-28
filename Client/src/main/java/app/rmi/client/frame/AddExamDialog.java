package app.rmi.client.frame;

import app.rmi.server.entity.Result;
import app.rmi.server.service.ClientActionI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/**
 * Class for GUI  dialog of adding exam results.
 *
 * @author Roman Grinevskiy
 * @version 1.0
 * @see JDialog
 */
public class AddExamDialog extends JDialog {
    private List<Result> results;
    public AddExamDialog(Frame owner, String title, boolean modal, List<Result> results) {
        super(owner, title, modal);
        this.results = results;
        init();
    }

    /**
     * Initialize dialog components.
     */
    private void init(){
        JPanel panel = new JPanel(new BorderLayout());
        JLabel topLabel = new JLabel("Exam info:");
        panel.add(topLabel, BorderLayout.NORTH);
        JPanel bottom = new JPanel(new FlowLayout());
        JButton add = new JButton("Add");
        bottom.add(add);
        panel.add(bottom, BorderLayout.SOUTH);

        JPanel mid = new JPanel(new GridLayout(2, 2));
        JLabel subjectLabel = new JLabel("Subject: "); final JTextField subjectField = new JTextField(10);
        JLabel markLabel = new JLabel("Mark: "); final JTextField markField = new JTextField(10);
        mid.add(subjectLabel);mid.add(subjectField);
        mid.add(markLabel);mid.add(markField);
        panel.add(mid, BorderLayout.CENTER);

        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    results.add(new Result(subjectField.getText(), Integer.parseInt(markField.getText())));
                }catch (NumberFormatException e1){
                    JOptionPane.showMessageDialog(null,e1.getMessage(),"Error!",JOptionPane.ERROR_MESSAGE);
                }
                dispose();
            }
        });

        setContentPane(panel);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        pack();
    }
}