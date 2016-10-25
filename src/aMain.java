import java.text.NumberFormat;
import java.util.*;


public class aMain {
	private static List<ShipInfo> productToShip = new LinkedList<ShipInfo>();
	private static List<Order> unableToFulfill = new LinkedList<Order>();
	private static List<Order> orderList;	
	private static Inventory[] inventoryArray;
	private static ShippingCost[] shoppingCostArray;	
	
	private static Map<Integer, List<Inventory>> productToInventoryMap = new HashMap<Integer, List<Inventory>>();
	private static Map<Region, List<ShippingCostNode_Problem2>> warehouseToDestinationMap = new HashMap<Region, List<ShippingCostNode_Problem2>>();
	
	
	public aMain(int testFlag) {
		if (testFlag == 1) {
			TestCase1.testData();	
			orderList = TestCase1.OrderList;
			inventoryArray = TestCase1.inventoryList;
			shoppingCostArray = TestCase1.shippingCostList;
		}
		
		if (testFlag == 2) {
			TestCase2 testCase = new TestCase2();
			orderList = testCase.getOrderList();
			inventoryArray = testCase.getInventoryList();
			shoppingCostArray = testCase.getShippingCostList();
		}
			
		initialVariable();
	}
	
	public static void initialVariable() {
		for (Inventory elem : inventoryArray) {
			if (productToInventoryMap.containsKey(elem.productId)) {
				List<Inventory> warehouseStoreList = productToInventoryMap
						.get(elem.productId);
				int index = findTheIndexOfTargetWasehouse(warehouseStoreList,
						elem.shipFrom);

				if (index == warehouseStoreList.size()) {
					productToInventoryMap.get(elem.productId).add(elem);
				}
			} else {
				List<Inventory> globalWarehouseStoreList = new ArrayList<Inventory>();
				globalWarehouseStoreList.add(elem);
				productToInventoryMap.put(elem.productId,
						globalWarehouseStoreList);
			}
		}

		for (ShippingCost elem : shoppingCostArray) {
			if (warehouseToDestinationMap.containsKey(elem.shipFrom)) {
				List<ShippingCostNode_Problem2> destinationList = warehouseToDestinationMap
						.get(elem.shipFrom);
				int index = findTheIndexOfTargetShippingCost(destinationList,
						elem.shipTo);

				if (index < destinationList.size()) {
					warehouseToDestinationMap.get(elem.shipFrom).get(index).shippingCostList
							.add(elem);
				} else {
					ShippingCostNode_Problem2 scn = new ShippingCostNode_Problem2(elem.shipTo);
					scn.shippingCostList.add(elem);
					warehouseToDestinationMap.get(elem.shipFrom).add(scn);
				}
			} else {
				List<ShippingCostNode_Problem2> destinationList = new ArrayList<ShippingCostNode_Problem2>();
				ShippingCostNode_Problem2 scn = new ShippingCostNode_Problem2(elem.shipTo);
				scn.shippingCostList.add(elem);
				destinationList.add(scn);
				warehouseToDestinationMap.put(elem.shipFrom, destinationList);
			}
		}
	}
	
	public static int findTheIndexOfTargetWasehouse(List<Inventory> list, Region region) {
		int index = 0;
		
		while (index < list.size()) {
			if (list.get(index).shipFrom.equals(region)) {
				break;
			} else {
				index++;
			}
		}
		
		return index;
	}
	
	public static int findTheIndexOfTargetShippingCost(List<ShippingCostNode_Problem2> list, Region destination) {
		int index = 0;
		
		while (index < list.size()) {
			if (list.get(index).destination.equals(destination)) {
				break;
			} else {
				index++;
			}
		}
		
		return index;
	}
	
	public static void getFillResult() {
		int filledNum = orderList.size() - unableToFulfill.size();
		int orderNum = orderList.size();
		double finishRate = (filledNum * 1.0) / orderNum;
		NumberFormat nt = NumberFormat.getPercentInstance();
		nt.setMinimumFractionDigits(2);
		System.out.println("total orders are " + orderNum + ", filled orders are " + filledNum + ", filled rate is " + nt.format(finishRate));
		System.out.println();
	}
	
	public static void showWarehouseReservation(Map<Integer, List<Inventory>> productToInventoryMap) {
		for (int productId : productToInventoryMap.keySet()) {
			List<Inventory> list = productToInventoryMap.get(productId);
			
			for (Inventory inventory : list) {
				System.out.println("warehouse [" + inventory.shipFrom + "], product [" + inventory.productId + "], quantity [" + inventory.quantity + "]");
			}
			System.out.println();
		}
	}
	
	public static void showProductToShip() {
		System.out.println("productToShip list is: ");
		
		for (ShipInfo info : productToShip) {
			System.out.println("productId [" + info.curInventory.productId + "], ship from [" + info.curInventory.shipFrom + "], quantity [" + info.quantity + "], cost [" + info.cost + "]");
		}
		
		System.out.println();
	}
	
	public static void showSomeClassListProblem1(List<SomeClass> costList) {
		for (SomeClass sc : costList) {
			System.out.println("warehouse [" + sc.inventory.shipFrom + "], productId [" + sc.inventory.productId + "]");
			
			for (ShippingCost subSc : sc.CostList) {
				System.out.println("from [" + subSc.shipFrom + "] to [" + subSc.shipTo + "], cost = [" + subSc.cost + "], need days = [" + subSc.shippingDays + "]");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public static void showShippingListProblem2(List<ShippingSolution_Problem2> shippingList) {	
		for (int i = 0; i < shippingList.size(); i++) {
			ShippingSolution_Problem2 ss = shippingList.get(i);
			
			System.out.println("order[" + (i+1) + "]: product is " + ss.order.productId + ", quantity needs " + ss.order.needQuantity);
			
			for (int j = 0; j < ss.warehouseList.size(); j++) {
				Region warehouse = ss.warehouseList.get(j);
				int quantity = ss.quantityList.get(j);
				System.out.println("from [" + warehouse + "] to [" + ss.order.destination + "], shipping quantity is " + quantity);				
			}
			System.out.println();
		}	
		System.out.println();		
	}
	
	public static void showShippingListProblem3(List<ShippingSolution_Problem3> shippingList_problem3) {		
		for (int i = 0; i < shippingList_problem3.size(); i++) {
			ShippingSolution_Problem3 ss3 = shippingList_problem3.get(i);
			
			System.out.println("order[" + (i+1) + "]: product is " + ss3.order.productId + ", quantity needs " + ss3.order.needQuantity + ", total cost is " + ss3.totalCost);
			
			for (int j = 0; j < ss3.warehouseList.size(); j++) {
				Region warehouse = ss3.warehouseList.get(j);
				int quantity = ss3.quantityList.get(j);
				int cost = ss3.costList.get(j);
				System.out.println("from [" + warehouse + "] to [" + ss3.order.destination + "], shipping quantity is " + quantity + ", cost is " + cost);				
			}
			System.out.println();
		}
	}

	
	
	public static void addToUnfillOrderList(Order curOrder) {
		unableToFulfill.add(curOrder);
	}
		
	public static List<Order> getOrderList() {
		return orderList;
	}
	
	public static Inventory[] getInventoryArray() {
		return inventoryArray;
	}
	
	public static ShippingCost[] getShippingCostArray() {
		return shoppingCostArray;
	}
	
	
	
	public static void transferToShipMent(Inventory curInventory, int Cost, int Quantity) {
		productToShip.add(new ShipInfo(curInventory, Cost, Quantity));
	}
	
	public static void ship() {
		int size = orderList.size() - unableToFulfill.size();
		int totalCost = 0;
		
		for (ShipInfo info : productToShip) {
			totalCost += info.cost;			
			List<Inventory> list = productToInventoryMap.get(info.curInventory.productId);
			int index = findTheIndexOfTargetWasehouse(list, info.curInventory.shipFrom);		
			productToInventoryMap.get(info.curInventory.productId).get(index).quantity -= info.quantity;
		}
		
		System.out.println("average cost = " + ((totalCost * 1.0) / size)); 
	}
	
	public static void unableToShip(Order curOrder) {
		unableToFulfill.add(curOrder);
	}
	
	public static void shippingCancel() {
		for (ShipInfo info : productToShip) {
			List<Inventory> list = productToInventoryMap.get(info.curInventory.productId);
			int index = findTheIndexOfTargetWasehouse(list, info.curInventory.shipFrom);	
			productToInventoryMap.get(info.curInventory.productId).get(index).quantity += info.quantity;
		}
		
		productToShip.clear();
		unableToFulfill.clear();
	}
	
	
	
	
	
	public static void main(String[] args) {
		int testFlag = 1;
		aMain t = new aMain(testFlag);
		
		int productId = 1;
		Region destination = Region.NORTH;
		int productQuantity = 3;
		
		
		System.out.println("******************** Problem1 ********************");
		List<SomeClass> costList = aProblem_1_Class.Problem1(productId, destination);
		showSomeClassListProblem1(costList);
		
		
		
		System.out.println("******************** Problem2 ********************");
		List<ShippingSolution_Problem2> shippingList = aProblem_2_Class.Problem2(orderList);
		getFillResult();
		showShippingListProblem2(shippingList);		
		
		System.out.println("product to ship: ");
		showProductToShip();		
		shippingCancel();
		
		
		System.out.println("******************** Problem3 ********************");
		List<ShippingSolution_Problem3> shippingList_problem3 = aProblem_3_Class.Problem3(orderList);	
		getFillResult();
		showShippingListProblem3(shippingList_problem3);	
		showProductToShip();
	}
}






class ShippingSolution_Problem2 {
	Order order;
	List<Region> warehouseList;
	List<Integer> quantityList;
	
	public ShippingSolution_Problem2(Order order){
		this.order = order;
		warehouseList = new LinkedList<Region>();
		quantityList = new LinkedList<Integer>();
	}
}

class ShippingSolution_Problem3 {
	Order order;
	int totalCost;
	List<Region> warehouseList;
	List<Integer> quantityList;
	List<Integer> costList;
	
	public ShippingSolution_Problem3(Order order){
		this.order = order;
		totalCost = 0;
		warehouseList = new LinkedList<Region>();
		quantityList = new LinkedList<Integer>();
		costList = new LinkedList<Integer>();
	}
}

class ShipInfo {
	Inventory curInventory;
	int cost;
	int quantity;
	
	public ShipInfo(Inventory curInventor, int cost, int quantity) {
		this.curInventory = curInventor;
		this.cost = cost;
		this.quantity = quantity;
	}
}
