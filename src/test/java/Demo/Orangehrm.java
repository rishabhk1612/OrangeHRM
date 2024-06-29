package Demo;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeTest;
import org.testng.Assert;

//import junit.framework.Assert;

import org.testng.annotations.*;

public class Orangehrm {
	public String baseURL = "https://opensource-demo.orangehrmlive.com/web/index.php/auth/login";
	public WebDriver driver;

	@BeforeTest
	public void setup() {

		System.out.println("Before Test Executed");
		driver = new ChromeDriver();
		//Maximize windows
		driver.manage().window().maximize();

		//open url
		driver.get(baseURL);

		//timer
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));


	}


	@Test(priority = 1, enabled=false)
	public void loginWithInvalidCredentials() throws InterruptedException {

		//find username and enter username "Admin"
		driver.findElement(By.xpath("//input[@placeholder='Username']")).sendKeys("Admin");

		//find password and enter password "admin123
		driver.findElement(By.xpath("//input[@placeholder='Password']")).sendKeys("1234"); //Wrong Password

		//login button click
		driver.findElement(By.xpath("//button[@type='submit']")).submit();

		String message_expected = "Invalid credentials";
		String message_actual = driver.findElement(By.xpath("//p[@class='oxd-text oxd-text--p oxd-alert-content-text']")).getText();
		// Assert.assertTrue(message_actual.contains(message_expected));
		Assert.assertEquals(message_expected, message_actual);

		Thread.sleep(1500);

	}

	@Test(priority = 2, enabled=false)
	public void loginTestWithValidCredentials() {

		//find username and enter username "Admin"
		driver.findElement(By.xpath("//input[@placeholder='Username']")).sendKeys("Admin");

		//find password and enter password "admin123
		driver.findElement(By.xpath("//input[@placeholder='Password']")).sendKeys("admin123");

		//login button click
		driver.findElement(By.xpath("//button[@type='submit']")).submit();

		// verfiy the login was successful by checking the page title or a specific element
		String pageTitle = driver.getTitle();
		/*if(pageTitle.equals("OrangeHRM")){
			System.out.println("Login Successful!");
		} else {
			System.out.println("Login Failed");
		}*/

		logOut();

		Assert.assertEquals("OrangeHRM", pageTitle);


	}

	@Test (priority = 3, enabled=true )
	public void addEmployee() throws InterruptedException, IOException {

		logIn();

		//pim : //span[text()='PIM']
		// add employees = //a[text()='Add Employee']
		// //input[@name='firstName']
		////input[@name='lastName']
		// //button[@type='submit']
		// //button[normalize-space()='Save']

		//Find PIM menu and click on PIM
		driver.findElement(By.xpath("//span[text()='PIM']")).click();

		//Find add employee and click on add employee
		driver.findElement(By.xpath("//a[text()='Add Employee']")).click();

		// Enter first name
		driver.findElement(By.xpath("//input[@name='firstName']")).sendKeys("Mandvi");

		// Enter Last name
		driver.findElement(By.xpath("//input[@name='lastName']")).sendKeys("Tripathi");
		
		///////////////////AddImage/////////////////
		
		driver.findElement(By.xpath("//button[@class='oxd-icon-button oxd-icon-button--solid-main employee-image-action']")).click();
		Thread.sleep(5000);
		
		Runtime.getRuntime().exec("F://OrangrHRM//AddImageOrangeHRM.exe"); // deprecated from java 18
		
//		String filePath = "F:/OrangrHRM/Photo.jpg";
//		WebElement uploadPhoto = driver.findElement(By.xpath("//button[@class='oxd-icon-button oxd-icon-button--solid-main employee-image-action']"));
//		Thread.sleep(5000);
//		uploadPhoto.sendKeys(filePath);
		
		Thread.sleep(5000);
		
		///////////////////////////////////////////

		//Thread.sleep(2000);
		//Find Save button and click on it
		driver.findElement(By.xpath("//button[normalize-space()='Save']")).click();

		//Find employee ID and clear already mentioned ID
		//		WebElement EmpID = driver.findElement(By.xpath("//input[@class='oxd-input oxd-input--active oxd-input--error']"));
		//		EmpID.clear();
		//		EmpID.sendKeys("564612");

		//Verify that the employee is successfully added by checking the eployee list personal details
		String confirmationMessage = driver.findElement(By.xpath("//h6[normalize-space()='Personal Details']")).getText();
		if (confirmationMessage.contains("Personal Details")) {
			System.out.println("Employee added successfully!");
		}  else {
			System.out.println("Failed to add Employee");
		}
		// other way of verification

		logOut();
		Assert.assertEquals(confirmationMessage, "Personal Details");

	}

	//search employee by name 
	@Test (priority = 4, enabled=false)
	public void searchEmployeeByName() throws InterruptedException {

		logIn();
		//Find PIM menu and click on PIM
		driver.findElement(By.xpath("//span[text()='PIM']")).click();

		//Find Employee List and click on it
		driver.findElement(By.xpath("//a[normalize-space()='Employee List']")).click();

		//Enter Employee name
		driver.findElements(By.tagName("input")).get(1).sendKeys("mandvi");

		//click on search button
		driver.findElement(By.xpath("//button[normalize-space()='Search']")).click();


		////span[@class='oxd-text oxd-text--span']

		Thread.sleep(5000);
		List<WebElement> element = driver.findElements(By.xpath("//span[@class='oxd-text oxd-text--span']"));

		String expected_message = "Records Found";
		String message_actual= element.get(0).getText();
		System.out.println(message_actual);

		logOut();
		Assert.assertTrue(message_actual.contains(expected_message));

		//for loop to check the index of found records/no records found using list of elements
		/*for(int i=0; i<element.size(); i++) {
				System.out.println("At index "+ i + "text is: "+ element.get(i).getText()); ;
			}*/

	}


	@Test (priority = 5, enabled = false)
	public void searchEmployeeById() throws InterruptedException {

		String empId = "0377";
		String message_actual = ""; 
		logIn();
		//Find PIM menu and click on PIM
		driver.findElement(By.xpath("//span[text()='PIM']")).click();

		//Find Employee List and click on it
		driver.findElement(By.xpath("//a[normalize-space()='Employee List']")).click();

		//Enter Employee ID
		driver.findElements(By.tagName("input")).get(2).sendKeys(empId);


		//click on search button
		driver.findElement(By.xpath("//button[normalize-space()='Search']")).click();


		Thread.sleep(5000);

		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("window.scrollBy(0," + 500 + ")");

		//verification of employee ID present or not
		//If row is greater then 1, that means it have record

		List<WebElement> rows = driver.findElements(By.xpath("(//div[@role='row'])"));

		if(rows.size()>1) {

			//Check for the employee ID
			message_actual = driver.findElement(By.xpath("((//div[@role='row'])[2]/div[@role='cell'])[2]")).getText();

		}

		logOut();
		Assert.assertEquals(message_actual, empId);


	}


	@Test (priority = 6, enabled = false)
	public void fileUpload() throws IOException, InterruptedException {

		logIn();

		//Find PIM menu and click on PIM
		driver.findElement(By.xpath("//span[text()='PIM']")).click();
		
		//Click on Configuration
		driver.findElement(By.xpath("//span[@class='oxd-topbar-body-nav-tab-item']")).click();
		
		//Click on Data Import
		driver.findElement(By.partialLinkText("Data ")).click();
		
		//Click on Browse to upload file
		driver.findElement(By.xpath("//div[@class='oxd-file-button']")).click();
		
		Thread.sleep(5000);
		
		Runtime.getRuntime().exec("F://OrangrHRM//FileUploadOrangeHRM.exe"); // File path
		
		Thread.sleep(5000);
		
		//Click on Upload Button
		driver.findElement(By.xpath("//button[@type='submit']")).submit();
		
		logOut();
	}
	

	public void logIn() {


		//find username and enter username "Admin"
		driver.findElement(By.xpath("//input[@placeholder='Username']")).sendKeys("Admin");

		//find password and enter password "admin123
		driver.findElement(By.xpath("//input[@placeholder='Password']")).sendKeys("admin123");

		//login button click
		driver.findElement(By.xpath("//button[@type='submit']")).submit();

	}

	public void logOut() {

		driver.findElement(By.xpath("//p[@class='oxd-userdropdown-name']")).click();
		//driver.findElement(By.linkText("Logout")).click();
		List <WebElement> elementlist = driver.findElements(By.xpath("//a[@class='oxd-userdropdown-link']"));

		/*for (int i = 0; i<elementlist.size(); i++) {
			Thread.sleep(1000);
			System.out.println(i + ":" + elementlist.get(i).getText());	
		}*/ //Important for loop hai ye 

		elementlist.get(3).click(); //click on 3rd element logout
	}

	@AfterTest
	public void tearDown() throws InterruptedException {

		Thread.sleep(5000); //wait for 5 seconds before quit
		driver.close();
		driver.quit();
	}

}
