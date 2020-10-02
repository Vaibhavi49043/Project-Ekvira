package com.online.shopping.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

import com.online.shopping.model.MenuItem;

public interface MenuItemRepository extends CrudRepository<MenuItem, Long> {
	List<MenuItem> findByItemName(String itemName);
}
