from flask import Flask, request, jsonify
import joblib
import numpy as np
from flask_cors import CORS

app = Flask(__name__)
CORS(app)

# Load the models
education_model = joblib.load("model/education_model.pkl")
marriage_model = joblib.load("model/marriage_model.pkl")

@app.route("/predict", methods=["POST"])
def predict():
    data = request.get_json()

    # Required fields
    try:
        model_type = data.get("modelType")  # "education" or "marriage"
        salary = float(data.get("salary"))
        expenses = float(data.get("expenses"))
        estimated_total_cost = float(data.get("estimatedTotalCost"))
        months_left = int(data.get("monthsLeft"))
    except (TypeError, ValueError, KeyError):
        return jsonify({"error": "Missing or invalid input fields"}), 400

    # Create feature array with 6 features to match your trained model
    if model_type == "education":
        # For education model: [salary, expenses, months_until_education, months_until_marriage, projected_education_cost, projected_marriage_cost]
        x = np.array([[salary, expenses, months_left, 0, estimated_total_cost, 0]])
    elif model_type == "marriage":
        # For marriage model: [salary, expenses, months_until_education, months_until_marriage, projected_education_cost, projected_marriage_cost]
        x = np.array([[salary, expenses, 0, months_left, 0, estimated_total_cost]])
    else:
        return jsonify({"error": "Invalid modelType. Use 'education' or 'marriage'."}), 400

    # Predict using the appropriate model
    if model_type == "education":
        prediction = education_model.predict(x)[0]
    elif model_type == "marriage":
        prediction = marriage_model.predict(x)[0]

    return jsonify({
        "monthlyContribution": round(prediction, 2),
        "status": "success"
    })

@app.route("/", methods=["GET"])
def health_check():
    return "Flask ML API is running.", 200

if __name__ == "__main__":
    app.run(debug=True)
