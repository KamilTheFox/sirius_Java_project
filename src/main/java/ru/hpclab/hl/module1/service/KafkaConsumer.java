package ru.hpclab.hl.module1.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import ru.hpclab.hl.module1.dto.DishCreateDTO;
import ru.hpclab.hl.module1.dto.KafkaMessage;
import org.springframework.kafka.annotation.KafkaListener;
import ru.hpclab.hl.module1.dto.OrderCreateDTO;
import ru.hpclab.hl.module1.dto.RestaurantCreateDTO;
import ru.hpclab.hl.module1.model.Dish;
import ru.hpclab.hl.module1.model.Restaurant;

import java.util.List;

@Service
@Slf4j
public class KafkaConsumer {

    private final ObjectMapper objectMapper;
    private final RestaurantService restaurantService;
    private final OrderService orderService;
    private final DishService dishService;

    public KafkaConsumer(ObjectMapper objectMapper,
                         RestaurantService restaurantService,
                         OrderService orderService,
                         DishService dishService) {
        this.objectMapper = objectMapper;
        this.restaurantService = restaurantService;
        this.orderService = orderService;
        this.dishService = dishService;
    }

    @KafkaListener(topics = "var13",
            groupId = "${spring.kafka.consumer.group-id}",
            concurrency = "3",
            containerFactory = "kafkaListenerContainerFactory")
    public void consume(List<String> messages) {
        for (String message : messages) {
            try {
                KafkaMessage kafkaMessage = objectMapper.readValue(message, KafkaMessage.class);
                consumeMain(kafkaMessage);
            } catch (JsonProcessingException e) {
                log.error("Error parsing Kafka message: {}", message, e);
            } catch (Exception e) {
                log.error("Error processing Kafka message: {}", message, e);
            }
        }
    }

    private void consumeMain(KafkaMessage message)
    {
        try {
            switch (message.getEntity().toUpperCase()) {
                case "RESTAURANT":
                    handleRestaurant(message);
                    break;
                case "ORDER":
                    handleOrder(message);
                    break;
                case "DISH":
                    handleDish(message);
                    break;
                default:
                    log.error("Unknown entity type: {}", message.getEntity());
            }
        } catch (Exception e) {
            log.error("Error processing message: {}", message, e);
        }
    }

    private void handleDish(KafkaMessage kafkaMessage) throws JsonProcessingException {
        if ("DEL".equals(kafkaMessage.getOperation())) {
            dishService.clearAll();
        } else if ("POST".equals(kafkaMessage.getOperation())) {
            DishCreateDTO dishDTO = objectMapper.convertValue(
                    kafkaMessage.getPayload(),
                    DishCreateDTO.class
            );
            dishService.addDish(
                    new Dish(
                            dishDTO.getName(),
                            dishDTO.getPrice(),
                            dishDTO.getWeight(),
                            dishDTO.getRestaurantId())
            );
        }
    }
    private void handleOrder(KafkaMessage kafkaMessage) throws JsonProcessingException {
        if ("DEL".equals(kafkaMessage.getOperation()))
        {
            orderService.clearAll();
        }
        else if ("POST".equals(kafkaMessage.getOperation()))
        {
            OrderCreateDTO orderDTO = objectMapper.convertValue(
                    kafkaMessage.getPayload(),
                    OrderCreateDTO.class
            );

            orderService.createOrder(
                    restaurantService.getRestaurantByUUID(orderDTO.getRestaurant()),
                    dishService.getDishesByIds(orderDTO.getDishes())
                    ,orderDTO.getDeliveryAddress());
        }
    }

    private void handleRestaurant(KafkaMessage message) throws JsonProcessingException {
        if ("DEL".equals(message.getOperation()))
        {
            restaurantService.clearAll();
        }
        else if ("POST".equals(message.getOperation()))
        {
            RestaurantCreateDTO restaurant = objectMapper.convertValue(
                    message.getPayload(),
                    RestaurantCreateDTO.class
            );
            restaurantService.addRestaurant(
                    new Restaurant(restaurant.getName(),
                    restaurant.getCuisine(),
                    restaurant.getMinimumOrder()));
        }
    }
}
