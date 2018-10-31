import java.util.Random;
import java.util.Scanner;


public class Population {

	/* Changes from default HW01 version:
	 * -Removed all mentions of a crossover site because of the switch to cycles crossover
	 * -Removed all mentions of a mutation point because of the switch to swap mutation
	 */
	
	private int popSize=0;//population size
	private int boardSize;
	private Individual[] pop;//array of all individuals in population
	
	//Population constructor
	//takes parameter for population size
	public Population(int populationSize, int boardSize){
		popSize = populationSize;
		pop = new Individual[popSize];
		this.boardSize = boardSize;
		int i=0;
		for (i=0; i<popSize; i++){
			pop[i] = new Individual(boardSize);
		}
	}
	
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		int i;
		for(i=0; i<popSize;i++){
			buffer.append(pop[i].getChrom() + " " + pop[i].getFitness() + " " + pop[i].getParents() + '\n');
		}
		String returnedString = buffer.toString();
		return returnedString;
	}
	
	//gets the array of all individuals in the Population
	public Individual[] getPop(){
		return pop;
	}
	
	public Individual getIndividual(int n){
		return pop[n];
	}
	
	//gets the size of the population
	public int getPopSize(){
		return popSize;
	}
	
	//method for generating new generation
		public Population newGeneration(){
			Population newGeneration = new Population(popSize, boardSize);//stores population members selected to reproduce
			double[] probability = new double[popSize];//holds probability values
			double sumFitness = 0;//stores sum of fitness for weighting probability
			int[] parentArray = new int[popSize];//stores addresses of parent individuals selected for crossover
			int i;//counters for nested
			int j;//for loops
			for(i = 0; i<popSize; i++){//sums fitness values
				sumFitness+= pop[i].getFitness();
			}
			for(i=0;i<popSize;i++){//computes probabilities based on relative fitness values
				probability[i] = pop[i].getFitness()/sumFitness;
			}
			//add all probability values until the result is >= rolled number
			//therefore rolled number was within the probability range of the added number
			//Example: rolled 0.7, 4 items in probability with values 0.3, 0.1, 0.4, 0.2
			//round 1: 0.3<0.7
			//round 2: 0.3+0.1 = 0.4<0.7
			//round 3: 0.4+0.4 = 0.8>0.7
			//item 3 is selected
			for(i=0; i<popSize; i++){//outer loop counts number of spins
				double randNum= Math.random();
				double probabilitySum = 0;
				//System.out.println("Spin " + i);
				for(j=0;j<popSize;j++){//inner loop checks what string was rolled
					probabilitySum += probability[j];
					if(probabilitySum>=randNum){
						//System.out.println("Rolled element " + j);
						newGeneration.pop[i] = new Individual(pop[j].getChrom());
						newGeneration.pop[i].evaluateFitness();
						parentArray[i] = j;
						break;
					}
				}
			}
			
			//System.out.println(newGeneration);
			//possibility: put a scan.next() into the generation loop after every generation is born
			//so that the user can choose to continue on a generation-by-generation basis
			//problem: as is, evaluateFitness is only called when a new individual is created
			//the current code modifies existing individuals but does not make new ones
			//new overridden constructor? Or just explicitly call evaluateFitness?
			for(i=0;i<popSize;i+=2){
				StringBuffer child1 = new StringBuffer();//string buffers for crossover method
				StringBuffer child2 = new StringBuffer();
				Random random = new Random();//random number generator
				StringMutations.crossover(newGeneration.pop[i].getChrom(), newGeneration.pop[i+1].getChrom(), child1, child2);//crosses over strings
				
				//mutates both child strings
				StringMutations.mutation(child1, 0.0333);
				StringMutations.mutation(child2, 0.0333);
				
				//new strings placed within new population
				newGeneration.pop[i].setChrom(child1.toString());
				newGeneration.pop[i+1].setChrom(child2.toString());
				
				newGeneration.pop[i].setParents(parentArray[i],parentArray[i+1]);
				newGeneration.pop[i+1].setParents(parentArray[i],parentArray[i+1]);
				
				newGeneration.pop[i].evaluateFitness();
				newGeneration.pop[i+1].evaluateFitness();
			}
			return newGeneration;
		}
		
	public static void main(String[] args) {
		//Main testing method
		double maxFitness = 0;//tracks highest observed fitness value across generations
		String binary = "null";//tracks optimal binary string across generations
		double avgFitness = 0;//tracks average fitness of current generation
		int populationSize;//population size
		int boardSize;//board size
		int i;//for loop counters
		int j;
		Scanner scan = new Scanner(System.in);
		
		System.out.println("Enter board size: ");
		boardSize = scan.nextInt();
		System.out.println("Enter population size: ");
		populationSize = scan.nextInt();
		
		Population generation = new Population(populationSize, boardSize);
		/*
		for(i = 0; i < generation.getPopSize(); i++){
			System.out.println(generation.getIndividual(i));
		}
		*/
		/*Tests generation method
		 *Printing output for all generations does not cause a program bug,
		 *but it does crash Eclipse on my laptop. Uncommenting the below 
		 *print statements is not recommended for >100 generation runs.
		 */
		
		int generations = 0;
		while(maxFitness != boardSize-1){
			//System.out.println("--------------------------");
			generation = generation.newGeneration();
			
			//tracks highest fitness value observed
			//updates decimalValue and binary when maxFitness is updated under the
			//assumption that the optimal fitness value is also the optimal decimal/string
			for(j=0; j<generation.getIndividual(0).getLength(); j++){
				if(maxFitness < generation.getIndividual(j).getFitness()){
					maxFitness = generation.getIndividual(j).getFitness();
					binary = generation.getIndividual(j).getChrom();
				}
			}
			generations++;
			//System.out.println(generation);
		}
		System.out.println();
		//prints output feed header
		System.out.printf("   #  parents %" + (boardSize + 2) + "s  fitness\n","string");
		for(i=0;i<70;i++){
			System.out.print("-");
		}
		System.out.println();
		
		//prints output feed
		for(i = 0; i < generation.getPopSize(); i++){
			System.out.printf("%3d)  ", i+1);
			System.out.print(generation.getIndividual(i)+"\n");
			avgFitness += generation.getIndividual(i).getFitness();
		}
		avgFitness = avgFitness/generation.getPopSize();
		for(i=0;i<70;i++){//prints 70 '-' characters
			System.out.print("-");
		}
		System.out.println();
		
		//prints highest fitness value observed
		System.out.println("Gen " + generations + " & Accumulated Statistics: " + "maxFitness=" + maxFitness + " avgFitness=" + avgFitness + " optimal binary string = " + binary);
		for(i=0;i<70;i++){
			System.out.print("-");
		}
		//System.out.println(generation);
	}

}
