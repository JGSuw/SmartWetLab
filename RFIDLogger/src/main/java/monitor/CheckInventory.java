package monitor;

import java.util.TimerTask;

public class CheckInventory extends TimerTask {
	private Inventory inventory;
	public void run(){
		inventory.checkList();
	}
	public CheckInventory(Inventory inventory){
		this.inventory = inventory;
	}
}
