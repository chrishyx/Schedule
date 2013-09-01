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
                System.out.println(position + " " + person);
                resultMap.get(position).add(person);
                persons.remove(person);
            }
        }
        return resultMap;
    }

    private String findOnePerson(Random random, List<String> persons, List<String> yesterday) {
        Set<String> visited = new HashSet<String>();
        while (true) {
            int index = Math.abs(random.nextInt() % persons.size());
            String person = persons.get(index).trim();
            visited.add(person);
            if (yesterday != null && !yesterday.contains(person)) {
                return person;
            }
            if (visited.size() == persons.size()) {
                break;
            }
        }
        return persons.get(Math.abs(random.nextInt() % persons.size())).trim();
    }

    public static void main(String[] args) {
        Map<String, Integer> positions = new HashMap<String, Integer>();
        positions.put("A", 1);
        positions.put("B", 2);
        positions.put("C", 4);
        List<String> persons = new ArrayList<String>();
        persons.add("A1");
        persons.add("A2");
        persons.add("A3");
        persons.add("A4");
        persons.add("A5");
        persons.add("A6");
        persons.add("A7");
        Controller controller = new Controller();
        Map<String, List<String>> yesterday = new HashMap<String, List<String>>();
        yesterday = controller.schedule(positions, persons, yesterday);
        System.out.println(yesterday);
        persons.add("A1");
        persons.add("A2");
        persons.add("A3");
        persons.add("A4");
        persons.add("A5");
        persons.add("A6");
        persons.add("A7");
        System.out.println(controller.schedule(positions, persons, yesterday));
    }
}
