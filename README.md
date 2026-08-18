# Gestion Bibliothèque — INFSP 500 Sétif

A desktop library management application built with **Java Swing** and **JDBC/MySQL**. It lets staff log in, and depending on their role, manage mémoires (theses), students, and loans.

## Features

- Login screen authenticating against a MySQL backend
- Role-based access: different tabs are shown depending on the connected user's role (`logAdmin`, `gestion`, or default)
- **Mémoires** — browse/manage thesis records
- **Étudiants** — manage student records (admin only)
- **Emprunts** — manage book/thesis loans (admin and gestion roles)

## Tech Stack

- Java (Swing for the UI)
- JDBC
- MySQL

## Project Structure

```
bibliotheque_Interface/
├── LoginForm.java      # Login screen; authenticates and launches MainFrame
├── MainFrame.java      # Main window; shows tabs based on user role
├── DBManager.java      # Holds DB credentials/connection, shared across the app
├── PanelMemoire.java   # "Mémoires" tab
├── PanelEtudiant.java  # "Étudiants" tab (admin only)
└── PanelEmprunt.java   # "Emprunts" tab (admin and gestion)
```

## Getting Started

### Prerequisites

- Java JDK 8+
- MySQL Server
- MySQL Connector/J (JDBC driver) on the classpath

### Database Setup
   ---You can run the script (GestBibl_mysql.sql) from the /MYSQL_DB folder using the command : 
  ' sudo mysql -u root -p <GestBibl_mysql.sql ' on linux , from the file directory 
  ,or simply import the file to a GUI SGBD like DBeaver or MySQL Workbench ... 


  or, You can create it manually using these queries :
1. Create the database:
   ```sql
   CREATE DATABASE GestBibl;
   ```
2. Create tables (adjust to your actual schema), e.g.:
   ```sql
   CREATE TABLE MEMOIRE (
       id_memoire INT PRIMARY KEY AUTO_INCREMENT,
       titre VARCHAR(255) NOT NULL
   );
   ```
3. Create MySQL user accounts matching the roles the app expects (`logAdmin`, `gestion`, etc.), with the appropriate `GRANT` privileges, since login credentials map directly to a MySQL account.

### Configuration

Update the DB connection URL in `DBManager.java` (or `LoginForm.java`) .

```java
private static final String URL = "jdbc:mysql://<your-host>:3306/GestBibl";
```

### Running

1. Compile all `.java` files (or open the project in your IDE of choice).
2. Run `LoginForm` — this is the application entry point.
3. Log in with valid MySQL credentials for the database above.
4. Based on the resolved MySQL user, the app opens `MainFrame` with the appropriate tabs.

## Architecture Notes

The app follows a simple layered approach:

- **UI layer** — `LoginForm`, `MainFrame`, and the `Panel*` classes handle only display and user interaction.
- **Data access** — `DBManager` centralizes DB credentials/connections so no other class opens its own connection.

## Known Limitations / Roadmap

- [ ] Move raw SQL out of the panels into dedicated DAO classes (`MemoireDAO`, `EtudiantDAO`, `EmpruntDAO`)
- [ ] Introduce plain model classes (`Memoire`, `Etudiant`, `Emprunt`) instead of passing `ResultSet`s around
- [ ] Replace MySQL-account-as-login with an application-level `utilisateurs` table (login, hashed password, role) authenticated through a single shared service account
- [ ] Add input validation and clearer error messages on the login screen

## License


This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

