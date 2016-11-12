package DBAccessor;

import java.sql.*;

public class DB {

	public Connection connection = null;
	
	public DB() {
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/recommender","root", "somethingcl3v3r");
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	public void safeClose(Connection con) {
		if (con != null) {
			try {con.close();}
			catch (SQLException e) {
				// ...
			}
		}
	}
	
	public void safeClose(Statement st) {
		if (st != null) {
			try {st.close();}
			catch (SQLException e) {
				// ...
			}
		}
	}
	
	public void safeClose (PreparedStatement cps) {
		if (cps != null) {
			try {cps.close();}
			catch (SQLException e) {
				// ...
			}
		}
	}
	
	public void safeClose(ResultSet crs) {
		if (crs != null) {
			try {crs.close();}
			catch (SQLException e) {
				// ...
			}
		}
	}
}
