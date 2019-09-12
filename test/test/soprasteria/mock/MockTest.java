package test.soprasteria.mock;

import static org.mockito.Matchers.any;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MockTest {
	@Mock
	Map<String, String> wordMap;

	@InjectMocks
	MyDictionary dic;

	@Test
	public void test() throws Exception {
		List<String> spyList = Mockito.spy(new ArrayList<String>());

		spyList.add("one");
		spyList.add("two");

		Mockito.verify(spyList).add("two");
		Mockito.verify(spyList).add("one");

		System.out.println("(1)"+spyList.size());

		Mockito.doReturn(100).when(spyList).size();
		System.out.println(spyList.size());
		
		

		Mockito.when(wordMap.get(any(String.class))).thenReturn("aMeaning");
//		Assert.assertEquals(dic.getMeaning("aWord"), "aMeaning");
		System.out.println(dic.getMeaning("aWord"));
//		Mockito.verify(wordMap).get("aWord");
		
		Mockito.doReturn("aMeaning2").when(wordMap).get(any(String.class));
		System.out.println(dic.getMeaning("aWord"));
	}
}

class MyDictionary {
	private Map<String, String> wordMap;

	public MyDictionary() {
//		wordMap = new HashMap<String, String>();
//		this.wordMap = wordMap;
		System.out.println(this.wordMap);
	}
	public void add(String word, String meaning) {
		wordMap.put(word, meaning);
	}
	public String getMeaning(String word) {
		System.out.println("(3)"+wordMap.size());
		return wordMap.get(word);
	}
}