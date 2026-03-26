package br.com.coderbank.operacoes_bancarias.entities;


import br.com.coderbank.operacoes_bancarias.entities.contas.Account;
import br.com.coderbank.operacoes_bancarias.entities.holders.CorporateHolder;
import br.com.coderbank.operacoes_bancarias.entities.holders.Holder;
import br.com.coderbank.operacoes_bancarias.entities.holders.IndividualHolder;
import br.com.coderbank.operacoes_bancarias.exceptions.AccountNotFoundException;
import br.com.coderbank.operacoes_bancarias.exceptions.ClosingAccountException;
import br.com.coderbank.operacoes_bancarias.exceptions.DuplicateAccountException;
import org.apache.catalina.Manager;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class Agency {

    private String name;
    private final String agencyNumber;
    private String address;
    private Manager manager;
    private final Map<String, Account> accounts = new HashMap<>();


    protected Agency(String name, String agencyNumber, String address, Manager manager) {
        this.name = name;
        this.agencyNumber = agencyNumber;
        this.address = address;
        this.manager = manager;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAgencyNumber() {
        return agencyNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }




    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Agency agency = (Agency) o;
        return Objects.equals(agencyNumber, agency.agencyNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(agencyNumber);
    }

    @Override
    public String toString() {
        return "Agency{" +
                "name='" + name + '\'' +
                ", agencyNumber='" + agencyNumber + '\'' +
                ", address='" + address + '\'' +
                ", manager=" + manager +
                ", accounts=" + accounts +
                '}';
    }
}
