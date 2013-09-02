package com.huyaxin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class Controller implements Initializable {

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

    public static void main(String[] args) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, List<String>> yesterday = new HashMap<String, List<String>>();
        File root = new File(System.getProperty("user.home") + "/.xpschedule/");
        if (!root.exists()) {
            root.mkdirs();
        }
        File yesFile = new File(root.getAbsolutePath() + File.separator + sdf.format(new Date(System.currentTimeMillis() - 86400000l)) + ".dat");
        ObjectMapper mapper = new ObjectMapper();
        try {
            yesterday = mapper.readValue(yesFile, yesterday.getClass());
            System.out.println(yesterday);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        List<String> tempPersons = new ArrayList<String>();
        tempPersons.add("马蕾");
        File personFile = new File(root.getAbsolutePath() + File.separator + "persons.dat");
        FileUtils.write(personFile, mapper.writeValueAsString(tempPersons));
    }

    @FXML
    ListView<String> list;
    @FXML
    ListView<String> list1;
    @FXML
    ListView<String> list2;
    @FXML
    ListView<String> list3;
    @FXML
    ListView<String> list4;

    @FXML
    Label date;
    @FXML
    Button schedule;
    @FXML
    TextField p1;
    @FXML
    TextField p2;
    @FXML
    TextField p3;
    @FXML
    TextField p4;

    @FXML
    Label err;

    private Map<String, List<String>> yesterday = new HashMap<String, List<String>>();
    private File root;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        root = new File(System.getProperty("user.home") + "/.xpschedule/");
        if (!root.exists()) {
            root.mkdirs();
        }

        ObjectMapper mapper = new ObjectMapper();
        File yesFile = new File(root.getAbsolutePath() + File.separator + sdf.format(new Date(System.currentTimeMillis() - 86400000l)) + ".dat");
        if (yesFile.exists()) {
            try {
                yesterday = mapper.readValue(yesFile, yesterday.getClass());
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        ObservableList<String> persons = FXCollections.observableArrayList();
        File personFile = new File(root.getAbsolutePath() + File.separator + "persons.dat");
        if (personFile.exists()) {
            List<String> tempPersons = new ArrayList<String>();
            try {
                tempPersons = mapper.readValue(personFile, tempPersons.getClass());
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            persons.addAll(tempPersons);
            err.setText("修改人员信息：" + personFile.getAbsolutePath());
        }

        if (list != null) {
            list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            list.setItems(persons);
        }

        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date.setText(sdf.format(new Date()));
        }

    }

    public void processSchedule() {
        Map<String, Integer> positions = new HashMap<String, Integer>();
        Integer totalNum = 0;
        try {
            Integer num1 = Integer.parseInt(p1.getText());
            Integer num2 = Integer.parseInt(p2.getText());
            Integer num3 = Integer.parseInt(p3.getText());
            Integer num4 = Integer.parseInt(p4.getText());
            positions.put("p1", num1);
            positions.put("p2", num2);
            positions.put("p3", num3);
            positions.put("p4", num4);
            totalNum = num1 + num2 + num3 + num4;
        } catch (Exception e) {
            err.setText("亲，填数字啊！");
            return;
        }
        List<String> persons = new ArrayList<String>();
        persons.addAll(list.getSelectionModel().getSelectedItems());
        if (persons.size() < totalNum) {
            err.setText("亲，人不够，怎么排啊！");
            return;
        }

        yesterday = this.schedule(positions, persons, yesterday);
        ObservableList<String> data1 = FXCollections.observableArrayList();
        ObservableList<String> data2 = FXCollections.observableArrayList();
        ObservableList<String> data3 = FXCollections.observableArrayList();
        ObservableList<String> data4 = FXCollections.observableArrayList();
        data1.addAll(yesterday.get("p1"));
        data2.addAll(yesterday.get("p2"));
        data3.addAll(yesterday.get("p3"));
        data4.addAll(yesterday.get("p4"));
        list1.setItems(data1);
        list2.setItems(data2);
        list3.setItems(data3);
        list4.setItems(data4);
        err.setText("亲，记得保存哦！");
    }

    public void save(ActionEvent actionEvent) {
        File newFile = new File(root.getAbsolutePath() + File.separator + sdf.format(new Date()) + ".dat");
        ObjectMapper mapper = new ObjectMapper();
        try {
            FileUtils.write(newFile, mapper.writeValueAsString(yesterday));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        System.out.println(newFile.getAbsolutePath());
    }
}
