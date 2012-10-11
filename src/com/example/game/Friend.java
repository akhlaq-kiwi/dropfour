package com.example.game;

public class Friend {
	private String name;
	private long id;
	private String fb_id;
	private long game_id;
		
	public String getName() {
		return name;
	}
	public void setName(String fb_name) {
		this.name = fb_name;
	}
		
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public long getGameId() {
		return game_id;
	}
	public void setGameId(long game_id) {
		this.game_id = game_id;
	}
	
	public String getFbId() {
		return fb_id;
	}
	public void setFbId(String fb_id) {
		this.fb_id = fb_id;
	}
}
