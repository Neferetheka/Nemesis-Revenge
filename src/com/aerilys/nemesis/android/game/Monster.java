package com.aerilys.nemesis.android.game;

public class Monster
{
	public String Name;
	
	public int Force = 1;
	public int Intelligence = 1;
	public int Endurance = 1;
	public int Vitalite = 1;
	public int XP = 50;
	
	
	public Monster(String name)
	{
		super();
		Name = name;
	}

	public Monster(String name, int force, int intelligence, int endurance, int vitalite, int xp)
	{
		super();
		Name = name;
		Force = force;
		Intelligence = intelligence;
		Endurance = endurance;
		Vitalite = vitalite;
		this.XP = xp;
	}
	
	
}
