import {useEffect, useState} from 'react';
import { ChevronRight, BarChart2, Shield, DollarSign, CreditCard, Users } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import axios from "axios";

export default function Landing() {
    const [email, setEmail] = useState('');
    const navigate = useNavigate();

    // Remove the automatic redirect useEffect
    // Only check authentication status after login, not on page load

    const handleLogin = async () => {
        try {
            // Redirect to Google OAuth
            window.location.href = "http://localhost:8080/oauth2/authorization/google";
        } catch (error) {
            console.error('Login error:', error);
        }
    };

    const handleJoinWaitlist = () => {
        if (email) {
            alert("Join waitlist with: " + email);
            setEmail('');
        } else {
            alert("Please enter your email address");
        }
    };

    return (
        <div className="min-h-screen bg-gradient-to-br from-blue-900 to-indigo-950 text-white">
            {/* Rest of your component remains the same */}
            {/* Navigation */}
            <nav className="flex justify-between items-center px-6 py-4 md:px-16 lg:px-24">
                <div className="flex items-center">
                    <h1 className="text-2xl font-bold tracking-tight">
                        <span className="text-emerald-400">Fin</span>Wise
                    </h1>
                </div>
                <div className="hidden md:flex space-x-8 text-gray-300">
                    <a href="#features" className="hover:text-emerald-400 transition">Features</a>
                    <a href="#testimonials" className="hover:text-emerald-400 transition">Testimonials</a>
                    <a href="#pricing" className="hover:text-emerald-400 transition">Pricing</a>
                </div>
                <button
                    onClick={handleLogin}
                    className="hidden md:block bg-emerald-500 hover:bg-emerald-600 px-5 py-2 rounded-lg font-medium transition"
                >
                    Login
                </button>
                <button className="md:hidden text-gray-300">
                    <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16"/>
                    </svg>
                </button>
            </nav>

            {/* Hero Section */}
            <div className="px-6 pt-16 pb-24 md:px-16 lg:px-24 lg:flex items-center justify-between">
                <div className="lg:w-1/2 lg:pr-12">
                    <h2 className="text-4xl md:text-5xl font-bold leading-tight mb-6">
                        Smart Financial Planning
                        <span className="text-emerald-400"> for Everyone</span>
                    </h2>
                    <p className="text-gray-300 text-lg mb-8">
                        Take control of your finances with FinWise. Track expenses, set goals,
                        and make informed decisions with our intelligent financial platform.
                    </p>
                    <div className="flex flex-col sm:flex-row space-y-4 sm:space-y-0 sm:space-x-4 mb-8">
                        <button
                            onClick={handleLogin}
                            className="bg-emerald-500 hover:bg-emerald-600 px-6 py-3 rounded-lg font-medium transition flex items-center justify-center"
                        >
                            Login with Google
                            <ChevronRight className="ml-2 h-5 w-5"/>
                        </button>
                        {/* Rest of your hero section... */}
                    </div>
                </div>
            </div>

            {/* Rest of your component remains the same */}
        </div>
    );
}
