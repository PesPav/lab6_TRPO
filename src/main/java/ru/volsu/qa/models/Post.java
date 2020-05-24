package ru.volsu.qa.models;

public class Post {
    Integer id;
    String first_name;
    String last_name;
    String gender;
    String dob;
    String email;
    String phone;
    String website;
    String address;
    String status;

    public Post(String first_name,String last_name, String gender,String dob,
                String email,String phone,String website,String address,String status) {
        this.first_name =first_name;
        this.last_name =last_name;
        this.gender =gender;
        this.dob =dob;
        this.email =email;
        this.phone =phone;
        this.website =website;
        this.address =address;
        this.status =status;
    }

    /*getters*/

    public String getAddress() {
        return address;
    }

    public String getDob() {
        return dob;
    }

    public String getEmail() {
        return email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getGender() {
        return gender;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getPhone() {
        return phone;
    }

    public String getStatus() {
        return status;
    }

    public String getWebsite() {
        return website;
    }

    /* setters */

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

}
