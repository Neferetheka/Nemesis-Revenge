package com.aerilys.nemesis.android;

import com.aerilys.nemesis.android.game.DataContainer;
import com.aerilys.nemesis.android.game.Monster;

import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aerilys.nemesis.android.models.Character;
import com.aerilys.nemesis.android.models.Character.ClasseE;
import com.aerilys.nemesis.android.models.Character.RaceE;
import com.aerilys.nemesis.android.tools.Converter;
import com.aerilys.nemesis.android.tools.RandomExtension;

public class MainActivity extends Activity
{
	private TextView console;
	private EditText editText;
	private LinearLayout linearInput;
	private boolean isCreated = false;
	private RandomExtension rnd = new RandomExtension();
	private int[] levelArray = new int[100];

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		console = (TextView) findViewById(R.id.console);
		editText = (EditText) findViewById(R.id.editText);
		linearInput = (LinearLayout) findViewById(R.id.linearInput);

		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		DataContainer.getInstance().initDatas();
		initLevelArray();

		initGame();
	}

	private void initLevelArray()
	{
		int i = 1;
		levelArray[0] = 1000;
		while (i <= levelArray.length-1)
		{
			levelArray[i] = (int) Math.floor(levelArray[i - 1] * 1.5);
			i++;
		}
	}

	private void initGame()
	{

		linearInput.setVisibility(View.GONE);
		println("Bienvenue aventurier ! Avant de p�n�trer dans le donjon du terrible Nemesis et de combattre sa col�re vengeresse, dis-moi en plus sur toi !");
		println("Quel est ton nom ?");
		prompt();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void validClick(View v)
	{
		if (!isCreated)
		{
			createCharacter();
		}
		else
			nextStepDungeon();
	}

	private void generateEvent()
	{
		int random = rnd.nextInt(0, 100);
		println("");
		if (random < 30)
		{
			println("It's a trap !");
			promptToContinue();
		}
		else if (random < 90)
		{
			int indexMonster = rnd.nextInt(0, DataContainer.getInstance().ListMonsters.size() - 1);
			Monster monster = DataContainer.getInstance().ListMonsters.get(indexMonster);
			fight(monster);
		}
		else
			nothingHappened();
	}

	private void fight(Monster monster)
	{
		println("Oh non ! Un " + monster.Name + " sauvage apparait !");

		int potentielAttaquant = 0;
		int potentielDefenseur = 0;

		if (DataContainer.getInstance().currentCharacter.Classe == ClasseE.Sorcier)
		{
			potentielAttaquant = DataContainer.getInstance().currentCharacter.Intelligence;
			potentielDefenseur = monster.Intelligence;
		}
		else
		{
			potentielAttaquant = DataContainer.getInstance().currentCharacter.Force;
			potentielDefenseur = monster.Force;
		}

		int pvAttaquant = DataContainer.getInstance().currentCharacter.currentPV;
		int pvDefenseur = monster.Vitalite;

		while (pvAttaquant > 0 && pvDefenseur > 0)
		{
			int randomDmg = rnd.nextInt(0, 7);
			int dmg = potentielAttaquant + randomDmg - monster.Endurance;
			if (dmg < 0)
				dmg = 1;
			pvDefenseur -= dmg;
			if (pvDefenseur < 0)
				pvDefenseur = 0;

			println("Vous infligez " + dmg + " d�g�ts � " + monster.Name + " ! Il lui reste " + pvDefenseur + "pv !");

			randomDmg = rnd.nextInt(0, 7);
			dmg = potentielDefenseur + randomDmg - DataContainer.getInstance().currentCharacter.Endurance;
			if (dmg < 0)
				dmg = 1;
			pvAttaquant -= dmg;
			if (pvAttaquant < 0)
				pvAttaquant = 0;

			println(monster.Name + " vous attaque et vous inflige " + dmg + " d�g�ts ! Il vous reste " + pvAttaquant
					+ "pv !");
		}

		if (pvAttaquant > 0 && pvDefenseur == 0)
		{
			println("\nVous avez vaincu le terrible " + monster.Name + " !");
			DataContainer.getInstance().currentCharacter.currentPV = pvAttaquant;

			DataContainer.getInstance().currentCharacter.XP += monster.XP;
			println("Vous gagnez " + monster.XP + " points d'experience !\nXP totale : "
					+ DataContainer.getInstance().currentCharacter.XP + "\n");

			int currentLevel = getPlayerLevel(DataContainer.getInstance().currentCharacter.XP);
			if (currentLevel > DataContainer.getInstance().currentCharacter.Level)
				levelUp();

			promptToContinue();
		}
		else if (pvAttaquant == 0 && pvDefenseur > 0)
		{
			println("Vous avez �t� vaincu par le terrible " + monster.Name + "...");
			characterDead();
		}
		else
		{
			println("Dans une lutte sanglante et �pique, vous et le " + monster.Name + " vous entretu�s.");
			characterDead();
		}

	}

	private void characterDead()
	{
		println("Malgr� une r�sistance exceptionnelle et de tr�s belles �pop�es, vos aventures se terminent ici.\nCi-git "
				+ DataContainer.getInstance().currentCharacter.Name
				+ ", "
				+ DataContainer.getInstance().currentCharacter.Classe
				+ " "
				+ DataContainer.getInstance().currentCharacter.Race
				+ " de niveau "
				+ DataContainer.getInstance().currentCharacter.Level);

		isCreated = false;
		DataContainer.getInstance().currentCharacter = new Character();
		initGame();
	}

	private void nextStepDungeon()
	{
		prompt();
		// scrollContainer.scrollTo(0, )
		generateEvent();
	}

	private void nothingHappened()
	{
		int index = rnd.nextInt(0, 2);
		switch (index)
		{
			case 0:
				println("Mon beau paladin, illumine mon chemin, ni troll ni gredin, ni aucun magicien, mais au moins des gobelins, histoire qu'enfin on se fasse plein d'XP, � coup de gourdin !");
				break;
			case 1:
				println("Vous entendez un bruit ! une b�te approche. Attention c'est... Ah bah non c'est un poulet...");
				break;
			case 2:
				println("Bon sang, vous entendez ces tambours ? Ca me rappelle cette histoire avec ce hobbitt. Ouais, m�me que apr�s le magicien il tombe dans le puit...");
				break;
		}
		DataContainer.getInstance().currentCharacter.currentPV = DataContainer.getInstance().currentCharacter.Vitalite;
		println("Puisque rien ne se passe, vous regagnez tous vos points de vie ! Vous �tes maintenant � "
				+ DataContainer.getInstance().currentCharacter.currentPV + "pv !");
		prompt();
		promptToContinue();
	}

	private void promptToContinue()
	{
		println("Validez pour continuer...");
	}

	public void createCharacter()
	{
		if (DataContainer.getInstance().currentCharacter.Name == null && editText.getText().toString().length() > 0)
		{
			DataContainer.getInstance().currentCharacter.Name = editText.getText().toString();
			// endPrompt();
			editText.setText("");
			println("Tu t'appelles donc " + DataContainer.getInstance().currentCharacter.Name
					+ " ? Sympa le nom. Et c'est quoi ta race ?");
			println("1)Humain (+5 en vitalit�)");
			println("2)Elfe (+5 intelligence)");
			println("3)Nain (+5 en endurance)");
		}
		else if (DataContainer.getInstance().currentCharacter.Race == Character.RaceE.None)
		{
			int raceIndex = Converter.ctI(editText.getText().toString());
			if (raceIndex == 1)
			{
				println("Oh un humain. Pas mal !");
				DataContainer.getInstance().currentCharacter.Race = RaceE.Humain;
				DataContainer.getInstance().currentCharacter.Vitalite += 5;
			}
			else if (raceIndex == 2)
			{
				println("Ah un elfe. La Classe quoi !");
				DataContainer.getInstance().currentCharacter.Race = RaceE.Elfe;
				DataContainer.getInstance().currentCharacter.Intelligence += 5;
			}
			else if (raceIndex == 3)
			{
				println("Beurk un machin miniature avec une barbe !");
				DataContainer.getInstance().currentCharacter.Race = RaceE.Nain;
				DataContainer.getInstance().currentCharacter.Endurance += 5;
			}
			else
			{
				println("Tu te crois malin hein ? Tu seras humain pour la peine !");
				DataContainer.getInstance().currentCharacter.Race = RaceE.Humain;
				DataContainer.getInstance().currentCharacter.Vitalite += 5;
			}

			println("Bon ok. Et c'est quoi ta classe ?");
			println("1)Guerrier (+10 force)");
			println("2)Sorcier (+10 intelligence)");
			prompt();
		}

		else if (DataContainer.getInstance().currentCharacter.Classe == Character.ClasseE.None)
		{
			int classeIndex = Converter.ctI(editText.getText().toString());

			if (classeIndex == 1)
			{
				println("Oh un bourrin. C'est super rare �a !");
				DataContainer.getInstance().currentCharacter.Classe = ClasseE.Guerrier;
				DataContainer.getInstance().currentCharacter.Force += 10;
			}
			else if (classeIndex == 2)
			{
				println("G�nial, j'adore les sorciers !");
				DataContainer.getInstance().currentCharacter.Classe = ClasseE.Sorcier;
				DataContainer.getInstance().currentCharacter.Intelligence += 10;
			}
			else
			{
				println("Tu te crois malin ? Tu seras un guerrier. Le destin a choisi pour toi !");
				DataContainer.getInstance().currentCharacter.Classe = ClasseE.Guerrier;
				DataContainer.getInstance().currentCharacter.Force += 10;
			}

			println("\nBon bah tu es pr�t pour l'aventure.\nTu es donc "
					+ DataContainer.getInstance().currentCharacter.Name + ", un "
					+ DataContainer.getInstance().currentCharacter.Classe + " "
					+ DataContainer.getInstance().currentCharacter.Race
					+ ", et tes caract�ristiques sont les suivantes : \nForce : "
					+ DataContainer.getInstance().currentCharacter.Force + " - Intelligence : "
					+ DataContainer.getInstance().currentCharacter.Intelligence + "\nEndurance : "
					+ DataContainer.getInstance().currentCharacter.Endurance + " - Vitalit� : "
					+ DataContainer.getInstance().currentCharacter.Vitalite + "\n\nPr�t ? Alors c'est parti !\n");

			editText.setText("");
			DataContainer.getInstance().currentCharacter.currentPV = DataContainer.getInstance().currentCharacter.Vitalite;
			promptToContinue();
		}
		else
		{
			initDungeon();
		}
	}

	private void initDungeon()
	{
		isCreated = true;
		println("Bienvenue dans le terrible donjon de Nemesis. Plus redoutable et sombre qu'autrefois, il cache de redoutables pi�ges !");
		println("Alors que vous entrez...");
		nextStepDungeon();
	}

	private int getPlayerLevel(int playerXp)
	{
		int i = 0;
		int level = 1;
		while (i < levelArray.length-1)
		{
			if (playerXp > levelArray[i])
			{
				level = i;
			}
			else
				return level;

			i++;
		}
		return level;
	}

	public void levelUp()
	{
		DataContainer.getInstance().currentCharacter.Level++;
		DataContainer.getInstance().currentCharacter.Force++;
		DataContainer.getInstance().currentCharacter.Intelligence++;
		DataContainer.getInstance().currentCharacter.Endurance++;

		int bonusVitalite = rnd.nextInt(2, 7);
		DataContainer.getInstance().currentCharacter.Vitalite += bonusVitalite;

		println("F�licitations, vous avez gagn� un niveau ! \nEn plus d'un point dans toutes les caract�ristiques, vous obtenez "
				+ bonusVitalite
				+ "pv en plus !\nVous �tes � pr�sent niveau "
				+ DataContainer.getInstance().currentCharacter.Level + " !");
		
	}

	public void println(String message)
	{
		String text = console.getText().toString();
		console.setText(text + "\n" + message);
	}

	public void prompt()
	{
		editText.setText("");
		linearInput.setVisibility(View.VISIBLE);
	}

	/* private void endPrompt() { linearInput.setVisibility(View.GONE);
	 * editText.setText(""); } */
}
