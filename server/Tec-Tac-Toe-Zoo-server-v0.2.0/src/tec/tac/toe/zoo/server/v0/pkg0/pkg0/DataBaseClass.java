/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tec.tac.toe.zoo.server.v0.pkg0.pkg0;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lw
 */public class DataBaseClass  {
     static Connection con=null;
     static PreparedStatement pst=null;
     static ResultSet rs=null;
     static int r;
   
    public DataBaseClass(String DB,String user,String pass)
    {
         try {
             DriverManager.registerDriver(new com.mysql.jdbc.Driver());
             con=DriverManager.getConnection("jdbc:mysql://localhost:3306/"+DB , user , pass);
         } catch (SQLException ex) {
             Logger.getLogger(DataBaseClass.class.getName()).log(Level.SEVERE, null, ex);
         }
    }
    public static void closseConection()
    {
         try {
             con.close();
             pst.close();
         } catch (SQLException ex) {
             Logger.getLogger(DataBaseClass.class.getName()).log(Level.SEVERE, null, ex);
         }
    }
    
   
    static boolean insertPlayer(String name,String pass )//params
    // adding player
    {
         boolean inserted = false;
         try 
         {
             
             String existed = DataBaseClass.selectPlayers(name, pass);
             if(!existed.equals("false")){
                 inserted = false;
                 return inserted; 
             }
             String sql="INSERT INTO player( userName, password)"+" VALUES (?,?)";
             pst=con.prepareStatement(sql);
             pst.setString(1, name);
             pst.setString(2, pass);
             r = pst.executeUpdate();
             if(r == 1){
                 inserted = true;
             }
             
         } catch (SQLException ex) {
             Logger.getLogger(DataBaseClass.class.getName()).log(Level.SEVERE, null, ex);
         }
         
         return inserted; 
    }
    static int deletePlayer(String name,String pass ) 
    // delete player
    //username pass        
    {
         try 
         {
             String sql="DELETE FROM player WHERE userName=? AND password=?" ;
             pst=con.prepareStatement(sql);
             pst.setString(1, name);
             pst.setString(2, pass);
             r = pst.executeUpdate();
         } catch (SQLException ex) {
             Logger.getLogger(DataBaseClass.class.getName()).log(Level.SEVERE, null, ex);
         }
         return r;
    }
    static int updateUserData(String name,String pass,String newName,String newPass)
    // player update his data
    //username pass // updatedData        
    {
         try 
         {
             String sql="UPDATE player SET  userName=? , password=? WHERE userName=? AND password=?" ;
             pst=con.prepareStatement(sql);
             pst.setString(1, newName);
             pst.setString(2, newPass);
             pst.setString(3, name);
             pst.setString(4, pass); 
             r = pst.executeUpdate();
         } catch (SQLException ex) {
             Logger.getLogger(DataBaseClass.class.getName()).log(Level.SEVERE, null, ex);
         }
         return r;
    }
    static int setscore(String name,String pass,int newScore) // username
    // update the score of player
    {
         try
         {
             String sql="UPDATE player SET score=? WHERE userName=? AND password=?" ;
             pst=con.prepareStatement(sql);
             pst.setInt(1, newScore);
             pst.setString(2, name);
             pst.setString(3, pass);
             r = pst.executeUpdate();
         } catch (SQLException ex) {
             Logger.getLogger(DataBaseClass.class.getName()).log(Level.SEVERE, null, ex);
         }
         return r;
    }
    static String selectPlayers(String userNamae , String userPass)//username pass
     {
        
         //get All Players From DataBase  
          //return query result   
   
        String userdata="";
         try 
         {
             String sql="SELECT* FROM player where userName = ? and password = ?";
             
             pst=con.prepareStatement(sql);
             pst.setString(1, userNamae);
             pst.setString(2, userPass);
             rs=pst.executeQuery();
             
            if(rs.next()){
                for(int i = 1 ; i < 4;i++){
                    userdata += rs.getString(i)+",";
                    System.out.println("re "+userdata);
                }
                System.out.println("data :" +userdata);
                return userdata;
            }else{
                userdata ="false";
                return userdata;
                // System.out.println("no next");
            }
             
         } catch (SQLException ex) {
             Logger.getLogger(DataBaseClass.class.getName()).log(Level.SEVERE, null, ex);
         }
         
         return userdata;
    }
    
    static int insertGame(int gameId,String player1,String player2,String winner,int request){ 
        //adding game  
        //return
    
         try {
             String sql="INSERT INTO game( gameId, player1Id, player2Id,winnerId,requestId)"+" VALUES (?,?,?,?,?)";
             pst=con.prepareStatement(sql);
             pst.setInt(1, gameId);
             pst.setString(2, player1);
             pst.setString(3, player2);
             pst.setString(4, winner);
             pst.setInt(5, request);
             r = pst.executeUpdate();
         } //params
         catch (SQLException ex) {
             Logger.getLogger(DataBaseClass.class.getName()).log(Level.SEVERE, null, ex);
         }
         return r;

    }
    
    
    
    static ResultSet selectGame(String player){
         try {
             //accept player id
             //return all games
             //showing
             String sql="SELECT* FROM game WHERE player1Id=?";
             pst=con.prepareStatement(sql);
              pst.setString(1, player);
             rs=pst.executeQuery();
              while(rs.next())
            {
                System.out.println(rs.getInt(1));
                System.out.println(rs.getString(2));
                System.out.println(rs.getString(3));
                System.out.println(rs.getString(4));
                System.out.println(rs.getInt(5));
            }
         } catch (SQLException ex) {
             Logger.getLogger(DataBaseClass.class.getName()).log(Level.SEVERE, null, ex);
         }
         return rs;
    }
    static ResultSet getPlayerRecords(String player)
    //accept player id return all player records
    {
         try 
         {
             String sql="SELECT record FROM clientgame WHERE userName=?"; 
             pst=con.prepareStatement(sql);
             pst.setString(1, player);
             rs=pst.executeQuery();  
              while(rs.next())
            {
                System.out.println(rs.getInt(1));
            }
         } catch (SQLException ex) {
             Logger.getLogger(DataBaseClass.class.getName()).log(Level.SEVERE, null, ex);
         }
         return rs;
    }
        
    static ResultSet getRecord(String player,int gameId)
    {
         try {
             // playername
             //accept Game
             //retur query result
             String sql="SELECT record FROM clientgame WHERE userName=? AND gameId=?";
             pst=con.prepareStatement(sql);   
             pst.setString(1, player);
             pst.setInt(2, gameId);
             rs=pst.executeQuery();
             while(rs.next())
            {
                System.out.println(rs.getInt(1));
            }
         } catch (SQLException ex) {
             Logger.getLogger(DataBaseClass.class.getName()).log(Level.SEVERE, null, ex);
         }
         return rs;
    }
    static int deleteRecord(String player,int gameId)
    {
         try {
             String sql="DELETE FROM record WHERE userName=? AND gameId=?";   
             pst=con.prepareStatement(sql);
             pst.setString(1, player);
             pst.setInt(2, gameId);
             r = pst.executeUpdate();
         } catch (SQLException ex) {
             Logger.getLogger(DataBaseClass.class.getName()).log(Level.SEVERE, null, ex);
         }
          return r;
    }
    static int addRequest(int req, String sender, String recever, int accept){
        //addnew Request //update Request To add Pinding request 0 1 2
        //return query result
         try {
             String sql="INSERT INTO request( requestId, senderId, reciverId, accepted)"+" VALUES (?,?,?,?)";
             pst=con.prepareStatement(sql);
             pst.setInt(1, req);
             pst.setString(2, sender);
             pst.setString(3, recever);
             pst.setInt(4, accept);
             r = pst.executeUpdate(); 
             
         } catch (SQLException ex) {
             Logger.getLogger(DataBaseClass.class.getName()).log(Level.SEVERE, null, ex);
         }
          return r;
    }
    static int deleterequest(int req)
    {   //request id
        //return request result   
         try {
             
             String sql="DELETE FROM request WHERE requestId=?";
             pst=con.prepareStatement(sql);
             pst.setInt(1, req);
             r = pst.executeUpdate();
         } catch (SQLException ex) {
             Logger.getLogger(DataBaseClass.class.getName()).log(Level.SEVERE, null, ex);
         }
          return r;
    }
 
    static int updateRequest(int req, int accept)
    {
         try {
             //accept request Id
             //update oly in pinding state //0 refused 1 accepted 2 pinding
             //return query result
             String sql="UPDATE request SET accepted=?  WHERE requestId=?";
             
             pst=con.prepareStatement(sql);   
             pst.setInt(2, req);
             pst.setInt(1, accept);
             r = pst.executeUpdate();
         } catch (SQLException ex) {
             Logger.getLogger(DataBaseClass.class.getName()).log(Level.SEVERE, null, ex);
         }
          return r;
    }
    static ResultSet selectRequests(String sender)
    // accept player Userame 
    // select player Requests
    // it should return the query result
    {
         try
         {
             String sql="SELECT* FROM request WHERE senderId=?";
             
             
             pst=con.prepareStatement(sql);
             pst.setString(1, sender);
             rs=pst.executeQuery();  
              while(rs.next())
            {
                System.out.println(rs.getInt(1));
                System.out.println(rs.getString(2));
                System.out.println(rs.getString(3));
                System.out.println(rs.getInt(4));
            }
             
         } catch (SQLException ex) {
             Logger.getLogger(DataBaseClass.class.getName()).log(Level.SEVERE, null, ex);
         }
         return rs;
    }
    
    /*public static void main(String[] args) {
        DataBaseClass.DataBaseClass("tic_tac_toe_zoo","root","");
        DataBaseClass.selectPlayers();
       // DataBaseClass.insertPlayer("engy","174");
        //DataBaseClass.addRequest(4, "engy","sara",1);
       // DataBaseClass.insertGame(17,"engy","sara","engy",7);
          DataBaseClass.deletePlayer("engy","174" );
        
    }*/
}
