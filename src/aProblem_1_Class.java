import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class aProblem_1_Class {
	/***************************************************
	 * Problem1 params: List<Order> OrderEntryList output: List<SomeClass>
	 * 
	 * 
	 ***************************************************/

	public static List<SomeClass> Problem1(int productId, Region destination) {
		List<SomeClass> ans = new ArrayList<SomeClass>();
		Map<Integer, List<Inventory>> productToWarehouseMap = new HashMap<Integer, List<Inventory>>();
		Map<Region, List<ShippingCostNode>> warehouseToDestination = new HashMap<Region, List<ShippingCostNode>>();
		initialVariable(productToWarehouseMap, warehouseToDestination);
		
		if (productToWarehouseMap.containsKey(productId)) {
			List<Inventory> warehouseList = productToWarehouseMap.get(productId);
			
			for (Inventory inventory : warehouseList) {
				if (warehouseToDestination.containsKey(inventory.shipFrom)) {
					List<ShippingCostNode> shippingCostList = warehouseToDestination.get(inventory.shipFrom);					
					int index = findIndexOfTargetShippingCostNode(shippingCostList, destination);
					
					if (index < shippingCostList.size()) {
						ans.add(new SomeClass(inventory, shippingCostList.get(index).shippingCostList));
					}
				}
			}
		}
		
		return ans;
	}

	
	public static void initialVariable(Map<Integer, List<Inventory>> productToWarehouseMap, Map<Region, List<ShippingCostNode>> warehouseToDestination) {
		Inventory[] inventoryArray = aMain.getInventoryArray();
		ShippingCost[] shippingCostArray = aMain.getShippingCostArray();
		
		for (Inventory inventory : inventoryArray) {
			if (productToWarehouseMap.containsKey(inventory.productId)) {
				List<Inventory> warehouseList = productToWarehouseMap.get(inventory.productId);
				int index = findIndexOfTargetWarehouse(warehouseList, inventory.shipFrom);
				
				if (index == warehouseList.size()) {
					productToWarehouseMap.get(inventory.productId).add(inventory);
				}
			} else {
				List<Inventory> list = new ArrayList<>();
				list.add(inventory);
				productToWarehouseMap.put(inventory.productId, list);
			}
		}
		
		for (ShippingCost sc : shippingCostArray) {
			if (warehouseToDestination.containsKey(sc.shipFrom)) {
				List<ShippingCostNode> shippingCostList = warehouseToDestination.get(sc.shipFrom);
				int index = findIndexOfTargetShippingCostNode(shippingCostList, sc.shipTo);
				
				if (index < shippingCostList.size()) {
					warehouseToDestination.get(sc.shipFrom).get(index).shippingCostList.add(sc);
				} else {
					ShippingCostNode scn = new ShippingCostNode(sc.shipTo);
					scn.shippingCostList.add(sc);
					shippingCostList.add(scn);
					warehouseToDestination.put(sc.shipFrom, shippingCostList);
				}
			} else {
				List<ShippingCostNode> shippingCostList = new ArrayList<ShippingCostNode>();
				ShippingCostNode scn = new ShippingCostNode(sc.shipTo);
				scn.shippingCostList.add(sc);
				shippingCostList.add(scn);
				warehouseToDestination.put(sc.shipFrom, shippingCostList);
			}
		}
	}
	
	public static int findIndexOfTargetWarehouse(List<Inventory> warehouseList, Region warehouse) {
		int index = 0;
		
		while (index < warehouseList.size()) {
			if (warehouseList.get(index).shipFrom.equals(warehouse)) {
				break;
			} else {
				index++;
			}
		}
		
		return index;
	}
	
	public static int findIndexOfTargetShippingCostNode(List<ShippingCostNode> shippingCostList, Region destination) {
		int index = 0;
		
		while (index < shippingCostList.size()) {
			if (shippingCostList.get(index).destination.equals(destination)) {
				break;
			} else {
				index++;
			}
		}
		
		return index;
	}
}
	
class ShippingCostNode {
	Region destination;
	List<ShippingCost> shippingCostList;
		
	public ShippingCostNode(Region destination) {
		this.destination = destination;
		this.shippingCostList = new ArrayList<ShippingCost>();
	}
}
