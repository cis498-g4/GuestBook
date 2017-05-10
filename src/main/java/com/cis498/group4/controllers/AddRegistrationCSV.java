package com.cis498.group4.controllers;

import com.cis498.group4.data.AttendanceDataAccess;
import com.cis498.group4.data.EventDataAccess;
import com.cis498.group4.data.UserDataAccess;
import com.cis498.group4.models.Attendance;
import com.cis498.group4.models.Event;
import com.cis498.group4.models.User;
import com.cis498.group4.util.AttendanceHelpers;
import com.cis498.group4.util.SessionHelpers;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.apache.commons.lang3.StringEscapeUtils.unescapeCsv;

/**
 * The AddRegistration servlet responds to requests to register users in a CSV file for an event.
 */
@WebServlet(name = "AddRegistrationCSV", urlPatterns = "/manager/add-registration-csv")
public class AddRegistrationCSV extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Limit file size to 4MB (about 11,000 firstName, lastName, email tuples)
    private final int MAX_UPLOAD_SIZE = 4194304;

    private AttendanceDataAccess attendanceData;
    private EventDataAccess eventData;
    private UserDataAccess userData;

    public AddRegistrationCSV() {
        super();
        attendanceData = new AttendanceDataAccess();
        eventData = new EventDataAccess();
        userData = new UserDataAccess();
    }

    /**
     * Parses data from posted file
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Restrict access by non-Organizers
        if (!SessionHelpers.checkOrganizer(request.getSession())) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN, "You do not have permission to access this resource");
            return;
        }

        String url;
        String pageTitle;
        String back;
        String statusMessage;
        String statusType;

        // Check that request is of multipart type (i.e. it contains a file upload)
        if (ServletFileUpload.isMultipartContent(request)) {
            CSVParser csvParser = null;

            try {
                DiskFileItemFactory factory = new DiskFileItemFactory();
                // All files (less than max upload size) get stored in memory
                factory.setSizeThreshold(MAX_UPLOAD_SIZE);

                ServletFileUpload upload = new ServletFileUpload(factory);
                // Set max upload size to MAX_UPLOAD_SIZE
                upload.setFileSizeMax(MAX_UPLOAD_SIZE);

                // Read file items from request
                List<FileItem> requestFileItems = upload.parseRequest(request);
                Iterator items = requestFileItems.iterator();

                Map<String, String> requestParams = new HashMap<String, String>();
                Map<String, String> requestFileContents = new HashMap<String, String>();

                String csv;

                while (items.hasNext()) {
                    FileItem item = (FileItem) items.next();

                    // Add non-form field data to fileContents list
                    if(!item.isFormField()) {
                        requestFileContents.put(item.getFieldName(), item.getString());
                    }

                    // Add form field data (e.g. eventId) to parameters list
                    if (item.isFormField()) {
                        requestParams.put(item.getFieldName(), item.getString());
                    }
                }

                // Get parameter data from list
                Event event = eventData.getEvent(Integer.parseInt(requestParams.get("eventId")));
                String eventDate = event.getStartDateTime().format(DateTimeFormatter.ofPattern("M/d/YY"));
                boolean headerRow = requestParams.containsKey("header-row");

                // Get CSV contents
                csv = unescapeCsv(requestFileContents.get("csv-list"));


                // TODO: Validate CSV (or just catch exceptions?)


                // Parse CSV into list of users
                List<User> csvUsers = new ArrayList<User>();

                csvParser = CSVParser.parse(csv, CSVFormat.DEFAULT);
                List<CSVRecord> records = csvParser.getRecords();

                // Start from row 2 if there is a header
                int i = headerRow ? 1 : 0;

                while(i < records.size()) {
                    User user = new User();

                    CSVRecord record = records.get(i);
                    user.setLastName(record.get(0).trim());
                    user.setFirstName(record.get(1).trim());
                    user.setEmail(record.get(2).trim());

                    csvUsers.add(user);

                    i++;
                }

                // Validate and register users
                List<User> existingUsers = new ArrayList<User>();
                List<User> newUsers = new ArrayList<User>();
                List<User> errorUsers = new ArrayList<User>();

                for (User csvUser : csvUsers) {
                    User regUser = userData.getUserByEmail(csvUser.getEmail());

                    if (csvUser.getType() == null) {
                        csvUser.setType(User.UserType.GUEST);
                    }

                    // Perform appropriate registration action / respond with appropriate message
                    int status;

                    if (regUser.getId() < 1) {
                        status = AttendanceHelpers.registerStatus(csvUser, event, new ArrayList<Attendance>());
                    } else {
                        // Get existing user registrations
                        List<Attendance> registrations = attendanceData.getFutureRegistrations(regUser);
                        status = AttendanceHelpers.registerStatus(regUser, event, registrations);
                    }

                    switch(status) {
                        case AttendanceHelpers.CLOSED_REGISTRATION:
                        case AttendanceHelpers.SUCCESSFUL_REGISTRATION:
                            int regStatus = attendanceData.insertAttendance(regUser, event);

                            if (regStatus == 0) {
                                existingUsers.add(regUser);
                            } else {
                                errorUsers.add(csvUser);
                            }
                            break;
                        case AttendanceHelpers.NEW_USER:
                            csvUser.setPassword("abc123");

                            int insertStatus = userData.insertUser(csvUser);

                            if (insertStatus == 0) {
                                regUser = userData.getUserByEmail(csvUser.getEmail());

                                int newRegStatus = attendanceData.insertAttendance(regUser, event);

                                if (newRegStatus == 0) {
                                    newUsers.add(regUser);
                                } else {
                                    errorUsers.add(csvUser);
                                }
                            } else {
                                errorUsers.add(csvUser);
                            }
                            break;
                        default:
                            errorUsers.add(csvUser);
                            break;
                    }

                }

                // TODO Generate response
                url = "/WEB-INF/views/add-registration-csv.jsp";
                pageTitle = String.format("Group Registration Status for %s %s", event.getName(), eventDate);
                back = String.format("list-user-regs-for-event?id=%s", event.getId());

                if (!existingUsers.isEmpty() || !newUsers.isEmpty()) {
                    statusMessage = "Registration succeeded";
                    statusType = "success";

                    if (!errorUsers.isEmpty()) {
                        statusMessage += ", but with errors. Registration attempts that failed are listed below";
                        statusType = "warning";
                    }

                    if (!newUsers.isEmpty()) {
                        statusMessage += ".<br>New users were created with a default password. " +
                                         "Please have new users change their password";
                        statusType = "warning";
                    }

                    statusMessage += ".";

                } else {
                    statusMessage = "No users were registered";
                    statusType = "danger";

                    if (!errorUsers.isEmpty()) {
                        statusMessage += ". Registration attempts that failed are listed below";
                    }

                    statusMessage += ".";
                }

                request.setAttribute("event", event);
                request.setAttribute("eventDate", eventDate);

                request.setAttribute("existingUsers", existingUsers);
                request.setAttribute("newUsers", newUsers);
                request.setAttribute("errorUsers", errorUsers);

                request.setAttribute("statusMessage", statusMessage);
                request.setAttribute("statusType", statusType);

            } catch (FileUploadException e) {
                //TODO: Problem with upload: invalid file, exceeds max size, ...
                url = "/WEB-INF/views/error-generic.jsp";
                pageTitle = "File upload error";
                back = "list-event-registrations";
                request.setAttribute("message", "Invalid file format");
            } catch (NullPointerException e) {
                // TODO invalid file format
                url = "/WEB-INF/views/error-generic.jsp";
                pageTitle = "File upload error";
                back = "list-event-registrations";
                request.setAttribute("message", "Invalid file format");
            } catch (ArrayIndexOutOfBoundsException e){
                // TODO invalid file format
                url = "/WEB-INF/views/error-generic.jsp";
                pageTitle = "File upload error";
                back = "list-event-registrations";
                request.setAttribute("message", "Invalid file format");
            } finally {
                if (csvParser != null) {
                    csvParser.close();
                }
            }

        } else {
            url = "/WEB-INF/views/error-generic.jsp";
            pageTitle = "File upload error";
            back = "list-event-registrations";
            request.setAttribute("message", "The request did not contain a file upload");
        }

        request.setAttribute("pageTitle", pageTitle);
        request.setAttribute("back", back);

        RequestDispatcher view = request.getRequestDispatcher(url);
        view.forward(request, response);

    }
}
