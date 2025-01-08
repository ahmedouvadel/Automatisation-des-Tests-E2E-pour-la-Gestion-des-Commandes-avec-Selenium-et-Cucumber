package com.vadel.stepDefinitions;

import com.vadel.base.TestBase;
import com.vadel.pages.LoginPage;
import com.vadel.pages.LogoutPage;
import com.vadel.pages.SearchPage;
import io.cucumber.java.en.*;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class LoginSteps {

    WebDriver driver = TestBase.getDriver();
    LoginPage loginPage = new LoginPage(driver);

    @Given("l'utilisateur est sur la page de connexion")
    public void l_utilisateur_est_sur_la_page_de_connexion() {
        driver.get("http://localhost:4200"); // Remplacer par ton URL réel
    }

    // Étape 2 : Saisir les identifiants
    @When("il saisit l'identifiant {string} et le mot de passe {string}")
    public void il_saisit_l_identifiant_et_le_mot_de_passe(String email, String password) {
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
    }

    // Étape 3 : Cliquer sur le bouton de connexion
    @And("clique sur le bouton de connexion")
    public void clique_sur_le_bouton_de_connexion() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(loginPage.getSignInButton()));
        loginPage.clickSignIn();
    }

    @Then("l'utilisateur est connecté avec succès")
    public void l_utilisateur_est_connecte_avec_succes() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        boolean isConnected = wait.until(ExpectedConditions.urlContains("/Accueil"));
        assert isConnected : "L'utilisateur n'est pas redirigé vers la page d'accueil.";
    }

    @Then("une erreur est connexion affichée et une capture d'écran est sauvegardée")
    public void une_erreur_est_connexion_affichee_et_une_capture_d_ecran_est_sauvegardee() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'alert-secondary')]")));

            assert errorMessage.isDisplayed() : "Le message d'erreur de connexion n'est pas affiché.";

            loginPage.captureScreenshot("login_error_displayed");
            System.out.println("Message d'erreur affiché et capture d'écran sauvegardée avec succès.");

        } catch (Exception e) {
            // Capture en cas d'exception
            LoginPage loginPage = new LoginPage(driver);
            loginPage.captureScreenshot("login_error_exception");
            System.err.println("Erreur détectée pendant la vérification de l'erreur de connexion.");
            throw e; // Relancer l'exception
        }
    }

    @Then("un message d'erreur est affiché")
    public void un_message_d_erreur_est_affiche() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        boolean isErrorDisplayed = wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("error-message"), "Invalid credentials"));
        assert isErrorDisplayed : "Le message d'erreur attendu n'est pas affiché.";
    }
}
