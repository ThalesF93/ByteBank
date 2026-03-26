package br.com.coderbank.operacoes_bancarias.entities;


import org.apache.catalina.Manager;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Bank {

    private final String name = "Banco Gold S/A";
    private  Agency agency;
    private final Map<String, Agency> agencies = new HashMap<>();

    private static final Random random = new Random();


    public Bank(){};

    public Bank(Agency agency){
        this.agency = agency;
    };


    public Map<String, Agency>  getAgencies() {
        return Collections.unmodifiableMap(agencies);
    }

    public Agency getAgency() {
        return agency;
    }

    public String getName() {
        return name;
    }

    public Agency openAgency(String name, String address, Manager manager){
        Agency agency = new Agency(name, generateAgencyNumber(), address, manager);
        agencies.put(agency.getAgencyNumber(), agency);
        return agency;
    }

    private String generateAgencyNumber(){
        String number;
        do {
            number = String.format("%04d", random.nextInt(9_000) + 1_000);
        } while (agencies.containsKey(number));
        return number;
    }

}
