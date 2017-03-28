package app.rmi.server.dao;

import app.rmi.server.entity.Student;
import app.rmi.server.exception.DAOException;
import app.rmi.server.pool.ConnectionPool;
import app.rmi.server.pool.ProxyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roman on 25.02.2017.
 */
public class StudentDAO extends AbstractDAO<Student>{

    private static final String SQL_SELECT_EXCELLENT = "SELECT * FROM Student WHERE " +
            "(SELECT COUNT(*) FROM Result WHERE " +
            "`Student`.`student_id`=`Result`.`student_id` AND mark < 9) = 0;";
    private static final String SQL_SELECT_MIDDLE = "SELECT * FROM Student WHERE " +
            "(SELECT COUNT(*) FROM Result WHERE " +
            "`Student`.`student_id`=`Result`.`student_id` AND mark < 4) = 0;";
    private static final String SQL_SELECT_LOOSER = "SELECT * FROM Student WHERE " +
            "(SELECT COUNT(*) FROM Result WHERE " +
            "`Student`.`student_id`=`Result`.`student_id` AND mark < 4) > 0;";
    private static final String SQL_INSERT_NEW_STUDENT =
            "INSERT INTO `Student` (`student_id`, `surname`, `group_num`)" +
                    " VALUES (?, ?, ?)";
    private static final String SQL_DELETE_STUDENT = "DELETE FROM Student WHERE `student_id`=?;";
    public List<Student> findExcellentStudents() throws DAOException{
        List<Student> result;
        ProxyConnection cn = null;
        Statement st = null;
        try {
            cn = ConnectionPool.getInstance().getConnection();
            st = cn.createStatement();
            ResultSet resultSet = st.executeQuery(SQL_SELECT_EXCELLENT);
            result = takeStudents(resultSet);
        } catch (SQLException e) {
           throw new DAOException(e);
        } finally {
            closeStatement(st);
            closeConnection(cn);
        }
        return result;
    }

    public List<Student> findMiddleStudents() throws DAOException{
        List<Student> result;
        ProxyConnection cn = null;
        Statement st = null;
        try {
            cn = ConnectionPool.getInstance().getConnection();
            st = cn.createStatement();
            ResultSet resultSet = st.executeQuery(SQL_SELECT_MIDDLE);
            result = takeStudents(resultSet);
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            closeStatement(st);
            closeConnection(cn);
        }
        return result;
    }

    public List<Student> findLooserStudents() throws DAOException{
        List<Student> result;
        ProxyConnection cn = null;
        Statement st = null;
        try {
            cn = ConnectionPool.getInstance().getConnection();
            st = cn.createStatement();
            ResultSet resultSet = st.executeQuery(SQL_SELECT_LOOSER);
            result = takeStudents(resultSet);
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            closeStatement(st);
            closeConnection(cn);
        }
        return result;
    }

    public int insertNewStudent(Student student) throws DAOException{
        ProxyConnection cn = null;
        PreparedStatement ps = null;
        int id = 0;
        try {
            cn = ConnectionPool.getInstance().getConnection();
            ps = cn.prepareStatement(SQL_INSERT_NEW_STUDENT, Statement.RETURN_GENERATED_KEYS);
            ps.setNull(1, Types.INTEGER);
            ps.setString(2, student.getSurname());
            ps.setInt(3, student.getGroup());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            id = rs.getInt(1);
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            closeStatement(ps);
            closeConnection(cn);
        }
        return id;
    }

    public void deleteStudent(int studentId) throws DAOException{
        ProxyConnection cn = null;
        PreparedStatement ps = null;
        try {
            cn = ConnectionPool.getInstance().getConnection();
            ps = cn.prepareStatement(SQL_DELETE_STUDENT);
            ps.setInt(1, studentId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            closeStatement(ps);
            closeConnection(cn);
        }
    }


    private List<Student> takeStudents(ResultSet rs) throws DAOException{
        List<Student> students = new ArrayList<>();
        try {
            while (rs.next()) {
                Student student = new Student();
                student.setStudentId(rs.getInt("student_id"));
                student.setSurname(rs.getString("surname"));
                student.setGroup(rs.getInt("group_num"));
                students.add(student);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return students;
    }
}
