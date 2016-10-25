import java.util.*;



public class aProblem_2_Class {
	/***************************************************
	 * Problem2
	 * params: List<Order> OrderEntryList
	 * output: List<ShippingSolution>
	 * 
	 * 
	 ***************************************************/
	
	public static List<ShippingSolution_Problem2> Problem2(List<Order> OrderEntryList) {
		List<ShippingSolution_Problem2> shippingList_problem2 = new LinkedList<ShippingSolution_Problem2>();
		
		if (OrderEntryList == null || OrderEntryList.size() == 0) {
			return shippingList_problem2;
		}

		Map<Integer, List<MyInventory_Problem2>> localProductToInventoryMap = new HashMap<Integer, List<MyInventory_Problem2>>();
		Map<Region, List<ShippingCostNode_Problem2>> localWarehouseToDestinationMap = new HashMap<Region, List<ShippingCostNode_Problem2>>();
		Inventory[] inventoryArray = aMain.getInventoryArray();
		ShippingCost[] shoppingCostArray = aMain.getShippingCostArray();
		initialVariable_Problem2(inventoryArray, localProductToInventoryMap, shoppingCostArray, localWarehouseToDestinationMap);

		Map<Integer, List<DependencyNode_Problem2>> productWarehouseDependencyMap = new HashMap<Integer, List<DependencyNode_Problem2>>(); 		
		ShippmentBuilder s = new ShippmentBuilder();
		
		List<ProcessedOrder_Problem2> processedOrderList = new ArrayList<ProcessedOrder_Problem2>();
		
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
				ProcessedOrder_Problem2 newProcessedOrderNode = new ProcessedOrder_Problem2(currentOrder, availWarehouseMinCostMap);
				processedOrderList.add(newProcessedOrderNode);
				
				for (Region warehouse : availWarehouseMinCostMap.keySet()) {
					List<DependencyNode_Problem2> warehouseDependencyList = null;
					
					if (productWarehouseDependencyMap.containsKey(currentOrder.productId)) {
						warehouseDependencyList = productWarehouseDependencyMap.get(currentOrder.productId);
					} else {
						warehouseDependencyList = new ArrayList<DependencyNode_Problem2> ();
					}
					
					int index = findTheIndexOfTargetDependencyNode(warehouseDependencyList, warehouse);
					
					if (index == warehouseDependencyList.size()) {
						warehouseDependencyList.add(new DependencyNode_Problem2(warehouse, 1));
					} else {
						warehouseDependencyList.get(index).dependencyNum++;
					}

					productWarehouseDependencyMap.put(currentOrder.productId, warehouseDependencyList);
				}
			} else {
				s.unableToShip(currentOrder);
			}
		}
		
		Collections.sort(processedOrderList, new Comparator<ProcessedOrder_Problem2>(){
			public int compare(ProcessedOrder_Problem2 p1, ProcessedOrder_Problem2 p2) {
				if (p1.order.productId != p2.order.productId) {
					return p1.order.productId - p2.order.productId;
				} else {
					return p1.order.needQuantity - p2.order.needQuantity;
				}
			}
		});
		
		for (ProcessedOrder_Problem2 po : processedOrderList) {
			if (po.order.needQuantity <= 0) {
				continue;
			}

			ShippingSolution_Problem2 solution = new ShippingSolution_Problem2(po.order);
			
			
			int productId = po.order.productId;
			int quantityNeeded = po.order.needQuantity;
			List<MyInventory_Problem2> warehouseReversionList = localProductToInventoryMap.get(productId);
			int currentTotalAvailQuantity = 0;
			
			for (Region warehouse : po.availWarehouseMinCostMap.keySet()) {
				List<MyInventory_Problem2> list = localProductToInventoryMap.get(productId);
				int index = findTheIndexOfTargetWasehouse(list, warehouse);		
				currentTotalAvailQuantity += localProductToInventoryMap.get(productId).get(index).localInventory.quantity;
			}

			if (currentTotalAvailQuantity < quantityNeeded) {
				s.unableToShip(po.order);
				continue;
			}
			
			List<Region> availWarehouseList = new LinkedList<Region>();
			
			for (Region re : po.availWarehouseMinCostMap.keySet()) {
				availWarehouseList.add(re);
			}
			
			Collections.sort(availWarehouseList, new Comparator<Region>() {
				public int compare(Region warehouse1, Region warehouse2) {
					List<DependencyNode_Problem2> list = productWarehouseDependencyMap.get(po.order.productId);
					int index1 = findTheIndexOfTargetDependencyNode(list, warehouse1);
					int index2 = findTheIndexOfTargetDependencyNode(list, warehouse2);
					int dependency1 = list.get(index1).dependencyNum;
					int dependency2 = list.get(index2).dependencyNum;
					
					if (dependency1 != dependency2) {
						return dependency1 - dependency2;
					} else {
						int warehouseIndex1 = findTheIndexOfTargetWasehouse(localProductToInventoryMap.get(productId), warehouse1);
						int warehouseIndex2 = findTheIndexOfTargetWasehouse(localProductToInventoryMap.get(productId), warehouse2);
						int reservation1 = localProductToInventoryMap.get(productId).get(warehouseIndex1).localInventory.quantity;
						int reservation2 = localProductToInventoryMap.get(productId).get(warehouseIndex2).localInventory.quantity;
						return reservation2 - reservation1;
					}
				}
			});

			for (Region warehouse : availWarehouseList) {
				int warehouseIndex = findTheIndexOfTargetWasehouse(warehouseReversionList, warehouse);					
				MyInventory_Problem2 currentIvt = warehouseReversionList.get(warehouseIndex);
				int reversionQuantity = currentIvt.localInventory.quantity;

				if (reversionQuantity == 0) {
					continue;
				}

				solution.warehouseList.add(warehouse);
				solution.quantityList.add(Math.min(reversionQuantity, quantityNeeded));
				
				int warehouseIndexInLocalMap = findTheIndexOfTargetWasehouse(localProductToInventoryMap.get(productId), warehouse);		
				MyInventory_Problem2 globalInventory = localProductToInventoryMap.get(productId).get(warehouseIndexInLocalMap);
				int cost = po.availWarehouseMinCostMap.get(warehouse);
				int quantity = Math.min(reversionQuantity, quantityNeeded);
				s.transferToShipMent(globalInventory.originInventory, cost, quantity);

				if (reversionQuantity >= quantityNeeded) {
					reversionQuantity -= quantityNeeded;
					quantityNeeded = 0;
				} else {
					quantityNeeded -= reversionQuantity;
					reversionQuantity = 0;
				}
				
				warehouseReversionList.get(warehouseIndex).localInventory.quantity = reversionQuantity;	
				List<DependencyNode_Problem2> warehouseDependencyList = productWarehouseDependencyMap.get(po.order.productId);	
				int index = findTheIndexOfTargetDependencyNode(warehouseDependencyList, warehouse);
				warehouseDependencyList.get(index).dependencyNum--;
				productWarehouseDependencyMap.put(po.order.productId, warehouseDependencyList);
				
				if (quantityNeeded == 0) {					
					break;
				}
			}	
		
			localProductToInventoryMap.put(productId, warehouseReversionList);	
			
			shippingList_problem2.add(solution);	
		}
		
		s.ship();		
		return shippingList_problem2;
	}
	
	
	
	
	
	public static void initialVariable_Problem2(Inventory[] inventoryArray, Map<Integer, List<MyInventory_Problem2>> localProductToInventoryMap, ShippingCost[] shoppingCostArray, Map<Region, List<ShippingCostNode_Problem2>> warehouseToDestinationMap) {	
		for (Inventory elem : inventoryArray) {
			if (localProductToInventoryMap.containsKey(elem.productId)) {
				List<MyInventory_Problem2> warehouseStoreList = localProductToInventoryMap.get(elem.productId);
				int index = findTheIndexOfTargetWasehouse(warehouseStoreList, elem.shipFrom);	
				
				if (index == warehouseStoreList.size()) {
					MyInventory_Problem2 curInventory = new MyInventory_Problem2(elem, new Inventory(elem.shipFrom, elem.productId, elem.quantity));
					localProductToInventoryMap.get(elem.productId).add(curInventory);					
				}				
			} else {
				List<MyInventory_Problem2> warehouseStoreList = new ArrayList<MyInventory_Problem2>();
				MyInventory_Problem2 curInventory = new MyInventory_Problem2(elem, new Inventory(elem.shipFrom, elem.productId, elem.quantity));
				warehouseStoreList.add(curInventory);
				localProductToInventoryMap.put(elem.productId, warehouseStoreList);
			}
		}
		
		for (ShippingCost elem : shoppingCostArray) {
			if (warehouseToDestinationMap.containsKey(elem.shipFrom)) {
				List<ShippingCostNode_Problem2> destinationList = warehouseToDestinationMap.get(elem.shipFrom);
				int index = findTheIndexOfTargetShippingCost(destinationList, elem.shipTo);
				
				if (index < destinationList.size()) {
					warehouseToDestinationMap.get(elem.shipFrom).get(index).shippingCostList.add(elem);
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
	
	public static int findTheIndexOfTargetWasehouse(List<MyInventory_Problem2> list, Region region) {
		int index = 0;
		
		while (index < list.size()) {
			if (list.get(index).localInventory.shipFrom.equals(region)) {
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
	
	public static int findTheIndexOfTargetDependencyNode(List<DependencyNode_Problem2> list, Region warehouse) {
		int index = 0;
		
		while (index < list.size()) {
			if (list.get(index).warehouse.equals(warehouse)) {
				break;
			} else {
				index++;
			}			
		}
		
		return index;
	}
}



class MyInventory_Problem2 {
	Inventory originInventory;
	Inventory localInventory;
	
	public MyInventory_Problem2(Inventory originInventory, Inventory localInventory) {
		this.originInventory = originInventory;
		this.localInventory = localInventory;
	}
}

class ProcessedOrder_Problem2 {
	Order order;
	Map<Region, Integer> availWarehouseMinCostMap;
	
	public ProcessedOrder_Problem2(Order order, Map<Region, Integer> availWarehouseMinCostMap) {
		this.order = new Order(order.productId, order.destination, order.needDays, order.needQuantity);
		this.availWarehouseMinCostMap = availWarehouseMinCostMap;
	}
}

class ShippingCostNode_Problem2 {
	Region destination;
	List<ShippingCost> shippingCostList;
	
	public ShippingCostNode_Problem2(Region destination) {
		this.destination = destination;
		shippingCostList = new ArrayList<ShippingCost>();
	}
}

class DependencyNode_Problem2 {
	Region warehouse;
	int dependencyNum;
	
	public DependencyNode_Problem2(Region warehouse, int dependencyNum) {
		this.warehouse = warehouse;
		this.dependencyNum = dependencyNum;
	}
}