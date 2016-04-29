import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Timer;

import javax.microedition.io.StreamConnection;


public class ReadThread extends Thread{

	StreamConnection streamConnection;
	volatile String readMessage;
	public static boolean receiveHeartBeat = false;
	private static Credential credential = new Credential();
	private static Timer timer;
	private static WriteThread writeThread;
	

	
	
	
	public ReadThread(StreamConnection streamConnection, String readMessage) throws Exception {
		this.streamConnection = streamConnection;
		this.readMessage=readMessage;
		this.writeThread=new WriteThread(streamConnection);
		this.writeThread.start();
	}

	public static byte[] readStream(InputStream inStream) throws Exception {  
	    ByteArrayOutputStream outSteam = new ByteArrayOutputStream();  
	    byte[] buffer = new byte[1024];  
	    int len = -1;  
	    while ((len = inStream.read(buffer)) != -1) {  
	        outSteam.write(buffer, 0, len);  
	    }  
	    outSteam.close();  
	    inStream.close();  
	    return outSteam.toByteArray();  
	}  

	public void startTimer(){
		timer = new Timer();
		HeartBeatTask heartBeatTask = new HeartBeatTask(this);
		timer.schedule(heartBeatTask, 11*60*1000);
	}
	
	/**
	 * restart timer
	 */
	public void updateTimer() {
		HeartBeatTask heartBeatTask = new HeartBeatTask(this);
		timer.cancel();
		timer = new Timer();
		timer.schedule(heartBeatTask, 11*60*1000);
	}
	
	public void setHeartBeat(boolean value) {
		receiveHeartBeat = value;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			InputStream inputStream=streamConnection.openInputStream();
			while(true){
			
			DataInputStream dataInputStream=new DataInputStream(inputStream);
			byte [] buffer=new byte[dataInputStream.available()];
			if (buffer.length!=0) {
				dataInputStream.readFully(buffer);
				readMessage=new String(buffer);
				System.out.println(readMessage);
				writeThread.sendMessage("Connection Success!");
				if (JSONUtil.parseJSONHeartBeat(readMessage)==5*60*1000) {
					setHeartBeat(true);
					String heartbeat_success = JSONUtil.generateJSON("Received HeartBeat!");
					System.out.println(heartbeat_success);
					writeThread.sendMessage(heartbeat_success);
					break;
				}else {
					//WriteThread writeThread=new WriteThread("Connection Success!", streamConnection);
					//witeThread.start();
					
					JSONUtil.parseJSON(readMessage, credential);
					System.out.println("DEVICE_KEY:"+credential.device_key);
					System.out.println("USER_KEY:"+credential.user_key);
					String response_success = JSONUtil.generateJSON("Login Success");
					String response_fail = JSONUtil.generateJSON("Fail, you are a new user!");
					String decrypt_success = JSONUtil.generateJSON("Decrypt Success");
					if (DataUtil.checkCredential(credential)) {
						writeThread.sendMessage(response_success);
						System.out.println("Index:"+DataUtil.getCredentialIndex(credential));
						String [] folderPath = DataUtil.getFolderPathArray(credential);
						
//						CryptUtil cryptUtil = new CryptUtil(folderPath, "C:\\Users\\charles\\Desktop\\keyfolder\\", "zx9956123", credential);
//						cryptUtil.generateKey();
//						cryptUtil.encryptFile();
						
						
						
						
						CryptUtil cryptUtil=new CryptUtil(folderPath, "C:\\Users\\charles\\Desktop\\keyfolder\\", "zx9956123",credential);
						cryptUtil.loadKey();
						cryptUtil.decryptionFile();
						writeThread.sendMessage(decrypt_success);
						startTimer();
						
					}else {
						writeThread.sendMessage(response_fail);
						DataUtil.inserCredential(credential);
						ConfigWindow configWindow = new ConfigWindow(credential);
						
					}
				}
				
				
			}
			}
			
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void interrupt() {
		// TODO Auto-generated method stub
		
		try {
			String[] folderPath = DataUtil.getFolderPathArray(credential);
			CryptUtil cryptUtil = new CryptUtil(folderPath, "C:\\Users\\charles\\Desktop\\keyfolder\\", "zx9956123", credential);
			cryptUtil.generateKey();
			cryptUtil.encryptFile();
			System.out.println("Close! Encrypt successfully!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		try {
			String[] folderPath = DataUtil.getFolderPathArray(credential);
			CryptUtil cryptUtil = new CryptUtil(folderPath, "C:\\Users\\charles\\Desktop\\keyfolder\\", "zx9956123", credential);
			cryptUtil.generateKey();
			cryptUtil.encryptFile();
			System.out.println("Close! Encrypt successfully!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
