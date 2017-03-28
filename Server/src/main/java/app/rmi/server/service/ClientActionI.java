package app.rmi.server.service;

import app.rmi.server.entity.Result;
import app.rmi.server.entity.Student;
import app.rmi.server.exception.DAOException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by Roman on 25.02.2017.
 */
public interface ClientActionI extends Remote {
    List<Student> findExcellentStudents() throws RemoteException, DAOException;
    List<Student> findMiddleStudents() throws RemoteException, DAOException;
    List<Student> findLooserStudents() throws RemoteException, DAOException;
    List<Result> findStudentResults(int id) throws RemoteException, DAOException;
    int addNewStudent(Student student) throws RemoteException, DAOException;
    void addStudentResults(List<Result> results) throws RemoteException, DAOException;
    void deleteStudent(int studentId) throws RemoteException, DAOException;
}
