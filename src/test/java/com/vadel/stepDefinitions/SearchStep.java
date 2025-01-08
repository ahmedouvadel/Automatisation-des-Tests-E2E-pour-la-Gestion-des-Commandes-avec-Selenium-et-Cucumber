package com.vadel.stepDefinitions;

import com.vadel.base.TestBase;
import com.vadel.pages.LoginPage;
import com.vadel.pages.SearchPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SearchStep {

    WebDriver driver = TestBase.getDriver();
    SearchPage searchPage = new SearchPage(driver);

    @Then("recherche la commande avec l'id {string}")
    public void saisit_l_id_de_commande(String commandId) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("searchCommandes")));

            searchInput.clear();
            searchInput.sendKeys(commandId);

        } catch (Exception e) {
            searchPage.captureScreenshot("search_input_error");
            throw e;
        }
    }

    @Then("clique sur le bouton de soumission")
    public void clique_sur_le_bouton_de_soumission() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("searchCommandButton")));
            submitButton.click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@class='table']")));

        } catch (Exception e) {
            searchPage.captureScreenshot("submit_error");
            throw e;
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

}
