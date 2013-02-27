package org.yiouli.algorithm.string;
import static org.junit.Assert.*;
import java.util.Random;
import org.junit.Test;
import org.yiouli.algorithm.string.KMPStringSearch;

public class KMPStringSearchTest {
	
	@Test
	public void testGetFaultTable() {
		String word = "ababcababad";
		int[] answer = new int[]{-1,0,0,1,2,0,1,2,3,4,3};
		int[] t = KMPStringSearch.getFaultTable(word);
		for(int i=0;i<t.length;i++)
			assertEquals("Wrong at position "+i, answer[i], t[i]);
	}

	String createString(int len) {
		return createString(len, 26);
	}

	String createString(int len, int range) {
		StringBuffer sb = new StringBuffer();
		Random r = new Random();
		for(int i=0;i<len;i++)
			sb.append((char)(97+r.nextInt(range+1)));
		return sb.toString();
	}

	int naiveSearch(String word, String context) {
		int n = context.length();
		int k = word.length();
		outer:
		for(int i=0; i<=n-k; i++) {
			for(int j=0; j<k; j++)
				if(context.charAt(i+j)!=word.charAt(j))
					continue outer;
			return i;
		}
		return n;
	}

	@Test
	public void testFindWordSimple() {
		String word = "ababcababad";
		String context = createString(10000, 4);
		assertEquals(KMPStringSearch.findWord(word, context), naiveSearch(word, context));
	}

	@Test
	public void testFindWord() {
		int t = 10000;
		Random r = new Random();
		int k = 50, n = 10000;
		int c = 0;
		for(int i=0; i<t; i++) {
			String word = createString(r.nextInt(k)+1);
			String context = createString(r.nextInt(n)+1);
			int res = KMPStringSearch.findWord(word, context);
			if(res != naiveSearch(word, context)) {
				c++;
				System.out.println("Wrong answer for word: "+ word +" and context: "+ context);
			}
		}

		System.out.println(c + " out of "+ t+ " are wrong");
		assertTrue(c==0);
	}
}