//bridge game in java. Updated to make AI smarter and make better decisions to throw a card with the minimal vaue
import java.util.*;

class bridge {

	//method to shuffle the deck
	public static void shuffle(ArrayList<String> list){
		int len = list.size();
		String temp = "";
		for(int i = 0; i < len; i++){
			int randIndex = (int) (len*Math.random());
			temp = list.get(i);
			list.set(i,list.get(randIndex));
			list.set(randIndex,temp); 
		}
	}
	//method to check if card of same suit exists
	public static boolean hasSame(ArrayList<String> cards, String card){
		boolean bool = false;
		
		for(int i = 0; i < cards.size(); i++){
			if(cards.get(i).charAt(0) == card.charAt(0)){
				bool = true;
				break;
			}
				}
		return bool;	
	}

	//method to add cards
	public static void addCards(ArrayList<String> list,ArrayList<String> source, int start, int end){
		for(int i = start; i < end; i++)
			list.add(source.get(i));
	}

	//method to pick the best card based on the previous dealings. AI smartness 
	public static String bestCard(String card, ArrayList<String> cards){
		ArrayList<String> ranks = new ArrayList<String>(Arrays.asList("2","3","4","5","6","7","8","9","10","J","Q","K","A"));
		String max = card;
		String minSame = card; //smallest card of same suite
		String min = card; //smallest card
		int small = 12; //smallest index
		int smallSame = 12; //smallest index in current suit
		String toShow = ""; //the card that the player will throw
		for(int i = 0; i < cards.size(); i++){

			//extract substring index of card from ranks and compare with the current card
			int high = ranks.indexOf(max.substring(1, max.length()));  //current highest rank
			int index = ranks.indexOf(cards.get(i).substring(1, cards.get(i).length())); //rank of card being iterated

			//iterates to find the highest value card with the same suit
			if(cards.get(i).charAt(0) == card.charAt(0) && index > high)
				max = cards.get(i);

			//iterates to find the minimum value card
			if(cards.get(i).charAt(0) != card.charAt(0) && index < small){
				min = cards.get(i);
				small = ranks.indexOf(cards.get(i).substring(1,cards.get(i).length()));
			}

			if(cards.get(i).charAt(0) == card.charAt(0) && index < smallSame){
				minSame = cards.get(i);
				smallSame = ranks.indexOf(cards.get(i).substring(1,cards.get(i).length()));
			}	
		}
		if(!max.equals(card))
			toShow = max;

		//return the minimum if higher not present
		else if(ranks.indexOf(minSame.substring(1,minSame.length())) < ranks.indexOf((card.substring(1, card.length()))))
			toShow = minSame;

		else
			toShow = min;

		cards.remove(toShow);
		return toShow;
	}

	//method to compare two cards and decide the better card. Important for deciding the winner of the round
	public static boolean isBetter(String card, String max){
		ArrayList<String> ranks = new ArrayList<String>(Arrays.asList("2","3","4","5","6","7","8","9","10","J","Q","K","A"));
		if (card.charAt(0) == max.charAt(0) && ranks.indexOf(card.substring(1,card.length())) > ranks.indexOf(max.substring(1,max.length())))
			return true;
		else
			return false;
	}

	//method for deciding the best card to send in first turn
	public static String sendCard(ArrayList<String> cards){
		ArrayList<String> ranks = new ArrayList<String>(Arrays.asList("2","3","4","5","6","7","8","9","10","J","Q","K","A"));
		
		int maxIndex = 0;
		String bestCard = "";

		for(int i = 0; i < cards.size(); i++){
			int index =   ranks.indexOf((cards.get(i).substring(1, cards.get(i).length())));

			if( index >= maxIndex){
				maxIndex = index;
				bestCard = cards.get(i);
			}
		}
		cards.remove(bestCard); //remove the best card and return it
		return bestCard;
	}

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		//Create a list for the suits 
		ArrayList<String> suits = new ArrayList<String>(Arrays.asList("D", "S", "C", "H"));
		//Create a list for the ranks
		ArrayList<String> ranks = new ArrayList<String>(Arrays.asList("2","3","4","5","6","7","8","9","10","J","Q","K","A"));

		//creates the deck list and shuffles it
		ArrayList<String> deck = new ArrayList<String>();
		for(int j = 0; j < 4; j++){
			for(int i = 0; i < 13; i++)
				deck.add(suits.get(j) + ranks.get(i)); //forms the cards
		}
		shuffle(deck); //shuffle

		//Create decks for each individual player
		ArrayList<String> P1Cards = new ArrayList<String>(); ArrayList<String> P2Cards = new ArrayList<String>();
		ArrayList<String> P3Cards = new ArrayList<String>(); ArrayList<String> P4Cards = new ArrayList<String>();

		//Distribute the shuffled deck to each player
		addCards(P1Cards, deck, 0,13); addCards(P2Cards, deck, 13,26);
		addCards(P3Cards, deck, 26,39); addCards(P4Cards, deck, 39,52);
		//Create copy decks for scoring
		ArrayList<String> P1Copy = new ArrayList<String>(); ArrayList<String> P2Copy = new ArrayList<String>();
		ArrayList<String> P3Copy = new ArrayList<String>(); ArrayList<String> P4Copy = new ArrayList<String>();
		addCards(P1Copy, deck, 0,13); addCards(P2Copy, deck, 13,26);
		addCards(P3Copy, deck, 26,39); addCards(P4Copy, deck, 39,52);

		int[] scores = {0,0,0,0};
		String roundWinner = ""; //needed for deciding whose turn is next
		String P1,P2,P3,P4 = "";
		System.out.println(P1Cards);
		int index;
		
		do{
		System.out.print("Which card would you like to throw?");
		 index = s.nextInt() - 1;
		}while(index >= P1Cards.size());
		
		String max = P1Cards.get(index);
		System.out.println(max);
		P1Cards.remove(max);

		P2 = (bestCard(max,P2Cards)); //The best card P2 can throw
		if(isBetter(P2,max)){
			max = P2;
		}
		P3 = (bestCard(max,P3Cards)); //The best card P3 can throw
		if(isBetter(P3,max)){
			max = P3;
		}
		P4 = (bestCard(max,P4Cards)); //The best card P4 can throw
		if(isBetter(P4,max)){
			max = P4;
		}

		System.out.println(P2);
		System.out.println(P3);
		System.out.println(P4);

		System.out.println();
		System.out.print(max + " wins the round. "); 

		if(P1Copy.indexOf(max)!=-1){
			scores[0] +=1;
			roundWinner = "player1";
		}
		else if(P2Copy.indexOf(max)!=-1){
			scores[1] +=1;
			roundWinner = "player2";
		}
		else if(P3Copy.indexOf(max)!=-1){
			scores[2] +=1;
			roundWinner = "player3";
		}
		else if(P4Copy.indexOf(max)!=-1){
			scores[3] +=1;
			roundWinner = "player4";
		}
		System.out.println(roundWinner + " won the round");

		for(int b = 0; b <= 11; b++){
			if(roundWinner.equals("player1")){ //When P1 wins the round
				System.out.println(P1Cards);
				do{
					System.out.print("Which card would you like to throw?");
					 index = s.nextInt() - 1;
					}while(index >= P1Cards.size());
				max = P1Cards.get(index);
				System.out.println(max);
				P1Cards.remove(max);

				P2 = (bestCard(max,P2Cards)); //The best card P2 can throw
				System.out.println(P2);

				if(isBetter(P2,max)){
					max = P2;
				}
				P3 = (bestCard(max,P3Cards)); //The best card P3 can throw
				System.out.println(P3);

				if(isBetter(P3,max)){
					max = P3;
				}
				P4 = (bestCard(max,P4Cards)); //The best card P4 can throw
				System.out.println(P4);

				if(isBetter(P4,max)){
					max = P4;
				}
				System.out.println();
				System.out.println(max + " wins the round."); 

				if(P1Copy.indexOf(max)!=-1){
					scores[0] +=1;
					roundWinner = "player1";
				}
				else if(P2Copy.indexOf(max)!=-1){
					scores[1] +=1;
					roundWinner = "player2";
				}
				else if(P3Copy.indexOf(max)!=-1){
					scores[2] +=1;
					roundWinner = "player3";
				}
				else if(P4Copy.indexOf(max)!=-1){
					scores[3] +=1;
					roundWinner = "player4";
				}
				System.out.println(roundWinner + " won the round");
			}

			else if(roundWinner.equals("player2")){ //When P2 wins the round

				max = sendCard(P2Cards); //The best card P2 can throw
				System.out.println(max);
				P3 = (bestCard(max,P3Cards)); //The best card P3 can throw
				System.out.println(P3);

				if(isBetter(P3,max)){
					max = P3;
				}
				P4 = (bestCard(max,P4Cards)); //The best card P4 can throw
				System.out.println(P4);

				if(isBetter(P4,max)){
					max = P4;
				}
				//Ask P1 to throw cards
				System.out.println(P1Cards);
				do{
					do{
				System.out.print("Which card would you like to throw?");
				index = s.nextInt() - 1;
					}while(index >= P1Cards.size());
				P1 = P1Cards.get(index);
				}while((hasSame(P1Cards,max) & P1.charAt(0) != max.charAt(0)));
				
				System.out.println(P1);
				P1Cards.remove(P1);

				if(isBetter(P1,max)){
					max = P1;
				}
				System.out.println();
				System.out.println(max + " wins the round."); 

				if(P1Copy.indexOf(max)!=-1){
					scores[0] +=1;
					roundWinner = "player1";
				}
				else if(P2Copy.indexOf(max)!=-1){
					scores[1] +=1;
					roundWinner = "player2";
				}
				else if(P3Copy.indexOf(max)!=-1){
					scores[2] +=1;
					roundWinner = "player3";
				}
				else if(P4Copy.indexOf(max)!=-1){
					scores[3] +=1;
					roundWinner = "player4";
				}
				System.out.println(roundWinner + " won the round");
			}
			

			else if(roundWinner.equals("player3")){ //If P3 is the round winner

				max = sendCard(P3Cards); //The best card P3 can throw
				System.out.println(max);
				P4 = (bestCard(max,P4Cards)); //The best card P4 can throw
				System.out.println(P4);
				if(isBetter(P4,max)){
					max = P4;
				}

				//Ask P1 to throw cards
				System.out.println(P1Cards);
				do{
					do{
						System.out.print("Which card would you like to throw?");
						index = s.nextInt() - 1;
							}while(index >= P1Cards.size());
				P1 = P1Cards.get(index);
				}while((hasSame(P1Cards,max) & P1.charAt(0) != max.charAt(0)) );
				
				System.out.println(P1);
				P1Cards.remove(P1);
				if(isBetter(P1,max)){
					max = P1;
				}

				P2 = (bestCard(max,P2Cards)); //The best card P2 can throw
				System.out.println(P2);
				if(isBetter(P2,max)){
					max = P2;
				}

				System.out.println();
				System.out.println(max + " wins the round."); 

				if(P1Copy.indexOf(max)!=-1){
					scores[0] +=1;
					roundWinner = "player1";
				}
				else if(P2Copy.indexOf(max)!=-1){
					scores[1] +=1;
					roundWinner = "player2";
				}
				else if(P3Copy.indexOf(max)!=-1){
					scores[2] +=1;
					roundWinner = "player3";
				}
				else if(P4Copy.indexOf(max)!=-1){
					scores[3] +=1;
					roundWinner = "player4";
				}
				System.out.println(roundWinner + " won the round");
			}
			else if(roundWinner.equals("player4")){ //if P4 winds the round

				max = sendCard(P4Cards); //The best card P4 can throw
				System.out.println(max);
				
				//Ask P1 to throw cards
				System.out.println(P1Cards);
				do{
					do{
						System.out.print("Which card would you like to throw?");
						index = s.nextInt() - 1;
							}while(index >= P1Cards.size());
				P1 = P1Cards.get(index);
				}while((hasSame(P1Cards,max) & P1.charAt(0) != max.charAt(0)) );
				System.out.println(P1);
				P1Cards.remove(P1);
				if(isBetter(P1,max)){
					max = P1;
				}
				
				
				P2 = (bestCard(max,P2Cards)); //The best card P2 can throw
				System.out.println(P2);
				if(isBetter(P2,max)){
					max = P2;
				}
				P3 = (bestCard(max,P3Cards)); //The best card P3 can throw
				System.out.println(P3);

				if(isBetter(P3,max)){
					max = P3;
				}

				System.out.println();
				System.out.println(max + " wins the round."); 

				if(P1Copy.indexOf(max)!=-1){
					scores[0] +=1;
					roundWinner = "player1";
				}
				else if(P2Copy.indexOf(max)!=-1){
					scores[1] +=1;
					roundWinner = "player2";
				}
				else if(P3Copy.indexOf(max)!=-1){
					scores[2] +=1;
					roundWinner = "player3";
				}
				else if(P4Copy.indexOf(max)!=-1){
					scores[3] +=1;
					roundWinner = "player4";
				}
				System.out.println(roundWinner + " won the round");
			}
		}


		System.out.println(Arrays.toString(scores));
			
		if(scores[0] + scores[2] > scores[1] + scores[3])
			System.out.println("Player 1 and Player 3 win");
		else
			System.out.println("Player 2 and Player 4 win");
	}

}
