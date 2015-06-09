package command;


import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Properties;

public class AseMasterCommand {
	
	Connection connection;
	public AseMasterCommand(String userName,String password,String serverName,String port){
		//for example serverName = 10.173.1.193
        try{
            DriverManager.registerDriver((Driver) Class.forName("com.sybase.jdbc4.jdbc.SybDriver").newInstance());
            String connectionString="jdbc:sybase:Tds:"+serverName+":"+port+"/master";
            Properties info = new Properties();
            info.put("user", userName);
            info.put("password", password);
            info.put("ENCRYPT_PASSWORD","true");
            connection=DriverManager.getConnection(connectionString,info);
        }catch(Exception exp){
            exp.printStackTrace();
        }
    }
	
	public  int getResultBySQLCmd(String sqlCmd){
        int numberOfItems=0;
        try{
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sqlCmd);
            ResultSetMetaData rsMetaData=rs.getMetaData();
            Integer totalCols=rsMetaData.getColumnCount();
            while (rs.next()) {
                numberOfItems++;
                for(int i=1;i<=totalCols;i++){
                    String value=rs.getString(i);
                    System.out.print(value+"\t\t\t");
                }
                System.out.println();
            }
            rs.close();
            stmt.close();
        }catch(Exception exp){
            exp.printStackTrace();
            return numberOfItems;
        }
        return  numberOfItems;
    }

	public int getSendTraceCount() {
		return getResultBySQLCmd("select * from rs_ticket_history");
	}

}
