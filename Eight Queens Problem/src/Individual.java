import java.util.ArrayList;
import java.util.Arrays;

public class Individual {
	
	/* Changes from default HW01 version:
	 * -Removed all mentions of a crossover site because of switch to cycles crossover
	 */

	private String chrom;//chessboard representation
	private int length;//size of chessboard
	private int parent1;//parent1 identity
	private int parent2;//parent2 identity
	private int fitness;//fitness value
	
	//constructor with no parameters
	//initializes with random string
	public Individual(int boardSize){
		chrom = chessNum(boardSize);
		length = boardSize;
		fitness = evaluateFitness();
		parent1 = -1;
		parent2 = -1;
	}
	
	//constructor that accepts a chromosome and two parent names
	public Individual(String chromosome, int firstParent, int secondParent){
		chrom = chromosome;
		length = chrom.length();
		fitness = evaluateFitness();
		parent1 = firstParent;
		parent2 = secondParent;
	}
	
	//constructor that only accepts a chromosome
	public Individual(String chromosome){
		chrom = chromosome;
		length = chrom.length();
		fitness = evaluateFitness();
		parent1 = -1;
		parent2 = -1;
	}
	
	//getter methods
	public String getChrom(){
		return chrom;
	}

	public int getLength(){
		return length;
	}
	
	public String getParents(){
		return "(" + parent1 + "," + parent2 + ")";
	}
	
	public double getFitness(){
		return fitness;
	}
	
	//setter methods
	public void setChrom(String newChrom){
		chrom=newChrom;
	}
	
	public void setParents(int parentData, int parentData2){
		parent1 = parentData;
		parent2 = parentData2;
	}
	
	//generates n-character string representation of a chessboard
	public static String chessNum(int n){
		//creates string buffer
		//generates random numbers from 0 to n-1 until string is 8 characters long
		StringBuffer chessMod = new StringBuffer();
		int i;
		//Fills list with numbers 1 to n
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		for(i=0; i<n; i++){
			numbers.add(i+1);
		}
		//picks random indices from list, then deletes the selected location
		//draws without replacement
		//avoids horizontal checks
		for(i=0; i<n; i++){
			int rand = (int)(Math.random()*(numbers.size()));
			chessMod.append(numbers.get(rand));
			numbers.remove(rand);
		}
		String chessString = chessMod.toString();
		return chessString;
	}

	//Fitness evaluation method
	//counts number of non-mutual checks in board arrangement
	//maximum of n-1 checks, fitness is equal to (n-1)-total checks
	//for an 8x8 board, fitness ranges from 0 to 7 with 0 = maximum checks and 7 = solved board
	int evaluateFitness(){
		
		int t1 = 0;//number of repetitive queens in one diagonal while seen from left corner
		int t2 = 0;//number of repetitive queens in one diagonal while seen from right corner
		int[] f1 = new int[length];//tracks left corner diagonals
		int[] f2 = new int[length];//tracks right corner diagonals

		int i;
		for(i=1; i<=length; i++){
			f1[i-1] = Character.getNumericValue(chrom.charAt(i-1)) - i;
			f2[i-1] = ((1 + length) - Character.getNumericValue(chrom.charAt(i-1))) - i;
		}
		Arrays.sort(f1);
		Arrays.sort(f2);
		for(i=2; i<=length; i++){
			if(f1[i-1]==f1[i-2]){//checks whether two Queens are in same diagonals seeing from left corner or not
				t1++;
			}
			if(f2[i-1]==f2[i-2]){//checks whether two Queens are in same diagonals seeing from right corner or not
				t2++;
			}
		}
		fitness = (length-1) - (t1 + t2);//added subtraction to make fitness scaling more intuitive (higher is better)
		return fitness;
	}
	
	public String toString(){
		String string = String.format("(%2d,%2d)   %s %8d", parent1, parent2, chrom, fitness);
		return string;
	}
	
	public void printBoard(){
		int i;
		int j;
		for(i=0; i<8; i++){
			for(j=0; j<8; j++){
				if(Character.getNumericValue(chrom.charAt(i)) == j+1){
					System.out.print("Q");
				}
				else{
					System.out.print("0");
				}
			}
			System.out.println();
		}
	}
	
	public static void main(String[] args) {
		int i = 0;
		for(i=0; i<30; i++){
			//System.out.println(binaryNum());
		}
		
		Individual individual = new Individual("12345678");
		System.out.println(individual);
		individual.printBoard();

	}

}

