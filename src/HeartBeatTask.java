import java.util.TimerTask;

public class HeartBeatTask extends TimerTask{

	private ReadThread readThread;
	

	public HeartBeatTask(ReadThread readThread) {
		super();
		
		this.readThread = readThread;
	}



	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		if(this.readThread.receiveHeartBeat==false){
			readThread.destroy();
		}else {
			
		}
		
	}
	
	

}
