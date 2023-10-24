package ro.ubbcluj.map.service;

import ro.ubbcluj.map.domain.validators.Validator;
import ro.ubbcluj.map.repository.InMemoryRepository;
import ro.ubbcluj.map.domain.Tuple;
import ro.ubbcluj.map.domain.Prietenie;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PrietenieService extends InMemoryRepository<Tuple<Long, Long>, Prietenie>{
    public PrietenieService(Validator<Prietenie> validator) {
        super(validator);
    }
    public int numarComunitati(){
        Map<Long, Boolean> visited = new HashMap<>();
        Map<Long, List<Long>> prieteni = new HashMap<>();
        for(Prietenie i : this.findAll()){
            // Marcam ca nevizitati
            visited.put(i.getId().getLeft(), false);
            visited.put(i.getId().getRight(), false);

            // Construim listele de adiacenta
            if(!prieteni.containsKey(i.getId().getLeft())){
                prieteni.put(i.getId().getLeft(), new LinkedList<>());
            }
            prieteni.get(i.getId().getLeft()).add(i.getId().getRight());

            if(!prieteni.containsKey(i.getId().getRight())){
                prieteni.put(i.getId().getRight(), new LinkedList<>());
            }
            prieteni.get(i.getId().getRight()).add(i.getId().getLeft());
        }

        int count = 0;
        for(Long i : visited.keySet()){
            if(!visited.get(i)){
                count++;
                DFS(i, visited, prieteni);
            }
        }
        return count;
    }
    public List<Long> mostSociableCommunity(){
        List<Long> community = new LinkedList<>();
        
        return community;
    }
    private void DFS(Long n, Map<Long, Boolean> visited, Map<Long, List<Long>> prieteni){
        visited.put(n, true);
        for(Long i : prieteni.get(n)){
            if(!visited.get(i)){
                DFS(i, visited, prieteni);
            }
        }
    }
}
