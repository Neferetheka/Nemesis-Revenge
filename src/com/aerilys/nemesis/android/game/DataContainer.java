package com.aerilys.nemesis.android.game;

import java.util.ArrayList;
import java.util.List;

import com.aerilys.nemesis.android.models.Character;

public class DataContainer
{
	private static DataContainer instance;

	public static DataContainer getInstance()
	{
		if (instance == null)
			instance = new DataContainer();
		return instance;
	}
	
	public Character currentCharacter = new Character();
	
	public List<Monster> ListMonsters = new ArrayList<Monster>();
	
	public void initDatas()
	{
		ListMonsters.add(new Monster("Gobelin nain"));
		ListMonsters.add(new Monster("Rat d'étang"));
		ListMonsters.add(new Monster("Chiffonnier mutant"));
		ListMonsters.add(new Monster("Gobelin", 10, 2, 10, 10, 100));
		ListMonsters.add(new Monster("Petitlapinmagik", 12, 8, 12, 25, 500));
		ListMonsters.add(new Monster("Voleur hobbit", 7, 4, 7, 15, 75));
		ListMonsters.add(new Monster("Windows", 10, 15, 10, 42, 700));
		ListMonsters.add(new Monster("Magicien aveugle", 2, 20, 5, 20, 300));
		ListMonsters.add(new Monster("Orc baveux", 25, 1, 18, 20, 400));
		ListMonsters.add(new Monster("Démon de flammes", 18, 18, 18, 15, 400));
	}
}
