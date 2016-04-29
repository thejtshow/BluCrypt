import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.microedition.io.StreamConnection;

public class WriteThread extends Thread{

	//String response;
	StreamConnection streamConnection;
	PrintWriter printWriter;
	
	public WriteThread( StreamConnection streamConnection) throws Exception {
		
		//this.response = response;
		this.streamConnection = streamConnection;
		OutputStream outputStream = streamConnection.openOutputStream();
		this.printWriter=new PrintWriter(new OutputStreamWriter(outputStream));
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		 
		//printWriter.write(response);
		printWriter.flush();

	}
	public void sendMessage(String message) {
		printWriter.write(message);
		printWriter.flush();
		
	}
	
	
	
	
	
}
