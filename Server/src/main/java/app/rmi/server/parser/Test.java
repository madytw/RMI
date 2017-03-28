package app.rmi.server.parser;

/**
 * Created by Roman on 27.03.2017.
 */
public class Test {
    private static final String FILE_NAME = "Server/src/main/resources/students.xml";
    public static void main(String[] args) {
        StudentStAXParser parser = new StudentStAXParser();
        parser.buildSessionResults(FILE_NAME);
    }
}
