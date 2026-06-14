/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.main;

/**
 * Registration and login class for Part 1.
 * The original method names are kept so Part 1 still works with Part 2.
 */
public class Login {
    private String userName;
    private String password;
    private String cellPhoneNumber;
    
    public boolean checkUserName(String userName){
        return userName != null && userName.contains("_") && userName.length() <= 5;
    }

    public boolean checkPasswordComplexity(String password){
        return password != null
                && password.length() >= 8
                && password.matches(".*[A-Z].*")
                && password.matches(".*[0-9].*")
                && password.matches(".*[^a-zA-Z0-9].*");
    }

    /**
     * Regex adapted from regular-expression phone-number validation examples.
     * Reference: Oracle Java Pattern documentation,
     * https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/regex/Pattern.html
     */
    public boolean checkCellPhoneNumber(String cellPhoneNumber){
        return cellPhoneNumber != null && cellPhoneNumber.matches("^\\+27\\d{9}$");
    }
    
    public String registerUser(String userName, String password, String cellPhoneNumber){
        this.userName = userName;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
        
        if(!checkUserName(userName)){
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }
        else if(!checkPasswordComplexity(password)){
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
        else if(!checkCellPhoneNumber(cellPhoneNumber)){
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }
        
        return "User successfully registered.";
    }

    public boolean loginUser(String userName, String password){
        return this.userName != null && this.password != null && this.userName.equals(userName) && this.password.equals(password);
    }

    public String returnLoginStatus(boolean success, String firstName, String lastName){
        if(success){
            return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
        }
        else{
            return "Username or password incorrect, please try again.";
        }
    }

    public String getCellPhoneNumber() {
        return cellPhoneNumber;
    }
}
