package com.github.kingbbode.messenger.teamup.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

/**
 * Created by YG on 2017-05-18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrganigrammeResponse {
    private int index;
    private String name;
    private List<User> users;
    private List<Department> department;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Department> getDepartment() {
        return department;
    }

    public void setDepartment(List<Department> department) {
        this.department = department;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class User {
        private long index;
        private String name;

        public long getIndex() {
            return index;
        }

        public void setIndex(long index) {
            this.index = index;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
        
        public void takeInfo(Map<Long, String> tmpMembers){
            tmpMembers.put(index,name);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Department {
        private int index;
        private String name;
        private List<User> users;
        private List<Department> department;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<User> getUsers() {
            return users;
        }

        public void setUsers(List<User> users) {
            this.users = users;
        }

        public List<Department> getDepartment() {
            return department;
        }

        public void setDepartment(List<Department> department) {
            this.department = department;
        }
    }
}
