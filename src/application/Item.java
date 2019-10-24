package application;

public class Item {
    private final String description;
    private final ItemCategory category;

    public Item (String description, ItemCategory category) {
        this.description = description;
        this.category = category;
    }

    public String description() {
        return this.description;
    }

    public ItemCategory category() {
        return this.category;
    }
}
