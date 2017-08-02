/**
 * We want to know when the three events occur, and how the timing depends on N, the initial number of individuals. 
 * To find the standard deviation, we need to calculate the mean, variance and standard deviation accordingly 
 * The SD gives us an indication of how well our randomized observations fit compared to the average. 
 */

public class GiantBook {
	
	// Amount of unions
	private int[][] roundsStats; 
	// Initially, none of the events exist 
	private boolean isNonIsolated = false;
	private boolean isGiant = false; 
	private boolean isConnected = false; 
	// Count for each event
	private int nonIsolatedCount;
	private int giantCount;
	private int connectedCount; 

	/**
	 * Calculates how long it should take approximately to achieve the three events 
	 * @param size - the total amount of individuals 
	 */
	public void findStandardDeviation(int size) {
		int i; 
		double nsum, gsum, csum, navg, gavg, cavg, ndiff, gdiff, cdiff; 
		nsum = gsum = csum = ndiff = gdiff = cdiff = 0.0;
		for (i=0; i < size; i++) {
			nsum += roundsStats[i][0];
			gsum += roundsStats[i][1];
			csum += roundsStats[i][2];
		}
		navg = nsum / size;
		gavg = gsum / size;
		cavg = csum / size;
		// Calculate standard deviation, (difference)^2 - mean
		for(i=0; i < size; i++) {
			ndiff += Math.pow((roundsStats[i][0] - navg), 2);
			gdiff += Math.pow((roundsStats[i][1] - gavg), 2);
			cdiff += Math.pow((roundsStats[i][2] - cavg), 2);
		}
		StdOut.println("size\t\tNon-Isolated\t\tGiant\t\tConnected");
		StdOut.printf("%d\t\t%.2f (%.2f)\t\t%.2f (%.2f)\t\t%.2f (%.2f)\n", 
			size, navg, Math.sqrt(ndiff), gavg, Math.sqrt(gdiff), cavg, Math.sqrt(cdiff));
	}

	public static void main(String[] args) {

		GiantBook giantBook = new GiantBook();
		int size = StdIn.readInt(); // Amount of individuals, N 
		MyUnionFind UF = new MyUnionFind(size);
		StdOut.println();
		// Amount of unions starts at 0 
		int unionCounter = 0;
		giantBook.roundsStats = new int[size][3];

		while(!giantBook.isConnected) {

			// Generates random integers between 0 (inclusive) and N (exclusive)
			int i = StdRandom.uniform(size);
			int j = StdRandom.uniform(size);
			UF.union(i,j);
			unionCounter++; // One union completed 

			// Print if all numbers have been connected
			if(UF.isConnected()) {
				giantBook.isConnected = true;
				giantBook.connectedCount = unionCounter;
			}

			if(!giantBook.isGiant) {
				
				if(UF.isGiant()) 
				{
					giantBook.isGiant = true;
					giantBook.giantCount = unionCounter;
				}

			}

			if(!giantBook.isNonIsolated) {
				if(UF.isNonIsolated()) {
					giantBook.isNonIsolated = true;
					giantBook.nonIsolatedCount = unionCounter;
				}
			}

		}

		StdOut.println("Connected after " + giantBook.connectedCount + " unions");
		StdOut.println("Giant after " + giantBook.giantCount + " unions");
		StdOut.println("nonIsolated after " + giantBook.nonIsolatedCount + " unions");
		StdOut.println();
		giantBook.findStandardDeviation(size);
		
	}



}