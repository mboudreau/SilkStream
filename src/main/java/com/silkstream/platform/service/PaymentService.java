package com.silkstream.platform.service;


import com.silkstream.platform.enums.EnvironmentType;
import com.silkstream.platform.enums.PaymentType;
import com.silkstream.platform.models.db.Payment;
import com.silkstream.platform.models.db.Tivity;
import com.silkstream.platform.models.db.User;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import urn.ebay.api.PayPalAPI.*;
import urn.ebay.apis.CoreComponentTypes.BasicAmountType;
import urn.ebay.apis.eBLBaseComponents.*;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service("paymentService")
public class PaymentService extends BasicService {

	private String dwollaId = "812-686-5197";
	PayPalAPIInterfaceServiceService service;
	String paypalUrl;
	@Inject
	private TivityService tivityService;

	public PaymentService() {
	}

	@PostConstruct
	public void init() {
		try {
			String configPath = "paypal/" + (properties.getEnvironment() == EnvironmentType.PROD ? "prod" : "dev") + ".properties";
			service = new PayPalAPIInterfaceServiceService(new ClassPathResource(configPath).getFile());
			paypalUrl = properties.getEnvironment() == EnvironmentType.PROD ? "https://www.paypal.com/cgi-bin/webscr" : "https://www.sandbox.paypal.com/cgi-bin/webscr";
		} catch (Exception e) {

		}
	}

	public void clobber(Payment payment) {
		if (payment != null) {
			mapper.clobber(payment);
		}
	}

	public void save(Payment payment) {
		if (payment != null) {
			mapper.save(payment);
		}
	}

	/*public void add(String tivityId, String userId, PaymentType type, Float total, HashMap<String, String> data) {
		if (payment != null) {
			payment.setId(createId());
			payment.setCreatedDate(System.currentTimeMillis());
			mapper.clobber(payment);
			//indexTivity(silkstream, place, user);
		}
	}*/

	public Payment get(String id) {
		return get(id, null);
	}

	public Payment get(String id, List<String> attributesToGet) {
		if (id != null) {
			return mapper.load(Payment.class, id, attributesToGet);
		}
		return null;
	}

	public String requestPaypalPayment(Tivity tivity, User user) {
		try {
			if (tivity != null) {
				String url = properties.getDomainUrl() + "/silkstream/" + tivity.getId();
				SetExpressCheckoutReq checkoutRequest = new SetExpressCheckoutReq();
				SetExpressCheckoutRequestType checkoutRequestType = new SetExpressCheckoutRequestType();
				checkoutRequestType.setVersion("95.0");
				SetExpressCheckoutRequestDetailsType details = new SetExpressCheckoutRequestDetailsType();
				details.setReturnURL(properties.getApiUrl() + "/payment/paypal/confirm/" + tivity.getId());
				details.setCancelURL(url);
				//details.setOrderDescription(silkstream.getActivity() + " - " + silkstream.getName());
				ObjectMapper objectMapper = new ObjectMapper();
				HashMap<String, String> customData = new HashMap<String, String>();
				customData.put("tivityId", tivity.getId());
				customData.put("userId", user.getId());
				//objectMapper.writeValueAsString(customData)
				details.setCustom(tivity.getId()+","+user.getId()); // setting user id as custom number to know which user has paid
				details.setNoShipping("1");
				details.setPaymentAction(PaymentActionCodeType.SALE);
				details.setTotalType(TotalType.TOTAL);
				BasicAmountType amount = new BasicAmountType(CurrencyCodeType.USD, tivity.getCost().toString());
				details.setOrderTotal(amount);
				PaymentDetailsType paymentDetails = new PaymentDetailsType();
				paymentDetails.setAllowedPaymentMethod(AllowedPaymentMethodType.INSTANTFUNDINGSOURCE);
				paymentDetails.setOrderURL(url);
				paymentDetails.setOrderDescription(tivity.getActivity() + " - " + tivity.getName());
				details.setPaymentDetails(Arrays.asList(paymentDetails));
				checkoutRequestType.setSetExpressCheckoutRequestDetails(details);
				checkoutRequest.setSetExpressCheckoutRequest(checkoutRequestType);
				SetExpressCheckoutResponseType expressCheckoutResponse = service.setExpressCheckout(checkoutRequest);

				if (expressCheckoutResponse.getAck() == AckCodeType.SUCCESS) {
					// After receiving token, redirect to paypal
					return paypalUrl + "?cmd=_express-checkout&useraction=commit&token=" + expressCheckoutResponse.getToken();
				} else {
					return null;
				}

				/*service.doExpressCheckoutPayment(request);
							 service.getExpressCheckoutDetails();*/
			}
		} catch (Exception e) {

		}

		return null;
	}

	public Payment finalizePaypalPayment(String token, String payerId) {
		try {
			// Get information back from Paypal
			GetExpressCheckoutDetailsReq getExpressCheckoutDetailsReq = new GetExpressCheckoutDetailsReq();
			GetExpressCheckoutDetailsRequestType getExpressCheckoutDetailsRequestType = new GetExpressCheckoutDetailsRequestType(token);
			getExpressCheckoutDetailsRequestType.setVersion("95.0");
			getExpressCheckoutDetailsReq.setGetExpressCheckoutDetailsRequest(getExpressCheckoutDetailsRequestType);
			GetExpressCheckoutDetailsResponseType getCheckoutResponse = service.getExpressCheckoutDetails(getExpressCheckoutDetailsReq);

			if (getCheckoutResponse.getAck() == AckCodeType.SUCCESS) {
				// Get information back from Paypal
				DoExpressCheckoutPaymentReq request = new DoExpressCheckoutPaymentReq();
				DoExpressCheckoutPaymentRequestType type = new DoExpressCheckoutPaymentRequestType();
				type.setVersion("95.0");
				DoExpressCheckoutPaymentRequestDetailsType details = new DoExpressCheckoutPaymentRequestDetailsType();
				details.setPaymentAction(PaymentActionCodeType.SALE);
				details.setPayerID(payerId);
				details.setToken(token);
				PaymentDetailsType detailsType = new PaymentDetailsType();
				//detailsType.setItemTotal(getCheckoutResponse.getGetExpressCheckoutDetailsResponseDetails().getPaymentDetails().get(0).getOrderTotal());
				detailsType.setOrderTotal(getCheckoutResponse.getGetExpressCheckoutDetailsResponseDetails().getPaymentDetails().get(0).getOrderTotal());
				details.setPaymentDetails(Arrays.asList(detailsType));
				type.setDoExpressCheckoutPaymentRequestDetails(details);
				request.setDoExpressCheckoutPaymentRequest(type);
				DoExpressCheckoutPaymentResponseType response = service.doExpressCheckoutPayment(request);

				if (response.getAck() == AckCodeType.SUCCESS) {
					// save payment information in DB
					ObjectMapper objectMapper = new ObjectMapper();
					//JsonNode node = objectMapper.convertValue(getCheckoutResponse.getGetExpressCheckoutDetailsResponseDetails().getCustom(), JsonNode.class);
					Payment payment = new Payment();
					payment.setId(createId());
					//node.get("tivityId").getTextValue()   node.get("userId").getTextValue()
					String[] custom = getCheckoutResponse.getGetExpressCheckoutDetailsResponseDetails().getCustom().split(",");
					payment.setTivityId(custom[0]);
					payment.setUserId(custom[1]);
					payment.setType(PaymentType.PAYPAL);
					payment.setTotal(Float.parseFloat(getCheckoutResponse.getGetExpressCheckoutDetailsResponseDetails().getPaymentDetails().get(0).getOrderTotal().getValue()));
					payment.setCreatedDate(System.currentTimeMillis());
					HashMap<String, String> data = new HashMap<String, String>();
					//data.put("getExpressCheckoutResponse", objectMapper.writeValueAsString(getCheckoutResponse));
					//data.put("doExpressCheckoutResponse", objectMapper.writeValueAsString(response));
					data.put("correlationId", getCheckoutResponse.getCorrelationID());
					data.put("token", getCheckoutResponse.getGetExpressCheckoutDetailsResponseDetails().getToken());
					data.put("checkoutStatus", getCheckoutResponse.getGetExpressCheckoutDetailsResponseDetails().getCheckoutStatus());
					data.put("invoiceId", getCheckoutResponse.getGetExpressCheckoutDetailsResponseDetails().getInvoiceID());
					data.put("note", getCheckoutResponse.getGetExpressCheckoutDetailsResponseDetails().getNote());
					data.put("payerId", getCheckoutResponse.getGetExpressCheckoutDetailsResponseDetails().getPayerInfo().getPayerID());
					data.put("payer", getCheckoutResponse.getGetExpressCheckoutDetailsResponseDetails().getPayerInfo().getPayer());
					data.put("contactPhone", getCheckoutResponse.getGetExpressCheckoutDetailsResponseDetails().getPayerInfo().getContactPhone());
					data.put("payerName", getCheckoutResponse.getGetExpressCheckoutDetailsResponseDetails().getPayerInfo().getPayerName().getFirstName() +" "+ getCheckoutResponse.getGetExpressCheckoutDetailsResponseDetails().getPayerInfo().getPayerName().getLastName());
					data.put("payerCountry", getCheckoutResponse.getGetExpressCheckoutDetailsResponseDetails().getPayerInfo().getPayerCountry().getValue());
					data.put("payerStatus", getCheckoutResponse.getGetExpressCheckoutDetailsResponseDetails().getPayerInfo().getPayerStatus().getValue());
					data.put("transactionId", getCheckoutResponse.getGetExpressCheckoutDetailsResponseDetails().getPaymentDetails().get(0).getTransactionId());
					data.put("orderTotal", getCheckoutResponse.getGetExpressCheckoutDetailsResponseDetails().getPaymentDetails().get(0).getOrderTotal().getValue());
					data.put("currencyId", getCheckoutResponse.getGetExpressCheckoutDetailsResponseDetails().getPaymentDetails().get(0).getOrderTotal().getCurrencyID().getValue());
					data.put("paymentDate", response.getDoExpressCheckoutPaymentResponseDetails().getPaymentInfo().get(0).getPaymentDate());
					data.put("receiptId", response.getDoExpressCheckoutPaymentResponseDetails().getPaymentInfo().get(0).getReceiptID());
					payment.setData(data);

					save(payment);

					return payment;
				}
			}

		} catch (Exception e) {

		}
		return null;
	}
}
