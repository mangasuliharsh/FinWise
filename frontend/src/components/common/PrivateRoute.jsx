import React, { useState, useEffect } from 'react';
import { Navigate } from 'react-router-dom';
import axios from 'axios';

const PrivateRoute = ({ children }) => {
    const [loading, setLoading] = useState(true);
    const [shouldRedirect, setShouldRedirect] = useState(false);
    const familyProfileId = localStorage.getItem('familyProfileId');

    useEffect(() => {
        const checkOnboardingStatus = async () => {
            try {
                if (!familyProfileId) {
                    setShouldRedirect(true);
                    setLoading(false);
                    return;
                }

                const response = await axios.get(`http://localhost:8080/api/family-profiles/${familyProfileId}`);
                if (!response.data.onboardingComplete) {
                    setShouldRedirect(true);
                }
            } catch (error) {
                console.error('Error checking onboarding status:', error);
                setShouldRedirect(true);
            } finally {
                setLoading(false);
            }
        };

        checkOnboardingStatus();
    }, [familyProfileId]);

    if (loading) {
        return <div>Loading...</div>;
    }

    if (shouldRedirect) {
        return <Navigate to="/onboarding" replace />;
    }

    return children;
};

export default PrivateRoute;
