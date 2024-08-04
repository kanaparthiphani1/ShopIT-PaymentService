package com.example.paymentservice.controllers;

import com.example.paymentservice.services.PaymentService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionListParams;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/stripe")
public class WebhookController {

    private final PaymentService paymentService;
    public WebhookController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @RequestMapping(value = "/webhook", method = RequestMethod.POST)
    public ResponseEntity<String> webhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        Event event = null;
        try {
            event = Webhook.constructEvent(payload, sigHeader, "whsec_4cb4a61852ee07d0b76062596659d1e24e92c6f56be282757eaf0bd03ab1a254");
        } catch (SignatureVerificationException e) {
            System.out.println("Failed signature verification");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;

        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        } else {
            // Deserialization failed, probably due to an API version mismatch.
            // Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
            // instructions on how to handle this case, or return an error here.
        }

        switch (event.getType()) {
            case "checkout.session.completed":
                // ...
                System.out.println("stripe obj: "+stripeObject.toString());
//                System.out.println("stripe obj2: "+stripeObject.getRawJsonObject().get("metadata"));
                Session session = (Session) stripeObject;
                SessionListParams params = SessionListParams.builder().build();
                Map<String,String> met = session.getMetadata();
                String orderId =  met.get("orderId");

                paymentService.afterSuccess(Long.parseLong(orderId));

                break;

            default:
                // Unexpected event type
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @RequestMapping(value = "/hi", method = RequestMethod.POST)
    public String hi(){
        return "hi";
    }
}
