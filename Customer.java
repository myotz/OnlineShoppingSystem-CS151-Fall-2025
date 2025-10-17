/*
Abstract class: User.java

Purpose:
- Represents a user in the online shopping system
- Provide common attributes, login/logout, and abstract method: product search
*/

import java.util.HashMap;
import java.util.Map;

public abstract class User {
  private String userID;
  private String name;
  private String phoneNumber;
  private String address;
  private String password;
  private boolean loggedIn;
  //private Map<Integer, Product> wishList = new HashMap<>();

  // Constructor
  @SuppressWarnings("OverridableMethodCallInConstructor")
  public User(String userID, String name, String phoneNumber, String address, String password) {
    this.userID = userID;
    this.name = name;
    setPhoneNumber(phoneNumber);
    this.address = address;
    setPassword(password); 
  }

  // Getters and setters
  public String getUserID() {
    return userID;
  }

  public String getName() {
    return name;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public String getAddress() {
    return address;
  }

  /**
    * Validates the phone number format.
    * Accepts formats like:
    *  - 123-456-7890
    *  - (123) 456-7890
    *  - 1234567890
    *  - +1 123-456-7890
  */
  public boolean isValidPhoneNumber(String phoneNumber) {
    if (phoneNumber == null || phoneNumber.isBlank()) {
      return false;
    }
    String regex = "^(\\+\\d{1,2}\\s?)?(\\(?\\d{3}\\)?[\\s-]?)?\\d{3}[\\s-]?\\d{4}$";
    return phoneNumber.matches(regex);
  }

  public void setPhoneNumber(String newPhoneNumber) {
    if (!isValidPhoneNumber(newPhoneNumber)) {
        System.out.println("Please type your phone number is valid form.");
        return;
    }
    this.phoneNumber = newPhoneNumber;
  }

  public void setAddress(String newAddress) {
    this.address = newAddress;
  }

  public void setName(String newName) {
    this.name = newName;
  }

  public void setUserID(String userID) {
    this.userID = userID;
  }

  public void setPassword(String password) {
    if (password == null) {
      System.out.println("Password cannot be null.");
      return;
    }
    if (isValidPassword(password)) {
      this.password = password;
      System.out.println("Password set successfully!");
    } 
    else {
      System.out.println("Failed to set password. Please try again with a stronger one.");
    }
  }

  public boolean isLoggedIn() {
    return loggedIn;
  }

  // Login/logout-related methods
  //I want to make the password more powerful that must contain at least 1 symbol and Uppercase letter and number to be valid
  public boolean isValidPassword(String password) {
    boolean containsSymbol = false;
    boolean containsNumber = false;
    boolean containsUppercase = false;
    if (password == null || password.length() < 10) {
      System.out.println("Password must be at least 10 characters long.");
      return false;
    }
    for (char c : password.toCharArray()) {
      if (Character.isUpperCase(c)) {
        containsUppercase = true;
      } 
      else if (Character.isDigit(c)) {
        containsNumber = true;
      } 
      else if (!Character.isLetterOrDigit(c)) {
        containsSymbol = true;
      }
    }
    if (!containsUppercase) {
      System.out.println("Password must contain at least one uppercase letter.");
      return false;
    }

    if (!containsNumber) {
      System.out.println("Password must contain at least one number.");
      return false;
    }

    if (!containsSymbol) {
      System.out.println("Password must contain at least one special symbol (e.g., !, @, #, $).");
      return false;
    }
    return true; 
  }

  public boolean login(String userID, String password) {
    if (this.userID.equals(userID) && this.password.equals(password)) {
      loggedIn = true;
    }
    return loggedIn;
  }

  public void logout() {
    if (loggedIn) {
      System.out.println("User " + userID + " logged out.");
      loggedIn = false;
    }
  }

  // Abstract method: searchProducts
  public abstract HashMap<String, Product> searchProducts(
      String category,
      double minPrice,
      double maxPrice,
      boolean isAvailable,
      boolean discountAvailable);
}
