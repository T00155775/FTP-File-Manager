import java.rmi.*;
import java.io.*;

/**
 * This is the remote interface for a FTP Client/Server system using 
 * the RMI distributed object paradigm.
 * @author Joe O Flaherty
 */

public interface FileManagerInt extends Remote {

/**
     * User(s)Log-in to the system using this method
     * @param username string that is the current users name..
     * @return response string confirming the success or failure of the log in attempt.
     * @throws RemoteException 
     */
	 // login method to be implemented
   	public String logIn(String username) 
      		throws java.rmi.RemoteException;
			
	// logout method to be implemented
	public void logout(String username)
		throws java.rmi.RemoteException;
		
	// upload method to be implemented
	public void upLoad(String username, byte[] file)
		throws java.rmi.RemoteException;
		
	// download method to be implemented
	public byte[] download(String username,String filename)
		throws java.rmi.RemoteException;


} //end interface