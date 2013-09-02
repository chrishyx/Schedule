package com.huyaxin.model;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: yaxin.hu
 * @date: 13-9-2
 */
public class Person {
    private SimpleStringProperty name = new SimpleStringProperty("");

    public Person(String name){
        setName(name);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }
}
