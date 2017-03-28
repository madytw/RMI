package app.rmi.server.valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * Created by Roman on 18.11.2016.
 */
public class StudentErrorHandler extends DefaultHandler {
    private Logger logger = LogManager.getLogger();
    private boolean isValid = true;

    public void warning(SAXParseException e) {
        logger.warn(getLineAddress(e) + "-" + e.getMessage());
        isValid = false;
    }
    public void error(SAXParseException e) {
        logger.error(getLineAddress(e) + " - " + e.getMessage());
        isValid = false;
    }
    public void fatalError(SAXParseException e) {
        logger.fatal(getLineAddress(e) + " - " + e.getMessage());
        isValid = false;
    }
    private String getLineAddress(SAXParseException e) {
        return e.getLineNumber() + " : " + e.getColumnNumber();
    }

    public boolean isValid(){
        return isValid;
    }

}
