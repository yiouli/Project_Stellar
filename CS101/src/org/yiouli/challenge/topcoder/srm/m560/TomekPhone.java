package org.yiouli.challenge.topcoder.srm.m560;

import java.util.Arrays;

/**
 * @division 1/2
 * @points 250/500
 * 
 * @description
 * Tomek thinks that smartphones are overrated. He plans to release a new cellphone with an old-school keyboard, which may require you to tap a key multiple times to type a single letter. For example, if the keyboard has two keys, one with the letters "adef" and the other one with the letters "zyx", then typing 'a' requires one keystroke, typing 'f' requires four keystrokes, typing 'y' requires two keystrokes, and so on.
 * <p>
 * Tomek has already designed the keyboard's layout. That is, he already knows the number of keys on the keyboard, and for each key he knows the maximum number of letters it may hold. He now wants to create a specific keyboard for a language that uses N different letters. He has a large body of text in this language, and he already analyzed it to find the frequencies of all N letters of its alphabet.
 * <p>
 * You are given a int[] frequencies with N elements. Each element of frequencies is the number of times one of the letters in Tomek's alphabet appears in the text he has. Each element of frequencies will be strictly positive. (I.e., each of the N letters occurs at least once in Tomek's text.)
 * <p> 
 * You are also given a int[] keySize. The number of elements of keySize is the number of keys on Tomek's keyboard. Each element of keySize gives the maximal number of letters on one of the keys.
 * <p> 
 * Find an assignment of letters to keys that minimizes the number of keystrokes needed to type Tomek's entire text. Return that minimum number of keystrokes. If there is not enough room on the keys and some letters of the alphabet won't fit, return -1 instead.
 *
 * @constraints
 * - frequencies will contain between 1 and 50 elements, inclusive.<br>
 * - Each element of frequencies will be between 1 and 1,000, inclusive.<br>
 * - keySizes will contain between 1 and 50 elements, inclusive.<br>
 * - Each element of keySizes will be between 1 and 50, inclusive.<br>
 *
 */
public class TomekPhone {

	public int minKeystrokes(int[] occurences, int[] keySizes) {
		int bucket = 0;
		for(int i=0;i<keySizes.length;i++)
			bucket += keySizes[i];
		if(bucket < occurences.length)
			return -1;
		Arrays.sort(occurences);
		int ret = 0;
		int round = 1;
		int key = 0;
		for(int i=occurences.length-1;i>=0;i--) {
			while(key < keySizes.length && keySizes[key] < round)
				key++;
			if(key == keySizes.length) {
				round++;
				key = 0;
				i++;
				continue;
			}
			ret += round*occurences[i];
			key++;
		}
		return ret;
	}
}
