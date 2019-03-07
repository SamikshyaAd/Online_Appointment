
package appointmentController;

import connection.DBconnection;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InsertAppointment extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String dateIn = request.getParameter("date");
        String timeIn = request.getParameter("time");
        String descriptionIn = request.getParameter("descStr");
       
        try{
            //Connects db  
            Connection c = DBconnection.connect();
            
            Statement statement = c.createStatement();
            String sql_table =  "CREATE TABLE IF NOT EXISTS appointment " +
                   "(id INTEGER AUTO_INCREMENT, " + 
                   " date DATE, " +
                   " time VARCHAR(255), "+
                   " description VARCHAR(255)," +
                   " PRIMARY KEY ( id ))"; 
            
            //Creates table if not exists 
            statement.execute(sql_table);
            
            String query = "INSERT INTO appointment( date, time, description) VALUES (?, ?, ?)";
            
            PreparedStatement pstmt = c.prepareStatement(query);
            
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            df.setTimeZone(TimeZone.getDefault()); 
            Date journeyDate = new java.sql.Date(df.parse(dateIn).getTime());
            
            pstmt.setDate(1, (java.sql.Date) journeyDate);
            pstmt.setString(2,timeIn);
            pstmt.setString(3,descriptionIn);
            
           //Inserts form data 
            pstmt.executeUpdate();
            
            //Closes statements
            pstmt.close();
            statement.close();
            
        }
        catch(ParseException | SQLException ex) {
            Logger.getLogger(InsertAppointment.class.getName()).log(Level.SEVERE, null, ex);
        } 
        finally{
             //Disconnects db  
             DBconnection.disconnect();
        } 
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
