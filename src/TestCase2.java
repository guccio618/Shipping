import java.util.ArrayList;
import java.util.List;


public class TestCase2 {
    static List<Order> orderList = new ArrayList<Order>();
    static Inventory[] inventoryList = new Inventory[2];
    static ShippingCost[] shippingCostList = new ShippingCost[3];
    static Region region1 = Region.EAST;
    static Region region2 = Region.WEST;
    static Region region3 = Region.SOUTH;
    static Region region4 = Region.NORTH;
    
    public TestCase2() {
    	Inventory inventory1 = new Inventory(region1, 1, 25);
    	Inventory inventory2 = new Inventory(region2, 1, 20);
    	inventoryList[0] = inventory1;
    	inventoryList[1] = inventory2;
    	
    	ShippingCost shopping1 = new ShippingCost(150, 8, Region.SOUTH, Region.EAST);
		ShippingCost shopping2 = new ShippingCost(251, 4, Region.NORTH, Region.EAST);
		ShippingCost shopping3 = new ShippingCost(352, 5, Region.NORTH, Region.WEST);
		shippingCostList[0] = shopping1;
		shippingCostList[1] = shopping2;
		shippingCostList[2] = shopping3;
		
		Order order1 = new Order(1, Region.SOUTH, 10, 20);
		Order order2 = new Order(1, Region.NORTH, 10, 15);
		orderList.add(order1);
		orderList.add(order2);
    }
    
    public static List<Order> getOrderList() {
		return orderList;
	}

	public static void setOrderList(List<Order> orderList) {
		TestCase2.orderList = orderList;
	}

	public static Inventory[] getInventoryList() {
		return inventoryList;
	}

	public static void setInventoryList(Inventory[] inventoryList) {
		TestCase2.inventoryList = inventoryList;
	}

	public static ShippingCost[] getShippingCostList() {
		return shippingCostList;
	}

	public static void setShippingCostList(ShippingCost[] shippingCostList) {
		TestCase2.shippingCostList = shippingCostList;
	}
}
