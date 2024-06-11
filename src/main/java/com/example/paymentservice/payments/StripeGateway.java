package com.example.paymentservice.payments;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentLink;
import com.stripe.model.Price;
import com.stripe.param.PaymentLinkCreateParams;
import com.stripe.param.PriceCreateParams;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component()
@Qualifier("stripe")
public class StripeGateway implements PaymentGateway{

    @Value("${spring.stripe.secret}")
    private String stripeSecret;

    @Override
    public String generatePaymentLink(Long orderId, Long amount) throws StripeException {
        Stripe.apiKey = stripeSecret;

        PriceCreateParams priceParams =
                PriceCreateParams.builder()
                        .setCurrency("INR")
                        .setUnitAmount(amount*100) // 10.00
                        .setProductData(
                                PriceCreateParams.ProductData.builder().setName("Pant").build()
                        )
                        .build();


        Price price = Price.create(priceParams);

        PaymentLinkCreateParams params =
                PaymentLinkCreateParams.builder()
                        .addLineItem(
                                PaymentLinkCreateParams.LineItem.builder()
                                        .setPrice(price.getId())
                                        .setQuantity(1L)
                                        .build()
                        ).setAfterCompletion(
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
