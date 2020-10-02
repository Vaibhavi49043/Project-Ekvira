package com.online.shopping.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.online.shopping.exception.ResourceNotFoundException;
import com.online.shopping.model.MenuItem;
import com.online.shopping.repository.MenuItemRepository;

@RestController
@RequestMapping("/api/menuitems")
public class MenuItemController {

	@Autowired
	private MenuItemRepository menuItemRepository;

	@GetMapping
	public Iterable<MenuItem> findAll() {
		return menuItemRepository.findAll();
	}

	@GetMapping("/name/{menuItemName}")
	public List<MenuItem> findByItemName(@PathVariable String menuItemName) {
		return menuItemRepository.findByItemName(menuItemName);
	}

	@GetMapping("/{id}")
	public MenuItem findOne(@PathVariable Long id) {
		return menuItemRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public MenuItem create(@RequestBody MenuItem menuItem) {
		return menuItemRepository.save(menuItem);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		menuItemRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
		menuItemRepository.deleteById(id);
	}

	@PutMapping("/{id}")
	public MenuItem updateBook(@RequestBody MenuItem menuItem, @PathVariable Long id) {
		/*
		 * if (book.getId() != id) { throw new BookIdMismatchException(); }
		 */
		menuItemRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
		return menuItemRepository.save(menuItem);
	}
}
