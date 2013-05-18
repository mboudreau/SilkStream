package com.silkstream.platform.web;

import com.silkstream.platform.enums.RSVPType;
import com.silkstream.platform.models.db.Payment;
import com.silkstream.platform.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/api/payment", produces = "application/json")
public class PaymentController extends BasicController {

	@Inject
	private PaymentService paymentService;
	@Inject
	private TivityController tivityController;

	/*@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "dwolla", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public void dwolla(@RequestBody Object data) {
	}*/

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "paypal/confirm/{id}", method = RequestMethod.GET)
	@ResponseBody
	public void paypalConfirmPayment(@PathVariable("id") String tivityId, @RequestParam(value = "token") String token, @RequestParam(value = "PayerID") String payerId, HttpServletResponse response) {
		try {
			// finalize order with paypal
			Payment payment = paymentService.finalizePaypalPayment(token, payerId);
			if(payment != null) {
				// save RSVP information in Tivity
				RSVPType confirmation = tivityController.confirmRSVP(tivityId, "yes", payment.getUserId());
				response.sendRedirect(properties.getDomainUrl() + "/silkstream/" + tivityId + "?paid=true");
			}
		} catch (Exception e) {

		}
	}
}