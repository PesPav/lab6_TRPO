package ru.volsu.qa;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.volsu.qa.models.Post;
import java.lang.Math;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ApiTests {

    public String access_token;
    public String resources;

    @BeforeClass
    public void beforeClass() {
        RestAssured.baseURI = "https://gorest.co.in";
        RestAssured.port = 443;
        access_token = "?access-token=-AbGdj2pL-4uMUUbEi4jQD2eHGGK3sqZa1lx";
        resources = "/public-api/users";
    }

    @DataProvider(name = "getFirst_nameDataProvider")
    public Object[][] first_nameDataProvider(){
        return new Object[][]{
                {"&first_name=Neva"},
                {"&first_name=Emelie"},
                {"&first_name=Ivah"}
        };
    }

    @DataProvider(name = "getIdUsersDataProvider")
    public Object[][] IdUsersDataProvider(){
        return new Object[][]{
                {"/26554"},
                {"/1544"},
                {"/1542"}
        };
    }

    @DataProvider(name = "deleteIdUsersDataProvider")
    public Object[][] DeleteIdUsersDataProvider(){
        return new Object[][]{
                {"/1542"}
        };
    }

    @DataProvider(name = "postRandomEmailDataProvider")
    public Object[][] RandomEmailDataProvider(){
        return new Object[][]{
                {"pestr"+Math.random()+"@mail.ru"}
        };
    }

    @Test
    public void testGetAllUsers() {
        given()
                .log().all()
        .when()
                .request("GET", resources+access_token)
        .then()
                .log().all()
                .statusCode(200);
    }

    @Test(dataProvider = "getFirst_nameDataProvider")
    public void testGetFirst_name(String firstname) {
        given()
                .log().all()
        .when()
                .request("GET", resources+access_token+firstname)
        .then()
                .log().all()
                .statusCode(200);
    }

    @Test(dataProvider = "getIdUsersDataProvider")
    public void testGetIdUser(String idUsers) {
        given()
                .log().all()
        .when()
                .request("GET", resources+idUsers+access_token)
        .then()
                .log().all()
                .statusCode(200);
    }

    @Test(dataProvider = "postRandomEmailDataProvider")
    public void testAddUsers(String randomEmail){
        Post newPost = new Post("Pavel", "Pestr", "male","01.01.1111",
                randomEmail,"+999-999-999", "https://gorest.co.in/","Volgograd","active");
        given()
                .log().body()
                .contentType(ContentType.JSON).body(newPost)
        .when()
                .post( resources+access_token)
        .then()
                .log().body()
                .assertThat()
                .body("result.first_name", equalTo(newPost.getFirst_name()))
                .statusCode(302);
    }

    @Test(dataProvider = "postRandomEmailDataProvider")
    public void testPutUsers(String randomEmail){
        Post newPost = new Post("Pavel", "Pestr", "male","01.01.1111",
                randomEmail,"+999-999-999", "https://gorest.co.in/","Volgograd","active");
        given()
                .log().body()
                .contentType(ContentType.JSON).body(newPost)
        .when()
                .put( resources+"/1544"+access_token)
        .then()
                .log().body()
                .assertThat()
                .body("result.first_name", equalTo(newPost.getFirst_name()))
                .statusCode(200);
    }

    @Test(dataProvider = "deleteIdUsersDataProvider")
    public void testDeleteIdUser(String idUsers) {
        given()
                .log().all()
        .when()
                .delete( resources+idUsers+access_token)
        .then()
                .log().all()
                .assertThat()
                .body("result", equalTo(null))
                .statusCode(200);
    }

    @DataProvider(name = "getUncorrectedUsersDataProvider")
    public Object[][] UncorrectedUsersDataProvider(){
        return new Object[][]{
                {""},
                {"?access-token=-AbGdj2pL-4"}
        };
    }

    @Test(dataProvider = "getUncorrectedUsersDataProvider")
    public void testUncorrectedGetAllUsers(String access_token) {

        try {
            given()
                    .log().all()
            .when()
                    .request("GET", resources + access_token)
            .then()
                    .log().all()
                    .assertThat()
                    .body("_meta.message", equalTo(""))
                    .statusCode(200);
        }catch (AssertionError e){
            System.out.println("Ожидаемый текст исключения: java.lang.AssertionError: 1 expectation failed.\n" +
                    "JSON path _meta.message doesn't match.\n" +
                    "Expected: \n" +
                    "  Actual: Authentication failed.\n");
            System.out.println("Текст исключения: " + e);
            Assert.assertEquals(e.toString(),"java.lang.AssertionError: 1 expectation failed.\n" +
                    "JSON path _meta.message doesn't match.\n" +
                    "Expected: \n" +
                    "  Actual: Authentication failed.\n");
        }

    }

    @Test
    public void testFailAddUsers(){
        Post newPost = new Post("Pavel", "Pestr", "male","01.01.1111",
                "pestr1@mail.ru","+999-999-999", "https://gorest.co.in/","Volgograd","active");
        try {
            given()
                    .log().body()
                    .contentType(ContentType.JSON).body(newPost)
            .when()
                    .post(resources + access_token)
            .then()
                    .log().body()
                    .assertThat()
                    .body("result.email", equalTo(newPost.getEmail()))
                    .statusCode(302);
        }catch(AssertionError e){
            System.out.println("Ожидаемый текст исключения: java.lang.AssertionError: 1 expectation failed.\n" +
                    "JSON path result.email doesn't match.\n" +
                    "Expected: pestr1@mail.ru\n" +
                    "  Actual: [null]\n");
            System.out.println("Текст исключения: " + e);
            Assert.assertEquals(e.toString(),"java.lang.AssertionError: 1 expectation failed.\n" +
                    "JSON path result.email doesn't match.\n" +
                    "Expected: pestr1@mail.ru\n" +
                    "  Actual: [null]\n");
        }
    }

    @Test(dataProvider = "deleteIdUsersDataProvider")
    public void testFailDeleteIdUser(String idUsers) {
        try {
            given()
                    .log().all()
            .when()
                    .delete(resources + idUsers + access_token)
            .then()
                    .log().all()
                    .assertThat()
                    .body("result", equalTo(null))
                    .statusCode(404);
        }catch(AssertionError e){
            System.out.println("Ожидаемый текст исключения: java.lang.AssertionError: 1 expectation failed.\n" +
                    "JSON path result doesn't match.\n" +
                    "Expected: null\n" +
                    "  Actual: {code=0, name=Not Found, message=Object not found: 1542, status=404}\n");
            System.out.println("Текст исключения: " + e);
            Assert.assertEquals(e.toString(),"java.lang.AssertionError: 1 expectation failed.\n" +
                    "JSON path result doesn't match.\n" +
                    "Expected: null\n" +
                    "  Actual: {code=0, name=Not Found, message=Object not found: 1542, status=404}\n");
        }
    }
}
