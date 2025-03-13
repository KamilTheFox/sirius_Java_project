package ru.hpclab.hl.module1.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hpclab.hl.module1.model.Dish;
import ru.hpclab.hl.module1.model.Order;
import ru.hpclab.hl.module1.model.Restaurant;
import ru.hpclab.hl.module1.service.DishService;
import ru.hpclab.hl.module1.service.OrderService;
import ru.hpclab.hl.module1.service.RestaurantService;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ServiceDebug")
public class ServiceController
{
    private final OrderService orderService;
    private final RestaurantService restaurantService;
    private final DishService dishService;

    public ServiceController(OrderService orderService,
                             RestaurantService restaurantService,
                             DishService dishService)
    {
        this.orderService = orderService;
        this.restaurantService = restaurantService;
        this.dishService = dishService;
    }

    @PostMapping
    private ResponseEntity<List<Order>> SendBDToFile()
    {
        try {
            List<Restaurant> restaurants = getRestaurantData();

            List<Dish> dishes = getDishData(restaurants);
            List<Order> orders = getOrderData(restaurants, dishes);
            return ResponseEntity.ok(orders);
        }
        catch (Exception e)
        {
            System.out.println("Ошибка при обработке данных: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    private List<Restaurant> getRestaurantData()
    {
        List<Restaurant> rest = Arrays.asList(
                new Restaurant("Вега","Вкусная",45),
                new Restaurant("Шаурмечная","Странная",250),
                new Restaurant("Элит","Элитная",1500),
                new Restaurant("Сушибар","Сушенная",350)
        );
        rest.forEach(restaurantService::addRestaurant);
        return rest;
    }
    private List<Dish> getDishData(
            List<Restaurant> restaurants
    )
    {
        List<Dish> dishes = Arrays.asList(
                new Dish("Суп",100,100,restaurants.get(0).getIdentifier()),
                new Dish("Картофель",150,120,restaurants.get(0).getIdentifier()),
                new Dish("Говядина",200,10,restaurants.get(0).getIdentifier()),
                new Dish("Курица",100,120,restaurants.get(0).getIdentifier()),
                new Dish("Шаурма",300,250,restaurants.get(1).getIdentifier()),
                new Dish("Шаурма с картофелем",360,350,restaurants.get(1).getIdentifier()),
                new Dish("Шаурма с чили",330,300,restaurants.get(1).getIdentifier()),
                new Dish("Запеканка",400,100,restaurants.get(2).getIdentifier()),
                new Dish("Крабы",400,200,restaurants.get(2).getIdentifier()),
                new Dish("Лобстеры",500,150,restaurants.get(2).getIdentifier()),
                new Dish("Мидии",500,150,restaurants.get(2).getIdentifier()),
                new Dish("Лосось",500,150,restaurants.get(2).getIdentifier()),
                new Dish("Говядина",500,150,restaurants.get(2).getIdentifier()),
                new Dish("Язык говяжий",500,150,restaurants.get(2).getIdentifier()),
                new Dish("Глаза",500,150,restaurants.get(2).getIdentifier()),
                new Dish("Сет мини",450,500,restaurants.get(3).getIdentifier()),
                new Dish("Сет Суши",1200,1350,restaurants.get(3).getIdentifier()),
                new Dish("Сет Дубай", 770,800,restaurants.get(3).getIdentifier()),
                new Dish("Комбо",650,650,restaurants.get(3).getIdentifier()),
                new Dish("Филадельфия",1000,1000,restaurants.get(3).getIdentifier())
        );
        dishes.forEach(dishService::addDish);
        return dishes;
    }


    private List<Order> getOrderData(
            List<Restaurant> restaurants,
            List<Dish> dishes
    )
    {
        Random random = new Random();
        List<Order> orders = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            // Выбираем рандомный ресторан
            Restaurant randomRestaurant = restaurants.get(random.nextInt(restaurants.size()));

            // Получаем блюда только этого ресторана
            List<Dish> restaurantDishes = dishes.stream()
                    .filter(dish -> dish.getRestaurant().equals(randomRestaurant.getIdentifier()))
                    .collect(Collectors.toList());

            // Создаем заказ
            orders.add(
                orderService.createOrder(
                    randomRestaurant,
                    ensureMinimumOrderSum(
                            randomRestaurant,
                            restaurantDishes),
                    getRandomAddress()
                )
            );
        }
        return orders;
    }

    private List<Dish> ensureMinimumOrderSum(
            Restaurant restaurant,
            List<Dish> allDishes)
    {
        List<Dish> resultDishes = new ArrayList<Dish>();
        double currentSum = 0;
        // Получаем все доступные блюда этого ресторана
        List<Dish> availableDishes = allDishes.stream()
                .filter(dish -> dish.getRestaurant().equals(restaurant.getIdentifier()))
                .collect(Collectors.toList());

        Random random = new Random();

        while (currentSum < restaurant.getMinimumOrder() && !availableDishes.isEmpty()) {
            // Берём случайное блюдо
            int randomIndex = random.nextInt(availableDishes.size());
            Dish additionalDish = availableDishes.get(randomIndex);

            resultDishes.add(additionalDish);
            availableDishes.remove(randomIndex);

            currentSum += additionalDish.getPrice();
        }

        return resultDishes;
    }
    private String getRandomAddress() {
        List<String> addresses = getRandomSiriusAddresses();
        return addresses.get(new Random().nextInt(addresses.size()));
    }
    private List<String> getRandomSiriusAddresses() {
        return Arrays.asList(
                "пгт Сириус, Олимпийский проспект, д. 15",
                "пгт Сириус, ул. Воскресенская, д. 12",
                "пгт Сириус, Парусный бульвар, д. 12",
                "пгт Сириус, проспект Континентальный, д. 6",
                "пгт Сириус, Триумфальный проезд, д. 9",
                "пгт Сириус, Крымская улица, д. 22",
                "пгт Сириус, Морской бульвар, д. 7",
                "пгт Сириус, Платановая аллея, д. 3",
                "пгт Сириус, Олимпийский парк, д. 11",
                "пгт Сириус, Имеретинская набережная, д. 1"
        );
    }
}
