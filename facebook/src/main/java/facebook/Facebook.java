package facebook;

import java.util.*;

public class Facebook {
	 private HashMap<String, HashSet<String>> friendships = new HashMap<>();
	  
	    // Add friendship between two persons.
	    public void addFriendship(String person1, String person2) {
	        friendships.putIfAbsent(person1, new HashSet<String>());
	        friendships.putIfAbsent(person2, new HashSet<String>());
	        friendships.get(person1).add(person2);
	        friendships.get(person2).add(person1);
	    }

	    // Add a single person to the network.
	    public void addPerson(String person) {
	        friendships.putIfAbsent(person, new HashSet<String>());
	    }
	  
	    // Calculate shortest path between two persons using BFS.
	    public List<String> shortestPath(String start, String end) {
	        Map<String, Integer> distances = new HashMap<>();
	        Map<String, String> predecessors = new HashMap<>();
	        Queue<String> queue = new LinkedList<>();
	        
	        // Initialize distances
	        for (String person : friendships.keySet()) {
	            distances.put(person, Integer.MAX_VALUE);
	        }
	        distances.put(start, 0);

	        queue.add(start);
	        
	        // BFS algorithm
	        while (!queue.isEmpty()) {
	            String current = queue.poll();
	            for (String neighbor : friendships.get(current)) {
	                if (distances.get(neighbor) == Integer.MAX_VALUE) {
	                    distances.put(neighbor, distances.get(current) + 1);
	                    predecessors.put(neighbor, current);
	                    queue.add(neighbor);
	                }
	            }
	        }

	        // Check if a path exists
	        if (distances.get(end) == Integer.MAX_VALUE) {
	            return null;
	        }

	        // Reconstruct the path
	        List<String> path = new LinkedList<>();
	        String step = end;
	        while (step != null) {
	            path.add(step);
	            step = predecessors.get(step);
	        }
	        Collections.reverse(path);
	        
	        return path;
	    }

	    // Calculate degree centrality for all persons.
	    public void degreeCentrality() {
	        for (String person : friendships.keySet()) {
	            System.out.println("Degree centrality for " + person + ": " + friendships.get(person).size());
	        }
	    }

	    // Clique detection using Bron-Kerbosch algorithm
	    public void bronKerbosch(Set<String> clique, Set<String> candidates, Set<String> excluded) {
	        if (candidates.isEmpty() && excluded.isEmpty()) {
	            if (clique.size() >= 3) {
	                System.out.println("Community found: " + clique);
	            }
	            return;
	        }
	        Set<String> newCandidates = new HashSet<>(candidates);
	        for (String v : candidates) {
	            Set<String> newClique = new HashSet<>(clique);
	            newClique.add(v);

	            Set<String> neighbors = friendships.getOrDefault(v, new HashSet<String>());
	            Set<String> newCandidatesIntersect = new HashSet<>(newCandidates);
	            newCandidatesIntersect.retainAll(neighbors);

	            Set<String> newExcluded = new HashSet<>(excluded);
	            newExcluded.retainAll(neighbors);

	            bronKerbosch(newClique, newCandidatesIntersect, newExcluded);

	            newCandidates.remove(v);
	            excluded.add(v);
	        }
	    }

	    public static void main(String[] args) {
	    	Facebook sn = new Facebook();
	        
	        // Add 50 people into the network.
	        for (int i = 1; i <= 50; i++) {
	            sn.addPerson("Person" + i);
	        }

	        // Scenario 1: Person1 to Person10 are friends with each other
	        for (int i = 1; i <= 10; i++) {
	            for (int j = i + 1; j <= 10; j++) {
	                sn.addFriendship("Person" + i, "Person" + j);
	            }
	        }
	        
	        // Scenario 2: Person11 to Person20 are friends with Person21 to Person30
	        for (int i = 11; i <= 20; i++) {
	            for (int j = 21; j <= 30; j++) {
	                sn.addFriendship("Person" + i, "Person" + j);
	            }
	        }
	        
	        // Scenario 3: Person21 to Person25 are friends with everyone else from Person26 to Person50
	        for (int i = 21; i <= 25; i++) {
	            for (int j = 26; j <= 50; j++) {
	                sn.addFriendship("Person" + i, "Person" + j);
	            }
	        }
	        sn.addFriendship("Person1", "Person11");
	        sn.addFriendship("Person1", "Person21");
	        sn.addFriendship("Person1", "Person31");
	        // Run shortest path
	        for(int i = 50; i >= 2; i--) {
	            List<String> path = sn.shortestPath("Person1", "Person" + i);
	            if (path != null) {
	                System.out.println("Shortest path between Person1 and Person" + i + ": " + path);
	            } else {
	                System.out.println("No path exists between Person1 and Person" + i);
	            }
	        }
	        
	        // Degree centrality
	        sn.degreeCentrality();
	        
	        // Community detection
	        sn.bronKerbosch(new HashSet<String>(), new HashSet<>(sn.friendships.keySet()), new HashSet<String>());
	    }
	}
