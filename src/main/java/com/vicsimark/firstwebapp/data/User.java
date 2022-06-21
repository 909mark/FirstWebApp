package com.vicsimark.firstwebapp.data;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {
    private String name;
    private String property1;
    private String property2;
    private String property3;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!Objects.equals(name, user.name)) return false;
        if (!Objects.equals(property1, user.property1)) return false;
        if (!Objects.equals(property2, user.property2)) return false;
        return Objects.equals(property3, user.property3);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (property1 != null ? property1.hashCode() : 0);
        result = 31 * result + (property2 != null ? property2.hashCode() : 0);
        result = 31 * result + (property3 != null ? property3.hashCode() : 0);
        return result;
    }

    public User(String name, String property1, String property2, String property3) {
        this.name = name;
        this.property1 = property1;
        this.property2 = property2;
        this.property3 = property3;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProperty1() {
        return property1;
    }

    public void setProperty1(String property1) {
        this.property1 = property1;
    }

    public String getProperty2() {
        return property2;
    }

    public void setProperty2(String property2) {
        this.property2 = property2;
    }

    public String getProperty3() {
        return property3;
    }

    public void setProperty3(String property3) {
        this.property3 = property3;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", property1='" + property1 + '\'' +
                ", property2='" + property2 + '\'' +
                ", property3='" + property3 + '\'' +
                '}';
    }
}
