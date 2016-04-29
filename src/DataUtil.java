import java.beans.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataUtil {

	public static void connectionDatabase() throws Exception {
		
		try {
			
			Connection connection = DriverManager.getConnection("jdbc:ucanaccess://credential.accdb","","");
			java.sql.Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("select * from credential");
			while (resultSet.next()) {
				System.out.println(resultSet.getString(2));
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static boolean checkCredential(Credential credential) throws Exception {
		Connection connection = DriverManager.getConnection("jdbc:ucanaccess://credential.accdb","","");
		java.sql.Statement statement = connection.createStatement();
		String statementStr = String.format("select * from credential where DEVICE_KEY=\'%s\' and USER_KEY=\'%s\'", credential.device_key,credential.user_key);
		System.out.println(statementStr);
		ResultSet resultSet = statement.executeQuery(statementStr);
		if (resultSet.next()) {
			return true;
		}
		else {
			return false;
		}
		
	}
	
	public static int getCredentialIndex(Credential credential) throws Exception {
		Connection connection = DriverManager.getConnection("jdbc:ucanaccess://credential.accdb","","");
		java.sql.Statement statement = connection.createStatement();
		String statementStr= String.format("select id from credential where DEVICE_KEY=\'%s\' and USER_KEY=\'%s\'", credential.device_key,credential.user_key);
		ResultSet resultSet = statement.executeQuery(statementStr);
		if (resultSet.next()) {
			return resultSet.getInt(1);
		}
		else {
			return 0;
		}
	}
	
	public static String[] getFolderPathArray(Credential credential) throws Exception {
	
		Connection connection = DriverManager.getConnection("jdbc:ucanaccess://credential.accdb","","");
		java.sql.Statement statement = connection.createStatement();
		String statementStr= String.format("select DIRECTORY from credential where DEVICE_KEY=\'%s\' and USER_KEY=\'%s\'", credential.device_key,credential.user_key);
		ResultSet resultSet = statement.executeQuery(statementStr);
		String reString=null;
		if (resultSet.next()) {
			reString=resultSet.getString(1);
			String [] folderPath=reString.split(",");
			return folderPath;
		}
		else {
			return null;
		}
	}
	public static void inserCredential(Credential credential) throws Exception {
		Connection connection = DriverManager.getConnection("jdbc:ucanaccess://credential.accdb","","");
		String statement = String.format("insert into credential(DEVICE_KEY,USER_KEY) values(\'%s\', \'%s\')", credential.device_key,credential.user_key);
		PreparedStatement preparedStatement = (PreparedStatement)connection.prepareStatement(statement);
		preparedStatement.executeUpdate();
		preparedStatement.close();
	}
	
	
	
	
	public static void insertDirectory(Credential credential,String directory) throws Exception {
		Connection connection = DriverManager.getConnection("jdbc:ucanaccess://credential.accdb","","");
		String statement1= String.format("insert DIRECTORY from credential where DEVICE_KEY=\'%s\' and USER_KEY=\'%s\'", credential.device_key,credential.user_key);
		PreparedStatement preparedStatement1 = (PreparedStatement)connection.prepareStatement(statement1);
		ResultSet resultSet1=preparedStatement1.executeQuery();
		String orignalDir=null;
		if (resultSet1.next()) {
			orignalDir=resultSet1.getString(1);
		}
		preparedStatement1.close();
		String newDir=orignalDir+","+directory;
		String statement2 = String.format("update credential(DEVICE_KEY,USER_KEY) set DIRECTORY=\'%s\' where DEVICE_KEY=\'%s\' and USER_KEY=\'%s\'",newDir,credential.device_key,credential.user_key);
		PreparedStatement preparedStatement2 = (PreparedStatement)connection.prepareStatement(statement2);
		preparedStatement2.executeUpdate();
	}
}
