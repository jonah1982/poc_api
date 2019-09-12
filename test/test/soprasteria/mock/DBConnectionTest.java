package test.soprasteria.mock;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DBConnectionTest {
	
	@InjectMocks private DBConnection dbConnection;
	@Mock private Connection mockConnection;
	@Mock private Statement mockStatement;
	
	@Before
	public void setUp() {
//		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testMockDBConnection() throws Exception {	
		Mockito.when(mockConnection.createStatement()).thenReturn(mockStatement);
		Mockito.when(mockConnection.createStatement().executeUpdate((String) Mockito.any())).thenReturn(1);
		Mockito.doReturn(1).when(dbConnection).getDBConnection();
		int value = dbConnection.executeQuery("12321");
		Assert.assertEquals(value, 1);
		Mockito.verify(mockConnection.createStatement(), Mockito.times(1));
	}

}

class DBConnection {
	
	private Connection dbConnection;
	
	public void getDBConnection() throws ClassNotFoundException, SQLException {		
		Class.forName("com.mysql.jdbc.Driver");
		dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:6666/jcg", "root", "password");		
	}
	
	public int executeQuery(String query) throws ClassNotFoundException, SQLException {
		System.out.println(query);
		try {
			getDBConnection();
			return dbConnection.createStatement().executeUpdate(query);
		} catch (Exception e) {
			return 0;
		}
	}

}
