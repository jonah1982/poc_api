package test.soprasteria.mock;

public class MyService {
	
	private MyDao myDao;
	
	public MyService() {
		this.myDao = new MyDao();
	}
	
	public MyEntity findById(long id) throws Exception {
		return myDao.findById(id);
	}

}
