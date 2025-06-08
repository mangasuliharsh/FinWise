# app.py - Fixed version
from flask import Flask, request, jsonify
import numpy as np
import pandas as pd
from sklearn.ensemble import RandomForestRegressor
from sklearn.preprocessing import StandardScaler
from sklearn.model_selection import train_test_split
import joblib
import logging
from datetime import datetime
import os
import warnings
warnings.filterwarnings('ignore')

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

app = Flask(__name__)

class PlanAllocationModel:
    def __init__(self):
        self.scaler = StandardScaler()
        # Fixed: Use compatible RandomForestRegressor parameters
        self.priority_model = RandomForestRegressor(
            n_estimators=100,
            random_state=42,
            max_depth=10
        )
        self.is_trained = False
        self.model_path = "models/"
        self.ensure_model_directory()
        
    def ensure_model_directory(self):
        """Create models directory if it doesn't exist"""
        os.makedirs(self.model_path, exist_ok=True)
    
    def generate_synthetic_data(self, n_samples=1000):
        """Generate synthetic training data - reduced samples for faster training"""
        logger.info(f"Generating {n_samples} synthetic training samples")
        
        data = []
        np.random.seed(42)
        
        for _ in range(n_samples):
            monthly_savings = np.random.uniform(5000, 50000)
            num_plans = np.random.randint(1, 4)  # Reduced for faster generation
            
            for i in range(num_plans):
                plan_type = np.random.choice(['education', 'marriage'])
                
                if plan_type == 'education':
                    years_to_target = np.random.randint(1, 15)
                    total_cost = np.random.uniform(200000, 1500000)
                else:
                    years_to_target = np.random.randint(1, 8)
                    total_cost = np.random.uniform(500000, 2000000)
                
                current_savings = np.random.uniform(0, total_cost * 0.2)
                inflation_rate = np.random.uniform(4, 8)
                
                features = self.extract_plan_features({
                    'estimated_total_cost': total_cost,
                    'current_savings': current_savings,
                    'years_to_target': years_to_target,
                    'inflation_rate': inflation_rate,
                    'plan_type': plan_type
                }, monthly_savings)
                
                # Business logic for optimal priority
                urgency_factor = 1 / max(1, years_to_target)
                progress_factor = 1 - (current_savings / total_cost)
                requirement_factor = min(1, (total_cost - current_savings) / (monthly_savings * years_to_target * 12))
                
                optimal_priority = (urgency_factor * 0.4 + progress_factor * 0.3 + requirement_factor * 0.3)
                optimal_priority = max(0.1, min(0.9, optimal_priority))  # Clamp values
                
                data.append({
                    'features': features,
                    'priority': optimal_priority
                })
        
        return data
    
    def extract_plan_features(self, plan_data, monthly_savings):
        """Extract features from plan data with error handling"""
        try:
            current_year = datetime.now().year
            
            # Handle different plan types
            if 'estimated_start_year' in plan_data:
                years_to_target = max(1, plan_data['estimated_start_year'] - current_year)
            elif 'estimated_year' in plan_data:
                years_to_target = max(1, plan_data['estimated_year'] - current_year)
            else:
                years_to_target = plan_data.get('years_to_target', 5)
            
            total_cost = max(1, plan_data['estimated_total_cost'])  # Avoid division by zero
            current_savings = max(0, plan_data.get('current_savings', 0))
            inflation_rate = plan_data.get('inflation_rate', 6.0) / 100
            monthly_savings = max(1, monthly_savings)  # Avoid division by zero
            
            # Calculate inflation-adjusted cost
            future_cost = total_cost * ((1 + inflation_rate) ** years_to_target)
            funding_gap = max(0, future_cost - current_savings)
            
            # Required monthly contribution
            months_remaining = max(1, years_to_target * 12)
            required_monthly = funding_gap / months_remaining
            
            features = [
                min(20, years_to_target),  # Cap at 20 years
                min(1, funding_gap / future_cost),  # Funding gap ratio
                min(1, current_savings / total_cost),  # Progress ratio
                min(5, required_monthly / monthly_savings),  # Requirement ratio (capped)
                min(1, 1 / max(1, years_to_target)),  # Urgency score
                min(10, future_cost / (monthly_savings * 12)),  # Cost-to-income ratio (capped)
                min(2, (future_cost - total_cost) / total_cost),  # Inflation impact (capped)
                1 if plan_data.get('plan_type') == 'education' else 0,  # Plan type
                min(1, 2 / max(1, years_to_target)),  # Completion urgency
                min(5, np.log1p(total_cost / 100000))  # Log-scaled cost (capped)
            ]
            
            return features
            
        except Exception as e:
            logger.error(f"Error extracting features: {e}")
            # Return default features
            return [5, 0.5, 0.1, 0.3, 0.2, 1.0, 0.1, 0, 0.4, 2.0]
    
    def train_model(self):
        """Train the allocation model with better error handling"""
        logger.info("Training allocation model...")
        
        try:
            training_data = self.generate_synthetic_data()
            
            if not training_data:
                logger.error("No training data generated")
                return False
            
            X = np.array([item['features'] for item in training_data])
            y = np.array([item['priority'] for item in training_data])
            
            # Validate data shapes
            if X.shape[0] == 0 or len(X.shape) != 2:
                logger.error(f"Invalid feature matrix shape: {X.shape}")
                return False
            
            logger.info(f"Training data shape: X={X.shape}, y={y.shape}")
            
            # Split data
            X_train, X_test, y_train, y_test = train_test_split(
                X, y, test_size=0.2, random_state=42
            )
            
            # Scale features
            X_train_scaled = self.scaler.fit_transform(X_train)
            X_test_scaled = self.scaler.transform(X_test)
            
            # Train model
            self.priority_model.fit(X_train_scaled, y_train)
            
            # Evaluate
            train_score = self.priority_model.score(X_train_scaled, y_train)
            test_score = self.priority_model.score(X_test_scaled, y_test)
            
            logger.info(f"Model trained successfully - Train Score: {train_score:.4f}, Test Score: {test_score:.4f}")
            
            # Save model
            self.save_model()
            self.is_trained = True
            
            return True
            
        except Exception as e:
            logger.error(f"Error training model: {e}")
            return False
    
    def save_model(self):
        """Save the trained model"""
        try:
            joblib.dump(self.priority_model, f"{self.model_path}priority_model.pkl")
            joblib.dump(self.scaler, f"{self.model_path}scaler.pkl")
            logger.info("Model saved successfully")
        except Exception as e:
            logger.error(f"Error saving model: {e}")
    
    def load_model(self):
        """Load the trained model"""
        try:
            if (os.path.exists(f"{self.model_path}priority_model.pkl") and 
                os.path.exists(f"{self.model_path}scaler.pkl")):
                
                self.priority_model = joblib.load(f"{self.model_path}priority_model.pkl")
                self.scaler = joblib.load(f"{self.model_path}scaler.pkl")
                self.is_trained = True
                logger.info("Model loaded successfully")
                return True
        except Exception as e:
            logger.error(f"Error loading model: {e}")
        
        return False
    
    def predict_priority(self, plan_features):
        """Predict priority score for a plan"""
        if not self.is_trained:
            return 0.5
        
        try:
            features_array = np.array(plan_features).reshape(1, -1)
            features_scaled = self.scaler.transform(features_array)
            priority = self.priority_model.predict(features_scaled)[0]
            return max(0.1, min(0.9, priority))
        except Exception as e:
            logger.error(f"Error predicting priority: {e}")
            return 0.5

class SmartAllocationEngine:
    def __init__(self, model: PlanAllocationModel):
        self.model = model
    
    def allocate_savings(self, total_monthly_savings, education_plans, marriage_plans):
        """Main allocation function"""
        if total_monthly_savings <= 0:
            return {}
        
        all_plans = []
        
        # Process education plans
        for plan in education_plans:
            try:
                features = self.model.extract_plan_features(plan, total_monthly_savings)
                priority = self.model.predict_priority(features)
                
                all_plans.append({
                    'id': f"education_{plan['id']}",
                    'type': 'education',
                    'priority': priority,
                    'plan_data': plan
                })
            except Exception as e:
                logger.error(f"Error processing education plan: {e}")
                continue
        
        # Process marriage plans
        for plan in marriage_plans:
            try:
                features = self.model.extract_plan_features(plan, total_monthly_savings)
                priority = self.model.predict_priority(features)
                
                all_plans.append({
                    'id': f"marriage_{plan['id']}",
                    'type': 'marriage',
                    'priority': priority,
                    'plan_data': plan
                })
            except Exception as e:
                logger.error(f"Error processing marriage plan: {e}")
                continue
        
        if not all_plans:
            return {}
        
        return self.perform_smart_allocation(total_monthly_savings, all_plans)
    
    def perform_smart_allocation(self, total_savings, plans):
        """Perform intelligent allocation based on priorities"""
        if not plans:
            return {}
        
        priorities = [plan['priority'] for plan in plans]
        total_priority = sum(priorities)
        
        if total_priority == 0:
            equal_amount = total_savings / len(plans)
            return {plan['id']: round(equal_amount, 2) for plan in plans}
        
        allocations = {}
        min_allocation = total_savings * 0.05  # 5% minimum
        
        for plan in plans:
            try:
                weight = plan['priority'] / total_priority
                allocation = max(min_allocation, total_savings * weight)
                allocations[plan['id']] = round(allocation, 2)
            except Exception as e:
                logger.error(f"Error calculating allocation: {e}")
                allocations[plan['id']] = round(min_allocation, 2)
        
        # Normalize if total exceeds available savings
        total_allocated = sum(allocations.values())
        if total_allocated > total_savings:
            factor = total_savings / total_allocated
            for plan_id in allocations:
                allocations[plan_id] = round(allocations[plan_id] * factor, 2)
        
        return allocations

# Initialize models
allocation_model = PlanAllocationModel()
allocation_engine = SmartAllocationEngine(allocation_model)

@app.route('/health', methods=['GET'])
def health_check():
    """Health check endpoint"""
    return jsonify({
        'status': 'healthy',
        'model_trained': allocation_model.is_trained,
        'timestamp': datetime.now().isoformat()
    })

@app.route('/allocate', methods=['POST'])
def allocate_savings():
    """Main allocation endpoint"""
    try:
        data = request.get_json()
        
        if not data:
            return jsonify({'error': 'No data provided'}), 400
        
        total_monthly_savings = data.get('totalMonthlySavings', 0)
        education_plans = data.get('educationPlans', [])
        marriage_plans = data.get('marriagePlans', [])
        
        if total_monthly_savings <= 0:
            return jsonify({'error': 'Invalid monthly savings amount'}), 400
        
        allocations = allocation_engine.allocate_savings(
            total_monthly_savings, education_plans, marriage_plans
        )
        
        total_allocated = sum(allocations.values())
        
        return jsonify({
            'success': True,
            'allocations': allocations,
            'summary': {
                'total_monthly_savings': total_monthly_savings,
                'total_allocated': round(total_allocated, 2),
                'number_of_plans': len(allocations),
                'timestamp': datetime.now().isoformat()
            }
        })
        
    except Exception as e:
        logger.error(f"Error in allocation: {e}")
        return jsonify({'success': False, 'error': str(e)}), 500

@app.route('/retrain', methods=['POST'])
def retrain_model():
    """Retrain the model"""
    try:
        success = allocation_model.train_model()
        return jsonify({
            'success': success,
            'message': 'Model retrained successfully' if success else 'Training failed'
        })
    except Exception as e:
        return jsonify({'success': False, 'error': str(e)}), 500

if __name__ == '__main__':
    # Load or train model
    if not allocation_model.load_model():
        logger.info("Training new model...")
        allocation_model.train_model()
    
    app.run(host='0.0.0.0', port=5000, debug=False)
