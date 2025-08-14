# FinWise - Intelligent Financial Planning for Families

**FinWise** is a modern, full-stack application designed to empower families to collaboratively manage their finances and plan for major life goals like education and marriage. It leverages a powerful backend, an intuitive frontend, and a machine learning model to provide intelligent insights and a seamless user experience.

---

## ✨ Key Features

*   **Collaborative Goal Setting:** Enables multiple family members to jointly contribute to financial goals and track shared expenses.
*   **Multi-Goal Savings Plans:** Create, manage, and prioritize savings across multiple streams:
    *   Education Planning
    *   Marriage Planning
    *   General Savings & Investments
*   **Intelligent Allocation:** A Random Forest ML model (92% accuracy) provides smart recommendations on how to best allocate savings.
*   **Dynamic Dashboard:** Visualize real-time financial indicators, track goal progress, and view recent activity at a glance.
*   **Secure Authentication:** Robust, session-based authentication to protect sensitive financial data.
*   **Comprehensive Reporting:** Generate reports to understand financial health and progress over time.
*   **Real-time Notifications:** Stay updated with timely alerts on goal milestones and transaction activity.

---

## 🛠️ Technology Stack

The project is built on a modern, decoupled three-tier architecture.

*   **Frontend:**
    *   **Framework:** React.js
    *   **Styling:** CSS, App.css (can be extended with a framework like Material-UI or Bootstrap)
    *   **State Management:** React Hooks / Context API

*   **Backend:**
    *   **Framework:** Spring Boot (Java)
    *   **Database:** MySQL
    *   **ORM:** Spring Data JPA (Hibernate)
    *   **Security:** Spring Security
    *   **Build Tool:** Maven

*   **Machine Learning Service:**
    *   **Framework:** Flask (Python)
    *   **Model:** Scikit-learn (Random Forest)

---

## 🏗️ System Architecture

FinWise uses a scalable three-tier system to decouple services and ensure maintainability. An API gateway would be the entry point, routing requests to the appropriate service.

```
+-----------------+      +------------------+      +--------------------+
|                 |      |                  |      |                    |
|  React Frontend |----->| Spring Boot Backend |----->|  Python ML Service |
| (User Interface)|      |  (Business Logic) |      | (Prediction Model) |
|                 |      |                  |      |                    |
+-----------------+      +------------------+      +--------------------+
       |                        | (JPA)
       |                        |
       v                        v
+-------------------------------------------------+
|                                                 |
|                    Database (MySQL)             |
|                                                 |
+-------------------------------------------------+

```

---

## 🚀 Getting Started

To get a local copy up and running, follow these simple steps.

### Prerequisites

*   **Java 17+** and **Maven**
*   **Node.js** and **npm**
*   **Python** and **pip**
*   A running **PostgreSQL** instance

### Installation & Setup

1.  **Clone the repository:**
    ```sh
    git clone https://github.com/mangasuliharsh/FinWise.git
    cd FinWise
    ```

2.  **Setup the Database:**
    *   Create a new MySQL database.
    *   Run the `backend/src/main/resources/scripts/ddl.sql` script to create the necessary tables.
    *   Update the database connection details in `backend/src/main/resources/application.properties`.

3.  **Run the Backend (Spring Boot):**
    ```sh
    cd backend
    ./mvnw spring-boot:run
    ```
    The backend server will start on `http://localhost:8080`.

4.  **Run the Frontend (React):**
    ```sh
    cd ../frontend
    npm install
    npm start
    ```
    The React development server will start and open the app on `http://localhost:3000`.

5.  **Run the ML Service (Flask):**
    ```sh
    cd ../ml-flask-api
    pip install -r requirements.txt
    python app.py
    ```
    The Flask API will be available at `http://localhost:5000`.

---

## 📄 API Documentation

The backend exposes a full suite of RESTful APIs. For detailed information on the available endpoints, please see the `api-endpoints.md` file in the `/backend` directory.

---

## 🤝 Contributing

Contributions, issues, and feature requests are welcome! Feel free to check the issues page.

---

## 📜 License

Distributed under the MIT License. See `LICENSE` for more information.
