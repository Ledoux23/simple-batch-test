Exemple de batch avec Spring Batch (traitement de base de données)
------------------------------------------------------------------
Spring Batch est un framework puissant pour automatiser les traitements de masse. 
Voici un exemple basique d'un batch qui lit des utilisateurs depuis une base SQL 
et les exporte vers un fichier.
-----------------------------------------------------
Voici un exemple de développement de batch en Java avec Spring Batch, en respectant 
les exigences suivantes :

✅ Utilisation de Spring Batch pour exécuter des tâches récurrentes sur des ensembles 
de données volumineux.
✅ Gestion des étapes (Step), des tâches (Tasklet), des lecteurs (ItemReader), 
processeurs (ItemProcessor) et écrivains (ItemWriter).
------------------------------------------------------
📌 Scénario du Batch
Nous avons une base de données contenant des utilisateurs.
Le batch va :
    1-Lire les utilisateurs depuis la base de données (ItemReader).
    2-Transformer les données (convertir le nom en majuscules) (ItemProcessor).
    3-Écrire les résultats dans un fichier CSV (ItemWriter).
    4-Exécuter ce processus sous forme de tâche planifiée.
--------------------------------------------------------
👉 H2 est utilisé comme base de données en mémoire pour simplifier les tests.
--------------------------------------------------------
L'entité Member représente notre table SQL. C'est le modèle.
--------------------------------------------------------
On utilise Spring Data JPA pour interagir avec la base.
--------------------------------------------------------
Le fichier(classe java) BatchConfig contient toutes les configurations du batch.
✅ Il configure le batch complet : Lecture (ItemReader), Traitement (ItemProcessor) 
et Écriture (ItemWriter).
--------------------------------------------------------
Initialisation de la Base de Données (data.sql)
✅ Ajoute ces données dans src/main/resources/data.sql pour tester notre batch.

INSERT INTO user (name, age) VALUES ('Alice', 25);
INSERT INTO user (name, age) VALUES ('Bob', 30);
INSERT INTO user (name, age) VALUES ('Charlie', 40);
--------------------------------------------------------
Exécution Automatique du Batch (BatchScheduler.java)
✅ Ajoutons une planification pour exécuter le batch toutes les 30 secondes.
✅ Ce fichier déclenche automatiquement le batch toutes les 30 secondes.

@Component
public class BatchScheduler {
    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @Scheduled(fixedRate = 30000) // Exécution toutes les 30 secondes
    public void runBatch() {
        try {
            jobLauncher.run(job, new JobParameters());
            System.out.println("Batch exécuté avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
--------------------------------------------------------
Lancer l'Application
--------------------------------------------------------
Résultat. Après exécution, le fichier members_output.csv est généré avec le contenu suivant :

id,name,age
1,ALICE,25
2,BOB,30
3,CHARLIE,40

✅ Les noms ont été transformés en majuscules !
✅ Le batch s’exécute automatiquement toutes les 30 secondes !
--------------------------------------------------------

🔹 Résumé
----------
Composant  et	Rôle
---------       ----
ItemReader	 : Lire les utilisateurs depuis la base
ItemProcessor: Transformer les noms en majuscules
ItemWriter	 : Enregistrer les résultats dans un fichier CSV
Step	       : Gérer un lot d'utilisateurs (5 par 5)
Job	         : Définir l'exécution du batch
BatchScheduler:	Automatiser l'exécution du batch

===============================================================

Ajouter ces dépendances à votre pom.xml (Maven) :

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-batch</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>

--------------------------------------------------------

Planification du batch avec cron
--------------------------------
Un batch peut être exécuté automatiquement à l'aide d'un scheduler, comme Spring Scheduler ou cron sous Linux.
Planification avec cron (exécution toutes les nuits à minuit)

Ajoutez cette tâche cron :

0 0 * * * java -jar batch-app.jar

Ou utilisez Spring :

@Component
public class BatchScheduler {
    @Scheduled(cron = "0 0 0 * * ?") // Tous les jours à minuit
    public void runBatch() {
        System.out.println("Exécution du batch...");
        // Appeler ici le batch (par exemple lancer une méthode qui exécute le Job Spring Batch)
    }
}
===============================================
Vérifier la configuration de la base de données
-----------------------------------------------
** Si la base de données n'est pas bien configurée dans application.properties, assure-toi 
d'avoir quelque chose comme :

spring.datasource.url=jdbc:mysql://localhost:3306/ma_base
spring.datasource.username=root
spring.datasource.password=motdepasse
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=update
-----------------------------------------------

** Si tu utilises H2 (base en mémoire), alors :

spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.h2.console.enabled=true
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

-----------------------------------------------
