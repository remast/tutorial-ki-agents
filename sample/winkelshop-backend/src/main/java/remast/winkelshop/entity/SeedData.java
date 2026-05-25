package remast.winkelshop.entity;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class SeedData {

    private final List<Category> categories = new ArrayList<>();
    private final List<Product> products = new ArrayList<>();
    private final List<UserAccount> users = new ArrayList<>();
    private final Map<String, String> tokenToUserId = new ConcurrentHashMap<>();
    private final Map<String, Cart> cartsByUserId = new ConcurrentHashMap<>();
    private final Map<String, List<Order>> ordersByUserId = new ConcurrentHashMap<>();
    private final AtomicLong cartItemCounter = new AtomicLong(1000);
    private final AtomicLong orderCounter = new AtomicLong(1);

    public SeedData() {
        seedCategories();
        seedProducts();
        seedUsers();
        seedOrders();
    }

    public List<Category> getCategories() {
        return categories;
    }

    public List<Product> getProducts() {
        return products;
    }

    public List<UserAccount> getUsers() {
        return users;
    }

    public Map<String, String> getTokenToUserId() {
        return tokenToUserId;
    }

    public Map<String, Cart> getCartsByUserId() {
        return cartsByUserId;
    }

    public Map<String, List<Order>> getOrdersByUserId() {
        return ordersByUserId;
    }

    public String nextCartItemId() {
        return "item_" + cartItemCounter.getAndIncrement();
    }

    public String nextOrderId() {
        return String.format("ord_2026_%04d", orderCounter.getAndIncrement());
    }

    public Optional<UserAccount> findUserByEmail(String email) {
        return users.stream().filter(user -> user.getEmail().equalsIgnoreCase(email)).findFirst();
    }

    public Optional<UserAccount> findUserById(String userId) {
        return users.stream().filter(user -> user.getId().equals(userId)).findFirst();
    }

    public Optional<Product> findProductById(String productId) {
        return products.stream().filter(product -> product.getId().equals(productId)).findFirst();
    }

    public Optional<Order> findOrderForUser(String userId, String orderId) {
        return ordersByUserId.getOrDefault(userId, List.of()).stream().filter(order -> order.getOrderId().equals(orderId)).findFirst();
    }

    public List<Order> findOrdersForUserSorted(String userId) {
        return ordersByUserId.getOrDefault(userId, new ArrayList<>())
                .stream()
                .sorted(Comparator.comparing(Order::getCreatedAt).reversed())
                .toList();
    }

    private void seedCategories() {
        categories.add(new Category("cat_wands", "Zauberstaebe", "Zauberstaebe fuer Hexen und Zauberer"));
        categories.add(new Category("cat_books", "Buecher", "Magische Lehr- und Fachbuecher"));
    }

    private void seedProducts() {
        products.add(new Product(
                "prod_wand_holly_001",
                "Stechpalme-Zauberstab",
                "11 Zoll, Phoenixfederkern, passend fuer praezise Zauber",
                7.0,
                Currency.GALLEON,
                true,
                14,
                "cat_wands",
                "https://cdn.zauberbedarf.test/products/prod_wand_holly_001.jpg"
        ));
        products.add(new Product(
                "prod_book_potions_001",
                "Fortgeschrittene Zaubertrankkunst",
                "Lehrbuch fuer fortgeschrittene Trankbrauer",
                5.0,
                Currency.GALLEON,
                true,
                30,
                "cat_books",
                "https://cdn.zauberbedarf.test/products/prod_book_potions_001.jpg"
        ));
        products.add(new Product(
                "prod_book_hogwarts_001",
                "Hogwarts: Eine Geschichte",
                "Historische Chronik von Hogwarts",
                4.0,
                Currency.GALLEON,
                true,
                20,
                "cat_books",
                "https://cdn.zauberbedarf.test/products/prod_book_hogwarts_001.jpg"
        ));
    }

    private void seedUsers() {
        users.add(new UserAccount("usr_harry", "Harry Potter", "harry.potter@hogwarts.test", "Expelliarmus123!", "buyer"));
        users.add(new UserAccount("usr_hermione", "Hermione Granger", "hermione.granger@hogwarts.test", "Expelliarmus123!", "buyer"));
        users.add(new UserAccount("usr_ron", "Ron Weasley", "ron.weasley@hogwarts.test", "Expelliarmus123!", "buyer"));
        users.add(new UserAccount("usr_draco", "Draco Malfoy", "draco.malfoy@hogwarts.test", "Expelliarmus123!", "buyer"));
        users.add(new UserAccount("usr_luna", "Luna Lovegood", "luna.lovegood@hogwarts.test", "Expelliarmus123!", "buyer"));
    }

    private void seedOrders() {
        Order order1 = new Order();
        order1.setOrderId("ord_2026_0001");
        order1.setUserId("usr_harry");
        order1.setStatus("placed");
        order1.setCurrency(Currency.GALLEON);
        order1.setCreatedAt(Instant.parse("2026-05-25T10:15:30Z"));
        order1.setShippingAddress(new ShippingAddress("Harry Potter", "Ligusterweg 4", "Little Whinging", "12345", "UK"));
        order1.setPaymentMethod("invoice");
        order1.setItems(List.of(
                new OrderItem("prod_wand_holly_001", "Stechpalme-Zauberstab", 3, 7.0),
                new OrderItem("prod_book_hogwarts_001", "Hogwarts: Eine Geschichte", 1, 4.0)
        ));
        order1.setTotal(25.0);

        Order order2 = new Order();
        order2.setOrderId("ord_2026_0002");
        order2.setUserId("usr_harry");
        order2.setStatus("shipped");
        order2.setCurrency(Currency.GALLEON);
        order2.setCreatedAt(Instant.parse("2026-05-20T08:02:10Z"));
        order2.setShippingAddress(new ShippingAddress("Harry Potter", "Ligusterweg 4", "Little Whinging", "12345", "UK"));
        order2.setPaymentMethod("invoice");
        order2.setItems(List.of(new OrderItem("prod_book_hogwarts_001", "Hogwarts: Eine Geschichte", 2, 4.0)));
        order2.setTotal(8.0);

        ordersByUserId.put("usr_harry", new ArrayList<>(List.of(order1, order2)));
        orderCounter.set(3);
    }
}
