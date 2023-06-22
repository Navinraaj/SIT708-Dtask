package com.example.trucksharing.util;

import android.content.Context;
import com.google.android.gms.wallet.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Includes static helper methods for working with the Payments API.
 *
 * <p>
 * Many parameters used here are optional and are set to showcase their
 * existence.
 * Please check the documentation to understand more and feel free to delete
 * irrelevant ones for your implementation.
 */
public class PaymentHelper {

    public static final BigDecimal PENNIES_PER_DOLLAR = new BigDecimal(100);

    /**
     * Builds a basic Google Pay API request object with properties common to all
     * requests.
     *
     * @return Base Google Pay API request object.
     * @throws JSONException if the object formation goes wrong.
     */
    private static JSONObject baseRequestBuilder() throws JSONException {
        return new JSONObject().put("apiVersion", 2).put("apiVersionMinor", 0);
    }

    /**
     * Constructs a {@link PaymentsClient} instance for use in a {@link Context},
     * using the
     * environment and theme defined in {@link Constants}.
     *
     * @param context context of the caller.
     */
    public static PaymentsClient initializePaymentsClient(Context context) {
        Wallet.WalletOptions walletOptions = new Wallet.WalletOptions.Builder()
                .setEnvironment(Constants.PAYMENTS_ENVIRONMENT).build();
        return Wallet.getPaymentsClient(context, walletOptions);
    }

    /**
     * Gateway Integration: Provide identification for your gateway and your app's
     * gateway merchant identifier.
     *
     * <p>
     * The Google Pay API response will include an encrypted payment method capable
     * of being charged
     * by a supported gateway after payer authorization.
     *
     * <p>
     * TODO: Confirm with your gateway about the parameters to pass and modify them
     * in Constants.java.
     *
     * @return Payment data tokenization for the CARD payment method.
     * @throws JSONException if the object is malformed.
     * @see <a href=
     *      "https://developers.google.com/pay/api/android/reference/object#PaymentMethodTokenizationSpecification">PaymentMethodTokenizationSpecification</a>
     */
    private static JSONObject buildGatewayTokenizationSpecification() throws JSONException {
        return new JSONObject() {
            {
                put("type", "PAYMENT_GATEWAY");
                put("parameters", new JSONObject() {
                    {
                        put("gateway", "example");
                        put("gatewayMerchantId", "exampleGatewayMerchantId");
                    }
                });
            }
        };
    }

    /**
     * {@code DIRECT} Integration: Decrypt a response directly on your servers. This
     * configuration demands
     * higher data security requirements from Google and additional PCI DSS
     * compliance complexity.
     *
     * <p>
     * Review the documentation for more information about {@code DIRECT}
     * integration. The
     * type of integration you choose depends on your payment processor.
     *
     * @return Payment data tokenization for the CARD payment method.
     * @throws JSONException if the object is malformed.
     * @see <a
     *      href=
     *      "https://developers.google.com/pay/api/android/reference/object#PaymentMethodTokenizationSpecification">PaymentMethodTokenizationSpecification</a>
     */
    private static JSONObject directTokenizationSpecificationBuilder()
            throws JSONException, RuntimeException {
        JSONObject tokenizationSpecification = new JSONObject();

        tokenizationSpecification.put("type", "DIRECT");
        JSONObject parameters = new JSONObject(Constants.DIRECT_TOKENIZATION_PARAMETERS);
        tokenizationSpecification.put("parameters", parameters);

        return tokenizationSpecification;
    }

    /**
     * Define card networks supported by your app and your gateway.
     *
     * <p>
     * TODO: Verify card networks supported by your app and gateway & update in
     * Constants.java.
     *
     * @return Allowed card networks
     * @see <a
     *      href=
     *      "https://developers.google.com/pay/api/android/reference/object#CardParameters">CardParameters</a>
     */
    private static JSONArray allowedCardNetworks() {
        return new JSONArray(Constants.SUPPORTED_NETWORKS);
    }

    /**
     * Define card authentication methods supported by your app and your gateway.
     *
     * <p>
     * TODO: Verify that your processor supports Android device tokens on your
     * supported card networks
     * and adjust in Constants.java.
     *
     * @return Allowed card authentication methods.
     * @see <a
     *      href=
     *      "https://developers.google.com/pay/api/android/reference/object#CardParameters">CardParameters</a>
     */
    private static JSONArray allowedCardAuthMethods() {
        return new JSONArray(Constants.SUPPORTED_METHODS);
    }

    /**
     * Describes your app's support for the CARD payment method.
     *
     * <p>
     * The properties given are relevant to both an IsReadyToPayRequest and a
     * PaymentDataRequest.
     *
     * @return A CARD PaymentMethod object describing the cards accepted.
     * @throws JSONException if the object is malformed.
     * @see <a
     *      href=
     *      "https://developers.google.com/pay/api/android/reference/object#PaymentMethod">PaymentMethod</a>
     */
    private static JSONObject basicCardPaymentMethod() throws JSONException {
        JSONObject cardPaymentMethod = new JSONObject();
        cardPaymentMethod.put("type", "CARD");

        JSONObject parameters = new JSONObject();
        parameters.put("allowedAuthMethods", allowedCardAuthMethods());
        parameters.put("allowedCardNetworks", allowedCardNetworks());
        // Optionally, you can include billing address/phone number associated with a
        // CARD payment method.
        parameters.put("billingAddressRequired", true);

        JSONObject billingAddressParameters = new JSONObject();
        billingAddressParameters.put("format", "FULL");

        parameters.put("billingAddressParameters", billingAddressParameters);

        cardPaymentMethod.put("parameters", parameters);

        return cardPaymentMethod;
    }

    /**
     * Describes the expected returned payment data for the CARD payment method
     *
     * @return A CARD PaymentMethod object describing accepted cards and optional
     *         fields.
     * @throws JSONException if the object is malformed.
     * @see <a
     *      href=
     *      "https://developers.google.com/pay/api/android/reference/object#PaymentMethod">PaymentMethod</a>
     */
    private static JSONObject cardPaymentMethod() throws JSONException {
        JSONObject cardPaymentMethod = basicCardPaymentMethod();
        cardPaymentMethod.put("tokenizationSpecification", buildGatewayTokenizationSpecification());

        return cardPaymentMethod;
    }

    /**
     * An object describing the accepted forms of payment by your app, used to
     * determine a user's
     * readiness to pay.
     *
     * @return API version and payment methods supported by the app.
     * @see <a
     *      href=
     *      "https://developers.google.com/pay/api/android/reference/object#IsReadyToPayRequest">IsReadyToPayRequest</a>
     */
    public static JSONObject readyToPayRequest() {
        try {
            JSONObject isReadyToPayRequest = baseRequestBuilder();
            isReadyToPayRequest.put(
                    "allowedPaymentMethods", new JSONArray().put(basicCardPaymentMethod()));

            return isReadyToPayRequest;

        } catch (JSONException e) {
            return null;
        }
    }

    /**
     * Provide Google Pay API with a payment amount, currency, and amount status.
     *
     * @return information about the requested payment.
     * @throws JSONException if the object is malformed.
     * @see <a
     *      href=
     *      "https://developers.google.com/pay/api/android/reference/object#TransactionInfo">TransactionInfo</a>
     */
    private static JSONObject transactionInfo(String price) throws JSONException {
        JSONObject transactionInfo = new JSONObject();
        transactionInfo.put("totalPrice", price);
        transactionInfo.put("totalPriceStatus", "FINAL");
        transactionInfo.put("countryCode", Constants.COUNTRY_CODE);
        transactionInfo.put("currencyCode", Constants.CURRENCY_CODE);
        transactionInfo.put("checkoutOption", "COMPLETE_IMMEDIATE_PURCHASE");

        return transactionInfo;
    }

    /**
     * Information about the merchant requesting payment information
     *
     * @return Information about the merchant.
     * @throws JSONException if the object is malformed.
     * @see <a
     *      href=
     *      "https://developers.google.com/pay/api/android/reference/object#MerchantInfo">MerchantInfo</a>
     */
    private static JSONObject merchantInfo() throws JSONException {
        return new JSONObject().put("merchantName", "Example Merchant");
    }

    /**
     * An object describing the requested information in a Google Pay payment sheet
     *
     * @return Payment data expected by your app.
     * @see <a
     *      href=
     *      "https://developers.google.com/pay/api/android/reference/object#PaymentDataRequest">PaymentDataRequest</a>
     */
    public static JSONObject paymentDataRequest(long priceCents) {

        final String price = convertCentsToString(priceCents);

        try {
            JSONObject paymentDataRequest = baseRequestBuilder();
            paymentDataRequest.put(
                    "allowedPaymentMethods", new JSONArray().put(cardPaymentMethod()));
            paymentDataRequest.put("transactionInfo", transactionInfo(price));
            paymentDataRequest.put("merchantInfo", merchantInfo());

            /*
             * An optional shipping address requirement is a top-level property of the
             * PaymentDataRequest
             * JSON object.
             */
            paymentDataRequest.put("shippingAddressRequired", true);

            JSONObject shippingAddressParameters = new JSONObject();
            shippingAddressParameters.put("phoneNumberRequired", false);

            JSONArray allowedCountryCodes = new JSONArray(Constants.SHIPPING_SUPPORTED_COUNTRIES);

            shippingAddressParameters.put("allowedCountryCodes", allowedCountryCodes);
            paymentDataRequest.put("shippingAddressParameters", shippingAddressParameters);
            return paymentDataRequest;

        } catch (JSONException e) {
            return null;
        }
    }

    /**
     * Converts cents to a string format accepted by
     * {@link PaymentsUtil#paymentDataRequest}.
     *
     * @param cents value of the price in cents.
     */
    public static String convertCentsToString(long cents) {
        return new BigDecimal(cents)
                .divide(CENTS_IN_A_UNIT, RoundingMode.HALF_EVEN)
                .setScale(2, RoundingMode.HALF_EVEN)
                .toString();
    }
}
