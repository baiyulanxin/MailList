package com.example.maillist.bean;

import java.util.List;

public class PhoneListBean {
    private List<Phone> employee;

    public List<Phone> getEmployee() {
        return employee;
    }

    public void setEmployee(List<Phone> employee) {
        this.employee = employee;
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
