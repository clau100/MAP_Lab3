package ro.ubbcluj.map.service;

import ro.ubbcluj.map.domain.validators.Validator;
import ro.ubbcluj.map.repository.InMemoryRepository;
import ro.ubbcluj.map.domain.Tuple;
import ro.ubbcluj.map.domain.Prietenie;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PrietenieService extends InMemoryRepository<Tuple<Long, Long>, Prietenie> {
    public PrietenieService(Validator<Prietenie> validator) {
        super(validator);
    }

    private Map<Long, Boolean> visited;
    private Map<Long, List<Long>> prieteni;

    private void makeLists() {
        Map<Long, Boolean> visited = new HashMap<>();
        Map<Long, List<Long>> prieteni = new HashMap<>();
        for (Prietenie i : this.findAll()) {
            // Marcam ca nevizitati
            visited.put(i.getId().getLeft(), false);
            visited.put(i.getId().getRight(), false);

            // Construim listele de adiacenta
            if (!prieteni.containsKey(i.getId().getLeft())) {
                prieteni.put(i.getId().getLeft(), new LinkedList<>());
            }
            prieteni.get(i.getId().getLeft()).add(i.getId().getRight());

            if (!prieteni.containsKey(i.getId().getRight())) {
                prieteni.put(i.getId().getRight(), new LinkedList<>());
            }
            prieteni.get(i.getId().getRight()).add(i.getId().getLeft());
        }
    }

    public int numarComunitati() {
        makeLists();
        int count = 0;
        for (Long i : visited.keySet()) {
            if (!visited.get(i)) {
                count++;
                DFS(i, visited, prieteni);
            }
        }
        return count;
    }

    private void DFS(Long n) {
        visited.put(n, true);
        for (Long i : prieteni.get(n)) {
            if (!visited.get(i)) {
                DFS(i, visited, prieteni);
            }
        }
    }

    public List<Long> mostSociableCommunity() {

        List<Long> maximum = new LinkedList<>();
        makeLists();

        for (Long i : visited.keySet()) {
            if (!visited.get(i)) {
                List<Long> community = new LinkedList<>();
                DFSSpecial(community, maximum, i);
            }
        }

        return maximum;
    }

    private void DFSSpecial(LinkedList<Long> currentPath, LinkedList<Long> longestPath, Long n) {
        currentPath.add(n);
        if (currentPath.size() > longestPath.size()) {
            longestPath = currentPath;
        }
        for (Long i : prieteni.get(n)) {
            DFSSpecial(currentPath, longestPath, n);
            currentPath.remove(i);
        }
    }
}
