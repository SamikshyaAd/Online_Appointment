/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointmentController;

import connection.DBconnection;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class RetriveAppointment extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/json;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            try{
                //Serach data sent through ajax
                 String searchData = request.getParameter("searchData");
                 
                 String query="";
                 
                 //Connects db 
                 Connection c = DBconnection.connect();
                 
                 if(searchData.length()==0){
                   query = "SELECT * FROM appointment";  
                 }
                 else {
                   query = "SELECT * FROM appointment WHERE description = ?";  
                 }
               
                 PreparedStatement pstmt = c.prepareStatement(query);
                 
                 if(searchData.length()!=0){
                      pstmt.setString(1, searchData);
                 }
                ResultSet rs = pstmt.executeQuery();
                
                String date =""; 
                String time ="";
                String des="";
                String result="";
                String item="";
                String eachitem="";
                 while(rs.next()){
                     Date d= rs.getDate("Date");
                     DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                     date = df.format(d);
                     time = rs.getString("time");
                     des = rs.getString("description");
                     eachitem="{\"date\":\"" +date+ "\",\"time\":\"" +time+ "\",\"description\":\"" +des+ "\"}";
                     item+=eachitem+",";
                 }
                pstmt.close();
                result = "{\"result\": [" + item.substring(0, item.length() - 1) + "]}";
                
               //Parsing the result into json object and sending response as json
                JSONObject json = (JSONObject) new JSONParser().parse(result);
                out.println(json.toJSONString());
               
            }catch(SQLException ex) {
                Logger.getLogger(InsertAppointment.class.getName()).log(Level.SEVERE, null, ex);
            } catch (org.json.simple.parser.ParseException ex) {
                Logger.getLogger(RetriveAppointment.class.getName()).log(Level.SEVERE, null, ex);
            } finally{
                 //Disconnects db  
                DBconnection.disconnect();
            }
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
