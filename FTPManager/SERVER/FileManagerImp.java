import java.rmi.*; 
import java.rmi.server.UnicastRemoteObject;
import javax.swing.*; 
import java.io.*;
import java.io.File;
import java.util.*;

/**
 * @(#)FileManagerImp.java
 *
 *
 * @author Joe O Flaherty
 * @version 1.00 2014/12/17
 */

public class FileManagerImp extends UnicastRemoteObject implements FileManagerInt
{ 
	// Global declarations 
	String username ="";
	File dir = null;
   	File userFile = null;
   	String message ="";
   	byte[] fileContent;
   	File userDirectory = null;
   	byte downloadFile[] = null;
   	byte buffer[] =null;
   	BufferedInputStream bufferIn;
   	ArrayList loggedIn = null;
   	
   	
  		// Constructor
		public FileManagerImp(String name) throws RemoteException 
  		{ 
    		super();
    		try 
    		{ 
      			Naming.rebind(name, this); 
    		} 
    		catch (Exception e) 
    		{ 
    			System.out.println("Exception: " + e.getMessage()); 
      			//e.printStackTrace(); 
    		} 
    		
    		// instantiate loggedIn user list
    		loggedIn = new ArrayList<String>();
    		
  		}//End Constructor
  
  // ******************************************************************************************************************************************** 
	 
			// login method logs a client user onto server and createss a folder
			// if a folder by that name does not already exist.
   			public String logIn(String username) throws java.rmi.RemoteException
   			{
	
				//Record any user that has logged into the system in a text file.
 				try {
          	
				PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("Users.txt", true)));
				out.println("Username: "+username);
				out.close();
								
        		} 
        		catch ( IOException e ) 
        		{
        			System.out.println("Exception: " + e.getMessage());
           			//e.printStackTrace();
        		}
							
				//Create a folder and file for the user to handle  download requests
            	userDirectory = new File(username);
						
				if (userDirectory.exists()) 
					{
						JOptionPane.showMessageDialog(null,"User folder already exists for: " +username);
					}
					
				// if the directory does not exist, create it
					
				if (!userDirectory.exists()) 
				{
						
						JOptionPane.showMessageDialog(null,"Creating user directory for: " +username);
						boolean result = false;

						try
						{
							userDirectory.mkdir();
							result = true;
						} 
						catch(SecurityException se)
						{
							JOptionPane.showMessageDialog(null,"Error in creating user folder");
						}  
							      
					if(result) 
						{   
							//Confirm folder is created to the user 
							JOptionPane.showMessageDialog(null,"User folder created");  
						}
 				}
            		
					String message ="";	
						
					//Add user to logged in list.
					loggedIn.add(username);
					// Display list of logged in users on Server console window
					System.out.println(loggedIn.toString());
					//confirm log in success.
					JOptionPane.showMessageDialog(null,"Log in successful\n" +username+" is logged onto the file manager");
					
					
					return message;
				
				 
	
				}//End Login method
				
				// ********************************************************************************************************************************************
			

				// Start upload method
				public void upLoad(String filename,byte[] file) throws java.rmi.RemoteException
				{
					
	
				
				try{
				
					FileOutputStream uploadFile = new FileOutputStream(userDirectory + "\\" + filename);
                	uploadFile.write(file);
                
                
                	JOptionPane.showMessageDialog(null,"Upload completed");
                
                	uploadFile.flush();
					uploadFile.close();
               
            
            
      			} 
      				catch (IOException ex) 
        			{
            			JOptionPane.showMessageDialog(null,"There was an issue uploading - to the user folder.");
        			}
        	
        			
        			
				}//End upload method
				
				// ********************************************************************************************************************************************
		
			
				//Start download method
				public byte[] download(String username,String filename) throws java.rmi.RemoteException{
				
				
				byte download[] = null;
        		try 
        {
            	File fileIn = new File("C:\\FTPManager\\SERVER\\"+username+"\\"+filename);
            	download = new byte[(int) fileIn.length()];
            	FileInputStream fileInStream = new FileInputStream(fileIn);
            	fileInStream.read(download);
            	
           
        } 
        catch (RemoteException ex) 
        {
            
        } 
        catch (IOException ex) 
        {
            System.out.println("IOException : " + ex);
        } 
        catch (ArrayIndexOutOfBoundsException ex) 
        {
            System.out.println("Folder is empty" + ex);
        } 
				
			
				try{
				
					File file = new File("C:\\FTPManager\\CLIENT\\Downloads\\"+username+"\\"+filename);
					FileOutputStream downLoadFile = new FileOutputStream(file);
                	downLoadFile.write(download);
                
                
                	JOptionPane.showMessageDialog(null,"Download completed");
                
                	downLoadFile.flush();
					downLoadFile.close();
               
            
            
      			} 
      				catch (IOException ex) 
        			{
            			JOptionPane.showMessageDialog(null,"There was an issue downloading - to the users download folder.");
        			}
			
        
        		return buffer;
        
        
			
	
				}//End download method
				
				// ********************************************************************************************************************************************
				
				
				//Start logout method
				public void logout(String username)throws java.rmi.RemoteException
				{
		
				// remove user from list of logged in users
				loggedIn.remove(username);
				// Display list of logged in users on Server console window
				System.out.println(loggedIn.toString());
				
				//Confirmation message.
				message = "Logging out "+ username;
				JOptionPane.showMessageDialog(null,message);
		
				}//End logout method
				
				
		
} //End Class