package app.rmi.server.frame;

import app.rmi.server.exception.FileNotValidException;
import app.rmi.server.service.ClientRemoteObjectXML;
import app.rmi.server.valid.StudentErrorHandler;
import app.rmi.server.valid.ValidatorXSD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * Created by Roman on 25.02.2017.
 */
public class ServerFrame extends JFrame {
    private static final String FILE_NAME = "Server/src/main/resources/students.xml";
    private static final String SCHEMA_NAME = "Server/src/main/resources/students.xsd";
    private static final Logger LOG = LogManager.getLogger();
    public ServerFrame(String s){
        super(s);
        JPanel p =new JPanel(new BorderLayout());
        JLabel label = new JLabel("SERVER STARTED");
        p.add(label, BorderLayout.NORTH);
        setContentPane(p);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400,400);
        setVisible(true);
    }

    public static void main(String... args){
        try {
            System.setProperty("java.rmi.server.hostname", "127.0.0.1");
            //System.setProperty("java.rmi.activation.port", "5566");
            //ClientRemoteObject obj = new ClientRemoteObject(5566);
            ValidatorXSD.validate(new StudentErrorHandler(), FILE_NAME, SCHEMA_NAME);
            ClientRemoteObjectXML obj = new ClientRemoteObjectXML(5566, FILE_NAME);
            LocateRegistry.createRegistry(1099);
            Naming.rebind("rmi://127.0.0.1:1099/action", obj);
            //registry.bind("Action", stub);
            new ServerFrame("Server");

        } catch (FileNotValidException e) {
            JOptionPane.showMessageDialog(null, "File not valid! Fix problems and restart server!", "Error", JOptionPane.ERROR_MESSAGE);
            LOG.fatal(e);
            throw new RuntimeException();
        } catch (RemoteException | MalformedURLException e) {
            LOG.fatal(e);
            throw new RuntimeException();
        }
    }

}
