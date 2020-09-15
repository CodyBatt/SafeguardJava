package com.oneidentity.safeguard.safeguardjava.app;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

class SafeguardOptions {
    String networkAddress = null;
    public String getNetworkAddress() { return networkAddress; }
    String safeguardUser = null;
    public String getSafeguardUser() { return safeguardUser; }
    String safeguardUserPassword = null;
    public String getSafeguardUserPassword() { return safeguardUserPassword; }

    String trustStorePath = null;
    public String getTrustStorePath() { return trustStorePath; }
    String trustStorePassword = null;
    public String getTrustStorePassword() { return trustStorePassword; }
    String keyStorePath = null;
    public String getKeyStorePath() { return keyStorePath; }
    String clientCertificatePath = null;
    public String getClientCertificatePath() { return clientCertificatePath; }
    String clientCertificatePassword = null;
    public String getClientCertificatePassword() { return clientCertificatePassword; }


    boolean validateCertificates = false;
    public boolean validateCertificates() { return validateCertificates; }
    boolean ignoreSslErrors = false;
    public boolean ignoreSslErrors() { return ignoreSslErrors; }

    String a2aApiKey = null;
    public String getA2AApiKey() { return a2aApiKey; }

    String command = "help";
    public String getCommand() { return command; }

    int apiVersion = 3;
    public int getApiVersion() {
        return apiVersion;
    }

    private void setOptionValue(Option option, String value) {
        switch(option.getOpt()) {
            case "o": command = value; break;
            case "n": networkAddress = value; break;
            case "u": safeguardUser = value; break;
            case "p": safeguardUserPassword = value; break;
            case "t": trustStorePath = value; break;
            case "x": trustStorePassword = value; break;
            case "k": keyStorePath = value; break;
            case "c": clientCertificatePath = value; break;
            case "v": validateCertificates = true; break;
            case "i": ignoreSslErrors = true; break;
            case "z": apiVersion = Integer.parseInt(value); break;
            case "w": clientCertificatePassword = value; break;
            case "a": a2aApiKey = value; break;
        }
    }

    private static Options getOptions() {
        Options options = new Options();

        options.addRequiredOption("o", "command", true, 
            "Command to execute: Help, ClientCert, Basic");
        options.addRequiredOption("n", "networkAddress", true,
            "Safeguard appliance network address");            
        options.addRequiredOption("u", "user", true,
            "Safeguard user");
        options.addRequiredOption("p", "password", true,
            "Safeguard user password");
        options.addOption("t", "trustStorePath", true,
            "Path to the Java trust store file, containing trusted certificates");
        options.addOption("x", "trustStorePassword", true,
            "Trust store password");
        options.addOption("k", "keyStorePath", true,
            "Path to the Java key store file, containing certificate/private key pairs");
        options.addOption("c", "clientCertificatePath", true,
            "Path to a pfx/p12 private key/certificate file");
        options.addOption("w", "clientCertificatePassword", true,
            "Client certificate password");
        options.addOption("v", "validateCertificates", false,
            "Validate SSL certificates");
        options.addOption("i", "ignoreSslErrors", false,
            "Ignore SSL certificate errors");
        options.addOption("z", "apiVersion", true, 
            "Safeguard API version to use");
        options.addOption("a", "a2aApiKey", true, 
            "A2A API Key of an account to monitor");
        return options;
    }

    public static SafeguardOptions parseArguments(String[] args) {
        Options options = SafeguardOptions.getOptions();
        CommandLine line = null;

        CommandLineParser parser = new DefaultParser();

        SafeguardOptions returnValue = new SafeguardOptions();
        try {
            line = parser.parse(options, args);
            for(Option option : options.getOptions())
            {
                if(option.isRequired() && !line.hasOption(option.getOpt())){
                    throw new ParseException(String.format("Missing required option: %s", option.getArgName()));
                }
                if(line.hasOption(option.getOpt())) {
                    returnValue.setOptionValue(option, line.getOptionValue(option.getOpt()));
                }
            }
        } catch (ParseException ex) {

            System.err.println(ex);
            printAppHelp();

            System.exit(1);
        }

        return returnValue;
    }

    public static void printAppHelp() {

        Options options = SafeguardOptions.getOptions();
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("SafeguardClientCli", options, true);
    }

}
