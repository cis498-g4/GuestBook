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
import java.io.PrintWriter;
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

        // Check that request is of multipart type (i.e. it contains a file upload)
        if (ServletFileUpload.isMultipartContent(request)) {
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

                String csv = null;

                while (items.hasNext()) {
                    FileItem item = (FileItem) items.next();

                    // Get other form data (e.g. eventId)
                    if (item.isFormField()) {
                        requestParams.put(item.getFieldName(), item.getString());
                    }

                    if(!item.isFormField()) {
                        requestFileContents.put(item.getFieldName(), item.getString());
                    }

                }

                // Get parameter data
                Event event = eventData.getEvent(Integer.parseInt(requestParams.get("eventId")));
                boolean headerRow = requestParams.containsKey("header-row");

                // Get CSV contents
                csv = unescapeCsv(requestFileContents.get("csv-list"));

                // TODO: Validate CSV


                // Parse CSV into list of users
                List<User> csvUsers = new ArrayList<User>();

                CSVParser parser = CSVParser.parse(csv, CSVFormat.DEFAULT);
                List<CSVRecord> records = parser.getRecords();

                // Start from row 2 if there is a header
                int i = 0;

                if (headerRow) {
                    i = 1;
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

                // Validate and register users
                List<User> existingUsers = new ArrayList<User>();
                List<User> newUsers = new ArrayList<User>();
                List<User> errorUsers = new ArrayList<User>();

                for (User csvUser : csvUsers) {
                    if (UserHelpers.validateCSVUser(csvUser)) {
                        User regUser = userData.getUserByEmail(csvUser.getEmail());

                        int regStatus = -1;
                        int insertStatus = -1;

                        if (regUser.getId() > 0) {
                            // If user valid and existing, register, add to existingUsers arrayList
                            regStatus = attendanceData.insertAttendance(regUser, event);

                            if (regStatus == 0) {
                                existingUsers.add(regUser);
                            } else {
                                errorUsers.add(regUser);
                            }

                        } else {
                            // If user valid and not existing, create and register, add to newUsers arrayList
                            // Generate default password (in production environment we would NOT do this)
                            csvUser.setPassword("abc123");
                            csvUser.setType(User.UserType.GUEST);

                            insertStatus = userData.insertUser(csvUser);

                            if (insertStatus == 0) {
                                regUser = userData.getUserByEmail(csvUser.getEmail());

                                regStatus = attendanceData.insertAttendance(regUser, event);

                                if (regStatus == 0) {
                                    newUsers.add(csvUser);
                                } else {
                                    errorUsers.add(csvUser);
                                }

                            } else {
                                errorUsers.add(csvUser);
                            }
                        }

                    } else {
                        // If user name or email invalid, add to error list
                        errorUsers.add(csvUser);

                    }

                }

                // TODO Generate response
                String url;
                String pageTitle;
                String statusMessage;
                String statusType;

                /*
                if (!errorUsers.isEmpty()) {
                    statusMessage = "Registration completed, but with errors. See below.";
                }
                */

                PrintWriter out = response.getWriter();
                response.setContentType("text/html");

                out.println("<h4>CSV Users</h4>");
                out.println("<ul>");

                Iterator<User> csvList = csvUsers.iterator();

                while(csvList.hasNext()) {
                    User csvListUser = csvList.next();
                    out.printf("<li>%s %s %s</li>\n", csvListUser.getFirstName(), csvListUser.getLastName(), csvListUser.getEmail());
                }

                out.println("</ul>");

                out.println("<h4>Existing Users</h4>");
                out.println("<ul>");

                Iterator<User> existingList = existingUsers.iterator();

                while(existingList.hasNext()) {
                    User exUser = existingList.next();
                    out.printf("<li>%s %s %s</li>\n", exUser.getFirstName(), exUser.getLastName(), exUser.getEmail());
                }

                out.println("</ul>");

                out.println("<h4>New Users</h4>");
                out.println("<ul>");

                Iterator<User> newList = newUsers.iterator();

                while(newList.hasNext()) {
                    User newUser = newList.next();
                    out.printf("<li>%s %s %s</li>\n", newUser.getFirstName(), newUser.getLastName(), newUser.getEmail());
                }

                out.println("</ul>");

                out.println("<h4>Registration Failures</h4>");
                out.println("<ul>");
                
                Iterator<User> errorList = errorUsers.iterator();

                while(errorList.hasNext()) {
                    User errUser = errorList.next();
                    out.printf("<li>%s %s %s</li>\n", errUser.getFirstName(), errUser.getLastName(), errUser.getEmail());
                }

                out.println("</ul>");

                out.close();

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
