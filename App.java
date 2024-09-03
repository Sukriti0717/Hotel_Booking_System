package com.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.sql.Timestamp;

// Hotel booking system
public class App 
{
	
	private static final String URL="jdbc:mysql://127.0.0.1:3306/hoteldb";
	private static final String USERNAME="root";
	private static final String PASSWORD="admin1234";
	
	
    public static void main( String[] args )
    {
        System.out.println("Welcome to Hotel....");
        // Load drivers - deprecated
        
//        try {
//        	Class.forName("com.mysql.jdbc.Driver");
//			
//        	System.out.println("driver loaded....");
//		} catch (ClassNotFoundException e) {
//			System.out.println("failed to load the drivers : " +e.getMessage());
//		}
    	
        // Connection to DB.
        
        try {
        	Connection connection=DriverManager.getConnection(URL,USERNAME,PASSWORD);
        	
        	
        	System.out.println("Connected to DB...");
        	System.out.println();
        	
        	/*
        	 MENU -
        	 1. Book a room 			- insert data of customer - save it into db
        	 2. View the room 			- show details of that customer -> room
        	 3. Show all the booking	- show all customer data
        	 4. Update booking details 	- update data 
        	 5. Delete data				- delete customer data 
        	 6. Exit 
        	 */
        	
        	// let's show these options to user 
        	
        	boolean flag= true;
        	while(flag) {
            	System.out.println("Please select from below : ");
            	System.out.println("1. Book a room");
            	System.out.println("2. View the room");
            	System.out.println("3. Show all the booking");
            	System.out.println("4. Update booking details");
            	System.out.println("5. Delete booking");
            	System.out.println("6. Exit");
                Scanner scanner = new Scanner(System.in);
                System.out.print("ENTER OPTION : ");
                int option=scanner.nextInt() ;
                System.out.println("Option selected by user : "+option);
                System.out.println();
                
                
                switch (option) {
				case 1:
					bookARoom(connection,scanner);
					break;
				case 2:
					viewBookedRoom(connection, scanner);
					break;
				case 3: 
					showALlBookings(connection);
					break;
				case 4: 
					updateDetails(connection, scanner);
					break;
				case 5: 
					deleteBooking(connection, scanner);
					break;
				case 6: 
					
      			System.out.println("Thank you... program is terminated");
      			 flag=false;
                 break;
				default:
					System.out.println("please select an option from 1 to 6");
					break;
				}
                
                
        	}
        	connection.close();
        	System.out.println("Connection closed...");
        	
    		} catch (SQLException e) {
    			System.out.println("failed to connect with DB.. "+e.getMessage());
    		}
       
        }
                
                
                
            	
//                if(option==1) {
//                	bookARoom(connection,scanner);
//                    System.out.println();
//
//                }else if(option==2) {
//                	viewBookedRoom(connection, scanner);
//                    System.out.println();
//
//                }else if (option==3) {
//                	showALlBookings(connection);
//                    System.out.println();
//
//                }else if (option==4) {
//                	updateDetails(connection,scanner);
//                    System.out.println();
//
//                }else if (option==5) {
//                	deleteBooking(connection,scanner);
//                    System.out.println();
//
//                }
//                else if(option==6) {
//        			System.out.println("Thank you... program is terminated");
//        			break;
//        		} else {
//        			System.out.println("please select an option from 1 to 6");
//        		}
//        	}
                
                
        	
        	
        	// close the connection here 
            
        
    // Method to book a room..
    private static void bookARoom(Connection connection,Scanner scanner) {
    	
    	// columns - id , name , room_no ,phone, timestamp
    	String sql ="INSERT INTO booking_table (name,room_no,phone)VALUES(?,?,?)";
    	
    	try {
    		
    	PreparedStatement preparedStatement=connection.prepareStatement(sql);
    	
    	
    	System.out.print("ENTER NAME : ");
    	String nameString=scanner.next();
    	System.out.print("ENTER ROOM NO : ");
    	int roomNo=scanner.nextInt();
    	System.out.print("ENTER PHONE NUMBER : ");
    	String phoneNumber=scanner.next();
    	if(nameString!=null && phoneNumber!=null&& roomNo!=0) {
    		preparedStatement.setString(1, nameString);
        	preparedStatement.setInt(2,roomNo);
        	preparedStatement.setString(3, phoneNumber);
        	
        	
        	int rowsAffected =preparedStatement.executeUpdate();
        	if(rowsAffected>0) {
        		System.out.println("Booked successfully...");
        		System.out.println();
        	}else {
        		System.out.println("Failed to book... please try again...");
        	}
    	}else {
    		System.out.println("please enter details");
    	}
    	
    	preparedStatement.close();
			
		} catch (SQLException e) {
			System.out.println("failed to book a room : "+ e.getMessage());
		}
    	
    	
    }

     // Method to View one booked room 
    private static void viewBookedRoom(Connection connection,Scanner scanner) {
    	
    	String sql="SELECT * FROM booking_table WHERE id =? AND name = ?";
    	
    	try {
			PreparedStatement preparedStatement=connection.prepareStatement(sql);
			
			
			
			System.out.print("ENTER ID : ");
			int userInputId = scanner.nextInt();
			if(!bookingExist(connection, userInputId)) {
  				System.out.println("Booking not available with this ID ..");
  				return;
  			}
			System.out.print("ENTER NAME : ");
			String userInputName = scanner.next();
		
			System.out.println();
			if(userInputName!=null&& userInputId!=0) {
				
				preparedStatement.setInt(1, userInputId);
				preparedStatement.setString(2, userInputName);
				ResultSet resultSet= preparedStatement.executeQuery();
				
				System.out.println("+----------+--------------+--------------+-------------+-------------------------+");
				System.out.println("    ID           NAME        ROOM NO        PHONE                 TIME               ");
				System.out.println("+----------+--------------+--------------+-------------+-------------------------+");

				while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String nameString = resultSet.getString("name");
				int roomNo= resultSet.getInt("room_no");
				String phoneNumber= resultSet.getString("phone");
				Timestamp timestamp = resultSet.getTimestamp("booking_time");
				System.out.println("   "+id+"            "+nameString+"           "+roomNo+"              "+phoneNumber+"      "+timestamp+" ");
				}
				System.out.println("+----------+--------------+--------------+-------------+-------------------------+");
	               System.out.println();
				resultSet.close();
				preparedStatement.close();
				
			}else {
				System.out.println("please enter the Id and name ...");
			}
			
		} catch (SQLException e) {
			System.out.println("failed to get single  data from table " + e.getMessage());
		}
    	
    }
    
    
    // Method to show all the booking 
    
    private static void showALlBookings(Connection connection) {
    	
    	String sql="SELECT * FROM booking_table";
    	
    	try {
    		
    		Statement statement=connection.createStatement();
    		ResultSet resultSet= statement.executeQuery(sql);
    		System.out.println("+----------+--------------+--------------+-------------+-------------------------+");
			System.out.println("    ID           NAME        ROOM NO        PHONE                 TIME               ");
			System.out.println("+----------+--------------+--------------+-------------+-------------------------+");

    		while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String nameString = resultSet.getString("name");
				int roomNo= resultSet.getInt("room_no");
				String phoneNumber= resultSet.getString("phone");
				Timestamp timestamp = resultSet.getTimestamp("booking_time");
				System.out.println("   "+id+"            "+nameString+"           "+roomNo+"              "+phoneNumber+"      "+timestamp+" ");
				}
				System.out.println("+----------+--------------+--------------+-------------+-------------------------+");
	               System.out.println();
	               resultSet.close();
	               statement.close();
			
		} catch (SQLException e) {
			System.out.println("Failed to get all data from Tabe : "+ e.getMessage());
		}
    	
    }

    
    private static void updateDetails(Connection connection,Scanner scanner) {
    	String sqlString= "UPDATE booking_table SET phone = ? WHERE id = ?";
    	try {
			
    		PreparedStatement preparedStatement=connection.prepareStatement(sqlString);
    		
    		
    		System.out.print("ENTER ID: ");
    		int inputId= scanner.nextInt();
    		if(!bookingExist(connection, inputId)) {
  				System.out.println("Booking not available with this ID ..");
  				return;
  			}
    		System.out.print("ENTER Phone : ");
    		
    		String inputPhoneNumber=scanner.next();
    		if(inputId!=0&& inputPhoneNumber!=null) {
    			
    			
    			preparedStatement.setString(1, inputPhoneNumber);
        		preparedStatement.setInt(2, inputId);
        		int rowsAffected =preparedStatement.executeUpdate();
        	if(rowsAffected>0) {
        		System.out.println("Updated the details...");
        	}else {
        		System.out.println("Not updated the details...");

        	}
        	
        	preparedStatement.close();
    		}else {
    			System.out.println("Pleas try again");
    		}
    		
		} catch (SQLException e) {
			System.out.println("Failed to update details  " + e.getMessage());
		}
    	
    	
    }

    private static void deleteBooking(Connection connection,Scanner scanner) {
    	String sqString = "DELETE FROM booking_table WHERE id = ? ";
    	
    	try {
    	      PreparedStatement preparedStatement=connection.prepareStatement(sqString);
    	   
      		System.out.print("ENTER ID TO DELETE Details : ");
      		int inputId= scanner.nextInt();
      		System.out.println(inputId);
      		if(inputId!=0) {
      			
      			if(!bookingExist(connection, inputId)) {
      				System.out.println("Booking not available with this ID ..");
      				return;
      			}
      			
      		  preparedStatement.setInt(1, inputId);
      		  int rowsAffected = preparedStatement.executeUpdate();
      		if(rowsAffected>0) {
        		System.out.println("Deleted  the details...");
        	}else {
        		System.out.println("Not deleted the details...");
        	}
      		preparedStatement.close();
      		}   
    	}catch(SQLException e) {
    		System.out.println("failed to delete : "+ e.getMessage());
    	}
    	
		
	}
    
    private static boolean bookingExist(Connection connection,int id) {
    	
    	String sqlString = "SELECT * FROM booking_table WHERE id = ?";
    	
    	try {
			
    		PreparedStatement preparedStatement=connection.prepareStatement(sqlString);
    		preparedStatement.setInt(1, id);
    		
    		ResultSet resultSet=preparedStatement.executeQuery();
    		if(resultSet.next()) {
    			return true;
    		}
    		resultSet.close();
    		preparedStatement.close();
    		
		} catch (SQLException e) {
		
			System.out.println("Message : "+ e.getMessage());
		}
		return false;
    }
}
