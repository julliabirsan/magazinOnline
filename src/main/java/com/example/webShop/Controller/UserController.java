package com.example.webShop.Controller;

import com.example.webShop.Service.OrderService;
import com.example.webShop.Service.ProductService;
import com.example.webShop.Service.UserService;
import com.example.webShop.database.*;
import com.example.webShop.Service.UserException;
import com.example.webShop.security.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    @Autowired
    UserSession userSession;

    @Autowired
    OrderLinesDao orderLinesDao;

    @Autowired
    OrderService orderService;

    int items = 0;

    @GetMapping("/register-form")
    public ModelAndView registerAction(@RequestParam("email") String email,
                                       @RequestParam("password1") String password1,
                                       @RequestParam("password2") String password2
                                       ){
        ModelAndView modelAndView = new ModelAndView("register");
//        try {
//            if (!password1.equals(password2)) {
//                throw new UserException("parolele nu sunt identice");
//            }
//        } catch (UserException e){
//            modelAndView.addObject("message", e.getMessage());
//            return modelAndView;
//        }
//        jdbcTemplate.update("insert into user values(null,?,?)", email,password1);
            try {
                userService.registerUser(email, password1, password2);
            } catch (UserException e){
                modelAndView.addObject("message", e.getMessage());
                return modelAndView;
            }
            return new ModelAndView("redirect:index.html");
        }

    @GetMapping("/register")
    public ModelAndView register(){
        return new ModelAndView("register");
    }

    @GetMapping("/login")
    public ModelAndView login(@RequestParam("email") String email,
                              @RequestParam("password") String password
                              ){
        ModelAndView modelAndView = new ModelAndView("index");
        //ce validam?
//        List<User> userList = jdbcTemplate.query("select * from user where email = '"
//                + email + "'", new UserRowMapper());
//        try {
//            if (userList.isEmpty()) {
//                throw new UserException("user/parola incorecte");
//            }
//        }catch (UserException e){
//            modelAndView.addObject("message", e.getMessage());
//            return modelAndView;
//        }
//        User user = userList.get(0);
//        try {
//            if (!user.getPassword().equals(password)) {
//                throw new UserException("user/parola incorecte");
//            }
//        }catch (UserException e){
//            modelAndView.addObject("message", e.getMessage());
//            return modelAndView;
//        }
        List<User> userList;
        try {
           userList =  userService.loginUser(email, password);
        }catch (UserException e){
            modelAndView.addObject("message", e.getMessage());
            return modelAndView;
        }

        userSession.setId(userList.get(0).getId());
        return new ModelAndView("redirect:dashboard");
    }

    @GetMapping("dashboard")
    public ModelAndView dashboard(){

        ModelAndView modelAndView = new ModelAndView("index");
        if (userSession.getId()<=0){
            return modelAndView;
        }
        List<Product> productList = productService.findAllProducts();
        modelAndView = new ModelAndView("dashboard");
        modelAndView.addObject("productList", productList);
        items = userSession.getCartSize();
        modelAndView.addObject("items", items);
        return modelAndView;
    }

    @GetMapping("/add-to-cart")
    public ModelAndView addToCart(@RequestParam("productId") int productId){
        ModelAndView modelAndView = new ModelAndView("dashboard");
        if (userSession.getId()<=0){
            return new ModelAndView("index");
        }
        List<Product> productList = productService.findAllProducts();
        modelAndView.addObject("productList", productList);

        userSession.addToCart(productId);
        //System.out.println(userSession.getCart());
        items = userSession.getCartSize();
        modelAndView.addObject("items", items);
        return new ModelAndView("redirect:dashboard");
    }

    @GetMapping("/cart")
    public ModelAndView getCart(){
        //1 - cart.html asemanator cu dashboard.html
        //2. logica pt a citi produsele clientului din hashmap usersession
        //din hashmap trebuie sa scoatem id-urile produselor
        //3. citire info produse din tabela products
        //din tabela trebuie sa scoatem info pt id produse din hashmap
        //si dupa sa facem o lista Products pt acele id-uri
        //4. add to view addObject lista de produse pe care clientul o are in cos
        ModelAndView modelAndView = new ModelAndView("cart");

        if (userSession.getId()<=0){
            return new ModelAndView("index");
        }

        List<Product> produseBD = productService.findAllProducts();
        List<CartProduct> produseCos = new ArrayList<>();
        double totalOrderAmount=0;

        for(int idProdusCos : userSession.getCart().keySet()){
            for (Product product:produseBD){
                if (product.getId() == idProdusCos){
//                    produseCos.add(product);
                    CartProduct cartProduct = new CartProduct();
                    cartProduct.setCantitate(userSession.getCart().get(idProdusCos));
                    cartProduct.setId(product.getId());
                    cartProduct.setCategory(product.getCategory());
                    cartProduct.setName(product.getName());
                    cartProduct.setPrice(product.getPrice());
                    cartProduct.setPretTotal(userSession.getCart().get(idProdusCos)*product.getPrice());
                    totalOrderAmount=totalOrderAmount+userSession.getCart().get(idProdusCos)*product.getPrice();
                    produseCos.add(cartProduct);
                }
            }
        }
        modelAndView.addObject("productList", produseCos);
        modelAndView.addObject("totalPretComanda", totalOrderAmount);
        return modelAndView;
    }

    @GetMapping("/logout")
    public ModelAndView logout(){
        userSession.setId(0);
        return new ModelAndView("index");
    }

    @PostMapping("/sendOrder")
    public ModelAndView sendOrder(){
        ModelAndView modelAndView = new ModelAndView("orderSuccess");

        List<Product> produseBD = productService.findAllProducts();
        Order order = new Order();

        for(int idProdusCos : userSession.getCart().keySet()){
            for (Product product:produseBD){
                if (product.getId() == idProdusCos){
                    OrderLines orderLines = new OrderLines();
                    orderLines.setProductId(idProdusCos); //id produs
                    orderLines.setQuantity(userSession.getCart().get(idProdusCos)); //cantitate din cos
                    orderLines.setTotalPrice(userSession.getCart().get(idProdusCos)*product.getPrice()); //pret total per tip produs
                    order.setUserId(userSession.getId());
                    order.setAddress("strada cu flori");
                    orderLines.setOrder(order);
                    orderLinesDao.save(orderLines);
                }
            }
            }
        userSession.getCart().clear();
        return modelAndView;
    }

    @GetMapping("/details")
    public ModelAndView getProductDetails(@RequestParam("productId") int productId){
        ModelAndView modelAndView = new ModelAndView("productDetails");
        if (userSession.getId()<=0){
            return new ModelAndView("index");
        }
        Product p = productService.getProductDetailsById(productId);
        modelAndView.addObject("p", p);
        return modelAndView;
    }

    @GetMapping("/history")
    public ModelAndView showOrderHistory(){
        ModelAndView modelAndView = new ModelAndView("orderHistory");
        List<Order> orders = orderService.getOrdersByUserId(userSession.getId());
        modelAndView.addObject("orders", orders);
        return  modelAndView;
    }

    @GetMapping("/orderDetails")
    public ModelAndView showOrderDetails(@RequestParam("orderId") int orderId){
        ModelAndView modelAndView = new ModelAndView("orderDetails");

        Iterable<OrderLines> orderLines = orderLinesDao.findAll();
        List<OrderDetails> orderDetailsPerUser = new ArrayList<>();

        for (OrderLines orderLinesDetails :  orderLines){
            if (orderLinesDetails.getOrder().getId() == orderId){
                Product product = productService.getProductDetailsById(orderLinesDetails.getProductId());
                OrderDetails orderDetails = new OrderDetails();
                orderDetails.setProductName(product.getName());
                orderDetails.setPricePerUnit(product.getPrice());
                orderDetails.setQuantity(orderLinesDetails.getQuantity());
                orderDetails.setTotalPrice(orderLinesDetails.getTotalPrice());
                orderDetails.setCategory(product.getCategory());
                orderDetailsPerUser.add(orderDetails);
            }
        }
        modelAndView.addObject("orderLines", orderDetailsPerUser);
        return modelAndView;
    }

}
