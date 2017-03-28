package app.rmi.server.service;

import app.rmi.server.dao.ResultDAO;
import app.rmi.server.dao.StudentDAO;
import app.rmi.server.entity.Result;
import app.rmi.server.entity.Student;
import app.rmi.server.exception.DAOException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 * Created by Roman on 25.02.2017.
 */
public class ClientRemoteObject extends UnicastRemoteObject implements ClientActionI {
    private StudentDAO studentDAO = new StudentDAO();
    private ResultDAO resultDAO = new ResultDAO();

    public ClientRemoteObject() throws RemoteException {
    }

    public ClientRemoteObject(int port) throws RemoteException {
        super(port);
    }

    @Override
    public List<Student> findExcellentStudents() throws RemoteException, DAOException {
        return studentDAO.findExcellentStudents();
    }

    @Override
    public List<Student> findMiddleStudents() throws RemoteException, DAOException {
        return studentDAO.findMiddleStudents();
    }

    @Override
    public List<Student> findLooserStudents() throws RemoteException, DAOException {
        return studentDAO.findLooserStudents();
    }

    @Override
    public List<Result> findStudentResults(int id) throws RemoteException, DAOException {
        return resultDAO.findResultsByStudentId(id);
    }

    @Override
    public int addNewStudent(Student student) throws RemoteException, DAOException {
        return studentDAO.insertNewStudent(student);
    }

    @Override
    public void addStudentResults(List<Result> results) throws RemoteException, DAOException {
        resultDAO.insertStudentResults(results);
    }

    @Override
    public void deleteStudent(int studentId) throws RemoteException, DAOException {
        studentDAO.deleteStudent(studentId);
    }

}
