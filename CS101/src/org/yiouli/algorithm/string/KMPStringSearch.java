package org.yiouli.algorithm.string;

/**
 * Knuth¨CMorris¨CPratt algorithm is a linear string search algorithm.
 * It pre-calculates a fault table so every time a mismatch occurs it
 * will trying to match as many prefix letters as possible.
 * 
 * {@link http://en.wikipedia.org/wiki/Knuth%E2%80%93Morris%E2%80%93Pratt_algorithm}
 * 
 * @author leoSU
 *
 */
public class KMPStringSearch {
	//word expected to be non-empty or null
	static int[] getFaultTable(String word) {
		int k = word.length();
		int[] ret = new int[k];
		ret[0] = -1;
		if(k == 1)
			return ret;
		ret[1] = 0;
		int pos = 2, cnd = 0;
		while(pos < k) {
			if(word.charAt(cnd) == word.charAt(pos - 1)) {
				cnd++;
				ret[pos] = cnd;
				pos++;
			}
			else if(cnd > 0)
				cnd = ret[cnd];
			else {
				ret[pos] = 0;
				pos++;
			}
		}
		return ret;
	}

	/**
	 * findWord finds the word in context and gives the index of 
	 * word's first letter in context.
	 * @param word the word to search for. Shouldn't be empty string or null.
	 * @param context the context where to search the word.
	 * @return index of word's first letter in context.
	 */
	public static int findWord(String word, String context) {
		int[] t = getFaultTable(word);
		int m = 0, i = 0;
		int n = context.length(); 
		while(m+i < n) {
			if(word.charAt(i) == context.charAt(m + i)) {
				if(i == word.length() - 1)
					return m;
				i++;
			}
			else {
				m = m + i - t[i];
				if(t[i] > -1)
					i = t[i];
				else
					i = 0;
			}
		}
		return context.length();
	}
}