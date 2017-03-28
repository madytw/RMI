package app.rmi.client.frame;

import app.rmi.server.entity.Result;
import app.rmi.server.entity.Student;
import app.rmi.server.exception.DAOException;
import app.rmi.server.service.ClientActionI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for GUI dialog of adding student.
 *
 * @author Roman Grinevskiy
 * @version 1.0
 * @see JDialog
 */
public class AddStudentDialog extends JDialog {
    private DefaultListModel<Result> listModel;
    private ClientActionI action;
    private List<Result> resultList;

    public AddStudentDialog(Frame owner, String title, boolean modal, ClientActionI action) {
        super(owner, title, modal);
        this.action = action;
        resultList = new ArrayList<>();
        init();
    }

    /**
     * Initialize dialog components.
     */
    private void init() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel topLabel = new JLabel("Student info:");
        panel.add(topLabel, BorderLayout.NORTH);
        JPanel bottom = new JPanel(new FlowLayout());
        JButton add = new JButton("Add");
        bottom.add(add);
        panel.add(bottom, BorderLayout.SOUTH);

        JPanel mid = new JPanel(new GridLayout(3, 2));
        JLabel nameLabel = new JLabel("Surname: ");
        final JTextField nameField = new JTextField(10);
        JLabel groupLabel = new JLabel("Group: ");
        final JTextField groupField = new JTextField(10);
        listModel = new DefaultListModel<>();
        final JList exams = new JList(listModel);
        JPanel temp = new JPanel(new FlowLayout());
        JButton addResult = new JButton("Add exam");
        temp.add(addResult);
        mid.add(nameLabel);
        mid.add(nameField);
        mid.add(groupLabel);
        mid.add(groupField);
        mid.add(new JScrollPane(exams));
        mid.add(temp);
        panel.add(mid, BorderLayout.CENTER);
        setContentPane(panel);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        pack();

        addResult.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddExamDialog dg = new AddExamDialog(null, "Add exam", true, resultList);
                dg.setVisible(true);
                listModel.clear();
                for (Result result : resultList) {
                    listModel.addElement(result);
                }
                exams.updateUI();
            }
        });

        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    String surname = nameField.getText();
                    int group = Integer.parseInt(groupField.getText());
                    if(surname.isEmpty() || resultList.isEmpty()){
                        JOptionPane.showMessageDialog(null,"Check your input data","Error",JOptionPane.ERROR_MESSAGE);
                    }else {
                        Student student = new Student(surname, group);
                        student.setStudentId(action.addNewStudent(student));
                        for (Result result : resultList) {
                            result.setStudentId(student.getStudentId());
                        }
                        action.addStudentResults(resultList);
                        JOptionPane.showMessageDialog(null, "Student " + surname + " was added successfully", "Info", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    }
                }catch (RemoteException | DAOException | NumberFormatException e1){
                    JOptionPane.showMessageDialog(null,e1.getMessage(),"Error!",JOptionPane.ERROR_MESSAGE);
                }
            }
        });


    }
}
