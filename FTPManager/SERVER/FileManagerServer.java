import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.net.*;
import java.io.*;
import javax.swing.*;

/**
 * This class represents the object server for a distributed
 * object of class FileManager, which implements the remote interface
 * FileManagerInt.
 * @author Joe O Flaherty
 */

public class FileManagerServer  
{
   	public static void main(String args[]) {
      
      String portNum, registryURL;
      try{     
        
         // Request registry port number
         portNum = JOptionPane.showInputDialog("Enter RMIregistry port number:\nClick OK to use default.");
         
         // if no port number is supplied, defaults to 1099
         if(portNum.equals(""))
         {
         	portNum = "1099";
         }
         int RMIPortNum = Integer.parseInt(portNum);
         
         //Start the registry using supplied port number
         startRegistry(RMIPortNum);
         
         FileManagerImp exportedObj = new FileManagerImp("FileManagerServer");
         registryURL = "localhost";
         Naming.rebind(registryURL, exportedObj);

         
      }// end try
      
      catch (Exception re) {
         System.out.println("Exception in FileManagerServer: " + re);
         
      } // end catch
      
  } // end main

   // This method starts a RMI registry on the local host, if it
   // does not already exists at the specified port number.
   private static void startRegistry(int RMIPortNum)
      throws RemoteException{
      try {
         Registry registry = LocateRegistry.getRegistry(RMIPortNum);
         registry.list( );  // This call will throw an exception
                            // if the registry does not already exist
      }
      catch (RemoteException e) { 
         // Confirm no valid registry at that port.
/**/     System.out.println
/**/        ("RMI registry cannot be located at port " 
/**/        + RMIPortNum);
         Registry registry = 
            LocateRegistry.createRegistry(RMIPortNum);
            
			JOptionPane.showMessageDialog(null,
			"RMI registry created at port " + RMIPortNum+
				"\n\nServer ready\n\nStart a client process!");
      }
   } // end startRegistry

  // This method lists the names registered with a Registry object
  private static void listRegistry(String registryURL)
     throws RemoteException, MalformedURLException {
       System.out.println("Registry " + registryURL + " contains: ");
       String [ ] names = Naming.list(registryURL);
       for (int i=0; i < names.length; i++)
          System.out.println(names[i]);
  } //end listRegistry
     
} // end class