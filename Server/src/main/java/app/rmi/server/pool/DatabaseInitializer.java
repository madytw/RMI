package app.rmi.server.pool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Created by Roman on 07.12.2016.
 */
class DatabaseInitializer {
    static final Logger LOG = LogManager.getLogger();
    private static final String PARAM_BUNDLE_NAME = "database";
    private static final String PARAM_DATABASE_URL = "db.url";
    private static final String PARAM_DATABASE_USER = "db.user";
    private static final String PARAM_DATABASE_PASSWORD = "db.password";
    private static final String PARAM_POOL_SIZE = "db.poolsize";
    final ResourceBundle BUNDLE;
    final String DATABASE_URL;
    final String DATABASE_USER;
    final String DATABASE_PASSWORD;
    final int POOL_SIZE;

    public DatabaseInitializer() {
        try {
            BUNDLE = ResourceBundle.getBundle(PARAM_BUNDLE_NAME);
            DATABASE_URL = BUNDLE.getString(PARAM_DATABASE_URL);
            DATABASE_USER = BUNDLE.getString(PARAM_DATABASE_USER);
            DATABASE_PASSWORD = BUNDLE.getString(PARAM_DATABASE_PASSWORD);
            POOL_SIZE = Integer.parseInt(BUNDLE.getString(PARAM_POOL_SIZE));
        }catch (NumberFormatException | MissingResourceException e){
            LOG.fatal("Database initializing error: ", e);
            throw new RuntimeException("Database initializing error: ", e);
        }
    }
}

