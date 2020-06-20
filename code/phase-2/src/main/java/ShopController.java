
public class ShopController implements PhaseCommand {
    private final AVLTree<Item> shop;
    private final ConsoleFormView view;

    public ShopController(AVLTree<Item> shop, ConsoleFormView view) {
        this.shop = shop;
        this.view = view;
    }

    @Override
    public void execute() {
        String itemPrice = this.view.getQueryResponse("What's the price of the item you want to buy? ");

        while(!"exit".equalsIgnoreCase(itemPrice)) {

            try {
                int price = Integer.parseInt(itemPrice);
                Item item = this.buyItem(price);

                if(null != item) {
                    //print what the user bought
                    this.view.displayMessage("Successfully purchased " + item.getName() + " for $" + item.getPrice());
                }
                else {
                    this.view.displayError("An item of that price is not in the shop!");
                }
            } catch (NumberFormatException nfe) {
                this.view.displayError("Sorry that's not a valid price!");
            }

            itemPrice = this.view.getQueryResponse("What's the price of the item you want to buy? ");
        }
    }

    public Item buyItem(int itemPrice) {
        //create item which would equal the item in the store
        Item itemToFind = new Item(itemPrice);
        Item boughtItem = this.shop.get(itemToFind);

        if(null != boughtItem) {
            this.shop.delete(boughtItem);
        }

        return boughtItem;
    }
}
