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
        visited = new HashMap<>();
        prieteni = new HashMap<>();
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
                DFS(i);
            }
        }
        return count;
    }

    private void DFS(Long n) {
        visited.put(n, true);
        for (Long i : prieteni.get(n)) {
            if (!visited.get(i)) {
                DFS(i);
            }
        }
    }

    public LinkedList<Long> mostSociableCommunity() {

        LinkedList<Long> maximum = new LinkedList<>();
        makeLists();

        for (Long i : visited.keySet()) {
            LinkedList<Long> community = new LinkedList<>();
            DFSSpecial(community, i);
            if(community.size() > maximum.size()){
                maximum = community;
            }
        }

        return maximum;
    }

    private void DFSSpecial(LinkedList<Long> currentPath, Long n) {
        visited.put(n, true);
        currentPath.add(n);

        for(Long i: prieteni.get(n)){
            if(!visited.get(i)){
                DFSSpecial(currentPath, i);
            }
        }
    }
}
