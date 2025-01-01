Feature: Connexion et Recherche des commandes et de la déconnexion

  Scenario: Recherche réussie d'une commande
    Given l'utilisateur est sur la page de connexion
    When il saisit l'identifiant "admin@admin.com" et le mot de passe "admin"
    And clique sur le bouton de connexion
    Then l'utilisateur est connecté avec succès
    Then recherche la commande avec l'id "61f5f7e2-5ede-4ea4-80b1-de3ed680bca9"
    Then clique sur le bouton de soumission
    And clique sur le premier résultat
    When il clique sur le bouton de déconnexion
    Then l'utilisateur est déconnecté avec succès

  Scenario: Recherche échouée d'une commande
    Given l'utilisateur est sur la page de connexion
    When il saisit l'identifiant "admin@admin.com" et le mot de passe "admin"
    And clique sur le bouton de connexion
    Then l'utilisateur est connecté avec succès
    Then recherche la commande avec l'id "invalid-command-id"
    Then clique sur le bouton de soumission
    Then une erreur est affichée et une capture d'écran est sauvegardée

  Scenario: Recherche échouée d'une connexion
    Given l'utilisateur est sur la page de connexion
    When il saisit l'identifiant "echouée@échouée.com" et le mot de passe "admin"
    And clique sur le bouton de connexion
    Then une erreur est connexion affichée et une capture d'écran est sauvegardée

  Scenario: Déconnexion réussie
    Given l'utilisateur est sur la page de connexion
    When il saisit l'identifiant "admin@admin.com" et le mot de passe "admin"
    And clique sur le bouton de connexion
    Then l'utilisateur est connecté avec succès
    When il clique sur le bouton de déconnexion
    Then l'utilisateur est déconnecté avec succès
