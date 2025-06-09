# app.py - Complete Updated Version with Comprehensive Evaluation
from flask import Flask, request, jsonify
import numpy as np
import pandas as pd
from sklearn.ensemble import RandomForestRegressor
from sklearn.preprocessing import StandardScaler
from sklearn.model_selection import train_test_split, cross_val_score
from sklearn.metrics import mean_absolute_error, mean_squared_error, r2_score
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
        self.priority_model = RandomForestRegressor(
            n_estimators=100,
            random_state=42,
            max_depth=10,
            oob_score=True  # Enable out-of-bag scoring for evaluation
        )
        self.is_trained = False
        self.model_path = "models/"
        self.ensure_model_directory()
        
    def ensure_model_directory(self):
        """Create models directory if it doesn't exist"""
        os.makedirs(self.model_path, exist_ok=True)
    
    def generate_synthetic_data(self, n_samples=1000):
        """Generate synthetic training data with overfunding scenarios"""
        logger.info(f"Generating {n_samples} synthetic training samples")
        
        data = []
        np.random.seed(42)
        
        for _ in range(n_samples):
            monthly_savings = np.random.uniform(5000, 50000)
            num_plans = np.random.randint(1, 4)
            
            for i in range(num_plans):
                plan_type = np.random.choice(['education', 'marriage'])
                
                if plan_type == 'education':
                    years_to_target = np.random.randint(1, 15)
                    total_cost = np.random.uniform(200000, 1500000)
                else:
                    years_to_target = np.random.randint(1, 8)
                    total_cost = np.random.uniform(500000, 2000000)
                
                # Include overfunded scenarios (20% chance)
                if np.random.random() < 0.2:
                    current_savings = np.random.uniform(total_cost * 0.8, total_cost * 1.5)
                else:
                    current_savings = np.random.uniform(0, total_cost * 0.6)
                
                inflation_rate = np.random.uniform(4, 8)
                
                features = self.extract_plan_features({
                    'estimated_total_cost': total_cost,
                    'current_savings': current_savings,
                    'years_to_target': years_to_target,
                    'inflation_rate': inflation_rate,
                    'plan_type': plan_type
                }, monthly_savings)
                
                # Enhanced priority calculation with overfunding handling
                optimal_priority = self.calculate_priority_score({
                    'estimated_total_cost': total_cost,
                    'current_savings': current_savings,
                    'years_to_target': years_to_target,
                    'inflation_rate': inflation_rate,
                    'plan_type': plan_type
                }, monthly_savings)
                
                data.append({
                    'features': features,
                    'priority': optimal_priority
                })
        
        return data
    
    def calculate_priority_score(self, plan_data, monthly_savings):
        """Enhanced priority calculation that handles overfunded plans properly"""
        current_savings = plan_data.get('current_savings', 0)
        total_cost = plan_data['estimated_total_cost']
        years_to_target = plan_data.get('years_to_target', 5)
        inflation_rate = plan_data.get('inflation_rate', 6.0) / 100
        
        # Calculate inflation-adjusted future cost
        future_cost = total_cost * ((1 + inflation_rate) ** years_to_target)
        
        # Check if plan is overfunded
        if current_savings >= future_cost:
            return 0.05  # Minimal priority for overfunded plans
        
        # Calculate components for underfunded plans
        funding_gap = future_cost - current_savings
        urgency_factor = min(1.0, 1 / max(1, years_to_target))
        progress_factor = min(1.0, funding_gap / future_cost)
        requirement_factor = min(1.0, funding_gap / (monthly_savings * years_to_target * 12))
        
        # Weighted priority score
        priority = (urgency_factor * 0.4 + progress_factor * 0.3 + requirement_factor * 0.3)
        return max(0.1, min(0.9, priority))
    
    def extract_plan_features(self, plan_data, monthly_savings):
        """Enhanced feature extraction with overfunding awareness"""
        try:
            current_year = datetime.now().year
            years_to_target = max(1, plan_data.get('years_to_target', 5))
            total_cost = max(1, plan_data['estimated_total_cost'])
            current_savings = max(0, plan_data.get('current_savings', 0))
            inflation_rate = plan_data.get('inflation_rate', 6.0) / 100
            monthly_savings = max(1, monthly_savings)
            
            # Calculate inflation-adjusted cost
            future_cost = total_cost * ((1 + inflation_rate) ** years_to_target)
            
            # Handle overfunded case
            if current_savings >= future_cost:
                return [
                    20,  # Max years (low urgency)
                    0,   # No funding gap
                    1,   # 100% progress
                    0,   # No additional requirement
                    0,   # No urgency
                    0,   # No cost pressure
                    0,   # Inflation already covered
                    1 if plan_data.get('plan_type') == 'education' else 0,
                    0,   # No completion urgency
                    0    # Minimal cost factor
                ]
            
            # Standard calculation for underfunded plans
            funding_gap = future_cost - current_savings
            months_remaining = max(1, years_to_target * 12)
            required_monthly = funding_gap / months_remaining
            
            features = [
                min(20, years_to_target),
                min(1, funding_gap / future_cost),
                min(1, current_savings / total_cost),
                min(5, required_monthly / monthly_savings),
                min(1, 1 / max(1, years_to_target)),
                min(10, future_cost / (monthly_savings * 12)),
                min(2, (future_cost - total_cost) / total_cost),
                1 if plan_data.get('plan_type') == 'education' else 0,
                min(1, 2 / max(1, years_to_target)),
                min(5, np.log1p(total_cost / 100000))
            ]
            
            return features
            
        except Exception as e:
            logger.error(f"Error extracting features: {e}")
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
            
            if X.shape[0] == 0 or len(X.shape) != 2:
                logger.error(f"Invalid feature matrix shape: {X.shape}")
                return False
            
            logger.info(f"Training data shape: X={X.shape}, y={y.shape}")
            
            X_train, X_test, y_train, y_test = train_test_split(
                X, y, test_size=0.2, random_state=42
            )
            
            X_train_scaled = self.scaler.fit_transform(X_train)
            X_test_scaled = self.scaler.transform(X_test)
            
            self.priority_model.fit(X_train_scaled, y_train)
            
            train_score = self.priority_model.score(X_train_scaled, y_train)
            test_score = self.priority_model.score(X_test_scaled, y_test)
            oob_score = getattr(self.priority_model, 'oob_score_', None)
            
            logger.info(f"Model trained successfully - Train Score: {train_score:.4f}, Test Score: {test_score:.4f}")
            if oob_score:
                logger.info(f"Out-of-bag Score: {oob_score:.4f}")
                if oob_score > 0.75:
                    logger.info("Model shows good generalization (OOB > 0.75)")
                else:
                    logger.warning("Model may be overfitting (OOB ≤ 0.75)")
            
            self.save_model()
            self.is_trained = True
            
            return True
            
        except Exception as e:
            logger.error(f"Error training model: {e}")
            return False
    
    def evaluate_model_performance(self):
        """Comprehensive model evaluation with multiple metrics"""
        try:
            # Generate test data
            test_data = self.generate_synthetic_data(n_samples=200)
            
            if not test_data:
                return None
                
            X_test = np.array([item['features'] for item in test_data])
            y_test = np.array([item['priority'] for item in test_data])
            
            X_test_scaled = self.scaler.transform(X_test)
            y_pred = self.priority_model.predict(X_test_scaled)
            
            # Calculate multiple regression metrics
            mae = mean_absolute_error(y_test, y_pred)
            mse = mean_squared_error(y_test, y_pred)
            rmse = np.sqrt(mse)
            r2 = r2_score(y_test, y_pred)
            
            # Mean Absolute Percentage Error
            mape = np.mean(np.abs((y_test - y_pred) / np.maximum(y_test, 1e-8))) * 100
            
            evaluation_results = {
                'mae': round(mae, 4),
                'mse': round(mse, 4),
                'rmse': round(rmse, 4),
                'r2_score': round(r2, 4),
                'mape': round(mape, 2),
                'oob_score': getattr(self.priority_model, 'oob_score_', None)
            }
            
            logger.info(f"Model Evaluation Results: {evaluation_results}")
            return evaluation_results
            
        except Exception as e:
            logger.error(f"Error in model evaluation: {e}")
            return None
    
    def analyze_feature_importance(self):
        """Analyze and visualize feature importance"""
        if not self.is_trained:
            logger.warning("Model not trained yet")
            return None
        
        try:
            feature_names = [
                'years_to_target', 'funding_gap_ratio', 'progress_ratio',
                'requirement_factor', 'urgency_factor', 'cost_pressure',
                'inflation_impact', 'is_education', 'completion_urgency',
                'cost_factor'
            ]
            
            # Get feature importances
            importances = self.priority_model.feature_importances_
            
            # Create importance analysis
            feature_importance = dict(zip(feature_names, importances))
            sorted_features = sorted(feature_importance.items(), 
                                   key=lambda x: x[1], reverse=True)
            
            logger.info("Feature Importance Ranking:")
            for feature, importance in sorted_features:
                logger.info(f"{feature}: {importance:.4f}")
            
            return {
                'feature_importance': feature_importance,
                'top_features': sorted_features[:5]
            }
            
        except Exception as e:
            logger.error(f"Error analyzing feature importance: {e}")
            return None
    
    def cross_validate_model(self, cv_folds=5):
        """Perform cross-validation for model robustness"""
        try:
            training_data = self.generate_synthetic_data(n_samples=500)
            
            X = np.array([item['features'] for item in training_data])
            y = np.array([item['priority'] for item in training_data])
            
            X_scaled = self.scaler.fit_transform(X)
            
            # Perform cross-validation
            cv_scores = cross_val_score(
                self.priority_model, X_scaled, y, 
                cv=cv_folds, scoring='r2'
            )
            
            cv_results = {
                'mean_cv_score': round(np.mean(cv_scores), 4),
                'std_cv_score': round(np.std(cv_scores), 4),
                'cv_scores': [round(score, 4) for score in cv_scores.tolist()]
            }
            
            logger.info(f"Cross-validation results: {cv_results}")
            return cv_results
            
        except Exception as e:
            logger.error(f"Error in cross-validation: {e}")
            return None
    
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
    
    def analyze_overfunding(self, plans):
        """Analyze and report overfunding status"""
        overfunded_analysis = []
        
        for plan in plans:
            current_savings = plan.get('current_savings', 0)
            total_cost = plan['estimated_total_cost']
            years_to_target = plan.get('years_to_target', 5)
            inflation_rate = plan.get('inflation_rate', 6.0) / 100
            
            future_cost = total_cost * ((1 + inflation_rate) ** years_to_target)
            
            if current_savings >= future_cost:
                excess_amount = current_savings - future_cost
                overfunded_analysis.append({
                    'plan_id': plan.get('id'),
                    'excess_amount': round(excess_amount, 2),
                    'overfunding_percentage': round((excess_amount / future_cost) * 100, 2),
                    'recommendation': 'Consider reducing contributions or reallocating excess'
                })
        
        return overfunded_analysis

class SmartAllocationEngine:
    def __init__(self, model: PlanAllocationModel):
        self.model = model
    
    def allocate_savings(self, total_monthly_savings, education_plans, marriage_plans):
        """Main allocation function with overfunding handling"""
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
        """Enhanced allocation that handles overfunded plans efficiently"""
        if not plans:
            return {}
        
        # Separate overfunded and underfunded plans
        overfunded_plans = []
        underfunded_plans = []
        
        for plan in plans:
            plan_data = plan['plan_data']
            current_savings = plan_data.get('current_savings', 0)
            total_cost = plan_data['estimated_total_cost']
            years_to_target = plan_data.get('years_to_target', 5)
            inflation_rate = plan_data.get('inflation_rate', 6.0) / 100
            
            future_cost = total_cost * ((1 + inflation_rate) ** years_to_target)
            
            if current_savings >= future_cost:
                overfunded_plans.append(plan)
            else:
                underfunded_plans.append(plan)
        
        allocations = {}
        
        # Allocate primarily to underfunded plans
        if underfunded_plans:
            # Calculate required allocations for underfunded plans
            total_priority = sum(plan['priority'] for plan in underfunded_plans)
            available_savings = total_savings
            
            # Reserve minimal amount for overfunded plans if any exist
            if overfunded_plans:
                reserve_for_overfunded = min(total_savings * 0.1, len(overfunded_plans) * 1000)
                available_savings = total_savings - reserve_for_overfunded
            
            # Allocate to underfunded plans based on priority
            for plan in underfunded_plans:
                if total_priority > 0:
                    weight = plan['priority'] / total_priority
                    allocation = available_savings * weight
                    allocations[plan['id']] = round(max(1000, allocation), 2)
                else:
                    equal_share = available_savings / len(underfunded_plans)
                    allocations[plan['id']] = round(equal_share, 2)
        
        # Minimal allocation to overfunded plans
        if overfunded_plans:
            if underfunded_plans:
                # Give minimal amounts to overfunded plans
                remaining = total_savings - sum(allocations.values())
                per_overfunded = max(500, remaining / len(overfunded_plans))
            else:
                # If all plans are overfunded, distribute equally
                per_overfunded = total_savings / len(overfunded_plans)
            
            for plan in overfunded_plans:
                allocations[plan['id']] = round(per_overfunded, 2)
        
        # Ensure total doesn't exceed available savings
        total_allocated = sum(allocations.values())
        if total_allocated > total_savings:
            factor = total_savings / total_allocated
            for plan_id in allocations:
                allocations[plan_id] = round(allocations[plan_id] * factor, 2)
        
        return allocations
    
    def evaluate_allocation_quality(self, allocations, plans, total_savings):
        """Evaluate allocation quality with business metrics"""
        try:
            evaluation_metrics = {
                'allocation_efficiency': 0,
                'priority_alignment': 0,
                'overfunding_handling': 0,
                'coverage_ratio': 0
            }
            
            if not allocations or not plans:
                return evaluation_metrics
            
            # 1. Allocation Efficiency (how well total savings are utilized)
            total_allocated = sum(allocations.values())
            evaluation_metrics['allocation_efficiency'] = min(1.0, total_allocated / total_savings)
            
            # 2. Priority Alignment (higher priority plans get more allocation)
            plan_priorities = {}
            overfunded_count = 0
            
            for plan in plans:
                plan_id = f"{plan.get('plan_type', 'unknown')}_{plan['id']}"
                
                # Calculate if overfunded
                current_savings = plan.get('current_savings', 0)
                total_cost = plan['estimated_total_cost']
                years_to_target = plan.get('years_to_target', 5)
                inflation_rate = plan.get('inflation_rate', 6.0) / 100
                future_cost = total_cost * ((1 + inflation_rate) ** years_to_target)
                
                if current_savings >= future_cost:
                    overfunded_count += 1
                
                # Get priority score
                features = self.model.extract_plan_features(plan, total_savings)
                priority = self.model.predict_priority(features)
                plan_priorities[plan_id] = priority
            
            # Calculate priority-weighted allocation score
            if plan_priorities:
                weighted_score = 0
                total_weight = 0
                
                for plan_id, priority in plan_priorities.items():
                    if plan_id in allocations:
                        allocation_ratio = allocations[plan_id] / total_allocated
                        weighted_score += priority * allocation_ratio
                        total_weight += priority
                
                if total_weight > 0:
                    evaluation_metrics['priority_alignment'] = weighted_score / total_weight
            
            # 3. Overfunding Handling (overfunded plans get minimal allocation)
            if overfunded_count > 0:
                overfunded_allocation = 0
                for plan in plans:
                    plan_id = f"{plan.get('plan_type', 'unknown')}_{plan['id']}"
                    current_savings = plan.get('current_savings', 0)
                    total_cost = plan['estimated_total_cost']
                    years_to_target = plan.get('years_to_target', 5)
                    inflation_rate = plan.get('inflation_rate', 6.0) / 100
                    future_cost = total_cost * ((1 + inflation_rate) ** years_to_target)
                    
                    if current_savings >= future_cost and plan_id in allocations:
                        overfunded_allocation += allocations[plan_id]
                
                # Good overfunding handling = low allocation to overfunded plans
                overfunded_ratio = overfunded_allocation / total_allocated
                evaluation_metrics['overfunding_handling'] = max(0, 1 - overfunded_ratio)
            else:
                evaluation_metrics['overfunding_handling'] = 1.0
            
            # 4. Coverage Ratio (percentage of plans that receive allocation)
            allocated_plans = len([p for p in allocations.values() if p > 0])
            total_plans = len(plans)
            evaluation_metrics['coverage_ratio'] = allocated_plans / max(1, total_plans)
            
            return evaluation_metrics
            
        except Exception as e:
            logger.error(f"Error evaluating allocation quality: {e}")
            return {'allocation_efficiency': 0, 'priority_alignment': 0, 
                    'overfunding_handling': 0, 'coverage_ratio': 0}
    
    def get_allocation_insights(self, total_monthly_savings, education_plans, marriage_plans):
        """Provide detailed insights about allocation and overfunding"""
        all_plans = education_plans + marriage_plans
        
        # Analyze overfunding
        overfunding_analysis = self.model.analyze_overfunding(all_plans)
        
        # Calculate funding status
        funding_status = []
        for plan in all_plans:
            current_savings = plan.get('current_savings', 0)
            total_cost = plan['estimated_total_cost']
            years_to_target = plan.get('years_to_target', 5)
            inflation_rate = plan.get('inflation_rate', 6.0) / 100
            
            future_cost = total_cost * ((1 + inflation_rate) ** years_to_target)
            funding_gap = max(0, future_cost - current_savings)
            progress_percentage = (current_savings / future_cost) * 100
            
            funding_status.append({
                'plan_id': plan.get('id'),
                'plan_type': plan.get('plan_type', 'unknown'),
                'current_savings': current_savings,
                'future_cost': round(future_cost, 2),
                'funding_gap': round(funding_gap, 2),
                'progress_percentage': round(progress_percentage, 2),
                'is_overfunded': current_savings >= future_cost
            })
        
        return {
            'overfunding_analysis': overfunding_analysis,
            'funding_status': funding_status,
            'total_overfunded_plans': len(overfunding_analysis),
            'total_underfunded_plans': len([s for s in funding_status if not s['is_overfunded']])
        }

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
    """Main allocation endpoint with overfunding insights"""
    try:
        data = request.get_json()
        
        if not data:
            return jsonify({'error': 'No data provided'}), 400
        
        total_monthly_savings = data.get('totalMonthlySavings', 0)
        education_plans = data.get('educationPlans', [])
        marriage_plans = data.get('marriagePlans', [])
        
        if total_monthly_savings <= 0:
            return jsonify({'error': 'Invalid monthly savings amount'}), 400
        
        # Get allocations
        allocations = allocation_engine.allocate_savings(
            total_monthly_savings, education_plans, marriage_plans
        )
        
        # Get insights
        insights = allocation_engine.get_allocation_insights(
            total_monthly_savings, education_plans, marriage_plans
        )
        
        # Evaluate allocation quality
        all_plans = education_plans + marriage_plans
        allocation_quality = allocation_engine.evaluate_allocation_quality(
            allocations, all_plans, total_monthly_savings
        )
        
        total_allocated = sum(allocations.values())
        
        return jsonify({
            'success': True,
            'allocations': allocations,
            'insights': insights,
            'allocation_quality': allocation_quality,
            'summary': {
                'total_monthly_savings': total_monthly_savings,
                'total_allocated': round(total_allocated, 2),
                'number_of_plans': len(allocations),
                'overfunded_plans': insights['total_overfunded_plans'],
                'underfunded_plans': insights['total_underfunded_plans'],
                'timestamp': datetime.now().isoformat()
            }
        })
        
    except Exception as e:
        logger.error(f"Error in allocation: {e}")
        return jsonify({'success': False, 'error': str(e)}), 500

@app.route('/evaluate-model', methods=['GET'])
def evaluate_model():
    """Comprehensive model evaluation endpoint"""
    try:
        if not allocation_model.is_trained:
            return jsonify({'error': 'Model not trained'}), 400
        
        # Perform comprehensive evaluation
        performance_metrics = allocation_model.evaluate_model_performance()
        feature_analysis = allocation_model.analyze_feature_importance()
        cv_results = allocation_model.cross_validate_model()
        
        return jsonify({
            'success': True,
            'model_performance': performance_metrics,
            'feature_importance': feature_analysis,
            'cross_validation': cv_results,
            'evaluation_timestamp': datetime.now().isoformat()
        })
        
    except Exception as e:
        logger.error(f"Error in model evaluation endpoint: {e}")
        return jsonify({'success': False, 'error': str(e)}), 500

@app.route('/analyze-overfunding', methods=['POST'])
def analyze_overfunding():
    """Dedicated endpoint for overfunding analysis"""
    try:
        data = request.get_json()
        
        if not data:
            return jsonify({'error': 'No data provided'}), 400
        
        education_plans = data.get('educationPlans', [])
        marriage_plans = data.get('marriagePlans', [])
        
        all_plans = education_plans + marriage_plans
        overfunding_analysis = allocation_model.analyze_overfunding(all_plans)
        
        return jsonify({
            'success': True,
            'overfunding_analysis': overfunding_analysis,
            'total_overfunded_plans': len(overfunding_analysis)
        })
        
    except Exception as e:
        logger.error(f"Error in overfunding analysis: {e}")
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
