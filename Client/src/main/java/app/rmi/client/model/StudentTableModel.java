package app.rmi.client.model;

import app.rmi.server.entity.Student;
import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * Enables to see all information as a table.
 * <table style="border: 1px solid black; border-collapse: collapse;">
 * <caption>Student info</caption>
 * <tr>
 * <th style="border: 1px solid black; border-collapse: collapse;">ID</th>
 * <th style="border: 1px solid black; border-collapse: collapse;">Surname</th>
 * <th style="border: 1px solid black; border-collapse: collapse;">Group</th>
 * </tr>
 * <tr>
 * <td style="border: 1px solid black; border-collapse: collapse;">Student's id</td>
 * <td style="border: 1px solid black; border-collapse: collapse;">Student's surname</td>
 * <td style="border: 1px solid black; border-collapse: collapse;">Student's group</td>
 * </tr>
 * </table>
 *
 * @author Roman Grinevskiy
 * @version 1.0
 */
public class StudentTableModel extends AbstractTableModel {
    private List<Student> students;

    /**
     * Set <code>List</code> of students according to the received <code>List</code>.
     *
     * @param students
     */
    public void setStudents(List<Student> students) {
        this.students = students;
    }

    /**
     * Determine each table column name according to column index.
     *
     * @param column index of current column.
     * @return name of the column.
     */
    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "ID";
            case 1:
                return "Surname";
            case 2:
                return "Group";
        }
        return "";
    }

    /**
     * Determines each table column class. It is <code>Integer</code> for numbers, <code>String</code> for others
     *
     * @param columnIndex index of temporary column
     * @return <code>Class</code> object of a column
     */

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Integer.class;
            case 1:
                return String.class;
            case 2:
                return Integer.class;
        }
        return String.class;
    }

    /**
     * Enables editing of cells. If returns true, client could change cells by clicking on it.
     *
     * @param rowIndex    current row index.
     * @param columnIndex current column index.
     * @return true if editing is enable, false otherwise.
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    /**
     * Determines number of table rows.
     *
     * @return number of table rows.
     */
    @Override
    public int getRowCount() {
        return students.size();
    }

    /**
     * Determine number of table columns.
     *
     * @return number of columns
     */
    @Override
    public int getColumnCount() {
        return 3;
    }

    /**
     * Determine value of current row and column according to column and row indexes.
     *
     * @param rowIndex    current row index.
     * @param columnIndex current column index.
     * @return value of the row and column.
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Student student = students.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return student.getStudentId();
            case 1:
                return student.getSurname();
            case 2:
                return student.getGroup();
        }
        return "";
    }

    /**
     * Delete row from model.
     *
     * @param index index of deleted row.
     */
    public void removeRow(int index){
        students.remove(index);
    }
}
