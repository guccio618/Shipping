import java.util.*;


public class TestCase1 {
    static Inventory inventory = new Inventory();
    static Order order = new Order();
    static ShippingCost shopping = new ShippingCost();
    static List<Order> orderList = new ArrayList<Order>();
    static Inventory[] inventoryList = new Inventory[30];
    static ShippingCost[] shippingCostList = new ShippingCost[30];
    //id, shippingCostObject
    static List<Order> OrderList = new ArrayList<Order>();

	public static void testData() {
		
		//shipFrom, id, quantity
		//1: 10 2: 30 3:60 4:100
		Inventory inventory1 = new Inventory(Region.WEST, 1, 100);
		Inventory inventory11 = new Inventory(Region.MID, 1, 1);
		Inventory inventory13 = new Inventory(Region.NORTH, 1, 1);//7
		Inventory inventory24 = new Inventory(Region.EAST, 1, 1);//7
		Inventory inventory2 = new Inventory(Region.EAST, 2, 30);
		Inventory inventory12 = new Inventory(Region.WEST, 2, 1);
		Inventory inventory22 = new Inventory(Region.NORTH, 2, 1);
		Inventory inventory5 = new Inventory(Region.MID, 5, 1);
		Inventory inventory25 = new Inventory(Region.WEST, 5, 2);
		Inventory inventory15 = new Inventory(Region.EAST, 5, 3);
		Inventory inventory3 = new Inventory(Region.NORTH, 3, 1);//
		Inventory inventory4 = new Inventory(Region.SOUTH, 4, 50);
		Inventory inventory6 = new Inventory(Region.SOUTH, 6, 30);
		Inventory inventory7 = new Inventory(Region.MID, 7, 35);//7
		Inventory inventory8 = new Inventory(Region.MID, 8, 40);
		Inventory inventory9 = new Inventory(Region.SOUTH, 9, 45);
		Inventory inventory10 = new Inventory(Region.MID, 10, 50);//
		Inventory inventory14 = new Inventory(Region.MID, 4, 70);
		Inventory inventory16 = new Inventory(Region.MID, 6, 80);
		Inventory inventory17 = new Inventory(Region.SOUTH, 7, 85);//7
		Inventory inventory18 = new Inventory(Region.NORTH, 8, 90);//
		Inventory inventory19 = new Inventory(Region.MID, 9, 95);
		Inventory inventory20 = new Inventory(Region.WEST, 10, 100);
		Inventory inventory21 = new Inventory(Region.NORTH, 4, 105);//7
		Inventory inventory23 = new Inventory(Region.SOUTH, 3, 115);
		Inventory inventory26 = new Inventory(Region.WEST, 6, 130);
		Inventory inventory27 = new Inventory(Region.WEST, 7, 135);//7
		Inventory inventory28 = new Inventory(Region.EAST, 8, 140);
		Inventory inventory29 = new Inventory(Region.EAST, 9, 145);
		Inventory inventory30 = new Inventory(Region.NORTH, 10, 150);//
		
		ShippingCost shopping1 = new ShippingCost(150, 8, Region.MID, Region.WEST);
		ShippingCost shopping2 = new ShippingCost(251, 4, Region.NORTH,Region.NORTH);
		ShippingCost shopping3 = new ShippingCost(352, 5, Region.MID, Region.SOUTH);
		ShippingCost shopping4 = new ShippingCost(113, 5, Region.SOUTH,Region.MID);//1S
		ShippingCost shopping5 = new ShippingCost(154, 3, Region.EAST,Region.SOUTH);
		ShippingCost shopping6 = new ShippingCost(135, 4, Region.WEST, Region.MID);
		ShippingCost shopping7 = new ShippingCost(256, 2, Region.NORTH, Region.MID); //
		ShippingCost shopping8 = new ShippingCost(287, 10, Region.SOUTH, Region.NORTH); //1S
		ShippingCost shopping9 = new ShippingCost(458, 10, Region.EAST, Region.EAST);
		ShippingCost shopping10 = new ShippingCost(559, 8, Region.NORTH, Region.MID);
		ShippingCost shopping11 = new ShippingCost(258, 4, Region.SOUTH, Region.NORTH); //1S
		ShippingCost shopping12 = new ShippingCost(357, 2, Region.MID, Region.MID);
		ShippingCost shopping13 = new ShippingCost(156, 8, Region.SOUTH, Region.SOUTH);  
		ShippingCost shopping14 = new ShippingCost(505, 2, Region.EAST,Region.WEST);
		ShippingCost shopping15 = new ShippingCost(254, 7, Region.WEST, Region.SOUTH);
		ShippingCost shopping16 = new ShippingCost(333, 3, Region.WEST,  Region.NORTH);
		ShippingCost shopping17 = new ShippingCost(252, 2, Region.EAST, Region.MID); //
		ShippingCost shopping18 = new ShippingCost(81, 10, Region.NORTH, Region.WEST);  //
		ShippingCost shopping19 = new ShippingCost(101, 5, Region.SOUTH,Region.EAST);   //1S
		ShippingCost shopping20 = new ShippingCost(52, 8, Region.WEST, Region.EAST);
		ShippingCost shopping21 = new ShippingCost(353, 2, Region.MID,Region.WEST);
		ShippingCost shopping22 = new ShippingCost(154, 8, Region.WEST, Region.WEST);
		ShippingCost shopping23 = new ShippingCost(555, 2, Region.EAST,  Region.NORTH);
		ShippingCost shopping24 = new ShippingCost(256, 7, Region.NORTH, Region.SOUTH);  //
		ShippingCost shopping25 = new ShippingCost(335, 3, Region.NORTH, Region.MID);
		ShippingCost shopping26 = new ShippingCost(258, 2, Region.SOUTH, Region.WEST);  //1S
		ShippingCost shopping27 = new ShippingCost(84, 10, Region.NORTH, Region.NORTH);  //
		ShippingCost shopping28 = new ShippingCost(159, 5, Region.NORTH,  Region.EAST); //
		ShippingCost shopping29 = new ShippingCost(55, 8, Region.MID,  Region.NORTH);
		ShippingCost shopping30 = new ShippingCost(355, 2, Region.MID,  Region.EAST);
//	Map.Entry<Region,Integer> entry1 = new AbstractMap.SimpleEntry<Region, Integer>(region1, 15);
		inventoryList[0] = inventory1;
		inventoryList[1] = inventory2;
		inventoryList[2] = inventory3;
		inventoryList[3] = inventory4;
		inventoryList[4] = inventory5;
		inventoryList[5] = inventory6;
		inventoryList[6] = inventory7;
		inventoryList[7] = inventory8;
		inventoryList[8] = inventory9;
		inventoryList[9] = inventory10;
		inventoryList[10] = inventory11;
		inventoryList[11] = inventory12;
		inventoryList[12] = inventory13;
		inventoryList[13] = inventory14;
		inventoryList[14] = inventory15;
		inventoryList[15] = inventory16;
		inventoryList[16] = inventory17;
		inventoryList[17] = inventory18;
		inventoryList[18] = inventory19;
		inventoryList[19] = inventory20;
		inventoryList[20] = inventory21;
		inventoryList[21] = inventory22;
		inventoryList[22] = inventory23;
		inventoryList[23] = inventory24;
		inventoryList[24] = inventory25;
		inventoryList[25] = inventory26;
		inventoryList[26] = inventory27;
		inventoryList[27] = inventory28;
		inventoryList[28] = inventory29;
		inventoryList[29] = inventory30;
				
		shippingCostList[0] = shopping1;
		shippingCostList[1] = shopping2;
		shippingCostList[2] = shopping3;
		shippingCostList[3] = shopping4;
		shippingCostList[4] = shopping5;
		shippingCostList[5] = shopping6;
		shippingCostList[6] = shopping7;
		shippingCostList[7] = shopping8;
		shippingCostList[8] = shopping9;
		shippingCostList[9] = shopping10;
		shippingCostList[10] = shopping11;
		shippingCostList[11] = shopping12;
		shippingCostList[12] = shopping13;
		shippingCostList[13] = shopping14;
		shippingCostList[14] = shopping15;
		shippingCostList[15] = shopping16;
		shippingCostList[16] = shopping17;
		shippingCostList[17] = shopping18;
		shippingCostList[18] = shopping19;
		shippingCostList[19] = shopping20;
		shippingCostList[20] = shopping21;
		shippingCostList[21] = shopping22;
		shippingCostList[22] = shopping23;
		shippingCostList[23] = shopping24;
		shippingCostList[24] = shopping25;
		shippingCostList[25] = shopping26;
		shippingCostList[26] = shopping27;
		shippingCostList[27] = shopping28;
		shippingCostList[28] = shopping29;
		shippingCostList[29] = shopping30;
		//id, shipTo, time, quantity
		// Order(int productId, Region destination, int needDays, int needQuantity)
		Order order1 = new Order(1, Region.EAST, 10, 20);
		Order order2 = new Order(1, Region.MID, 15, 10);
		Order order3 = new Order(1, Region.SOUTH, 20, 30);
		Order order4 = new Order(1, Region.WEST, 10, 40);
		Order order5 = new Order(2, Region.NORTH, 5, 10);
		Order order6 = new Order(2, Region.SOUTH,7, 30);
		Order order7 = new Order(2, Region.MID, 8, 20);
		Order order8 = new Order(5, Region.EAST, 19, 10);
		Order order9 = new Order(5, Region.WEST, 12, 20);
		Order order10 = new Order(5, Region.EAST, 23, 30);  //550
		
//		List<List<SomeClass>> OrderEntryList = new ArrayList<List<SomeClass>>();
//		List<SomeClass> some1 = first.deliverPossible(order1.productId, order1.destination);
//		List<SomeClass> some2 = first.deliverPossible(order2.productId, order2.destination);
//		List<SomeClass> some3 = first.deliverPossible(order3.productId, order3.destination);
//		List<SomeClass> some4 = first.deliverPossible(order4.productId, order4.destination);
//		List<SomeClass> some5 = first.deliverPossible(order5.productId, order5.destination);
//		List<SomeClass> some6 = first.deliverPossible(order6.productId, order6.destination);
//		List<SomeClass> some7 = first.deliverPossible(order7.productId, order7.destination);
//		List<SomeClass> some8 = first.deliverPossible(order8.productId, order8.destination);
//		List<SomeClass> some9 = first.deliverPossible(order9.productId, order9.destination);
//		List<SomeClass> some10 = first.deliverPossible(order10.productId, order10.destination);
//		OrderEntryList.add(some1);
//		OrderEntryList.add(some2);
//		OrderEntryList.add(some3);
//		OrderEntryList.add(some4);
//		OrderEntryList.add(some5);
//		OrderEntryList.add(some6);
//		OrderEntryList.add(some7);
//		OrderEntryList.add(some8);
//		OrderEntryList.add(some9);
//		OrderEntryList.add(some10);
		
		OrderList.add(order1);
		OrderList.add(order2);
		OrderList.add(order3);
		OrderList.add(order4);
		OrderList.add(order5);
		OrderList.add(order6);
		OrderList.add(order7);
		OrderList.add(order8);
		OrderList.add(order9);
		OrderList.add(order10);
		//testSecond(OrderEntryList);
		
        
		
		
		
		
		
		
		
	}// this function is used for generating the list for the tests

//	public static void testFirst() {
//		//List<SomeClass> result = first.deliverPossible(1, Region.SOUTH);
//		List<SomeClass> result = zMyTest.Problem1(1, Region.MID);
//		for (SomeClass sc : result) {
//		    for (ShippingCost shopping : sc.CostList) {
//		    	System.out.print("cost = " + shopping.cost + " ");
//		    	System.out.print("shippingdays = " + shopping.shippingDays + " ");
//		    	System.out.print("shipTo = " + shopping.shipTo + " ");
//		    	System.out.print("shipFrom = " + shopping.shipFrom + " ");
//		    	System.out.println(" ");
//		  
//		    }
//		}
//	}
	
	public static void testSecond(List<List<SomeClass>> OrderEntryList) {
		//first.secondPart(OrderList);
	}

	

//	public static void main(String args[]) {
//		testData();
//		aMyTest_2 test = new aMyTest_2();
////		testFirst();
//		test.secondPart(OrderList);
//		
//		System.out.println(OrderList.size());
//	}
}
 