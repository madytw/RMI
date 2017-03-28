package app.rmi.server.service;

import app.rmi.server.entity.Result;
import app.rmi.server.entity.Student;
import app.rmi.server.entity.StudentType;
import app.rmi.server.exception.DAOException;
import app.rmi.server.parser.StudentStAXParser;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 * Created by Roman on 22.03.2017.
 */
public class ClientRemoteObjectXML extends UnicastRemoteObject implements ClientActionI {
    private StudentStAXParser parser;

    public ClientRemoteObjectXML(int port, String fileName) throws RemoteException {
        super(port);
        parser = new StudentStAXParser();
        parser.buildSessionResults(fileName);
    }

    @Override
    public List<Student> findExcellentStudents() throws RemoteException, DAOException {
        return parser.findStudents(StudentType.EXCELLENT);
    }

    @Override
    public List<Student> findMiddleStudents() throws RemoteException, DAOException {
        return parser.findStudents(StudentType.MIDDLE);
    }

    @Override
    public List<Student> findLooserStudents() throws RemoteException, DAOException {
        return parser.findStudents(StudentType.LOOSER);
    }

    @Override
    public List<Result> findStudentResults(int id) throws RemoteException, DAOException {
        return parser.findStudentResults(id);
    }

    @Override
    public int addNewStudent(Student student) throws RemoteException, DAOException {
        return parser.addNewStudent(student);
    }

    @Override
    public void addStudentResults(List<Result> results) throws RemoteException, DAOException {
        parser.addStudentResults(results);
    }

    @Override
    public void deleteStudent(int studentId) throws RemoteException, DAOException {
        parser.deleteStudent(studentId);
    }
}
