import java.util.*;



public class aProblem_3_Class {
	/***************************************************
	 * Problem3
	 * params: List<Order> OrderEntryList
	 * output: List<ShippingSolution_Problem3>
	 * 
	 * 
	 ***************************************************/
	
	public static List<ShippingSolution_Problem3> Problem3(List<Order> OrderEntryList) {
		List<ShippingSolution_Problem3> shippingList_problem3 = new LinkedList<ShippingSolution_Problem3>();
		
		if (OrderEntryList == null || OrderEntryList.size() == 0) {
			return shippingList_problem3;
		}

		Map<Integer, List<Inventory>> localProductToInventoryMap = new HashMap<Integer, List<Inventory>>();
		Map<Region, List<ShippingCostNode_Problem3>> localWarehouseToDestinationMap = new HashMap<Region, List<ShippingCostNode_Problem3>>();
		Inventory[] inventoryArray = aMain.getInventoryArray();
		ShippingCost[] shoppingCostArray = aMain.getShippingCostArray();
		initialVariable_Problem2(inventoryArray, localProductToInventoryMap, shoppingCostArray, localWarehouseToDestinationMap);
		ShippmentBuilder s = new ShippmentBuilder();
		
		Queue<ProcessedOrder_Problem3> maxHeap = new PriorityQueue<ProcessedOrder_Problem3>(OrderEntryList.size(), new Comparator<ProcessedOrder_Problem3>(){
			public int compare(ProcessedOrder_Problem3 p1, ProcessedOrder_Problem3 p2) {
				if (p1.order.productId != p2.order.productId) {
					return p1.order.productId - p2.order.productId;
				} else {
					return p1.order.needQuantity - p2.order.needQuantity;
				}
			}
		});
		
		for (Order currentOrder : OrderEntryList) {					
			List<SomeClass> someClassList = aProblem_1_Class.Problem1(currentOrder.productId, currentOrder.destination);
			int totalAvailQuantity = 0;
			Map<Region, Integer> availWarehouseMinCostMap = new HashMap<Region, Integer>();

			for (SomeClass sc : someClassList) {
				if (!availWarehouseMinCostMap.containsKey(sc.inventory.shipFrom)) {
					totalAvailQuantity += sc.inventory.quantity;
					
					int minCost = Integer.MAX_VALUE;
					int index = findTheIndexOfTargetShippingCost(localWarehouseToDestinationMap.get(sc.inventory.shipFrom), currentOrder.destination);
					List<ShippingCost> shippingMethods = localWarehouseToDestinationMap.get(sc.inventory.shipFrom).get(index).shippingCostList;

					for (ShippingCost curShipCost : shippingMethods) {
						minCost = Math.min(minCost, curShipCost.cost);
					}
					
					availWarehouseMinCostMap.put(sc.inventory.shipFrom, minCost);
				}	
			}

			if (totalAvailQuantity >= currentOrder.needQuantity) {
				ProcessedOrder_Problem3 newProcessedOrderNode = new ProcessedOrder_Problem3(currentOrder, availWarehouseMinCostMap);
				maxHeap.offer(newProcessedOrderNode);
			} else {
				s.unableToShip(currentOrder);
			}
		}
		
		while (!maxHeap.isEmpty()) {			
			ProcessedOrder_Problem3 po = maxHeap.poll();	

			if (po.order.needQuantity <= 0) {
				continue;
			}

			int productId = po.order.productId;
			int quantityNeeded = po.order.needQuantity;
			ShippingSolution_Problem3 solution = new ShippingSolution_Problem3(po.order);
			List<Inventory> warehouseReversionList = localProductToInventoryMap.get(productId);
			int currentTotalAvailQuantity = 0;
			
			for (Region warehouse : po.availWarehouseMinCostMap.keySet()) {
				List<Inventory> list = localProductToInventoryMap.get(productId);
				int index = findTheIndexOfTargetWasehouse(list, warehouse);
				currentTotalAvailQuantity += localProductToInventoryMap.get(productId).get(index).quantity;
			}

			if (currentTotalAvailQuantity < quantityNeeded) {
				s.unableToShip(po.order);
				continue;
			}
			
			List<ShippingMethod_Problem3> shippingMethodList = getShippingMethods(po, localProductToInventoryMap);			
			
			for (ShippingMethod_Problem3 spm : shippingMethodList) {
				solution.totalCost += spm.cost;
				solution.warehouseList.add(spm.warehouse);
				solution.quantityList.add(spm.quantity);
				solution.costList.add(spm.cost);
				
				int globalWarehouseIndex = findTheIndexOfTargetWasehouse(localProductToInventoryMap.get(productId), spm.warehouse);		
				Inventory globalInventory = localProductToInventoryMap.get(productId).get(globalWarehouseIndex);
				int cost = spm.cost;
				int quantity = spm.quantity;
				s.transferToShipMent(globalInventory, cost, quantity);
				
				int warehouseIndex = findTheIndexOfTargetWasehouse(warehouseReversionList, spm.warehouse);		
				warehouseReversionList.get(warehouseIndex).quantity -= spm.quantity;
				
//				int leftQuantity = warehouseReversionList.get(warehouseIndex).quantity - spm.quantity;
//				warehouseReversionList.get(warehouseIndex).quantity = leftQuantity;
			}
			
			localProductToInventoryMap.put(productId, warehouseReversionList);
			shippingList_problem3.add(solution);	
		}
		
		s.ship();
		return shippingList_problem3;
	}
	
	public static List<ShippingMethod_Problem3> getShippingMethods(ProcessedOrder_Problem3 po, Map<Integer, List<Inventory>> localProductToInventoryMap) {
		List<ShippingMethod_Problem3> shippingMethodList = new ArrayList<ShippingMethod_Problem3>();
		List<WarehouseCostNode_Problem3> warehoustList = new LinkedList<WarehouseCostNode_Problem3>();
		
		for (Region re : po.availWarehouseMinCostMap.keySet()) {
			List<Inventory> list = localProductToInventoryMap.get(po.order.productId);
			int index = findTheIndexOfTargetWasehouse(list, re);
			int availQuantity = localProductToInventoryMap.get(po.order.productId).get(index).quantity;
			warehoustList.add(new WarehouseCostNode_Problem3(re, availQuantity, po.availWarehouseMinCostMap.get(re)));
		}
		
		Collections.sort(warehoustList, new Comparator<WarehouseCostNode_Problem3>() {
			public int compare(WarehouseCostNode_Problem3 wcn1, WarehouseCostNode_Problem3 wcn2) {
				return wcn1.minCost - wcn2.minCost;
			}
		});	
		
		int needQuantity = po.order.needQuantity;
		DynamicProgrammingNode_Problem3[] dp = new DynamicProgrammingNode_Problem3[needQuantity + 1];
		int len = warehoustList.size();
		int currentQuantity = 0;
		int startIndex = 0;
		
		for (int i = 0; i <= needQuantity; i++) {
			dp[i] = new DynamicProgrammingNode_Problem3();
		}
		
		dp[0].cost = 0;

		for (int i = startIndex; i < len && currentQuantity <= needQuantity; i++) {
			WarehouseCostNode_Problem3 node = warehoustList.get(i);

			for (int j = currentQuantity; j <= currentQuantity + node.quantity && j <= needQuantity; j++) {
				for (int pickNum = 1; pickNum <= node.quantity; pickNum++) {
					if (j >= pickNum && dp[j - pickNum].cost != Integer.MAX_VALUE && dp[j].cost > dp[j - pickNum].cost + node.minCost) {
						dp[j].cost = dp[j - pickNum].cost + node.minCost;
						dp[j].currentWarehouse = node.warehouse;
						dp[j].quantity = pickNum;
						dp[j].prevIndex = j - pickNum;
					}
				}
			}

			currentQuantity += warehoustList.get(startIndex).quantity;
			startIndex++;
		}

		int printIndex = needQuantity;
		
		while (printIndex > 0) {
			shippingMethodList.add(0, new ShippingMethod_Problem3(dp[printIndex].currentWarehouse, dp[printIndex].quantity, dp[printIndex].cost));		
			printIndex = dp[printIndex].prevIndex;
		}
		
		return shippingMethodList;
	}
	
	
	
	
	public static void initialVariable_Problem2(Inventory[] inventoryArray, Map<Integer, List<Inventory>> localProductToInventoryMap, ShippingCost[] shoppingCostArray, Map<Region, List<ShippingCostNode_Problem3>> warehouseToDestinationMap) {	
		for (Inventory elem : inventoryArray) {
			if (localProductToInventoryMap.containsKey(elem.productId)) {
				List<Inventory> warehouseStoreList = localProductToInventoryMap.get(elem.productId);
				int index = findTheIndexOfTargetWasehouse(warehouseStoreList, elem.shipFrom);	
				
				if (index == warehouseStoreList.size()) {
					localProductToInventoryMap.get(elem.productId).add(new Inventory(elem.shipFrom, elem.productId, elem.quantity));					
				}				
			} else {
				List<Inventory> warehouseStoreList = new ArrayList<Inventory>();
				warehouseStoreList.add(new Inventory(elem.shipFrom, elem.productId, elem.quantity));
				localProductToInventoryMap.put(elem.productId, warehouseStoreList);
			}
		}
		
		for (ShippingCost elem : shoppingCostArray) {
			if (warehouseToDestinationMap.containsKey(elem.shipFrom)) {
				List<ShippingCostNode_Problem3> destinationList = warehouseToDestinationMap.get(elem.shipFrom);
				int index = findTheIndexOfTargetShippingCost(destinationList, elem.shipTo);
				
				if (index < destinationList.size()) {
					warehouseToDestinationMap.get(elem.shipFrom).get(index).shippingCostList.add(elem);
				} else {
					ShippingCostNode_Problem3 scn = new ShippingCostNode_Problem3(elem.shipTo);
					scn.shippingCostList.add(elem);
					warehouseToDestinationMap.get(elem.shipFrom).add(scn);
				}
			} else {
				List<ShippingCostNode_Problem3> destinationList = new ArrayList<ShippingCostNode_Problem3>();
				ShippingCostNode_Problem3 scn = new ShippingCostNode_Problem3(elem.shipTo);
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
	
	public static int findTheIndexOfTargetShippingCost(List<ShippingCostNode_Problem3> list, Region destination) {
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
}





class ProcessedOrder_Problem3 {
	Order order;
	Map<Region, Integer> availWarehouseMinCostMap;
	
	public ProcessedOrder_Problem3(Order order, Map<Region, Integer> availWarehouseMinCostMap) {
		this.order = new Order(order.productId, order.destination, order.needDays, order.needQuantity);
		this.availWarehouseMinCostMap = availWarehouseMinCostMap;
	}
}

class ShippingCostNode_Problem3 {
	Region destination;
	List<ShippingCost> shippingCostList;
	
	public ShippingCostNode_Problem3(Region destination) {
		this.destination = destination;
		shippingCostList = new ArrayList<ShippingCost>();
	}
}

class ShippingMethod_Problem3 {
	Region warehouse;
	int quantity;
	int cost;
	
	public ShippingMethod_Problem3(Region warehouse, int quantity, int cost) {
		this.warehouse = warehouse;
		this.quantity = quantity;
		this.cost = cost;
	}
}

class WarehouseCostNode_Problem3 {
	Region warehouse;
	int quantity;
	int minCost;
	
	public WarehouseCostNode_Problem3(Region warehouse, int quantity, int minCost) {
		this.warehouse = warehouse;
		this.quantity = quantity;
		this.minCost = minCost;
	}
}

class DynamicProgrammingNode_Problem3 {
	int cost;
	Region currentWarehouse;
	int prevIndex;
	int quantity;
	
	public DynamicProgrammingNode_Problem3() {
		cost = Integer.MAX_VALUE;
		prevIndex = 0;
		currentWarehouse = null;
		quantity = 0;
	}
}
