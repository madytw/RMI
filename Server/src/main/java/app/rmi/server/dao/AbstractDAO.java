package app.rmi.server.dao;

import app.rmi.server.entity.Entity;
import app.rmi.server.pool.ProxyConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.sql.Statement;

public abstract class AbstractDAO<T extends Entity> {

    private static final Logger LOG = LogManager.getLogger();

    public void closeStatement(Statement st) {
        try {
            if (st != null) {
                st.close();
            }
        } catch (SQLException e) {
            LOG.error("Cannot close statement: ", e);
        }
    }

    public void closeConnection(ProxyConnection connection) {
        try {
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        } catch (SQLException e) {
            LOG.error("Cannot return connection to pool: ", e);
        }
    }

}
