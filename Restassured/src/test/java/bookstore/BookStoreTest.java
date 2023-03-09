package bookstore;

import static io.restassured.RestAssured.given;
import static io.restassured.specification.ProxySpecification.auth;
import static org.hamcrest.Matchers.equalTo;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;




public class BookStoreTest {

    @Test
    @DisplayName("Teste para buscar todos os livros disponíveis")
    public void testSearchAllBooksGet(){
        var response = given()
                        .when()
                                .get("https://demoqa.com/BookStore/v1/Books")
                        .then()
                                .statusCode(200)
                        ;
        //response.log().body();
    }

    @Test
    @DisplayName("Teste para buscar todos os livros utilizando método não disponível no endpoint")
    public void testSearchAllBooksPost(){
        var response = given()
                        .when()
                             .post("https://demoqa.com/BookStore/v1/Books")
                        .then()
                                .statusCode(415)
                        ;
        //response.log().body();
        //Conversar com o desenvolvimento sobre o código retornado.
    }

    @Test
    @DisplayName("Teste para buscar livro pelo ISBN, com ISBN válido")
    public void testSearchBookByValidIsbn(){
        final String ISBN =  "9781449331818";
        given()
                .queryParam("ISBN", ISBN)
        .when()
                .get("https://demoqa.com/BookStore/v1/Book")
        .then()
                .statusCode(200)
        ;
    }

    @Test
    @DisplayName("Teste para buscar livro pelo ISBN, com ISBN inválido")
    public void testsearchBookByInvalidIsbn(){
        final String ISBN =  "invalid";
        given()
                .queryParam("ISBN", ISBN)
        .when()
                .get("https://demoqa.com/BookStore/v1/Book")
        .then()
                .statusCode(400)
        ;
    }

    @Test
    @DisplayName("Teste para cadastrar livro com UserId e ISBN válidos")
    public void testRegisterBookSuccessfully(){

        given()
                .auth().preemptive().basic("DaniloTorquato", "Danilo123@")
                .queryParam("UserId", "22873cc6-c228-4db9-b3c8-ffe1f206b72f")
                .when()
                .delete("https://demoqa.com/BookStore/v1/Books")
                .then()
                .statusCode(204)
        ;

        var response = given()
                                .auth().preemptive().basic("DaniloTorquato", "Danilo123@")
                                .contentType(ContentType.JSON)
                                .body("""
                                        {
                                          "userId": "22873cc6-c228-4db9-b3c8-ffe1f206b72f",
                                          "collectionOfIsbns": [
                                            {
                                              "isbn": "9781449331818"
                                            }
                                          ]
                                        }
                                        """)
                        .when()
                                .post("https://demoqa.com/BookStore/v1/Books")
                        .then()
                                .statusCode(201)
                ;
        //response.log().all();
    }

    @Test
    @DisplayName("Teste para cadastrar livro com UserId inválido e ISBN válido")
    public void testRegisterBookUnsuccessfully(){
        var response = given()
                                .auth().preemptive().basic("invalidUserId", "Danilo123@")
                                .contentType(ContentType.JSON)
                                .body("""
                                        {
                                          "userId": "22873cc6-c228-4db9-b3c8-ffe1f206b72f",
                                          "collectionOfIsbns": [
                                            {
                                              "isbn": "9781449331818"
                                            }
                                          ]
                                        }
                                        """)
                        .when()
                                .post("https://demoqa.com/BookStore/v1/Books")
                        .then()
                                .statusCode(401)
                        ;
        //response.log().all();
    }

    @Nested
    class TestNest{

        @BeforeEach
        public void init() {
            var response =
                    given()
                            .auth().preemptive().basic("DaniloTorquato", "Danilo123@")
                            .queryParam("UserId", "22873cc6-c228-4db9-b3c8-ffe1f206b72f")
                    .when()
                            .delete("https://demoqa.com/BookStore/v1/Books")
                    .then()
                            .statusCode(204)
                    ;
            response.log().body();

                    given()
                            .auth().preemptive().basic("DaniloTorquato", "Danilo123@")
                            .contentType(ContentType.JSON)
                            .body("""
                                        {
                                          "userId": "22873cc6-c228-4db9-b3c8-ffe1f206b72f",
                                          "collectionOfIsbns": [
                                            {
                                              "isbn": "9781449331818"
                                            }
                                          ]
                                        }
                                        """)
                    .when()
                            .post("https://demoqa.com/BookStore/v1/Books")
                    .then()
                            .statusCode(201)
                    ;
        }

        @Test
        @DisplayName("Teste para atualizar livro com UserId, UserName, ISBN e senha inválida")
        public void testUpdateBookSuccessfully() {
            var response =
                    given()
                            .auth().preemptive().basic("DaniloTorquato", "Danilo123@")
                            .contentType(ContentType.JSON)
                            .body("""
                                {
                                  "userId": "22873cc6-c228-4db9-b3c8-ffe1f206b72f",
                                  "isbn": "9781449325862"
                                }
                                """)
                            .pathParam("ISBN", "9781449331818")
                    .when()
                            .put("https://demoqa.com/BookStore/v1/Books/{ISBN}")
                    .then()
                            .statusCode(200)
                    ;
            //response.log().body();
        }

        @Test
        @DisplayName("Teste para atualizar livro com UserId, UserName e ISBN válidos, e senha inválida")
        public void testUpdateBookUnsuccessfully() {
            var response =
                    given()
                            .auth().preemptive().basic("DaniloTorquato", "senhaInvalida")
                            .contentType(ContentType.JSON)
                            .body("""
                                {
                                  "userId": "22873cc6-c228-4db9-b3c8-ffe1f206b72f",
                                  "isbn": "9781449325862"
                                }
                                """)
                            .pathParam("ISBN", "9781449331818")
                    .when()
                            .put("https://demoqa.com/BookStore/v1/Books/{ISBN}")
                    .then()
                            .statusCode(401)
                    ;
            //response.log().body();
        }
    }
}
