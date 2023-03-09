package account;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static io.restassured.RestAssured.given;

public class AccountTest {

    @Test
    @DisplayName("Teste validar a autorização")
    public void testAuthorizedSuccessfully(){
        var response = given()
                                .contentType(ContentType.JSON)
                                .body("""
                        {
                          "userName": "DaniloTorquato",
                          "password": "Danilo123@"
                        }
                        """)
                        .when()
                                .post("https://demoqa.com/Account/v1/Authorized")
                        .then()
                                .statusCode(200)
                ;
        //response.log().body();
    }
    @Test
    @DisplayName("Teste validar a autorização")
    public void testAuthorizedUnsuccessfully(){
        var response = given()
                                .contentType(ContentType.JSON)
                                .body("""
                                             {
                                                "userName": "DaniloTorquato",
                                                "password": "invalid"
                                             }
                                   """)
                        .when()
                                .post("https://demoqa.com/Account/v1/Authorized")
                        .then()
                                .statusCode(404)
                ;
        //response.log().body();
    }

    @Test
    @DisplayName("Teste para validar a função de gerar token com Username e senha válidos")
    public void testGenerateTokenSuccessfully(){
        var response = given()
                                .contentType(ContentType.JSON)
                                .body("""
                                        {
                                          "userName": "DaniloTorquato",
                                          "password": "Danilo123@"
                                        }
                                        """)
                        .when()
                                .post("https://demoqa.com/Account/v1/GenerateToken")
                        .then()
                                .statusCode(200)
                ;
        //response.log().body();
    }
    @Test
    @DisplayName("Teste para validar a função de gerar token com UserId válido e senha inválida")
    public void testGenerateTokenUnsuccessfully(){
        var response = given()
                                .contentType(ContentType.JSON)
                                .body("""
                                        {
                                          "userName": "DaniloTorquato",
                                          "password": "invalid"
                                        }
                                        """)
                        .when()
                                .post("https://demoqa.com/Account/v1/Authorized")
                        .then()
                                .statusCode(404)
                ;
        //response.log().body();
    }

    @Test
    @DisplayName("Teste para Validar criação de usuário com UserName e senha válidos")
    public void testAuthorizeUserSuccessfully() {
        var response = given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "userName": "DaniloTest", 
                          "password": "Danilo123@"
                        }
                        """)
                .when()
                .post("https://demoqa.com/Account/v1/User")
                .then()
                .statusCode(201);
        //response.log().body();
    }
    @Test
    @DisplayName("Teste validar a autorização do usuário com UserId válido e senha inválida")
    public void testAuthorizeUserUnsuccessfully(){
        var response = given()
                                .contentType(ContentType.JSON)
                                .body("""
                                        {
                                          "userName": "DaniloTorquato",
                                          "password": "invalid"
                                        }
                                        """)
                        .when()
                                .post("https://demoqa.com/Account/v1/User")
                        .then()
                                .statusCode(400)
                ;
        //response.log().body();
    }

    @Test
    @DisplayName("Teste para validar a requisição de GET user com UserId válido")
    public void testGetUserSuccessfully(){
        given()
                .auth().preemptive().basic("DaniloTorquato", "Danilo123@")
                .contentType(ContentType.JSON)
                .pathParam("UUID", "22873cc6-c228-4db9-b3c8-ffe1f206b72f")
        .when()
                .get("https://demoqa.com/Account/v1/User/{UUID}")
        .then()
                .statusCode(200)
        ;
        //response.log().body();
    }
    @Test
    @DisplayName("Teste validar a requisição de GET user com UserId inválido")
    public void testGetUserUnsuccessfully(){
        given()
                .auth().preemptive().basic("DaniloTorquato", "Danilo123@")
                .contentType(ContentType.JSON)
                .pathParam("UUID", "invalid")
        .when()
                .get("https://demoqa.com/Account/v1/User/{UUID}")
        .then()
                .statusCode(401)
        ;
        //response.log().body();
    }
}
