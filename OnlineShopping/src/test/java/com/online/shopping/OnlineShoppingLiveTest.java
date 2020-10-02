package com.online.shopping;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.online.shopping.MathUtility.MathUtils;
import com.online.shopping.model.MenuItem;
import com.online.shopping.repository.MenuItemRepository;

import io.restassured.RestAssured;
import io.restassured.response.Response;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OnlineShoppingLiveTest {

	@Autowired
	private MenuItemRepository menuItemRepository;

	@LocalServerPort
	private int port;

    static MenuItemRepository staticMenuItemRepository;   

    @Autowired
    public void setStaticMenuItemRepository(MenuItemRepository menuItemRepository) {
    	OnlineShoppingLiveTest.staticMenuItemRepository = menuItemRepository;
    }

	@AfterClass
	public static void tearDown() {
		staticMenuItemRepository.deleteAll();
	}

	@Test
	public void whenGetAllMenuItems_thenOK() {
		final Response response = RestAssured.get(createURLWithPort());
		assertEquals(HttpStatus.OK.value(), response.getStatusCode());
	}

	@Test
	public void whenGetMenuItemsByItemName_thenOK() {
		final MenuItem menuItem = createRandomMenuItem();
		createMenuItemAsUri(menuItem);

		final Response response = RestAssured.get(createURLWithPort() + "/name/" + menuItem.getItemName());
		assertEquals(HttpStatus.OK.value(), response.getStatusCode());
		assertTrue(response.as(List.class).size() > 0);
	}

	@Test
	public void whenGetCreatedMenuItemById_thenOK() {
		final MenuItem menuItem = createRandomMenuItem();
		final String location = createMenuItemAsUri(menuItem);

		final Response response = RestAssured.get(location);
		assertEquals(HttpStatus.OK.value(), response.getStatusCode());
		assertEquals(menuItem.getItemName(), response.jsonPath().get("itemName"));
	}

	@Test
	public void whenGetNotExistMenuItemById_thenNotFound() {
		final Response response = RestAssured.get(createURLWithPort() + "/" + randomNumeric(4));
		assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
	}

	// POST
	@Test
	public void whenCreateNewMenuItem_thenCreated() {
		final MenuItem menuItem = createRandomMenuItem();

		final Response response = RestAssured.given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(menuItem)
				.post(createURLWithPort());
		assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
	}

	@Test
	public void whenInvalidMenuItem_thenError() {
		final MenuItem menuItem = createRandomMenuItem();
		menuItem.setItemName(null);

		final Response response = RestAssured.given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(menuItem)
				.post(createURLWithPort());
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
	}

	@Test
	public void whenUpdateCreatedMenuItem_thenUpdated() {
		final MenuItem menuItem = createRandomMenuItem();
		final String location = createMenuItemAsUri(menuItem);

		menuItem.setId(Long.parseLong(location.split("api/menuitems/")[1]));
		menuItem.setPricePerItem(MathUtils.round(110d, 2));
		Response response = RestAssured.given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(menuItem)
				.put(location);
		assertEquals(HttpStatus.OK.value(), response.getStatusCode());

		response = RestAssured.get(location);
		assertEquals(HttpStatus.OK.value(), response.getStatusCode());
		assertEquals(Double.valueOf(110d), Double.valueOf(response.jsonPath().get("pricePerItem").toString()));

	}

	@Test
	public void whenDeleteCreatedMenuItem_thenOk() {
		final MenuItem menuItem = createRandomMenuItem();
		final String location = createMenuItemAsUri(menuItem);

		Response response = RestAssured.delete(location);
		assertEquals(HttpStatus.OK.value(), response.getStatusCode());

		response = RestAssured.get(location);
		assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
	}

	private MenuItem createRandomMenuItem() {
		MenuItem menuItem = new MenuItem();
		menuItem.setItemName(randomAlphabetic(10));
		menuItem.setPricePerItem(MathUtils.round(111.11, 2));
		return menuItem;
	}

	private String createMenuItemAsUri(MenuItem menuItem) {
		Response response = RestAssured.given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(menuItem)
				.post(createURLWithPort());
		return createURLWithPort() + "/" + response.jsonPath().get("id");
	}

	private String createURLWithPort() {
		return "http://localhost:"+port+"/api/menuitems";
	}
}
