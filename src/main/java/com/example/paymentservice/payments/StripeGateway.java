package com.example.paymentservice.payments;

import com.example.paymentservice.models.order.Order;
import com.example.paymentservice.models.order.OrderItems;
import com.example.paymentservice.services.client.OrderFeignClient;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentLink;
import com.stripe.model.Price;
import com.stripe.param.PaymentLinkCreateParams;
import com.stripe.param.PriceCreateParams;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.stripe.model.Price.create;

@Component()
@Qualifier("stripe")
public class StripeGateway implements PaymentGateway{

    @Value("${spring.stripe.secret}")
    private String stripeSecret;

    @Override
    public String generatePaymentLink(Order order, Jwt jwt) throws StripeException {
        Stripe.apiKey = stripeSecret;

        if (order == null) {return "Order Not Found";}
        List<OrderItems> orderItems = order.getOrderItemsList();
        List<PriceCreateParams> priceCreateParams = new ArrayList<>();
        List<PaymentLinkCreateParams.LineItem> lineItems = new ArrayList<>();
        orderItems.forEach(orderItems1 -> {

        PriceCreateParams priceParams =
                PriceCreateParams.builder()
                        .setCurrency("INR")
                        .setUnitAmount((long)orderItems1.getPrice()*100) // 10.00
                        .setProductData(
                                PriceCreateParams.ProductData.builder().setName(orderItems1.getName()).build()
                        )
                        .build();
        priceCreateParams.add(priceParams);
            try {
                lineItems.add( PaymentLinkCreateParams.LineItem.builder()
                        .setPrice(Price.create(priceParams).getId())
                        .setQuantity((long) orderItems1.getQuantity())
                        .build());
            } catch (StripeException e) {
                throw new RuntimeException(e);
            }
        });

        Map<String,String> metadata = new HashMap<>();
        metadata.put("orderId",order.getId().toString());
        metadata.put("userId",order.getUserId().toString());


        PaymentLinkCreateParams params =
                PaymentLinkCreateParams.builder()
                        .addAllLineItem(
                                lineItems
                        )
                        .putAllMetadata(metadata)
                        .setAfterCompletion(
                                PaymentLinkCreateParams.AfterCompletion.builder()
                                        .setRedirect(
                                                PaymentLinkCreateParams.AfterCompletion.Redirect.builder()
                                                        .setUrl("http://www.google.com")
                                                        .build()
                                        )
                                        .setType(PaymentLinkCreateParams.AfterCompletion.Type.REDIRECT)
                                        .build()
                        )
                        .build();
        PaymentLink paymentLink = PaymentLink.create(params);
        return paymentLink.toString();
    }
}
