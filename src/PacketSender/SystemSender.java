package PacketSender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.SocketException;

import Login.ConfigurationController;
import Products.ConstantData;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


/**
 * This Class uses as thread that create a request to the server for the relevant data
 * and also wait for arrived data from the server if needed
 *
 */
public class SystemSender extends Thread
{
	/**
	 *  private attributes
	 */
	
	private SystemClient client;
	private Packet packet;
	private IResultHandler handler;
	
	/**
	 * Constructor that initialize the handler object that implement the solution when data arrived
	 * @param packet instance of packet that we want send to the server
	 * @param handler instance of requested data class
	 */
	public SystemSender(Packet packet, IResultHandler handler)
	{
		this.client = initClient(handler);
		this.packet = packet;
		this.handler = handler;
	}
	
	/**
	 * Constructor that initialize the handler object that implement the solution when data arrived
	 * @param packet instance of packet that we want send to the server
	 */
	public SystemSender(Packet packet)
	{
		this(packet, null);
	}
	
	/**
	 * Register an handler for client
	 * 
	 * @param handler the handler implements
	 */
	public void registerHandler(IResultHandler handler)
	{
		this.handler = handler;
		this.client.registerHandler(handler);
	}
	
	/**
	 * Initialize client connection from configuration file or uses of default parameters
	 * @return Instance of Client
	 */
	private SystemClient initClient(IResultHandler handler) {
		String host = ConfigurationController.address;
		int port = ConfigurationController.port;
		
		return new SystemClient(handler, host, port);
	}
	
	public void run()
	{
		try
		{
			client.sendToServer(packet);
			if (handler != null)
				handler.onWaitingForResult();
		}
		catch (Exception e)
		{
			Platform.runLater(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					ConstantData.displayAlert(AlertType.ERROR, "Error", "Server Error", e.getMessage());
				}
			});
		}
	}
	
}
