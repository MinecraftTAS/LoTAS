package de.pfannekuchen.lotas.challenges;

import java.io.Serializable;
import java.net.URL;
import java.util.UUID;

public class ChallengeMap implements Serializable {
	
	private static final long serialVersionUID = -2179267199923671549L;

	public String username;
	public UUID uuid;
	public URL map;
	public String displayName;
	public String name;
	public String description;
	public String[] leaderboard;
	
}
