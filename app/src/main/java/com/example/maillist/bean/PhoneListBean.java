package com.example.maillist.bean;

import java.util.List;

public class PhoneListBean {
    private List<Phone> employees;

    public List<Phone> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Phone> employees) {
        this.employees = employees;
    }

    public static class Phone {
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTelphone() {
            return telphone;
        }

        public void setTelphone(String telphone) {
            this.telphone = telphone;
        }

        /**
         * firstName : Bill
         * lastName : Gates
         */
        private String name;
        private String telphone;

    }
}
