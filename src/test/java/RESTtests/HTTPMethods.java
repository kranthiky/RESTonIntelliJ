package RESTtests;

import Utilities.Commons;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.google.gson.JsonObject;
import io.restassured.http.ContentType;
import org.json.simple.JSONObject;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.awt.peer.CanvasPeer;
import java.lang.reflect.Method;
import java.sql.Time;
import java.util.concurrent.TimeUnit;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class HTTPMethods {
    public static ExtentSparkReporter htmlReporter;
    public static ExtentReports extent;
    public static ExtentTest logger;

    @BeforeTest
    public void setup(){
        baseURI ="https://reqres.in";
        htmlReporter = new ExtentSparkReporter(System.getProperty("user.dir")+"/report/"+"TestExecution_"+ Commons.date+".html");
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
    }
    @Test(priority = 1)
    public void GETmethod(Method testMethod){
        logger = extent.createTest(testMethod.getName());
        given().
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
        with().
               // pathParam("id",7).
                 queryParam("page",2).
        when().
             //   get("/api/users/{id}").
                 get("/api/users").
         then().
                 statusCode(200).
                 header("Content-Type",containsString("application/json")).
                 time(lessThan(10L), TimeUnit.SECONDS).
                 body("data.last_name",hasItems("Lawson","Ferguson")).
                 log().body();
        logger.log(Status.PASS,"GET Method working well");
    }
    @Test(priority = 2)
    public void POSTmethod(Method testMethod){
        logger = extent.createTest(testMethod.getName());
        JSONObject req = new JSONObject();
        req.put("email","test@gmail.com");
        req.put("first_name","kumar");
        req.put("last_name","Duh");

        given().
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
                body(req.toJSONString()).
        when().
                post("/api/users").
        then().
                statusCode(201).
                time(lessThan(2L), TimeUnit.SECONDS).
         //       body("data.last_name",hasItem("Duh")).
                log().body();
        logger.log(Status.PASS,"POST working well");
    }
    @Test(priority = 3)
    public void PUTmethod(Method testMethod){
        logger = extent.createTest(testMethod.getName());
        JSONObject request = new JSONObject();
        request.put("email","hey@test.com");
        request.put("first_name","Sow");
        request.put("last_name","mya");

        given().
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
                body(request.toJSONString()).
        with().
                pathParam("id",2).
        when().
                put("api/users/{id}").
        then().
                statusCode(200).
                time(lessThan(5L),TimeUnit.SECONDS).
             //   body("data.last_name",hasItem("mya")).
                log().body();
        logger.log(Status.PASS,"PUT working well");
    }
    @Test(priority = 4)
    public void PATCHmethod(Method testMethod){
        logger = extent.createTest(testMethod.getName());
        JSONObject request = new JSONObject();
        request.put("first_name","Sow");

        given().
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
                body(request.toJSONString()).
        with().
                pathParam("id",2).
        when().
                patch("/api/user/{id}").
        then().
                statusCode(200).
                time(lessThan(5L),TimeUnit.SECONDS).
                log().body();
        logger.log(Status.PASS,"PATCH working well");
    }
    @Test(priority = 5)
    public void Deletemethod(Method testMethod){
        logger = extent.createTest(testMethod.getName());
        given().
                accept(ContentType.JSON).
        with().
                pathParam("id",2).
        when().
                delete("/api/users/{id}").
        then().
                statusCode(204).
                log().body();
        logger.log(Status.PASS,"DELETE working well");
    }
    @AfterTest
    public void tearDown(){
        extent.flush();
    }
}
