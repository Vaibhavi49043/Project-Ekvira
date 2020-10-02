package com.online.shopping.model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "menu_item")
public class MenuItem extends BasicEntitiy {

	@Column(name = "item_name", nullable = false, unique = true)
	private String itemName;

	@Column(name = "price_per_item", nullable = false)
	private Double pricePerItem;

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Double getPricePerItem() {
		return pricePerItem;
	}

	public void setPricePerItem(Double pricePerItem) {
		this.pricePerItem = pricePerItem;
	}

	@Override
	public String toString() {
		return "MenuItem [itemName=" + itemName + ", pricePerItem=" + pricePerItem + "]";
	}
}