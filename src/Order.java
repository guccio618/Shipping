public class Order {
       int productId;
       Region destination;
       int needDays;
       int needQuantity;
       
       public Order(int productId, Region destination, int needDays, int needQuantity) {
    	   this.needDays = needDays;
    	   this.productId = productId;
    	   this.destination = destination;
    	   this.needQuantity = needQuantity;
       }
       public Order() {
    	   
       }
}
