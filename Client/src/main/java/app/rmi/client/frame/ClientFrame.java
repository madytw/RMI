package app.rmi.client.frame;

import app.rmi.client.model.StudentTableModel;
import app.rmi.server.entity.Result;
import app.rmi.server.entity.Student;
import app.rmi.server.exception.DAOException;
import app.rmi.server.service.ClientActionI;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.MalformedURLException;
import java.rmi.*;
import java.util.List;

/**
 * Main class for client's GUI.
 *
 * @author Roman Grinevskiy
 * @see Frame
 */
public class ClientFrame extends JFrame {
    /**
     * The object of remote class implementing ClientActionI.
     */
    private ClientActionI stub;
    private List<Student> students;
    private JLabel topLabel, bottLabel;
    private JTable table;
    private JList exams;
    private DefaultListModel<Result> listModel;
    private StudentTableModel model;

    /**
     * Creates ClientFrame a object. Initialises GUI. <p>Receives access to remote interface.</p>
     */
    private ClientFrame(String s) throws NotBoundException, MalformedURLException, RemoteException {
        super(s);
        System.setSecurityManager(new RMISecurityManager());
        stub = (ClientActionI) Naming.lookup("rmi://127.0.0.1:1099/action");
        model = new StudentTableModel();

        JMenuBar menuBar = new JMenuBar();
        JMenu show = new JMenu("Show");
        JMenu edit = new JMenu("Edit");
        JMenuItem showExcellent = new JMenuItem("Excellent students");
        JMenuItem showMiddle = new JMenuItem("Middle students");
        JMenuItem showLoosers = new JMenuItem("Looser students");
        JMenuItem addStudent = new JMenuItem("Add student");
        show.add(showExcellent);
        show.add(showMiddle);
        show.add(showLoosers);
        edit.add(addStudent);
        menuBar.add(show);
        menuBar.add(edit);



        JPanel p =new JPanel(new BorderLayout());
        JPanel center = new JPanel(new GridLayout(2,1));
        topLabel = new JLabel();
        table = new JTable();
        listModel = new DefaultListModel<>();
        exams = new JList<Result>(listModel);
        bottLabel = new JLabel();
        center.add(new JScrollPane(table));
        center.add(new JScrollPane(exams));
        p.add(topLabel, BorderLayout.NORTH);
        p.add(center, BorderLayout.CENTER);
        p.add(bottLabel, BorderLayout.SOUTH);


        table.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_DELETE){
                    try {
                        int index = table.getSelectedRow();
                        Student student = students.get(index);
                        stub.deleteStudent(student.getStudentId());
                        model.removeRow(index);
                        table.updateUI();
                        JOptionPane.showMessageDialog(null, "Student " + student.getSurname() + " was deleted successfully", "Info", JOptionPane.INFORMATION_MESSAGE);
                    }catch (RemoteException | DAOException e1){
                        JOptionPane.showMessageDialog(null,e1.getMessage(),"Error!",JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                listModel.clear();
                Student student = students.get(table.getSelectedRow());
                try{
                    for(Result result : stub.findStudentResults(student.getStudentId())){
                        listModel.addElement(result);
                    }
                    exams.updateUI();
                }catch (RemoteException | DAOException e){
                    JOptionPane.showMessageDialog(null,e.getMessage(),"Error!",JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        showExcellent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    listModel.clear();
                    topLabel.setText("Excellent students");
                    students = stub.findExcellentStudents();
                    model.setStudents(students);
                    table.setModel(model);
                    table.updateUI();
                    bottLabel.setText("Total amount of excellent students: " + students.size());
                }catch (RemoteException | DAOException e1){
                    JOptionPane.showMessageDialog(null,e1.getMessage(),"Error!",JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        showMiddle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    listModel.clear();
                    topLabel.setText("Middle students");
                    students = stub.findMiddleStudents();
                    model.setStudents(students);
                    table.setModel(model);
                    table.updateUI();
                    bottLabel.setText("Total amount of middle students: " + students.size());
                }catch (RemoteException | DAOException e1){
                    JOptionPane.showMessageDialog(null,e1.getMessage(),"Error!",JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        showLoosers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    listModel.clear();
                    topLabel.setText("Looser students");
                    students = stub.findLooserStudents();
                    model.setStudents(students);
                    table.setModel(model);
                    table.updateUI();
                    bottLabel.setText("Total amount of looser students: " + students.size());
                }catch (RemoteException | DAOException e1){
                    JOptionPane.showMessageDialog(null,e1.getMessage(),"Error!",JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        addStudent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddStudentDialog dg = new AddStudentDialog(null, "Add student", true, stub);
                dg.setVisible(true);
            }
        });

        setContentPane(p);
        setJMenuBar(menuBar);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400,400);
        setVisible(true);
    }

    /**
     * Starts application.
     */
    public static void main(String[] args) {

        try {
            new ClientFrame("Client");
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }
    }
}
