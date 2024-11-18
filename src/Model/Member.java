/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author AL MUBDIE
 */
public class Member {

    private String memberID;
    private String name;
    private String address;
    private String email;
    private String phoneNo;

    public Member(String memberID, String name, String address, String email, String phoneNo) {
        this.memberID = memberID;
        this.name = name;
        this.address = address;
        this.email = email;
        this.phoneNo = phoneNo;
    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getphoneNo() {
        return phoneNo;
    }

    public void setphoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    @Override
    public String toString() {
        return "Member{" + "memberID=" + memberID + ", name=" + name + ", address=" + address 
                + ", email=" + email + ", phoneNo=" + phoneNo + '}';
    }
    
    

}