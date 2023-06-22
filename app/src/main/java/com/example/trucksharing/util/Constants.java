package com.example.trucksharing.util;

import com.google.android.gms.wallet.WalletConstants;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Constants {

        /**
         * Setting this to ENVIRONMENT_PRODUCTION enables the API to return chargeable
         * card details.
         * Consult the documentation for the necessary steps to activate
         * ENVIRONMENT_PRODUCTION.
         *
         * @value #PAYMENTS_ENVIRONMENT
         */
        public static final int PAYMENTS_ENVIRONMENT = WalletConstants.ENVIRONMENT_TEST;

        /**
         * Specifies the card networks that can be fetched from the API. Cards from
         * networks not
         * included here will not be available for selection in the popup.
         *
         * @value #SUPPORTED_NETWORKS
         */
        public static final List<String> SUPPORTED_NETWORKS = Arrays.asList(
                        "AMEX",
                        "DISCOVER",
                        "JCB",
                        "MASTERCARD",
                        "VISA");

        /**
         * Google Pay API may return stored cards on Google.com (PAN_ONLY) and/or a
         * device token on
         * an Android device secured with a 3-D Secure cryptogram (CRYPTOGRAM_3DS).
         *
         * @value #SUPPORTED_METHODS
         */
        public static final List<String> SUPPORTED_METHODS = Arrays.asList(
                        "PAN_ONLY",
                        "CRYPTOGRAM_3DS");

        /**
         * Needed by the API, but not shown to the user.
         *
         * @value #COUNTRY_CODE This is your local country
         */
        public static final String COUNTRY_CODE = "US";

        /**
         * Required by the API, but not displayed to the user.
         *
         * @value #CURRENCY_CODE This is your local currency
         */
        public static final String CURRENCY_CODE = "USD";

        /**
         * Countries supported for shipping (use ISO 3166-1 alpha-2 country codes). This
         * is important only when
         * a shipping address is requested.
         *
         * @value #SHIPPING_SUPPORTED_COUNTRIES
         */
        public static final List<String> SHIPPING_SUPPORTED_COUNTRIES = Arrays.asList("US", "GB");

        /**
         * The name of your chosen payment processor/gateway. For more information,
         * consult their documentation.
         *
         * @value #PAYMENT_GATEWAY_TOKENIZATION_NAME
         */
        public static final String PAYMENT_GATEWAY_TOKENIZATION_NAME = "example";

        /**
         * Custom parameters needed by the processor/gateway.
         * Often, your processor/gateway will just need a gatewayMerchantId.
         * Refer to your processor's documentation for details. The required parameters
         * and their names depend on the processor.
         *
         * @value #PAYMENT_GATEWAY_TOKENIZATION_PARAMETERS
         */
        public static final HashMap<String, String> PAYMENT_GATEWAY_TOKENIZATION_PARAMETERS = new HashMap<String, String>() {
                {
                        put("gateway", PAYMENT_GATEWAY_TOKENIZATION_NAME);
                        put("gatewayMerchantId", "exampleGatewayMerchantId");
                        // Additional parameters may be required by your processor.
                }
        };

        /**
         * Only applicable for {@code DIRECT} tokenization. Can be omitted when using
         * {@code PAYMENT_GATEWAY}
         * tokenization.
         *
         * @value #DIRECT_TOKENIZATION_PUBLIC_KEY
         */
        public static final String DIRECT_TOKENIZATION_PUBLIC_KEY = "REPLACE_ME";

        /**
         * Parameters required for {@code DIRECT} tokenization.
         * Only applicable for {@code DIRECT} tokenization. Can be omitted when using
         * {@code PAYMENT_GATEWAY}
         * tokenization.
         *
         * @value #DIRECT_TOKENIZATION_PARAMETERS
         */
        public static final HashMap<String, String> DIRECT_TOKENIZATION_PARAMETERS = new HashMap<String, String>() {
                {
                        put("protocolVersion", "ECv2");
                        put("publicKey", DIRECT_TOKENIZATION_PUBLIC_KEY);
                }
        };
}
