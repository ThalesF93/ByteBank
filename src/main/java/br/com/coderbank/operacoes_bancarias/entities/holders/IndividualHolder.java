package br.com.coderbank.operacoes_bancarias.entities.holders;



import br.com.coderbank.operacoes_bancarias.enums.MaritalStatus;
import br.com.coderbank.operacoes_bancarias.exceptions.InvalidIdentificationException;

import java.util.Objects;

public class IndividualHolder extends Holder {

    private MaritalStatus maritalStatus;
    private String cpf;


    public IndividualHolder(String name, String address, String email, String cpf, MaritalStatus maritalStatus) {
        super(name, address, email);
        setCpf(cpf);
        this.maritalStatus = maritalStatus;
    }

    public String getCpf() {
        return cpf;
    }

    private void setCpf(String cpf) {
        if (cpf == null){
            throw new InvalidIdentificationException("CPF must not be null");
        }
        if (!validateCPF(cpf)){
            throw new InvalidIdentificationException("CPF invalid");
        }
        this.cpf = cpf;
    }

    private static boolean validateCPF(String cpf){
        return cpf.matches("[0-9]{3}\\.[0-9]{3}\\.[0-9]{3}-\\d{2}");
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        IndividualHolder that = (IndividualHolder) o;
        return Objects.equals(cpf, that.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(cpf);
    }

    @Override
    public String toString() {
        return super.toString() + "IndividualHolder{"  +
                "maritalStatus=" + maritalStatus +
                ", cpf='" + cpf + '\'' +
                '}';
    }
}
