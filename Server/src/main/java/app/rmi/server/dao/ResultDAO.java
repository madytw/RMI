package app.rmi.server.dao;

import app.rmi.server.entity.Result;
import app.rmi.server.exception.DAOException;
import app.rmi.server.pool.ConnectionPool;
import app.rmi.server.pool.ProxyConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roman on 02.03.2017.
 */
public class ResultDAO extends AbstractDAO<Result> {
    private static final String SQL_SELECT_RESULTS_BY_ID = "SELECT * FROM Result WHERE `student_id`=?";
    private static final String SQL_INSERT_NEW_RESULT =
            "INSERT INTO `Result` (`student_id`, `subject`, `mark`)" +
                    " VALUES (?, ?, ?)";

    public List<Result> findResultsByStudentId(int id) throws DAOException{
        List<Result> results;
        ProxyConnection cn = null;
        PreparedStatement st = null;
        try {
            cn = ConnectionPool.getInstance().getConnection();
            st = cn.prepareStatement(SQL_SELECT_RESULTS_BY_ID);
            st.setInt(1, id);
            ResultSet resultSet = st.executeQuery();
            results = takeResults(resultSet);
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            closeStatement(st);
            closeConnection(cn);
        }
        return results;
    }

    public void insertStudentResults(List<Result> results) throws DAOException{
        ProxyConnection cn = null;
        PreparedStatement ps = null;
        try {
            cn = ConnectionPool.getInstance().getConnection();
            ps = cn.prepareStatement(SQL_INSERT_NEW_RESULT);
            for(Result result : results) {
                ps.setInt(1, result.getStudentId());
                ps.setString(2, result.getSubject());
                ps.setInt(3, result.getMark());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            closeStatement(ps);
            closeConnection(cn);
        }
    }

    private List<Result> takeResults(ResultSet rs) throws DAOException{
        List<Result> results = new ArrayList<>();
        try {
            while (rs.next()) {
                Result result = new Result();
                result.setStudentId(rs.getInt("student_id"));
                result.setSubject(rs.getString("subject"));
                result.setMark(rs.getInt("mark"));
                results.add(result);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return results;
    }
}
