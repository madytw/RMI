package app.rmi.server.parser;

import app.rmi.server.entity.Result;
import app.rmi.server.entity.Student;
import app.rmi.server.entity.StudentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.namespace.QName;
import javax.xml.stream.*;
import javax.xml.stream.events.*;
import java.io.*;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Roman on 22.03.2017.
 */

@SuppressWarnings("Duplicates")
public class StudentStAXParser {
    private static final Logger LOG = LogManager.getLogger();
    private XMLInputFactory inputFactory;
    private XMLOutputFactory outputFactory;
    private XMLEventFactory eventFactory;
    private XMLEventWriter writer;
    private XMLEventReader reader;
    private String fileName;
    private Map<Integer, List<Result>> results;
    private Map<Integer, Student> students;

    public StudentStAXParser() {
        inputFactory = XMLInputFactory.newInstance();
        outputFactory = XMLOutputFactory.newInstance();
        eventFactory = XMLEventFactory.newInstance();
        students = new HashMap<>();
        results = new HashMap<>();
    }


    public void buildSessionResults(String fileName) {
        this.fileName = fileName;
        FileInputStream inputStream = null;
        XMLStreamReader reader = null;
        String name;
        try {
            inputStream = new FileInputStream(new File(fileName));
            reader = inputFactory.createXMLStreamReader(inputStream);
            while (reader.hasNext()) {
                int elementType = reader.next();
                if (elementType == XMLStreamConstants.START_ELEMENT) {
                    name = reader.getLocalName();
                    switch (name) {
                        case "student":
                            Student st = createStudent(reader);
                            students.put(st.getStudentId(), st);
                            break;
                        case "result":
                            Result rs = createResult(reader);
                            List<Result> resultList = results.get(rs.getStudentId());
                            if (resultList != null) {
                                resultList.add(rs);
                            } else {
                                resultList = new ArrayList<>();
                                resultList.add(rs);
                                results.put(rs.getStudentId(), resultList);
                            }
                            break;
                    }
                }
            }
        } catch (XMLStreamException ex) {
            LOG.error("StAX parsing error! " + ex.getMessage());
        } catch (FileNotFoundException ex) {
            LOG.error("File " + fileName + " not found! " + ex);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                LOG.error("Impossible close file " + fileName + " : " + e);
            }
        }
    }

    private Result createResult(XMLStreamReader reader) throws XMLStreamException {
        Result rs = new Result();
        rs.setStudentId(Integer.parseInt(reader.getAttributeValue(0)));
        String name;
        while (reader.hasNext()) {
            int elementType = reader.next();
            switch (elementType) {
                case XMLStreamConstants.START_ELEMENT:
                    name = reader.getLocalName();
                    switch (name) {
                        case "subject":
                            String subject = getXMLText(reader);
                            rs.setSubject(subject);
                            break;
                        case "mark":
                            int mark = Integer.parseInt(getXMLText(reader));
                            rs.setMark(mark);
                            break;
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    name = reader.getLocalName();
                    if ("result".equals(name)) {
                        return rs;
                    }
                    break;
            }
        }
        return rs;
    }

    private Student createStudent(XMLStreamReader reader) throws XMLStreamException {
        Student st = new Student();
        st.setStudentId(Integer.parseInt(reader.getAttributeValue(0)));
        String name;
        while (reader.hasNext()) {
            int elementType = reader.next();
            switch (elementType) {
                case XMLStreamConstants.START_ELEMENT:
                    name = reader.getLocalName();
                    switch (name) {
                        case "surname":
                            String surname = getXMLText(reader);
                            st.setSurname(surname);
                            break;
                        case "group":
                            int group = Integer.parseInt(getXMLText(reader));
                            st.setGroup(group);
                            break;
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    name = reader.getLocalName();
                    if ("student".equals(name)) {
                        return st;
                    }
                    break;
            }
        }
        return st;
    }

    public List<Student> findStudents(StudentType type) {
        List<Student> students = new ArrayList<>();
        switch (type) {
            case EXCELLENT:
                students = findExcellentStudents();
                break;
            case MIDDLE:
                students = findMiddleStudents();
                break;
            case LOOSER:
                students = findLooserStudents();
                break;
        }
        return students;
    }

    private List<Student> findExcellentStudents() {
        List<Student> students = new ArrayList<>();
        for(Map.Entry<Integer, List<Result>> entry : results.entrySet()){
            List<Result> currentResults = entry.getValue();
            boolean isExcellent = true;
            for(Result res : currentResults){
                isExcellent &= res.getMark()>=9;
            }
            if(isExcellent){
                students.add(this.students.get(entry.getKey()));
            }
        }
        return students;
    }

    private List<Student> findMiddleStudents() {
        List<Student> students = new ArrayList<>();
        for(Map.Entry<Integer, List<Result>> entry : results.entrySet()){
            List<Result> currentResults = entry.getValue();
            boolean isMiddle = true;
            for(Result res : currentResults){
                isMiddle &= res.getMark()>=4;
            }
            if(isMiddle){
                students.add(this.students.get(entry.getKey()));
            }
        }
        return students;
    }

    private List<Student> findLooserStudents() {
        List<Student> students = new ArrayList<>();
        for(Map.Entry<Integer, List<Result>> entry : results.entrySet()){
            List<Result> currentResults = entry.getValue();
            boolean isLooser = false;
            for(Result res : currentResults){
                isLooser |= res.getMark()<4;
            }
            if(isLooser){
                students.add(this.students.get(entry.getKey()));
            }
        }
        return students;
    }

    private String getXMLText(XMLStreamReader reader) throws XMLStreamException {
        String text = null;
        if (reader.hasNext()) {
            reader.next();
            text = reader.getText();
        }
        return text;
    }

    public List<Result> findStudentResults(int id) {
        return results.get(id);
    }

    public int addNewStudent(Student student) {
        int id = students.size() + 1;
        try {
            reader = inputFactory.createXMLEventReader(new FileInputStream(fileName), "UTF-8");
            writer = outputFactory.createXMLEventWriter(new FileOutputStream(fileName), "UTF-8");
            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();
                switch (event.getEventType()) {
                    case XMLEvent.START_DOCUMENT:
                        event = eventFactory.createStartDocument("utf-8");
                        break;
                    case XMLEvent.END_ELEMENT:
                        EndElement endElement = event.asEndElement();
                        String endTagName = endElement.getName().getLocalPart();
                        switch (endTagName) {
                            case "session":
                                students.put(id, student);
                                student.setStudentId(id);
                                writeStudent(student);
                                writeSimpleTag("session", false);
                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                }
                writer.add(event);
            }
        } catch (XMLStreamException | IOException | ParseException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (XMLStreamException e) {
                    LOG.error(e);
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (XMLStreamException e) {
                    LOG.error(e);
                }
            }
        }
        return id;
    }

    private void writeStudent(Student student) throws XMLStreamException, ParseException {
        XMLEvent newLineSequence = eventFactory.createCharacters("\n");
        XMLEvent tabSequence = eventFactory.createCharacters("\t");
        writer.add(tabSequence);
        writeSimpleTag("student", true);
        writer.add(eventFactory.createAttribute("id", String.valueOf(student.getStudentId())));
        writer.add(newLineSequence);
        writeSimpleElement("surname", student.getSurname());
        writer.add(newLineSequence);
        writeSimpleElement("group", String.valueOf(student.getGroup()));
        writer.add(newLineSequence);
        writer.add(tabSequence);
        writeSimpleTag("student", false);
        writer.add(newLineSequence);
    }

    private void writeSimpleTag(String tagName, boolean isOpen) throws XMLStreamException {
        XMLEvent event;
        if (isOpen) {
            QName name = QName.valueOf(tagName);
            event = eventFactory.createStartElement(name.getPrefix(), name.getNamespaceURI(), name.getLocalPart());
        } else {
            QName name = QName.valueOf(tagName);
            event = eventFactory.createEndElement(name.getPrefix(), name.getNamespaceURI(), name.getLocalPart());
        }
        writer.add(event);
    }

    private void writeSimpleElement(String tagName, String value) throws XMLStreamException {
        XMLEvent controlSeq1 = eventFactory.createCharacters("\t\t");
        writer.add(controlSeq1);
        writeSimpleTag(tagName, true);
        XMLEvent event = eventFactory.createCharacters(value);
        writer.add(event);
        writeSimpleTag(tagName, false);
    }

    public void addStudentResults(List<Result> results) {
        try {
            reader = inputFactory.createXMLEventReader(new FileInputStream(fileName), "UTF-8");
            writer = outputFactory.createXMLEventWriter(new FileOutputStream(fileName), "UTF-8");
            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();
                switch (event.getEventType()) {
                    case XMLEvent.START_DOCUMENT:
                        event = eventFactory.createStartDocument("utf-8");
                        break;
                    case XMLEvent.END_ELEMENT:
                        EndElement endElement = event.asEndElement();
                        String endTagName = endElement.getName().getLocalPart();
                        switch (endTagName) {
                            case "session":
                                List<Result> studResults = new ArrayList<>();
                                int id = 0;
                                for(Result r : results) {
                                    id = r.getStudentId();
                                    studResults.add(r);
                                    writeResult(r);
                                }
                                this.results.put(id,studResults);
                                writeSimpleTag("session", false);
                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                }
                writer.add(event);
            }
        } catch (XMLStreamException | IOException | ParseException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (XMLStreamException e) {
                    LOG.error(e);
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (XMLStreamException e) {
                    LOG.error(e);
                }
            }
        }
    }

    private void writeResult(Result r) throws XMLStreamException, ParseException {
        XMLEvent newLineSequence = eventFactory.createCharacters("\n");
        XMLEvent tabSequence = eventFactory.createCharacters("\t");
        writer.add(tabSequence);
        writeSimpleTag("result", true);
        writer.add(eventFactory.createAttribute("student_id", String.valueOf(r.getStudentId())));
        writer.add(newLineSequence);
        writeSimpleElement("subject", r.getSubject());
        writer.add(newLineSequence);
        writeSimpleElement("mark", String.valueOf(r.getMark()));
        writer.add(newLineSequence);
        writer.add(tabSequence);
        writeSimpleTag("result", false);
        writer.add(newLineSequence);
    }

    public void deleteStudent(int studentId) {
        try {
            reader = inputFactory.createXMLEventReader(new FileInputStream(fileName), "UTF-8");
            writer = outputFactory.createXMLEventWriter(new FileOutputStream(fileName), "UTF-8");
            String tagName;
            boolean flag = false;
            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();
                switch (event.getEventType()) {
                    case XMLEvent.START_ELEMENT:
                        if (flag) {
                            continue;
                        }
                        StartElement element = event.asStartElement();
                        tagName = element.getName().getLocalPart();
                        switch (tagName) {
                            case "student":
                                int currId = Integer.parseInt(element.getAttributeByName(QName.valueOf("id")).getValue());
                                if (currId == studentId) {
                                    flag = true;
                                    continue;
                                }
                                break;
                            case "result":
                                int currResId = Integer.parseInt(element.getAttributeByName(QName.valueOf("student_id")).getValue());
                                if (currResId == studentId) {
                                    flag = true;
                                    continue;
                                }

                        }
                        break;
                    case XMLEvent.END_ELEMENT:
                        EndElement element1 = event.asEndElement();
                        if (flag) {
                            if ("student".equals(element1.getName().getLocalPart()) || "result".equals(element1.getName().getLocalPart())) {
                                flag = false;
                            }
                            continue;
                        }
                        break;
                    case XMLEvent.CHARACTERS:
                        if (flag) {
                            continue;
                        }
                        break;
                }
                writer.add(event);
            }
            students.remove(studentId);
            results.remove(studentId);
        } catch (XMLStreamException | IOException e) {
            System.err.println(e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (XMLStreamException e) {
                    e.printStackTrace();
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (XMLStreamException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
