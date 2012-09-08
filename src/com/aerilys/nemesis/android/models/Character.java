package com.aerilys.nemesis.android.models;

public class Character
{
	public String Name;

	public enum RaceE
	{
		None, Humain, Elfe, Nain
	};
	
	public RaceE Race = RaceE.None;
	
	public enum ClasseE
	{
		None, Guerrier, Sorcier
	}
	
	public ClasseE Classe = ClasseE.None;
	
	public int Force = 10;
	public int Intelligence = 10;
	public int Endurance = 10;
	public int Vitalite = 30;
	public int currentPV = Vitalite;;
	
	public int Level = 1;
	public int XP = 0;
	

}
