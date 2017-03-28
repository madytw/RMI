package app.rmi.server.service;

import app.rmi.server.entity.Result;
import app.rmi.server.entity.Student;
import app.rmi.server.exception.DAOException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * <code>ClientActionI</code> is an interface for remote server. <p>The class implementing this interface
 * is located on the server. Using this interface client can have remote access to class on server.</p>
 *
 * @author Roman Grinevskiy
 * @see Remote
 */
public interface ClientActionI extends Remote {
    /**
     * Finds excellent students according to the results.
     *
     * @return {@link java.util.List} of students
     * @throws RemoteException  if smth happens in remote invocation.
     * @throws DAOException  if smth happens with DAO layer on server side.
     */
    List<Student> findExcellentStudents() throws RemoteException, DAOException;

    /**
     * Finds middle students according to the results.
     *
     * @return <code>List</code> of students
     * @throws RemoteException  if smth happens in remote invocation.
     * @throws DAOException  if smth happens with DAO layer on server side.
     */
    List<Student> findMiddleStudents() throws RemoteException, DAOException;

    /**
     * Finds looser students according to the results.
     *
     * @return <code>List</code> of students
     * @throws RemoteException  if smth happens in remote invocation.
     * @throws DAOException  if smth happens with DAO layer on server side.
     */
    List<Student> findLooserStudents() throws RemoteException, DAOException;

    /**
     * Finds student's results.
     *
     * @param id id of a student
     * @return <code>List</code> of students
     * @throws RemoteException  if smth happens in remote invocation.
     * @throws DAOException  if smth happens with DAO layer on server side.
     */
    List<Result> findStudentResults(int id) throws RemoteException, DAOException;

    /**
     * Adds student to the application.
     *
     * @param student is adding student
     * @return int id of new student
     * @throws RemoteException if smth happens in remote invocation.
     * @throws DAOException  if smth happens with DAO layer on server side.
     */
    int addNewStudent(Student student) throws RemoteException, DAOException;

    /**
     * Adds student's results to the application.
     *
     * @param results is a <code>List</code> of student's results
     * @throws RemoteException if smth happens in remote invocation.
     * @throws DAOException  if smth happens with DAO layer on server side.
     */
    void addStudentResults(List<Result> results) throws RemoteException, DAOException;

    /**
     * Delete student and his results from the application.
     *
     * @param studentId is id of deleted student
     * @throws RemoteException if smth happens in remote invocation.
     * @throws DAOException  if smth happens with DAO layer on server side.
     */
    void deleteStudent(int studentId) throws RemoteException, DAOException;
}
