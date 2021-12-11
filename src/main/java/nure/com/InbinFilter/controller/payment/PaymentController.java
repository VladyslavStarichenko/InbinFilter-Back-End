package nure.com.InbinFilter.controller.payment;


import com.stripe.model.Charge;
import io.swagger.annotations.Api;
import nure.com.InbinFilter.service.payment.client.StripeClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@Api(value = "Operations with exercises")
@CrossOrigin(origins = {"http://localhost:3000", "http://someserver:8000"},
        methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.POST},
        allowCredentials = "true", maxAge = 3600, allowedHeaders = "*")
public class PaymentController {

    private StripeClient stripeClient;

    @Autowired
    PaymentController(StripeClient stripeClient) {
        this.stripeClient = stripeClient;
    }

    @PostMapping("/charge")
    public Charge chargeCard(@RequestHeader(value="token") String token, @RequestHeader(value="amount") Double amount) throws Exception {
        return this.stripeClient.chargeNewCard(token, amount);
    }
}
