package com.cis498.group4.controllers;

import com.cis498.group4.data.AttendanceDataAccess;
import com.cis498.group4.data.EventDataAccess;
import com.cis498.group4.data.UserDataAccess;
import com.cis498.group4.models.Event;
import com.cis498.group4.models.User;
import com.cis498.group4.util.SessionHelpers;
import com.cis498.group4.util.UserHelpers;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

        Event event = eventData.getEvent(Integer.parseInt(request.getParameter("eventId")));

        // Check that request is of multipart type (i.e. it contains a file upload)
        if (ServletFileUpload.isMultipartContent(request)) {
            try {
                DiskFileItemFactory factory = new DiskFileItemFactory();
                // All files (less than max upload size) get stored in memory
                factory.setSizeThreshold(MAX_UPLOAD_SIZE);

                ServletFileUpload upload = new ServletFileUpload(factory);
                // Set max upload size to MAX_UPLOAD_SIZE
                upload.setFileSizeMax(MAX_UPLOAD_SIZE);

                // Read first file item from request
                List<FileItem> requestFileItems = upload.parseRequest(request);
                Iterator items = requestFileItems.iterator();
                String csv = null;

                while (items.hasNext()) {
                    FileItem item = (FileItem) items.next();

                    if(!item.isFormField()) {
                        csv = item.getString();
                        break;  // Stop after first file
                    }
                }

                // TODO: Validate CSV

                // TODO: Parse CSV into list of users
                List<User> csvUsers = new ArrayList<User>();

                CSVParser parser = CSVParser.parse(csv, CSVFormat.DEFAULT);
                List<CSVRecord> records = parser.getRecords();

                int i;

                // Start from row 2 if there is a header
                if (request.getParameter("header-row") != null) {
                    i = 1;
                } else {
                    i = 0;
                }

                while(i < records.size()) {
                    User user = new User();

                    CSVRecord record = records.get(i);
                    user.setLastName(record.get(0).trim());
                    user.setFirstName(record.get(1).trim());
                    user.setEmail(record.get(2).trim());

                    csvUsers.add(user);

                    i++;
                }

                // TODO: Validate users
                List<User> existingUsers = new ArrayList<User>();
                List<User> newUsers = new ArrayList<User>();
                List<User> errorUsers = new ArrayList<User>();

                for (User csvUser : csvUsers) {
                    if (UserHelpers.validateCSVUser(csvUser)) {
                        User regUser = userData.getUserByEmail(csvUser.getEmail());

                        if (regUser.getId() > 0) {
                            // If valid and existing, register, add to existingUsers arrayList
                            // TODO This can be factored out since it is used by both existing and new users?
                            int regStatus = attendanceData.insertAttendance(regUser, event);

                            if (regStatus == 0) {
                                existingUsers.add(regUser);
                            } else {
                                errorUsers.add(regUser);
                            }

                        } else {
                            // TODO: If valid and not existing, create and register, add to newUsers arrayList
                            // Generate default password (in production environment we would NOT do this)
                            regUser.setPassword("abc123");
                            regUser.setType(User.UserType.GUEST);

                            int insertStatus = userData.insertUser(regUser);

                            if (insertStatus == 0) {
                                int regStatus = attendanceData.insertAttendance(regUser, event);

                                if (regStatus == 0) {
                                    existingUsers.add(regUser);
                                } else {
                                    errorUsers.add(regUser);
                                }
                            } else {
                                errorUsers.add(regUser);
                            }

                        }

                    } else {
                        errorUsers.add(csvUser);
                    }

                }

                // TODO Generate response



            } catch (FileUploadException e) {
                //TODO: Problem with upload: invalid file, exceeds max size, ...
            } catch (NullPointerException e) {
                //TODO
            } finally {
                // TODO close CSVParser (and others?)
            }

        } else {
            // TODO handle conditions where request does not contain file upload
        }

        // TODO reject uploads greater than a certain size (by default >10kB files must be written to disk

    }
}
