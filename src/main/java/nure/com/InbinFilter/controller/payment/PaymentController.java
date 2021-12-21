package nure.com.InbinFilter.controller.payment;


import com.stripe.model.Charge;
import io.swagger.annotations.Api;
import nure.com.InbinFilter.exeption.CustomException;
import nure.com.InbinFilter.models.user.Resident;
import nure.com.InbinFilter.models.user.User;
import nure.com.InbinFilter.repository.resident.ResidentRepository;
import nure.com.InbinFilter.security.service.UserServiceSCRT;
import nure.com.InbinFilter.service.payment.client.StripeClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/payment")
@Api(value = "Operations with exercises")
@CrossOrigin(origins = {"http://localhost:3000", "http://someserver:8000"},
        methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.POST},
        allowCredentials = "true", maxAge = 3600, allowedHeaders = "*")
public class PaymentController {


    private final UserServiceSCRT userServiceSCRT;
    private final ResidentRepository residentRepository;

    private StripeClient stripeClient;

    @Autowired
    PaymentController(UserServiceSCRT userServiceSCRT, ResidentRepository residentRepository, StripeClient stripeClient) {
        this.userServiceSCRT = userServiceSCRT;
        this.residentRepository = residentRepository;
        this.stripeClient = stripeClient;
    }

    @PostMapping("/charge")
    public Charge chargeCard(@RequestHeader(value="token") String token, @RequestHeader(value="amount") Double amount) throws Exception {
        User user = userServiceSCRT.getCurrentLoggedInUser();
        Optional<Resident> resident =  residentRepository.findResidentByUser(user.getId());
        if(resident.isPresent()){
            Resident residentDb = resident.get();

            Charge charge = this.stripeClient.chargeNewCard(token, amount);
            if(charge !=null){
                residentDb.setBill(0.0);
            }
            residentRepository.save(residentDb);
            return charge;
        }
       throw  new CustomException("Payment was failed", HttpStatus.BAD_REQUEST);
    }
}
