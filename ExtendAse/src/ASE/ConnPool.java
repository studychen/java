package ASE;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class ConnPool {

	public ConnPool() throws Exception
    {
    	DriverManager.registerDriver((Driver) Class.forName("com.sybase.jdbc4.jdbc.SybDriver").newInstance());
    }
	
	public Connection getConn(String userName,String password,String serverName,String port) throws Exception
    {
       String connectionString="jdbc:sybase:Tds:"+serverName+":"+port+"/master";
       Properties info = new Properties();
       info.put("user", userName);
       info.put("password", password);
       info.put("ENCRYPT_PASSWORD","true");
       Connection connection=DriverManager.getConnection(connectionString,info);
       return connection;
    }
	
	public Connection getConnSid(String userName,String password,String serverName,String port,String sid) throws Exception
    {
       String connectionString="jdbc:sybase:Tds:"+serverName+":"+port+"/" + sid;
       Properties info = new Properties();
       info.put("user", userName);
       info.put("password", password);
       info.put("ENCRYPT_PASSWORD","true");
       Connection connection=DriverManager.getConnection(connectionString,info);
       return connection;
    }

}
