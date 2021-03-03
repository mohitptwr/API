import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.testng.Assert;

import files.Payload;
import files.ReusableMethods;
public class Basics {
	public static void main(String[] args) {
		
		
		//validate if add place API is working as expected
		
		//given- all input details
		//when-submit all the API's and https(POST,PUT,DELETE,GET)
		//Then- validate the respose
		RestAssured.baseURI="https://rahulshettyacademy.com";
		String response=given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body(Payload.AddPlace()).when().post("maps/api/place/add/json")
		.then().assertThat().statusCode(200).body("scope",equalTo("APP"))
		.header("Server", "Apache/2.4.18 (Ubuntu)").extract().response().asString();
		
		System.out.println(response);
		
		JsonPath js=new JsonPath(response); //for parsing  JSON we use JsonPath class and it will take string as response
		String placeId=js.get("place_id");
		
		System.out.println(placeId);
		
		//Add place ->Update place with new address ->Get place to validate  if new address is present in response
		
		//Update Place
		String newAddress="E-8 Boston WTC United States";
		given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body("{\r\n"
				+ "\"place_id\":\""+placeId+"\",\r\n"
				+ "\"address\":\""+newAddress+"\",\r\n"
				+ "\"key\":\"qaclick123\"\r\n"
				+ "}")
		.when().put("maps/api/place/update/json")
		.then().assertThat().log().all().statusCode(200).body("msg", equalTo("Address successfully updated"));
		
		//Get Place
		
		
		String getPlaceResponse=given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeId)
		.when().get("maps/api/place/get/json")
		.then().assertThat().log().all().statusCode(200).extract().response().asString();
		
		JsonPath jp = ReusableMethods.rawToJson(getPlaceResponse);
		String actualAddress=jp.get("address");
		Assert.assertEquals(actualAddress, newAddress);
		
	}

}
