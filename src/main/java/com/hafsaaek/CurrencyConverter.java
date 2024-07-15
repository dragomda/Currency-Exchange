package com.hafsaaek;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

public class CurrencyConverter {
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public static final String API_CURRENCY_TOKEN = "api.currency.token";

    private static String API_Key = System.getProperty(API_CURRENCY_TOKEN);


    private final static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule());
    }

    public String getAPI_Key() {
        return Objects.requireNonNull(API_Key, String.format("Please set the Currency API key using: -D%s=<key>", API_CURRENCY_TOKEN));
    }

    public void setAPI_Key(String API_Key) {
        // this.API_Key = API_Key; // this would be OK, if it was an instance variable but API Key is a class variable so it belongs to the class not an instance of the class
        CurrencyConverter.API_Key = API_Key; // class variable 
    }


    public static void main(String[] args) {
        boolean programRunning = true;
        do {
            // Create a Hashmap to map currency codes to currency
            HashMap<Integer, String> currencyCodeMap = new HashMap<>();

            // Add currency codes to the HashMap with integers 1-10 as keys
            currencyCodeMap.put(1, "GBP");
            currencyCodeMap.put(2, "USD");
            currencyCodeMap.put(3, "KSH");
            currencyCodeMap.put(4, "EUR");
            currencyCodeMap.put(5, "JPY");

            String fromCode, toCode; // currency codes we want to convert from and to
            double amount; // amount we want to convert 
            int from;
            int to; // integer values from the scanner

            Scanner sc = new Scanner(System.in); // sc is an object initialized to read user input from console
            System.out.println("Welcome to the currency converter ");

            System.out.println("What currency are you converting from? Please select an integer from the below:");
            System.out.println("1: GBP \t 2: USD \t 3: KSH \t 4: EUR \t 5: JPY"); // \t introduces tab
            System.out.print(">> ");
            from = sc.nextInt(); // reads the next integer input from the user and fromCode is assigned to the HashMap value associated with the integer input

            // create a while loop to re-prompt users to enter an integer value between 1&5 for fromCode if it's not between 1 & 5
            while (from < 1 || from > 5) {
                System.out.println("You have selected an in valid integer value, Please select an integer between 1 and 5 from the currencies below");
                System.out.println("1: GBP \t 2: USD \t 3: KSH \t 4: EUR \t 5: JPY");
                from = sc.nextInt();
            }
            fromCode = currencyCodeMap.get(from); // stores the retrieved currency code of the variable from

            System.out.println("What currency are you converting to? Please select an integer from the below:");
            System.out.println("1: GBP \t 2: USD \t 3: KSH \t 4: EUR \t 5: JPY"); // \t introduces tab
            System.out.print(">> ");
            to = sc.nextInt();
            while (to < 1 || to > 5) {
                System.out.println("You have selected an in valid integer value. Please select an integer between 1 and 5 from the currencies below");
                System.out.println("1: GBP \t 2: USD \t 3: KSH \t 4: EUR \t 5: JPY");
                to = sc.nextInt();
            }
            toCode = currencyCodeMap.get(to); // stores the retrieved currency code from scanner

            System.out.println("Please enter the amount you wish to convert:");
            System.out.print(">> ");
            amount = sc.nextFloat();

            // Now, how do we get the exchange rate? Use an API that will provide this in real time using HTTP
            sendHTTPGETRequest(fromCode, toCode, amount); // to be called later

            System.out.println("Would you like to continue and make another conversion?");
            System.out.println("1: Yes \t Any other integer: No");
            System.out.print(">> ");
            if (sc.nextInt() != 1) {
                programRunning = false;
            }

        } while (programRunning);

        System.out.println("Thank you for using the Currency Converter program!"); // when the conversion has completed
    }

    public static void sendHTTPGETRequest(String fromCode, String toCode, double amount) {

        // https://api.currencyapi.com/v3/convert?apikey=YOUR_API_KEY&base_currency=BASE_CURRENCY&target_currency=TARGET_CURRENCY&value=AMOUNT
        String getURL = "https://api.currencyapi.com/v3/latest?apikey=" + API_Key + "&base_currency=" + fromCode + "&target_currency=" + toCode + "&value=" + amount;
        // Test for selecting USD --> GBP
        System.out.println(getURL); // https://api.currencyapi.com/v3/latest?apikey=&base_currency=USD&target_currency=GBP&amount=20.0

        try {
            URI uri = new URI(getURL); // make the get URL an actual URI - URL is not supported
            HttpURLConnection httpURLConnection = (HttpURLConnection) uri.toURL().openConnection(); // setup and open the HTTP connection 
            httpURLConnection.setRequestMethod("GET"); // set the request method as GET
            int responseCode = httpURLConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream())); // if get request is successful, we want to read the response i.e. currency conversion

                String inputLine;
                StringBuilder response = new StringBuilder(); // a string that will parse the response form the request

                // While there is stuff to read --> keep reading and add to string Buffer
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close(); // close resource

                CcyConversionResult result = objectMapper.readValue(response.toString(), CcyConversionResult.class);
                Double exchangeRate = result.getData().get(toCode).value;
                System.out.printf("%s/%s - exchange rate: %f%n", fromCode, toCode, exchangeRate);

                // Final output with all doubles to 2 decimal places 
                System.out.println(df.format(amount) + fromCode + " = " + df.format((amount * exchangeRate)) + toCode);
            } else {
                System.out.println("Get request failed with: " + responseCode);
            }
        } catch (URISyntaxException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


}