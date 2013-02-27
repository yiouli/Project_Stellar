package org.yiouli.challenge.topcoder.srm.m558;

/**
 * @division 2
 * @points 900
 * 
 * @description
 * Cat and Rabbit are going to play the following game.  There are some tiles in a row. Each of the tiles is colored white or black. You are given a String tiles, describing the initial arrangement of tiles. The characters '.' and '#' represent a white tile and a black tile, respectively.  Cat and Rabbit take alternating turns. Cat plays first. 
 * <p>
 * In each turn, the following actions must be performed:<br>
 *  - First, the player must select a black tile and step on it.<br>
 *  - Then, the player must make some steps (as many as they want, but at least one). In each step, the player must select an adjacent white tile, move to that tile, and change its color to black.<br>
 * <p>
 * The animal who is unable to take a valid turn loses the game. Return the String "Cat" if Cat will win, "Rabbit" if Rabbit will win, assuming both animals play optimally.
 *
 * @constraints
 * - tiles will contain between 1 and 50 characters, inclusive.<br>
 * - Each character in tiles will be '.' or '#'.<br>
 *
 */
public class CatAndRabbit {

	/**
	 * cat play first, assuming both cat and rabbit play optimally, 
	 * return "Cat" or "Rabbit" as winner.
	 * @param tiles is the tiles to be played, '#' as black tile and '.' as white tile.
	 * @return "Cat" or "Rabbit".
	 */
	public String getWinner(String tiles) {
		if(!tiles.contains("#"))
			return "Rabbit";
		int ans = 0;
		for(int i=0;i<tiles.length();i++) {
			int len = 0;
			while(i<tiles.length() && tiles.charAt(i) == '.') {
				i++;
				len++;
			}
			ans ^=len;
		}
		if(ans == 0)
			return "Rabbit";
		else
			return "Cat";
	}
}
