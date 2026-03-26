package br.com.coderbank.operacoes_bancarias.entities.holders;

public abstract class Holder {

    private String name;
    private String address;
    private String email;

    public Holder(String name,  String address, String email) {
        this.name = name;
        this.address = address;
        this.email = email;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Holder{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}


