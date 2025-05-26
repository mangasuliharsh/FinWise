// src/components/AuthCallback.js
import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const AuthCallback = () => {
    const navigate = useNavigate();

    useEffect(() => {
        const handleAuthCallback = async () => {
            try {
                // Check user authentication status after OAuth callback
                const res = await axios.get("http://localhost:8080/api/auth/user", {
                    withCredentials: true
                });

                if (res.data.isAuthenticated) {
                    // Check the is_new_user flag and redirect accordingly
                    if (res.data.user.isNewUser) {
                        navigate('/family-details');
                    } else {
                        navigate('/dashboard');
                    }
                } else {
                    // Authentication failed, redirect to landing
                    navigate('/');
                }
            } catch (error) {
                console.error('Auth callback error:', error);
                navigate('/');
            }
        };

        handleAuthCallback();
    }, [navigate]);

    return (
        <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-900 to-indigo-950">
            <div className="text-white text-center">
                <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-emerald-400 mx-auto mb-4"></div>
                <p className="text-lg">Completing your login...</p>
            </div>
        </div>
    );
};

export default AuthCallback;
