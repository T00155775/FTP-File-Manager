import java.rmi.*; 
import java.rmi.registry.*; 
import java.rmi.server.*;
import javax.swing.*;
import java.io.*;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.*;
 
public class FileManagerClient 
{

public static void main(String args[]) 
{ 
	// Global declarations
	String username = "";
	String message = "";
	String option ="login";
	byte[] fileContent = null;
	String filename ="";
	String downloadFile ="";
	File userDownloadDirectory = null;
	File downloadsFolder = null;
	boolean loop;
	
		// Create and install the security manager 
		System.setSecurityManager(new RMISecurityManager()); 

		try 
		{  
			 
			
			//myFileManager is an object reference use "localhost" to run on a single machine.
			FileManagerInt myFileManager = (FileManagerInt)Naming.lookup("rmi://" 
                      + args[0] + "/" + "FileManagerServer");
                      
                    
             //Create a download directory to contain users indivdual
             //download folders.        
             downloadsFolder = new File("Downloads");
				
			// if the directory does not exist, create it
				
				if (!downloadsFolder.exists()) 
				{
					boolean result = false;

					try
					{
						downloadsFolder.mkdir();
						result = true;
					} 
					catch(SecurityException se)
					{
						JOptionPane.showMessageDialog(null,"Error in creating download folder");
					}
					catch(Exception e) 
					{ 
						System.err.println("System Exception" + e); 
					}  
						      
				}
                      
            
            // ********************************************************************************************************************************************
			// Start user login and folder set-up
			while(loop =true)
			{
				
				if(option.equals("login"))
				{
				//Log onto Server using userName() method
				try{
				username = userName();
			   	myFileManager.logIn(username);
				}
				
				// NullPointerException if user hits cancel
				catch(NullPointerException ex)
				{
					JOptionPane.showMessageDialog(null,"Error in operation");
					username = userName();
			   		myFileManager.logIn(username);
					
				}
				
				
				//create download folder for each individual user as they log on
				userDownloadDirectory = new File(downloadsFolder+"\\"+username);
					
				
				// if the directory does not exist, create it
				
				if (!userDownloadDirectory.exists()) 
				{
					
					boolean result = false;

					try
					{
						userDownloadDirectory.mkdir();
						result = true;
					} 
					catch(SecurityException se)
					{
						JOptionPane.showMessageDialog(null,"Error in creating download folder for "+username);
					}  
						      
				}
					// login is successful.
					// Call options menu.
					option = userMenu();
				}
				
				
					
				// ********************************************************************************************************************************************
			
				//invoke remote method based on selected options
				// Offer further option to user until log-out
			
				
				if(option.equals("Upload"))
				{
					// Allow users select a file to upload
					JFileChooser fileopen = new JFileChooser();

					int ret = fileopen.showDialog(null, "Open file");

					if (ret == JFileChooser.APPROVE_OPTION) {
				
					try{
						File file = fileopen.getSelectedFile();
				
						FileInputStream fin = new FileInputStream(file);
	
            			fileContent = new byte[(int)file.length()];

            			fin.read(fileContent);

						filename = file.getName();
				
					
				
				}
					catch(FileNotFoundException e){JOptionPane.showMessageDialog(null,"File not Found");}
        			catch(IOException e){JOptionPane.showMessageDialog(null,"File Sytem error");}

				}	
					// invoke remote method upLoad(), display menu	
					myFileManager.upLoad(filename,fileContent);
					option = userMenu();
				}
				// Upload ends here
				// ********************************************************************************************************************************************
				
				
				if(option.equals("Download"))
				{
				// List all the files in the current users folder
				// Users can only download from their own folder for security reasons
				try{
				
				String dirPath = "C:\\FTPManager\\SERVER\\"+username;
				File folder = new File(dirPath);
				File[] listOfFiles = folder.listFiles();
				String[] filenames = new String[listOfFiles.length];
				for (int i = 0; i < listOfFiles.length; i++) 
				{
    					if (listOfFiles[i].isFile()) 
    					{
    						filenames[i] = listOfFiles[i].getName();
    
    					} 
    					else if (listOfFiles[i].isDirectory()) 
    					{
     							System.out.println("Directory " + listOfFiles[i].getName());
    					}
				}

				
				
				// Get name of file to be downloaded
				String input = (String) JOptionPane.showInputDialog(null, "Select File","DOWNLOAD", JOptionPane.QUESTION_MESSAGE, null,filenames, filenames[0]);
					

					// invoke remote method download(), display menu
					myFileManager.download(username,input);
					option = userMenu();
				
				}
				catch (ArrayIndexOutOfBoundsException ex) 
        		{
            		System.out.println("Folder is empty" + ex);
        		} 
				}
				// Download ends here
				// ********************************************************************************************************************************************
				
				// ********************************************************************************************************************************************
				
				// log out of server
				if(option.equals("LogOut"))
				{
					// invoke remote logout method
					// exit while loop
					myFileManager.logout(username);
					
					loop = false;
					
					option = "login";		
				}
			
					
				
				// ********************************************************************************************************************************************
			
				
			}// End While Loop 
				

	}// End Try

	catch(NullPointerException np) 
	{
						
		JOptionPane.showMessageDialog(null,"Fatal error. Exiting system");
		System.exit(0);				
	}// End Catch
	catch(Exception e) 
	{ 
		System.err.println("System Exception" + e); 
	}// End Catch
 

}//End Main

				// *****************************************************Helper Methods Follow***************************************************************************************
				

				//Method that displays user menu.
				public static String userMenu()
				{
					String input ="";
					
					try{
					
					String[] choices = { "Upload","Download","LogOut" };
					input = (String) JOptionPane.showInputDialog(null, "Choose option","OPTIONS", JOptionPane.QUESTION_MESSAGE, null,choices, choices[0]); // Initial choice
					}
					catch(NullPointerException np) {
						
						JOptionPane.showMessageDialog(null,"Fatal error. Exiting system");
						System.exit(0);
						
					}
					return input;
				
				} 
					
				//Method used to capture user name.
				public static String userName()
				{
					
					String input ="";
					try{
					
					input = JOptionPane.showInputDialog("Please log-on to the server\nSupply a username:");
						
					// Username cannot be blank or null program execution will not continue until a username is supplied
					while(input.length()<=0|| input.equals(null))
						{
							input = JOptionPane.showInputDialog("Error invalid log-in attempt!!\nPlease log-on to the server\nSupply a username:");
						}	
					
					}
					catch(NullPointerException np) {
						
						JOptionPane.showMessageDialog(null,"Fatal error. Exiting system");
						System.exit(0);
					}
					return input;	
					
				}
				
			
}// End class