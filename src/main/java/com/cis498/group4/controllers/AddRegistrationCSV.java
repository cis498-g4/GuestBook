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
                String url = "/WEB-INF/views/add-registration-csv.jsp";
                String pageTitle = String.format("Group Registration for %s %s", event.getName(), eventDate);
                String statusMessage;
                String statusType;

                if (!existingUsers.isEmpty() || !newUsers.isEmpty()) {
                    statusMessage = "Registration succeeded";
                    statusType = "success";

                    if (!errorUsers.isEmpty()) {
                        statusMessage += ", but with errors. Registration attempts that failed are listed below";
                        statusType = "warning";
                    }

                    if (!newUsers.isEmpty()) {
                        statusMessage += ".<br>New users were created with a default password. Please have new users change their password";
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
                request.setAttribute("pageTitle", pageTitle);
                request.setAttribute("statusMessage", statusMessage);
                request.setAttribute("statusType", statusType);

                request.setAttribute("existingUsers", existingUsers);
                request.setAttribute("newUsers", newUsers);
                request.setAttribute("errorUsers", errorUsers);

                RequestDispatcher view = request.getRequestDispatcher(url);
                view.forward(request, response);

            } catch (FileUploadException e) {
                //TODO: Problem with upload: invalid file, exceeds max size, ...
            } catch (NullPointerException e) {
                // TODO invalid file format
            } catch (ArrayIndexOutOfBoundsException e){
                // TODO invalid file format
            } finally {
                if (csvParser != null) {
                    csvParser.close();
                }
            }

        } else {
            // TODO handle conditions where request does not contain file upload
        }

        // TODO reject uploads greater than a certain size (by default >10kB files must be written to disk

    }
}
