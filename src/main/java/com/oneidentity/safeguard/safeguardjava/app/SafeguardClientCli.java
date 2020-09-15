package com.oneidentity.safeguard.safeguardjava.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.oneidentity.safeguard.safeguardjava.Safeguard;
import com.oneidentity.safeguard.safeguardjava.event.ISafeguardEventHandler;
import com.oneidentity.safeguard.safeguardjava.event.ISafeguardEventListener;

public class SafeguardClientCli {

    public static void main(String[] args) {
               
        SafeguardOptions options = SafeguardOptions.parseArguments(args);
        

        try {

            // To use a truststore for ssl validation, either the following property must be set to the file path of the 
            //  truststore that contains the server public certificate or the public certificate must be added to the
            //  default java truststore.  Otherwise all calls must set the last parameter on the Connect API to ignore
            //  the SSL certificate.
            if(options.getTrustStorePath() != null) {
                System.setProperty("javax.net.ssl.trustStore", options.getTrustStorePath());
                
            }
            if(options.getTrustStorePassword() != null){
                System.setProperty("javax.net.ssl.trustStorePassword", options.getTrustStorePassword());
            }

            // To use a keystore for certificate user authentication, either the following property must be set to the file path of the 
            //  keystore that contains the certificate and private key or the certificate and private key must be added added to the
            //  default java keystore.
            if(options.getKeyStorePath() != null) {
                System.setProperty("javax.net.ssl.keyStore", options.getKeyStorePath());
            }

            if(options.getCommand().equalsIgnoreCase("basic")) {
                runBasicTest(options);
            } else if(options.getCommand().equalsIgnoreCase("clientcert")) {
                runClientCertTest(options);
            } else if(options.getCommand().equalsIgnoreCase("a2a")) {
                runA2ATest(options);
            } else {
                runHelp(options);
            }
        } catch (Throwable ex) {
            Logger.getLogger(SafeguardClientCli.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    private static void runBasicTest(SafeguardOptions options) throws Throwable {

        ISafeguardEventListener eventListener = Safeguard.Event.getPersistentEventListener(
            options.getNetworkAddress(), 
            "local", 
            options.getSafeguardUser(), 
            options.getSafeguardUserPassword().toCharArray(), 
            options.getApiVersion(), 
            options.ignoreSslErrors());
        
        listenForEvents(eventListener);
    }

    private static void runClientCertTest(SafeguardOptions options) throws Throwable {

        ISafeguardEventListener eventListener = Safeguard.Event.getPersistentEventListener(
            options.getNetworkAddress(),
            options.getClientCertificatePath(),
            options.getClientCertificatePassword().toCharArray(),
            options.getApiVersion(), 
            options.ignoreSslErrors());
        
        listenForEvents(eventListener);
    }

    private static void listenForEvents(ISafeguardEventListener eventListener) throws Throwable {
        eventListener.registerEventHandler("UserCreated", new ISafeguardEventHandler() {
            @Override
            public void onEventReceived(String eventName, String eventBody) {
                System.out.println("Got the UserCreated event");
            }
        });

        eventListener.registerEventHandler("UserDeleted", new ISafeguardEventHandler() {
            @Override
            public void onEventReceived(String eventName, String eventBody) {
                System.out.println("Got the UserDeleted event");
            }
        });
        eventListener.start();       
        readLine("Press enter to stop...");
        eventListener.stop();
    }

    private static void runA2ATest(SafeguardOptions options) throws Throwable {

        ISafeguardEventListener eventListener = Safeguard.A2A.Event.getPersistentA2AEventListener(
            options.getA2AApiKey().toCharArray(),
               new ISafeguardEventHandler() {
                   @Override
                   public void onEventReceived(String eventName, String eventBody) {
                       System.out.println("Got the A2A event");
                   }
               }, 
               options.getNetworkAddress(), 
               options.getClientCertificatePath(),
               options.getClientCertificatePassword().toCharArray(), 
               options.getApiVersion(), options.ignoreSslErrors());

        listenForEvents(eventListener);
    }

    private static void runHelp(SafeguardOptions options) {
        SafeguardOptions.printAppHelp();
    }
    

    public static String toJsonString(String name, Object value, boolean prependSep) {
        if (value != null) {
            return (prependSep ? ", " : "") + "\"" + name + "\" : " + (value instanceof String ? "\"" + value.toString() + "\"" : value.toString());
        }
        return "";
    }

    public static String readLine(String format, Object... args) throws IOException {
        if (System.console() != null) {
            return System.console().readLine(format, args);
        }
        System.out.print(String.format(format, args));
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                System.in));
        return reader.readLine();
    }
}
