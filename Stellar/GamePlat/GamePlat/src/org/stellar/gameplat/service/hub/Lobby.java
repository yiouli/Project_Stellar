package org.stellar.gameplat.service.hub;

import org.stellar.gameplat.service.contract.IGameService;

class Lobby<PlayerType> {

	public static final int DEFAULT_CAPACITY = 8;
	public final IGameService game;
	public final int capacity;
	
	private int host;
	private int seatCount;
	private int playerCount;
	private boolean[] seat;	//false for closed
	private PlayerType[] player;
	
	String gameParams;

	boolean hasPlayer(int index) {
		boolean ret = index > -1 && index < seatCount && player[index] != null;
		if(ret)
			assert seatAvailable(index);
		return ret;
	}
	
	boolean seatAvailable(int index) {
		return index > -1 && index < seat.length && seat[index];
	}
	
	private boolean check() {
		boolean ret = game != null && seat != null && player != null;
		ret &= seat.length == capacity && player.length == capacity;
		ret &= host > -1 && hasPlayer(host);
		ret &= seatCount <= capacity && playerCount <= seatCount;
		int sc = 0;
		for(boolean s : seat)
			if(s) sc++;
		ret &= sc == seatCount;
		int pc = 0;
		for(int i = 0; i < player.length; i++)
			if(player[i] != null) {
				assert seatAvailable(i);
				pc++;
			}
		return ret && pc == playerCount;
	}

	@SuppressWarnings("unchecked")
	private void init(PlayerType[] player, int seatCount) {
		assert game != null && player != null && player.length > 0;
		assert seatCount <= this.capacity && player.length <= seatCount;
		this.seatCount = seatCount;
		this.playerCount = player.length;
		this.seat = new boolean[this.capacity];
		for(int i = this.seatCount; i < this.capacity; i++)
			seat[i] = true;
		this.player = (PlayerType[])new Object[this.capacity];
		for(int i = 0; i < player.length; i++) {
			if(player[i] == null)
				throw new NullPointerException();
			this.player[i] = player[i];
		}
		this.host = 0;
		assert check();
	}
	
	Lobby(IGameService game, PlayerType[] player) {
		this.game = game;
		this.capacity = DEFAULT_CAPACITY;
		init(player, this.capacity);
	}

	Lobby(IGameService game, PlayerType[] player, int seatCount) {
		this.game = game;
		this.capacity = DEFAULT_CAPACITY;
		init(player, seatCount);
	}
	
	Lobby(IGameService game, PlayerType[] player, int seatCount, int capacity) {
		this.game = game;
		this.capacity = capacity;
		init(player, seatCount);
	}
	
	boolean addPlayer(PlayerType p) {
		if(p == null)
			throw new NullPointerException();
		assert check();
		for(int i = 0; i < player.length; i++)
			if(seatAvailable(i) && !hasPlayer(i)) {
				player[i] = p;
				playerCount++;
				assert check();
				return true;
			}
		return false;
	}
	
	PlayerType getPlayer(int index) {
		assert check();
		if(!hasPlayer(index))
			throw new IllegalArgumentException();
		return player[index];
	}
	
	PlayerType[] getPlayers() {
		assert check();
		@SuppressWarnings("unchecked")
		PlayerType[] ret = (PlayerType[])new Object[playerCount];
		int i = 0;
		for(PlayerType p : player) {
			if (p != null)
				ret[i++] = p;
		}
		assert i == playerCount;
		return ret;
	}
	
	void removePlayer(int index) {
		assert check();
		if(!seatAvailable(index) || !hasPlayer(index))
			return;
		player[index] = null;
		playerCount--;
		assert check();
	}
	
	void openSeat(int index) {
		if(seatAvailable(index))
			return;
		if(index < 0 && index >= seat.length)
			return;
		seat[index] = true;
		seatCount++;
		assert check();
	}
	
	void closeSeat(int index) {
		if(!seatAvailable(index))
			return;
		removePlayer(index);
		seat[index] = false;
		seatCount--;
		assert check();
	}
	
	int getSeatCount() {
		return seatCount;
	}
	
	int getPlayerCount() {
		return playerCount;
	}
	
	int getHost() {
		assert check();
		return host;
	}
	
	void changeHost(int index) {
		assert check();
		if(!hasPlayer(index))
			throw new IllegalArgumentException();
		host = index;
	}
}
