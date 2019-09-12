package test.soprasteria.mock;

import static org.mockito.Matchers.any;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.gson.Gson;

@RunWith(MockitoJUnitRunner.class)
public class MyServiceTest {
	
	@Mock private MyDao myDao;
	
	@InjectMocks MyService myService;
	
	@Test
	public void testFindById() throws Exception {
//		MyService myService = new MyService(myDao);
		myService.findById(1L);
		Mockito.verify(myDao).findById(1L);
		
	}
	
	@Test
	public void test() throws Exception {
//		MyService myService = new MyService(myDao);
		Mockito.when(myDao.findById(any(Long.class))).thenReturn(createTestEntity());
		MyEntity actual = myService.findById(1L);
		System.out.println(new Gson().toJson(actual));
		Assert.assertEquals("My first name", actual.getFirstName());
		Assert.assertEquals("My surname", actual.getSurname());
		Mockito.verify(myDao).findById(1L);
	}
	
	private MyEntity createTestEntity() {
		MyEntity myEntity = new MyEntity();
		myEntity.setFirstName("My first name");
		myEntity.setSurname("My surname");
		return myEntity;
	}

}
