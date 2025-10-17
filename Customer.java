/*
 Class: Customer.java

 Purpose:
 - Represents a customer in the online shopping system
 - Manages the customer's shopping cart, payment method, and orders
  - Extends User class for customer-specific attributes
*/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Customer extends User implements Operations {

  private static final int MAX_INSTANCES = 100;
  private static int instanceCount = 0;
  private ShoppingCart cart;
  private String paymentMethod;
  private ArrayList<Order> orders;

  // Constructor
  public Customer(String userID, String name, String phoneNumber, String address, String password) {
    super(userID, name, phoneNumber, address, password);

    if (instanceCount >= MAX_INSTANCES) {
      throw new IllegalStateException("Reached max Customer limit " + MAX_INSTANCES);
    }

    this.cart = new ShoppingCart(userID);
    this.orders = new ArrayList<>();

    instanceCount++;
  }

  // Getters and setters
  public static int getMaxInstances() {
    return MAX_INSTANCES;
  }

  public static int getInstanceCount() {
    return instanceCount;
  }

  public ShoppingCart getCart() {
    return cart;
  }

  public String getPaymentMethod() {
    return paymentMethod;
  }

  public ArrayList<Order> getOrders() {
    return orders;
  }

  public void setCart(ShoppingCart cart) {
    this.cart = cart;
  }

  public void setPaymentMethod(String paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  public void setOrders(ArrayList<Order> orders) {
    this.orders = orders;
  }

  // Customer-related functions
  public void changePaymentMethod(String paymentMethod) {
    setPaymentMethod(paymentMethod);
    System.out.printf("Your payment method was changed to %s!%n", paymentMethod);
  }

  // Product-related functions
  @Override
  public void addProduct(Product p, int qty) {
    cart.addProduct(p, qty);
  }

  @Override
  public void removeProduct(Product p, int qty) {
    cart.removeProduct(p, qty);
  }

  // Order-related functions
  public void displayOrders() {
    if (orders.isEmpty()) {
      System.out.println("You have no orders yet.");
      return;
    }

    for (Order o : orders) {
      System.out.println(o.getOrderId());
    }
  }

  public void placeOrder() {
    if (this.cart.getproductsList().isEmpty()) {
      System.out.println("Your cart is empty. Please add items before placing an order.");
      return;
    }

    if (paymentMethod == null) {
      System.out.println("Please set a payment method before placing an order.");
      return;
    }

    // Create new order
    Order order = new Order();

    // Copy from cart to order
    order.setProductOrdered(this.cart.getproductsList());

    // Print order content
    System.out.println("You ordered:");
    for (Map.Entry<Product, Integer> entry : order.getProductOrdered().entrySet()) {
      System.out.printf("- %s x %d%n", entry.getKey().getName(), entry.getValue());
    }

    // Process order
    order.processOrder();

    // Output total cost
    System.out.printf("Total cost: $%.2f%n", order.computeTotalCost());

    // Add order to user order history
    orders.add(order);
  }

  public void confirmDelivery(String orderID) {

    Order found = null;
    for (Order o : orders) {
      if (o.getOrderId().equals(orderID)) {
        found = o;
        break;
      }
    }

    if (found.isCanceled()) {
      System.out.println("Cannot deliver a canceled order.");
      return;
    }

    if (orders.isEmpty()) {
      System.out.println("You have no orders. Please place an order first.");
      return;
    }

    if (found.isDelivered()) {
      System.out.println("This order has already been marked as delivered.");
      return;
    }

    // Mark as delivered
    found.setDelivered(true);
    found.setDeliveryDate(new java.util.Date());
    System.out.println("Your order has been marked as delivered.");
  }

  public void cancelOrder(String orderID) {
    if (orders.isEmpty()) {
      System.out.println("You have no orders. Please place an order first.");
      return;
    }

    Order found = null;
    for (Order o : orders) {
      if (o.getOrderId().equals(orderID)) {
        found = o;
        break;
      }
    }

    if (found == null) {
      System.out.println("Order not found.");
      return;
    }

    found.cancelOrder();
    orders.remove(found);
  }

  public void returnOrder(String orderID) {
    if (orders.isEmpty()) {
      System.out.println("You have no orders. Please place an order first.");
      return;
    }

    Order found = null;
    for (Order o : orders) {
      if (o.getOrderId().equals(orderID)) {
        found = o;
        break;
      }
    }

    if (found == null) {
      System.out.println("Order not found.");
      return;
    }

    found.initiateReturn();
  }

  public void completeReturn(String orderID) {
    if (orders.isEmpty()) {
      System.out.println("You have no orders. Please place an order first.");
      return;
    }

    Order found = null;
    for (Order o : orders) {
      if (o.getOrderId().equals(orderID)) {
        found = o;
        break;
      }
    }

    if (found == null) {
      System.out.println("Order not found.");
      return;
    }

    found.completeReturn();
  }

  public void checkRefund(String orderID) {
    if (orders.isEmpty()) {
      System.out.println("You have no orders. Please place an order first.");
      return;
    }

    Order found = null;
    for (Order o : orders) {
      if (o.getOrderId().equals(orderID)) {
        found = o;
        break;
      }
    }

    if (found == null) {
      System.out.println("Order not found.");
      return;
    }

    found.refundStatus();
  }

  @Override
  public HashMap<String, Product> searchProducts(
      String category,
      double minPrice,
      double maxPrice,
      boolean isAvailable,
      boolean discountAvailable) {
    HashMap<String, Product> out = new HashMap<>();
    for (Product p : Main.products) { // <- static access
      if (category != null && !category.equalsIgnoreCase(p.getCategory())) continue;
      if (p.getPrice() < minPrice || p.getPrice() > maxPrice) continue;
      if (isAvailable && !p.isAvailable()) continue;
      if (discountAvailable && p.getDiscountPercent() <= 0) continue;
      out.put(p.getProductId(), p);
    }
    return out;
  }
}
