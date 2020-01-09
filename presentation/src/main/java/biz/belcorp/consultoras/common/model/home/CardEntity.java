package biz.belcorp.consultoras.common.model.home;

/**
 *
 */
public class CardEntity {

    private int cardType;
    private String description;

    private int amount;
    private double value;
    private boolean state;
    private String name;

    public CardEntity() {
    }

    public CardEntity(int cardType, String description) {
        this.cardType = cardType;
        this.description = description;
    }

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
