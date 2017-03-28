package app.rmi.server.pool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Roman on 07.12.2016.
 */
public class ConnectionPool {
    private static final Logger LOG = LogManager.getLogger();
    private static AtomicBoolean isCreated = new AtomicBoolean(false);
    private static ReentrantLock lock = new ReentrantLock();
    private static ConnectionPool instance;
    private static DatabaseInitializer initializer;

    private BlockingQueue<ProxyConnection> connections;

    private ConnectionPool(){
        initializer = new DatabaseInitializer();
        connections = new ArrayBlockingQueue<ProxyConnection>(initializer.POOL_SIZE);
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        }catch (SQLException e){
            LOG.fatal("Cannot register database driver:", e);
            throw new RuntimeException("Cannot register database driver:", e);
        }
        fillConnectionPool();
    }

    public static ConnectionPool getInstance() {
        if (!isCreated.get()) {
            lock.lock();
            try {
                if (!isCreated.get()) {
                    instance = new ConnectionPool();
                    isCreated.getAndSet(true);
                }
            } finally {
                lock.unlock();
            }
        }
        return instance;

    }

    public ProxyConnection getConnection() {
        ProxyConnection conn = null;
        try {
            conn =  connections.take();
        } catch (InterruptedException e) {
            LOG.error("Cannot get connection form the pool: ", e);
        }
        return conn;
    }

    public void putConnection(ProxyConnection connection) {
        try {
            connections.put(connection);
        } catch (InterruptedException e) {
            LOG.error("Cannot put connection to the pool: ", e);
        }
    }

    public void closePool() {
        if (instance != null) {
            for (int i = 0; i < connections.size(); i++) {
                try {
                    connections.take().shutDownConnection();
                } catch (InterruptedException | SQLException e) {
                    LOG.error("Cannot shut down connection: ", e);
                }
            }
        }
    }

    private int size(){
        return connections.size();
    }

    private void addNewConnection(){
        try {
            Connection connection = DriverManager.getConnection(initializer.DATABASE_URL, initializer.DATABASE_USER, initializer.DATABASE_PASSWORD);
            ProxyConnection proxyConnection = new ProxyConnection(connection);
            connections.add(proxyConnection);
        }catch (SQLException e){
            LOG.error("Cannot create ProxyConnection: ", e);
        }
    }

    private void fillConnectionPool(){
        for(int i = 0; i < initializer.POOL_SIZE; i++){
            addNewConnection();
        }

        if(connections.isEmpty()){
            LOG.fatal("Empty connection pool!");
            throw new RuntimeException("Empty connection pool!");
        }

        int sizeDifference = initializer.POOL_SIZE - size();
        if(sizeDifference > 0){
            for(int i = 0; i < sizeDifference; i++){
                addNewConnection();
            }
        }
    }
}
