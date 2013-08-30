package com.huyaxin;

import java.util.*;

public class Controller {

    public Map<String, List<String>> schedule(Map<String, Integer> positions, List<String> persons, Map<String, List<String>> yesterday) {
        Map<String, List<String>> resultMap = new HashMap<String, List<String>>();
        Random random = new Random(System.currentTimeMillis());
        for (String position : positions.keySet()) {
            for (int i = 0; i < positions.get(position); i++) {
                String person = findOnePerson(random, persons, yesterday.get(position));
                if (resultMap.get(position) == null) {
                    resultMap.put(position, new ArrayList<String>());
                }
                resultMap.get(position).add(person);
                persons.remove(person);
            }
        }
        return resultMap;
    }

    private String findOnePerson(Random random, List<String> persons, List<String> yesterday) {
        Set<String> visited = new HashSet<String>();
        while (true) {
            String person = persons.get(Math.abs(random.nextInt() % persons.size())).trim();
            visited.add(person);
            if (!yesterday.contains(person)) {
                return person;
            }
            if (visited.size() == persons.size()) {
                break;
            }
        }
        return persons.get(Math.abs(random.nextInt() % persons.size())).trim();
    }

    public static void main(String[] args) {

    }
}
