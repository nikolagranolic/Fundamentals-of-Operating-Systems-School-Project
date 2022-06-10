package src;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;
import java.util.HashMap;

public class MMU {
	private int numOfReferences;
	private int numOfFrames;
	private ArrayList<Integer> references = new ArrayList<>();
	
	public MMU(int numOfReferences, int numOfFrames, ArrayList<Integer> references) {
		this.numOfReferences = numOfReferences;
		this.numOfFrames = numOfFrames;
		this.references = references;
	}
	
	public void fifo() {
		for(int i = 0; i < 3*numOfReferences - 7; i++)
			System.out.print("-");
		System.out.print("FIFO algoritam");
		for(int i = 0; i < 3*numOfReferences - 7; i++)
			System.out.print("-");
		System.out.println();
		
		String[][] table = new String[numOfFrames + 2][numOfReferences];
		LinkedList<Integer> queue = new LinkedList<Integer>();
		int numOfPF = 0;
		for(int i = 0; i < numOfReferences; i++) {
			table[0][i] = Integer.toString(references.get(i));
			if(!queue.contains(references.get(i))) {
				table[1][i] = queue.contains(references.get(i)) ? "" : "PF";
				numOfPF++;
			}
			if(!queue.contains(references.get(i)))
				queue.addFirst(references.get(i));
			if(queue.size() > numOfFrames)
				queue.pollLast();
			for(int j = 0; j < queue.size(); j++) {
				table[j + 2][i] = Integer.toString(queue.get(j));
			}
		}
		
		printTable(table, numOfPF);
	}
	
	public void lru() {
		for(int i = 0; i < 3*numOfReferences - 7; i++)
			System.out.print("-");
		System.out.print("LRU algoritam-");
		for(int i = 0; i < 3*numOfReferences - 7; i++)
			System.out.print("-");
		System.out.println();
		
		String[][] table = new String[numOfFrames + 2][numOfReferences];
		LinkedList<Integer> queue = new LinkedList<Integer>();
		int numOfPF = 0;
		for(int i = 0; i < numOfReferences; i++) {
			table[0][i] = Integer.toString(references.get(i));
			if(!queue.contains(references.get(i))) {
				table[1][i] = queue.contains(references.get(i)) ? "" : "PF";
				numOfPF++;
			}
			if(queue.contains(references.get(i)))
				queue.remove(references.get(i));
			queue.addFirst(references.get(i));
			if(queue.size() > numOfFrames)
				queue.pollLast();
			for(int j = 0; j < queue.size(); j++) {
				table[j + 2][i] = Integer.toString(queue.get(j));
			}
		}
		
		printTable(table, numOfPF);
	}
	
	public void sc() {
		for(int i = 0; i < 3*numOfReferences - 6; i++)
			System.out.print("-");
		System.out.print("SC algoritam");
		for(int i = 0; i < 3*numOfReferences - 6; i++)
			System.out.print("-");
		System.out.println();
		//dodati eventualno da korisnik unosi privilegovanu stranicu
		int privileged = 2;
		boolean hasSC = true;
		
		String[][] table = new String[numOfFrames + 2][numOfReferences];
		LinkedList<Integer> queue = new LinkedList<Integer>();
		int numOfPF = 0;
		for(int i = 0; i < numOfReferences; i++) {
			table[0][i] = Integer.toString(references.get(i));
			if(!queue.contains(references.get(i))) {
				table[1][i] = queue.contains(references.get(i)) ? "" : "PF";
				numOfPF++;
			}
			if(!queue.contains(references.get(i))) {
				queue.addFirst(references.get(i));
				if(references.get(i) == privileged)
					hasSC = true;
			}
			if(queue.size() > numOfFrames) {
				if(queue.peekLast() == privileged && hasSC) {
					queue.pollLast();
					queue.pollLast();
					queue.add(1, privileged);
					hasSC = false;
				}
				else
					queue.pollLast();
			}
			
			for(int j = 0; j < queue.size(); j++) {
				if(queue.get(j) == privileged && hasSC)
					table[j + 2][i] = Integer.toString(queue.get(j)) + "r";
				else
					table[j + 2][i] = Integer.toString(queue.get(j));
			}
		}
		
		printTable(table, numOfPF);
	}
	
	public void lfu() {
		for(int i = 0; i < 3*numOfReferences - 7; i++)
			System.out.print("-");
		System.out.print("LFU algoritam-");
		for(int i = 0; i < 3*numOfReferences - 7; i++)
			System.out.print("-");
		System.out.println();
		int initial = 10;
		int increment = 4;
		String[][] table = new String[numOfFrames + 2][numOfReferences];
		LinkedList<Integer> queue = new LinkedList<Integer>();
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		int numOfPF = 0;
		
		for(int i = 0; i < numOfReferences; i++) {
			table[0][i] = Integer.toString(references.get(i));
			
			if(map.containsKey(references.get(i))) {
				map.replace(references.get(i), map.get(references.get(i)) + increment);
			}
			else {
				map.put(references.get(i), initial);
			}
			Set<Integer> temp = map.keySet();
			for(Integer item : temp) {
				if(item != references.get(i)) {
					map.put(item, map.get(item) - 1);
				}
			}	
			if(!queue.contains(references.get(i))) {
				table[1][i] = queue.contains(references.get(i)) ? "" : "PF";
				numOfPF++;
			}
			queue.remove(references.get(i));
			if(queue.size() == 0)
				queue.addFirst(references.get(i));
			else {
				for(int j = 0; j < queue.size(); j++) {
					if(map.get(queue.get(j)) <= map.get(references.get(i))) {	
						queue.add(j, references.get(i));
						break;
					}
				}
			}
			
			if(queue.size() > numOfFrames) {
				map.remove(queue.pollLast());
			}
			
			
			for(int j = 0; j < queue.size(); j++) {
				table[j + 2][i] = Integer.toString(queue.get(j)) + "[" + Integer.toString(map.get(queue.get(j))) + "]";
			}
		}
		printTable(table, numOfPF);
	}
	
	public void oa() {
		for(int i = 0; i < 3*numOfReferences - 10; i++)
			System.out.print("-");
		System.out.print("Optimalni algoritam-");
		for(int i = 0; i < 3*numOfReferences - 10; i++)
			System.out.print("-");
		System.out.println();
		
		String[][] table = new String[numOfFrames + 2][numOfReferences];
		LinkedList<Integer> queue = new LinkedList<Integer>();
		int numOfPF = 0;
		for(int i = 0; i < numOfReferences; i++) {
			table[0][i] = Integer.toString(references.get(i));
			if(!queue.contains(references.get(i))) {
				table[1][i] = queue.contains(references.get(i)) ? "" : "PF";
				numOfPF++;
			}
			if(!queue.contains(references.get(i)) && queue.size() < numOfFrames)
				queue.addFirst(references.get(i));
			if(queue.size() == numOfFrames && !queue.contains(references.get(i))) {
				if(i == numOfReferences - 1) {
					queue.set(numOfFrames - 1, references.get(i));
				}
				else {
					Integer item = null;
					int forRemovalLen = 0;
					int index = -1;
					for(int p = 0; p < queue.size(); p++) {
						item = queue.get(p);
						int temp = 1;
						for(int k = i + 1; k < numOfReferences; k++) {
							if(item == references.get(k))
								break;
							temp++;
						}
						if(temp > forRemovalLen) {
							forRemovalLen = temp;
							index = p;
						}
					}
					queue.set(index, references.get(i));
				}
			}
			for(int j = 0; j < queue.size(); j++) {
				table[j + 2][i] = Integer.toString(queue.get(j));
			}
		}
		
		printTable(table, numOfPF);
	}
	
	private void printTable(String[][] table, int numOfPF) {
		for(int i = 0; i < numOfFrames + 2; i++) {
			for(int j = 0; j < numOfReferences; j++) {
				if(table[i][j] == null)
					table[i][j] = "";
				System.out.printf("%6s", table[i][j]);
			}
			System.out.println();
		}
		System.out.println("Efikasnost algoritma: PF = " + numOfPF + "    =>    pf = " + numOfPF + " / " + numOfReferences + " = " + Math.round(((double)numOfPF/numOfReferences)*100) + "%");
		System.out.println();
	}
	
	public static void main(String args[]) {
		Scanner scan = new Scanner(System.in);
		int numberOfFrames;
		int numberOfReferences;
		ArrayList<Integer> references = new ArrayList<>();
		
		System.out.println("-------Simulacija jedinice za upravljanje memorijom-------");
		
		System.out.println("Unesite broj okvira memorije:");
		do {
			numberOfFrames = scan.nextInt();
		} while (numberOfFrames <= 0);
		
		System.out.println("Unesite broj memorijskih referenci stranica koje ucestvuju u simulaciji:");
		do {
			numberOfReferences = scan.nextInt();
		} while (numberOfReferences <= 0);
		Integer[] array = {1,2,3,4,2,1,5,6,2,1,2,3,7,6,3,2,1,2,3,6};
		for(int k = 0; k < array.length; k++) {
			references.add(array[k]);
		}
		// for(int i = 1, temp = 0; i <= numberOfReferences; ) {
		// 	System.out.println("Unesite " + i + ". referencu:");
		// 	temp = scan.nextInt();
		// 	if(temp > 0) {
		// 		references.add(temp);
		// 		i++;
		// 	}
		// }
		scan.nextLine();
		MMU mmu = new MMU(numberOfReferences, numberOfFrames, references);
		System.out.println("Odaberite jedan ili vise algoritama koji ce ucestvovati u simulaciji (ukoliko birate vise odvojite ih razmacima):");
		System.out.println("(koristite sljedece skracenice: FIFO-First in first out, LRU-Last recently used, SC-Second chance, LFU-Least frequently used, OA-Optimalni algoritam)");
		String opcija = "";
		String[] opcije;
		opcija = scan.nextLine();
		
		opcije = opcija.split(" ");
		for(int i = 0; i < opcije.length; i++) {
			switch(opcije[i]) {
			case "FIFO":
				mmu.fifo();
				break;
			case "LRU":
				mmu.lru();
				break;
			case "SC":
				mmu.sc();
				break;
			case "LFU":
				mmu.lfu();
				break;
			case "OA":
				mmu.oa();
				break;
			default:
				System.out.println("Nepoznata opcija");
				break;
			}
		}
		scan.close();
	}
}
