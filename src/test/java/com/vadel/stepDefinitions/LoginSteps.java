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
    SearchPage searchPage = new SearchPage(driver);
    LogoutPage logoutPage = new LogoutPage(driver);

    // Étape 1 : Accéder à la page de connexion
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

    // Étape 4 : Vérifier la connexion réussie
    @Then("l'utilisateur est connecté avec succès")
    public void l_utilisateur_est_connecte_avec_succes() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        boolean isConnected = wait.until(ExpectedConditions.urlContains("/Accueil"));
        assert isConnected : "L'utilisateur n'est pas redirigé vers la page d'accueil.";
    }

    // Étape 5 : Rechercher une commande
    /*@Then("recherche la commande avec l'id {string}")
    public void recherche_commande(String commandId) {
        try {
            searchPage.searchCommand(commandId);
            boolean isDisplayed = searchPage.getFirstResult().isDisplayed();
            assert isDisplayed : "Aucun résultat trouvé pour la commande : " + commandId;
            searchPage.clickFirstResult();
        } catch (Exception e) {
            searchPage.captureScreenshot("search_error");
            throw e;
        }
    }*/

    @Then("recherche la commande avec l'id {string}")
    public void saisit_l_id_de_commande(String commandId) {
        try {
            // Attendre que le champ de recherche soit visible
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("searchCommandes")));

            // Saisir l'ID de la commande
            searchInput.clear(); // Effacer tout texte existant
            searchInput.sendKeys(commandId);

        } catch (Exception e) {
            searchPage.captureScreenshot("search_input_error");
            throw e; // Lancer une exception en cas d'erreur
        }
    }


    @Then("clique sur le bouton de soumission")
    public void clique_sur_le_bouton_de_soumission() {
        try {
            // Attendre que le bouton soit visible et cliquable
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("searchCommandButton")));

            // Cliquer sur le bouton de soumission
            submitButton.click();

            // **Attendre la fin du traitement des résultats du back-end**
            // Supposons que les résultats apparaissent dans un tableau avec un ID "resultsTable"
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@class='table']")));

        } catch (Exception e) {
            searchPage.captureScreenshot("submit_error");
            throw e; // Lancer une exception en cas d'échec
        }
    }

    @And("clique sur le premier résultat")
    public void clique_sur_le_premier_resultat() {
        try {
            // Attendre que la première cellule (td) dans la première ligne (tr) soit visible et cliquable
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement firstCell = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//tbody/tr[1]/td[1]") // Sélectionne le premier <td> dans le premier <tr>
            ));

            // Cliquer sur la première cellule
            firstCell.click();

        } catch (Exception e) {
            // Capture d'écran en cas d'échec
            searchPage.captureScreenshot("click_first_td_error");
            throw e; // Relancer l'exception pour un diagnostic ultérieur
        }
    }

    @Then("une erreur est connexion affichée et une capture d'écran est sauvegardée")
    public void une_erreur_est_connexion_affichee_et_une_capture_d_ecran_est_sauvegardee() {
        try {
            // Attendre que le message d'erreur soit visible
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Localiser l'élément du message d'erreur avec son sélecteur CSS ou XPath
            WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'alert-secondary')]")));

            // Vérifier si le message d'erreur est visible
            assert errorMessage.isDisplayed() : "Le message d'erreur de connexion n'est pas affiché.";

            // Capture d'écran via LoginPage
            loginPage.captureScreenshot("login_error_displayed"); // Sauvegarde la capture

            System.out.println("Message d'erreur affiché et capture d'écran sauvegardée avec succès.");

        } catch (Exception e) {
            // Capture en cas d'exception
            LoginPage loginPage = new LoginPage(driver);
            loginPage.captureScreenshot("login_error_exception");
            System.err.println("Erreur détectée pendant la vérification de l'erreur de connexion.");
            throw e; // Relancer l'exception
        }
    }


    @Then("une erreur est affichée et une capture d'écran est sauvegardée")
    public void une_erreur_est_affichee_et_une_capture_d_ecran_est_sauvegardee() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Vérifier si le message "Aucun command trouvé pour le moment." est affiché
            WebElement noResultMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(text(), 'Aucun command trouvé pour le moment.')]")
            ));

            // Vérifier que le message d'erreur est bien affiché
            assert noResultMessage.isDisplayed() : "Le message 'Aucun command trouvé pour le moment.' n'est pas affiché.";

            // Sauvegarder une capture d'écran si l'erreur est présente
            searchPage.captureScreenshot("no_result_error");
            System.out.println("Erreur détectée : Aucun résultat trouvé. Capture d'écran sauvegardée.");

        } catch (Exception e) {
            // Capture d'écran supplémentaire en cas d'exception
            searchPage.captureScreenshot("exception_error");
            System.err.println("Erreur inattendue détectée, capture d'écran sauvegardée.");
            throw e; // Relancer l'exception pour gérer l'échec du test
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
            searchPage.captureScreenshot("logout_click_error");
            throw e;
        }
    }

    @Then("l'utilisateur est déconnecté avec succès")
    public void l_utilisateur_est_deconnecte_avec_succes() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Vérifier si la redirection vers la page de connexion a réussi
            String currentUrl = driver.getCurrentUrl();
            boolean isOnLocalhost = currentUrl.contains("localhost:4200");
            assert isOnLocalhost : "L'utilisateur et deconnecte avec succe.";

        } catch (Exception e) {
            searchPage.captureScreenshot("logout_verification_error");
            throw e;
        }
    }


    // Étape 7 : Déconnexion
    /*@Then("déconnecte l'utilisateur si sur localhost")
    public void deconnecte_utilisateur_si_localhost() {
        try {
            if (driver.getCurrentUrl().contains("localhost:4200")) {
                logoutPage.clickLogout();
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                wait.until(ExpectedConditions.urlContains("/login"));
            }
        } catch (Exception e) {
            logoutPage.captureScreenshot("logout_error");
            throw e;
        }
    }*/



    // Étape 8 : Vérification d'un message d'erreur
    @Then("un message d'erreur est affiché")
    public void un_message_d_erreur_est_affiche() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        boolean isErrorDisplayed = wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("error-message"), "Invalid credentials"));
        assert isErrorDisplayed : "Le message d'erreur attendu n'est pas affiché.";
    }
}
