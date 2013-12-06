package org.liuhuo.spring.model;

public class DummyUser {
    private int id;
    private String name;
    private String role;

    public DummyUser(int id, String name, String role) {
	this.id = id;
	this.name = name;
	this.role = role;
    }

    public int getId() {
	return this.id;
    }

    public String getName() {
	return this.name;
    }

    public String getRole() {
	return this.role;
    }

    @Override
    public String toString() {
	return "< " + id + " " + name +  " " + role + " >";
    }
}
