import React from 'react';
import {
    BrowserRouter as Router,
    Routes,
    Route,
    Navigate
} from "react-router-dom";
import Dashboard from './components/Dashboard';
import Login from './components/auth/Login';
import EducationPlanList from './components/education/EducationPlanList';
import EducationPlanForm from './components/education/EducationPlanForm';
import EducationPlanDetails from './components/education/EducationPlanDetails';
import Onboarding from './components/onboarding/Onboarding';
import PrivateRoute from './components/common/PrivateRoute';
import { isAuthenticated } from './utils/auth';

function Main() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={
                    isAuthenticated() ? <Navigate to="/dashboard" /> : <Login />
                } />
                <Route path="/login" element={
                    isAuthenticated() ? <Navigate to="/dashboard" /> : <Login />
                } />
                <Route path="/onboarding" element={
                    <PrivateRoute>
                        <Onboarding />
                    </PrivateRoute>
                } />
                <Route path="/dashboard" element={
                    <PrivateRoute>
                        <Dashboard />
                    </PrivateRoute>
                } />
                <Route path="/education/family/:familyProfileId/plan/new" element={
                    <PrivateRoute>
                        <EducationPlanForm />
                    </PrivateRoute>
                } />
                <Route path="/education/family/:familyProfileId/edit/:planId" element={
                    <PrivateRoute>
                        <EducationPlanForm />
                    </PrivateRoute>
                } />
                <Route path="/education/family/:familyProfileId/children" element={
                    <PrivateRoute>
                        <EducationPlanList />
                    </PrivateRoute>
                } />
                <Route path="/education/plan/:planId" element={
                    <PrivateRoute>
                        <EducationPlanDetails />
                    </PrivateRoute>
                } />
            </Routes>
        </Router>
    );
}

export default Main;
