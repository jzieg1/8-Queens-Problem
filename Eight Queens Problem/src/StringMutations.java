import java.util.Arrays;
import java.util.Random;


public class StringMutations {
	
	//generates random numbers
	//n = number of random numbers generated
	//lowBound = lower limit of random range, inclusive
	//highBound = higher limit of random range, inclusive
	public static void generator(int n, int lowBound, int highBound){
		Random random = new Random();//creates random number generator
		int i;
		//calculates range of numbers generated
		//+1 allows highBound parameter to be inclusive for ease of use
		int range = highBound - lowBound + 1;
		//array used to store counts of numbers generated
		//slots in array equals the range of numbers generated
		int[] array = new int [range];
		//tracks quantity of integers entered into array for verification
		int total = 0;
		//calls nextInt n times
		for(i=0; i<n; i++){
			//generates number according to input bounds
			int number = random.nextInt(range)+lowBound;
			//increments slot in array equal to point in range occupied by generated number
			//example for 3-12 range: 5 - 3 = 2 means array[2] is incremented by 1
			array[number - lowBound]++;
		}
		
		//calculates total numbers in array for verification
		for(i=0; i<range; i++){
			total += array[i];
		}

		//prints counts of each number generated
		System.out.println("Counts of random numbers generated:\n");
		for(i=0; i<range; i++){
			System.out.println(i+lowBound + ": " + array[i]);
		}
		System.out.println("\nTotal numbers generated: " + total);
	}
	
	//Uses cycles crossover method to preserve absolute position of queens
	public static void crossover(String parent1, String parent2, StringBuffer child1, StringBuffer child2){
		
		int[] cycleTracker = new int [parent1.length()];//tracks which array positions belong to which cycle
		int i = 0;//tracks how many elements have been put in a cycle
		int j = 0;//tracks current cycle number
		int counter = 0;//tracks current array element being examined
		int start = 0;//tracks where the counter started in the current loop
		
		for(i=0; i<cycleTracker.length; i++){
			cycleTracker[i] = -1;
		}
		
		i = 0;//resets i for next loop
		
		while(i<parent1.length()){//loop performs all cycles
			while(cycleTracker[counter] != -1){
				counter++;
			}
			start = counter;
			do{
				//System.out.println("Counter = " + counter + " j = " + j); //test print statement
				cycleTracker [counter] = j;
				counter = parent1.indexOf(parent2.charAt(counter));
				//System.out.println(Arrays.toString(cycleTracker)); //test print statement
				i++;
				/* For testing bugs in crossover method
				 * Thread delay is needed to keep rapid print statements
				 * from crashing Eclipse.
				 * Mauch, I know this doesn't happen on your machine but
				 * mine is just different this way.
				 */
				/*
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				*/
			} while (counter != start);
			j++;//
			counter = j;
		}
		
		for(i=0; i<parent1.length(); i++){
			//program appends characters from parent to same-numbered child if cycleTracker element is even
			//different-numbered child if odd
			//checks for evenness by checking if the first bit of the integer equals 0
			//idea taken from here: https://stackoverflow.com/questions/7342237/check-whether-number-is-even-or-odd
			if((cycleTracker[i] & 1) == 0){
				child1.append(parent1.charAt(i));
				child2.append(parent2.charAt(i));
			}
			else{
				child1.append(parent2.charAt(i));
				child2.append(parent1.charAt(i));
			}
		}
		
		//System.out.println(child1); //test print statements
		//System.out.println(child2);
		
	}
	
	//mutates string at mutationPoint with  probability pm
	//comment more
	public static void mutation(StringBuffer s, double pm){
		StringBuffer swapStorage = new StringBuffer(s);//copies stringBuffer to preserve original string
		double roll = Math.random();//random number determines if mutation happens or not
		if(roll<=pm){
			int swap1 = (int) (Math.random()*s.length());//generates two random numbers from 0 to n-1
			int swap2 = (int) (Math.random()*s.length());
			while(swap1==swap2){//if same number is generated, changes second number until the two are not equal
				swap2 = (int) (Math.random()*s.length());
			}
			//System.out.println("Swap1 = " + swap1);
			//System.out.println("Swap2 = " + swap2);
			s.replace(swap1, swap1 + 1, swapStorage.substring(swap2, swap2 + 1));//draws substrings from swapStorage
			s.replace(swap2, swap2 + 1, swapStorage.substring(swap1, swap1 + 1));//doesn't overwrite original positions
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		/*//tests generator method
		generator(1000, 3, 12);
		
		System.out.println();
		
		//tests crossover method
		String parent1 = "1011101011";
		String parent2 = "0000110100";
		StringBuffer child1 = new StringBuffer();
		StringBuffer child2 = new StringBuffer();
		crossover(parent1, parent2, 6, child1, child2);
		System.out.println("Original strings:");
		System.out.println(parent1 + "\n" + parent2);
		System.out.println("Crossed strings:");
		System.out.println(child1 + "\n" + child2);
		
		System.out.println();
		*/
		//tests mutation method
		System.out.println("Mutant strings:");
		StringBuffer mutant = new StringBuffer("12345");
		System.out.println("Original string: "+mutant);
		mutation(mutant, 1);
		System.out.println("Mutated string: "+mutant);
		System.out.println();
		
		String parent1 = "327654981";
		String parent2 = "235861479";
		StringBuffer child1 = new StringBuffer();
		StringBuffer child2 = new StringBuffer();

		crossover(parent1, parent2, child1, child2);
		System.out.println(child1); //test print statements
		System.out.println(child2);
	}


}
