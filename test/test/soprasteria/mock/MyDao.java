package test.soprasteria.mock;

public class MyDao {
	
	public MyEntity findById(long id) throws Exception {
		System.out.println("here");
		return new MyEntity();
	}

}
