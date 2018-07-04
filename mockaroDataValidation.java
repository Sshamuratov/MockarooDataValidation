package mockaroovalidation2;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class mockaroDataValidation {
	static WebDriver driver;
	Select select;
	String expected;
	String actual;
	static String temp;
	static List<String> data;
	static List<String> citiesList;
	static List<String> countryList;
	static BufferedReader br;

	@BeforeClass
	public void beforeClassRun() throws FileNotFoundException {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		br = new BufferedReader(new FileReader("C:\\Users\\bzikb\\Downloads\\MOCK_DATA.CSV"));
		data = new ArrayList<>();
		citiesList = new ArrayList<>();
		countryList = new ArrayList<>();
		driver.get("https://mockaroo.com/");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}

	@Test(priority = 0) // verifying url
	public void navigatingToWebSite() {
		expected = "https://mockaroo.com/";
		actual = driver.getCurrentUrl();
		assertEquals(actual, expected);
	}

	@Test(priority = 1) // Assert title is correct
	public void titleVerify() {
		expected = "Mockaroo - Random Data Generator and API Mocking Tool | JSON / CSV / SQL / Excel";
		actual = driver.getTitle();
		assertEquals(actual, expected);
	}

	@Test(priority = 2)
	public void headerTextVerify() { // Assert Mockaroo andrealistic data generator are displayed
		WebElement mockaroo = driver.findElement(By.xpath("//div[@class='brand']"));
		WebElement realisticDataGenerator = driver.findElement(By.xpath("//div[@class='tagline']"));
		assertTrue(mockaroo.isDisplayed());
		assertTrue(realisticDataGenerator.isDisplayed());
	}

	@Test(priority = 3)
	public void removeAllExistingFields() { // Remove all existing fields by clicking on x icon link
		removeXs();
		assertFalse(driver.findElement(By.xpath("//a[@class='close remove-field remove_nested_fields']")).isDisplayed());
	}

	@Test(priority = 4)
	public void verifyFieldTypeOptions() { // Assert that ‘Field Name’ , ‘Type’, ‘Options’ labels are displayed
		assertTrue(driver.findElement(By.xpath("//div[@class='column column-header column-name']")).isDisplayed());
		assertTrue(driver.findElement(By.xpath("//div[@class='column column-header column-type']")).isDisplayed());
		assertTrue(driver.findElement(By.xpath("//div[@class='column column-header column-options']")).isDisplayed());
	}

	@Test(priority = 5)
	public void verifyAddButtonEnabled() { // Assert that ‘Add another field’ button is enabled. Find using xpath
											// withtagname and text. isEnabled() method in selenium
		assertTrue(driver.findElement(By.xpath("//a[.='Add another field']")).isDisplayed());
	}

	@Test(priority = 6)
	public void verifyDefault1000() { // Assert thatdefault number of rows is 1000.
		actual = driver.findElement(By.xpath("//input[@id='num_rows']")).getAttribute("value");
		expected = "1000";
		assertEquals(actual, expected);
	}

	@Test(priority = 7)
	public void verifyDefaultCSV() { // Assert that default format selection is CSV
		select = new Select(driver.findElement(By.xpath("//select[@id='schema_file_format']")));
		actual = select.getFirstSelectedOption().getText();
		expected = "CSV";
		assertEquals(actual, expected);
	}

	@Test(priority = 8) // Assert that Line Ending is Unix(LF)
	public void verifyDefaultUnixLF() { // checking if default file type is "CSV"
		select = new Select(driver.findElement(By.xpath("//select[@id='schema_line_ending']")));
		actual = select.getFirstSelectedOption().getText();
		expected = "Unix (LF)";
		assertEquals(actual, expected);
	}

	@Test(priority = 9)
	public void checkHeaderAndBOM() { // Assert that header checkbox is checked and BOM is unchecked
		assertTrue(driver.findElement(By.xpath("//input[@id='schema_include_header']")).isSelected());
		assertFalse(driver.findElement(By.xpath("//input[@id='schema_bom']")).isSelected());
	}

	@Test(priority = 10)
	public void addFieldEnterCity() throws InterruptedException { // Click on ‘Add another field’ and enter name “City”
		driver.findElement(By.xpath("//a[.='Add another field']")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("(//input[@placeholder='enter name...'])[7]")).sendKeys("city");
	}

	@Test(priority = 10)
	public void chooseTypeBoxDisplay() throws InterruptedException { // Click on Choose type and assert that Choose a
																		// Type dialog box is displayed.
		driver.findElement(By.xpath("//*[@id=\"fields\"]/div[7]/div[3]/input[3]")).click();
		Thread.sleep(2000);
		assertTrue(driver.findElement(By.xpath("//h3[@class='modal-title']")).isDisplayed());
		Thread.sleep(3000);
	}

	@Test(priority = 11)
	public void searchForCityAndClick() throws InterruptedException { // Search for “city” and click on City on search
																		// results
		driver.findElement(By.xpath("//*[@id=\"type_search_field\"]")).sendKeys("city");
		driver.findElement(By.xpath("//*[@id=\"type_list\"]/div/div[3]")).click();
		Thread.sleep(3000);
	}

	@Test(priority = 12)
	public void addFieldEnterCountry() throws InterruptedException { // Click on ‘Add another field’ and enter name
																		// “Country”
		driver.findElement(By.xpath("//a[.='Add another field']")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("(//input[@placeholder='enter name...'])[8]")).sendKeys("country");
	}

	@Test(priority = 13)
	public void chooseTypeSearchCountry() throws InterruptedException { // Click on Choose type, search for country and
																		// click on Country search results and click
																		// download.
		driver.findElement(By.xpath("//*[@id=\"fields\"]/div[8]/div[3]/input[3]")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//*[@id=\"type_search_field\"]")).sendKeys("country");
		driver.findElement(By.xpath("//*[@id=\"type_list\"]/div[1]/div[1]")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//*[@id=\"download\"]")).click();
	}

	@Test(priority = 14)
	public void checkEnteredFieldsAndBR() throws Exception {// Open the downloaded file using BufferedReader, Assert
															// that first row is matching with Field names that we
															// selected.
		step171819();
	}

	@Test(priority = 15)
	public void check1000Records() throws Exception {// Assert that there are 1000 records
		assertEquals(getCityCountryList().size() - 1, 1000);

	}

	@Test(priority = 16)// From ﬁle add all Cities to Cities ArrayListAdd all countries to Countries ArrayLis
//	Sort all cities and ﬁnd the city with the longest name and shortest name
//	In Countries ArrayList, ﬁnd how many times each Country is mentioned. 
//	and print out
	public void addCityCountryToList() throws Exception {
		String longestCityName = "";
		String shortestCityName = "";
		List<String> citiesList = new ArrayList<>(getCityList());
		List<String> countryList = new ArrayList<>(getCountryList());
		Collections.sort(citiesList);
		Collections.sort(countryList);
		int longestCity = citiesList.get(0).length();
		int shortestCity = citiesList.get(0).length();
		int countryCount = 0;

		for (String eachCity : citiesList) {
			if (eachCity.length() > longestCity) {
				longestCity = eachCity.length();
				longestCityName = eachCity;
			}
		}
		for (String eachCity : citiesList) {
			if (eachCity.length() < shortestCity) {
				shortestCity = eachCity.length();
				shortestCityName = eachCity;
			}
		}

		System.out.println("longestCity: " + longestCity + " longestCityName: " + longestCityName);
		System.out.println("shortestCity: " + shortestCity + " shortestCityName: " + shortestCityName);

		SortedSet<String> sortedCountry = new TreeSet<>(countryList);
		for (String eachCountry : sortedCountry) {
			if ((countryCount = Collections.frequency(countryList, eachCountry)) > 0) {
				System.out.println(eachCountry + ": " + countryCount);
			}
		}
	}

	@Test(priority = 17)
//	From ﬁle add all Cities to citiesSet HashSet
//	Count how many unique cities are in Cities ArrayList and assert that
//	it is matching with the count of citiesSet HashSet.
//	Add all Countries to countrySet HashSet 
//	Count how many unique cities are in Countries ArrayList and assert that
//	it is matching with the count of countrySet HashSet.

	public void comparingUniqueCitiesAndCountries() throws Exception {
		List<String> uniqueListOfCities = new ArrayList<>(getCityList());
		for (int i = 0; i < uniqueListOfCities.size(); i++) {
			for (int j = i + 1; j < uniqueListOfCities.size(); j++) {
				if (uniqueListOfCities.get(i).equals(uniqueListOfCities.get(j))) {
					uniqueListOfCities.remove(j);
					j--;
				}
			}
		}
		
		List<String> uniqueListOfCountries = new ArrayList<>(getCountryList());
		for (int i = 0; i < uniqueListOfCountries.size(); i++) {
			for (int j = i + 1; j < uniqueListOfCountries.size(); j++) {
				if (uniqueListOfCountries.get(i).equals(uniqueListOfCountries.get(j))) {
					uniqueListOfCountries.remove(j);
					j--;
				}
			}
		}
		Set<String> citiesSet = new HashSet<>(getCityList());
		int setCityCount = citiesSet.size();
		int listCityCount = uniqueListOfCities.size();
		assertEquals(setCityCount, listCityCount);

		Set<String> countriesSet = new HashSet<>(getCountryList());
		int setCountryCount = countriesSet.size();
		int listCountryCount = uniqueListOfCountries.size();
		assertEquals(setCountryCount, listCountryCount);

	}

	@AfterClass
	public void afterClassRun() {
		System.out.println("Test COMPLETED, closing the browser...");
		driver.close();
	}

	public void removeXs() {
		List<WebElement> elem = driver.findElements(By.xpath("//a[@class='close remove-field remove_nested_fields']"));
		for (WebElement eachElement : elem) {
			eachElement.click();
		}
	}

	public static void step171819() throws Exception {
		String city = ""+ driver.findElement(By.xpath("(//input[@placeholder='enter name...'])[7]")).getAttribute("value");
		String country = ""+ driver.findElement(By.xpath("(//input[@placeholder='enter name...'])[8]")).getAttribute("value");
		String cityAndCountry = city + "," + country;
		assertEquals(cityAndCountry, getCityCountryList().get(0));
	}

	public static List<String> getCityCountryList() throws Exception {
		while ((temp = br.readLine()) != null) {
			data.add(temp);
		}
		return data;
	}

	public static List<String> getCityList() throws Exception {
		for (String each : getCityCountryList()) {
			citiesList.add(each.substring(0, each.indexOf(",")));
		}
		return citiesList;
	}

	public static List<String> getCountryList() throws Exception {
		for (String each : getCityCountryList()) {
			countryList.add(each.substring(each.indexOf(",") + 1));
		}
		return countryList;

	}

}
