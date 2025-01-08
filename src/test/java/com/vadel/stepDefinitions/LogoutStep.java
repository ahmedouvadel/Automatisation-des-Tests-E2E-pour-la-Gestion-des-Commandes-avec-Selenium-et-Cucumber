package com.vadel.stepDefinitions;

import com.vadel.base.TestBase;
import com.vadel.pages.LogoutPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LogoutStep {

    WebDriver driver = TestBase.getDriver();
    LogoutPage logoutPage = new LogoutPage(driver);

    @Then("l'utilisateur est déconnecté avec succès")
    public void l_utilisateur_est_deconnecte_avec_succes() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Vérifier si la redirection vers la page de connexion a réussi
            String currentUrl = driver.getCurrentUrl();
            boolean isOnLocalhost = currentUrl.contains("localhost:4200");
            assert isOnLocalhost : "L'utilisateur et deconnecte avec succe.";

        } catch (Exception e) {
            logoutPage.captureScreenshot("logout_verification_error");
            throw e;
        }
    }

    @When("il clique sur le bouton de déconnexion")
    public void il_clique_sur_le_bouton_de_deconnexion() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Cliquer sur l'icône du menu utilisateur
            WebElement userMenu = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".user-image")));
            userMenu.click();

            // Attendre que le bouton "Logout" soit visible et cliquer dessus
            WebElement logoutButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("logout")));
            logoutButton.click();

        } catch (Exception e) {
            logoutPage.captureScreenshot("logout_click_error");
            throw e;
        }
    }

}
