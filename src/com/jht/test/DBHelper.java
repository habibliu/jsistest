package com.jht.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBHelper {

	private static final String DRIVER = "DRIVER";

	private static final String URL = "URL";

	private static final String USER = "USER";

	private static final String PASSWORD = "PASSWORD";

	/**
	 * @return
	 */
	public static Connection getConnection() {
		Properties pro = ConfigHelper.getProperties("jdbc");
		String driver = pro.getProperty(DRIVER);
		String url = pro.getProperty(URL);
		String user = pro.getProperty(USER);
		String password = pro.getProperty(PASSWORD);
		try {
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, user, password);
			return conn;
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	public static void closeConnection(Connection conn){
		if(conn!=null){
			try {
				if(!conn.isClosed()){
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				conn=null;
			}
		}
	}
}
