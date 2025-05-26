import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Dashboard from './components/Dashboard';
import FamilyDetails from './components/FamilyProfile/FamilyDetails';
import AuthCallback from './components/AuthCallback';
import DynamicNavbar from './components/DynamicNavbar';
import LandingPage from './components/landing';

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<LandingPage />} />
                <Route path="/auth/callback" element={<AuthCallback />} />
                <Route path="/family-details" element={<FamilyDetails />} />
                <Route path="/dashboard" element={<Dashboard />} />

            </Routes>
        </Router>
    );
}

export default App;
