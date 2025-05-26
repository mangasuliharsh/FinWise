import {useEffect, useState} from 'react';
import { ChevronRight, BarChart2, Shield, DollarSign, CreditCard, Users } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import axios from "axios";

export default function Landing() {
    const [email, setEmail] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const checkUserProfile = async () => {
            try {
                const res = await axios.get("http://localhost:8080/api/family/profile", { withCredentials: true });
                if (res.data.exists) {
                    navigate('/dashboard');
                } else {
                    navigate('/family-details');
                }
            } catch (error) {
                // Not authenticated or error, stay on landing page
            }
        };
        checkUserProfile();
    }, [navigate]);

    const handleLogin = () => {
        // For development/demo purposes, navigate directly to family-details
        // In production, this would redirect to your backend OAuth endpoint
        window.location.href = "http://localhost:8080/oauth2/authorization/google";
        // navigate("/family-details");
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
                        <div className="relative">
                            <input
                                type="email"
                                placeholder="Enter your email"
                                className="px-6 py-3 rounded-lg bg-white/10 border border-white/30 w-full focus:outline-none focus:ring-2 focus:ring-emerald-500 text-white placeholder-gray-300"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                            />
                            <button
                                className="absolute right-2 top-1/2 -translate-y-1/2 bg-emerald-500 hover:bg-emerald-600 px-4 py-1 rounded-md text-sm font-medium transition"
                                onClick={handleJoinWaitlist}
                            >
                                Join Waitlist
                            </button>
                        </div>
                    </div>
                    <div className="flex items-center text-gray-400 text-sm">
                        <Shield className="h-4 w-4 mr-2"/>
                        Your financial data is encrypted and secure
                    </div>
                </div>
                <div className="hidden lg:block lg:w-1/2">
                    <div className="bg-white/10 backdrop-blur-lg rounded-2xl p-8 border border-white/20 shadow-xl">
                        <div className="bg-gradient-to-br from-emerald-400/20 to-blue-500/20 rounded-lg h-64 flex items-center justify-center">
                            <p className="text-gray-300">Dashboard Preview</p>
                        </div>
                    </div>
                </div>
            </div>

            {/* Features Section */}
            <div id="features" className="bg-indigo-900/50 py-16 px-6 md:px-16 lg:px-24">
                <div className="max-w-5xl mx-auto">
                    <h3 className="text-emerald-400 font-semibold mb-2">FEATURES</h3>
                    <h2 className="text-3xl font-bold mb-12">Everything you need to manage your finances</h2>

                    <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-8">
                        <div className="bg-white/5 rounded-xl p-6 border border-white/10 hover:border-emerald-500/50 transition">
                            <div className="bg-emerald-500/20 rounded-lg p-3 inline-block mb-4">
                                <BarChart2 className="h-6 w-6 text-emerald-400"/>
                            </div>
                            <h3 className="text-xl font-semibold mb-3">Smart Analytics</h3>
                            <p className="text-gray-300">Get insights into your spending habits and identify areas for improvement.</p>
                        </div>
                        <div className="bg-white/5 rounded-xl p-6 border border-white/10 hover:border-emerald-500/50 transition">
                            <div className="bg-emerald-500/20 rounded-lg p-3 inline-block mb-4">
                                <Users className="h-6 w-6 text-emerald-400" />
                            </div>
                            <h3 className="text-xl font-semibold mb-3">Family Forecasting</h3>
                            <p className="text-gray-300">Plan future expenses like education, marriage, and retirement for your family with tailored insights.</p>
                        </div>
                        <div className="bg-white/5 rounded-xl p-6 border border-white/10 hover:border-emerald-500/50 transition">
                            <div className="bg-emerald-500/20 rounded-lg p-3 inline-block mb-4">
                                <DollarSign className="h-6 w-6 text-emerald-400"/>
                            </div>
                            <h3 className="text-xl font-semibold mb-3">Budget Planning</h3>
                            <p className="text-gray-300">Create custom budgets for different categories and track your progress.</p>
                        </div>
                        <div className="bg-white/5 rounded-xl p-6 border border-white/10 hover:border-emerald-500/50 transition">
                            <div className="bg-emerald-500/20 rounded-lg p-3 inline-block mb-4">
                                <CreditCard className="h-6 w-6 text-emerald-400"/>
                            </div>
                            <h3 className="text-xl font-semibold mb-3">Expense Tracking</h3>
                            <p className="text-gray-300">Easily categorize and monitor all your expenses in one place.</p>
                        </div>
                    </div>
                </div>
            </div>

            {/* Testimonials */}
            <div id="testimonials" className="py-16 px-6 md:px-16 lg:px-24">
                <div className="max-w-5xl mx-auto text-center mb-12">
                    <h3 className="text-emerald-400 font-semibold mb-2">TESTIMONIALS</h3>
                    <h2 className="text-3xl font-bold">Trusted by thousands of users</h2>
                </div>

                <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-8 max-w-5xl mx-auto">
                    {[
                        { name: "Sarah Johnson", role: "Small Business Owner", quote: "FinWise completely changed how I manage my money. The insights are invaluable!" },
                        { name: "Michael Chen", role: "Software Engineer", quote: "I've tried many financial apps, but FinWise stands out with its intuitive interface and powerful features." },
                        { name: "Lisa Rodriguez", role: "Marketing Specialist", quote: "The budgeting tools have helped me save more than I ever thought possible. Highly recommend!" }
                    ].map((testimonial, index) => (
                        <div key={index} className="bg-white/5 rounded-xl p-6 border border-white/10">
                            <div className="flex mb-4">
                                {[1, 2, 3, 4, 5].map(star => (
                                    <svg key={star} xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 text-yellow-400" viewBox="0 0 20 20" fill="currentColor">
                                        <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118l-2.8-2.034c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z"/>
                                    </svg>
                                ))}
                            </div>
                            <p className="text-gray-300 mb-4">"{testimonial.quote}"</p>
                            <div className="flex items-center">
                                <div className="bg-emerald-500/20 rounded-full h-10 w-10 flex items-center justify-center mr-3">
                                    <Users className="h-5 w-5 text-emerald-400"/>
                                </div>
                                <div>
                                    <p className="font-medium">{testimonial.name}</p>
                                    <p className="text-xs text-gray-400">{testimonial.role}</p>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            </div>

            {/* CTA Section */}
            <div className="bg-emerald-500/10 py-16 px-6 md:px-16 lg:px-24 border-t border-b border-emerald-500/20">
                <div className="max-w-4xl mx-auto text-center">
                    <h2 className="text-3xl font-bold mb-6">Ready to take control of your finances?</h2>
                    <p className="text-gray-300 text-lg mb-8 max-w-2xl mx-auto">
                        Join thousands of users who are already making smarter financial decisions with FinWise.
                    </p>
                    <button
                        onClick={handleLogin}
                        className="bg-emerald-500 hover:bg-emerald-600 px-8 py-4 rounded-lg font-medium transition text-lg"
                    >
                        Get Started for Free
                    </button>
                </div>
            </div>

            {/* Footer */}
            <footer className="py-12 px-6 md:px-16 lg:px-24">
                <div className="max-w-6xl mx-auto">
                    <div className="flex flex-col md:flex-row justify-between mb-8">
                        <div className="mb-8 md:mb-0">
                            <h2 className="text-2xl font-bold mb-4">
                                <span className="text-emerald-400">Fin</span>Wise
                            </h2>
                            <p className="text-gray-400 max-w-xs">
                                Smart financial planning for everyone. Take control of your money and achieve your financial goals.
                            </p>
                        </div>

                        <div className="grid grid-cols-2 md:grid-cols-3 gap-8">
                            <div>
                                <h3 className="text-lg font-semibold mb-4">Product</h3>
                                <ul className="space-y-2 text-gray-400">
                                    <li><a href="#features" className="hover:text-emerald-400 transition">Features</a></li>
                                    <li><a href="#pricing" className="hover:text-emerald-400 transition">Pricing</a></li>
                                    <li><a href="/security" className="hover:text-emerald-400 transition">Security</a></li>
                                </ul>
                            </div>
                            <div>
                                <h3 className="text-lg font-semibold mb-4">Company</h3>
                                <ul className="space-y-2 text-gray-400">
                                    <li><a href="/about" className="hover:text-emerald-400 transition">About</a></li>
                                    <li><a href="/blog" className="hover:text-emerald-400 transition">Blog</a></li>
                                    <li><a href="/careers" className="hover:text-emerald-400 transition">Careers</a></li>
                                </ul>
                            </div>
                            <div>
                                <h3 className="text-lg font-semibold mb-4">Support</h3>
                                <ul className="space-y-2 text-gray-400">
                                    <li><a href="/help" className="hover:text-emerald-400 transition">Help Center</a></li>
                                    <li><a href="/contact" className="hover:text-emerald-400 transition">Contact Us</a></li>
                                    <li><a href="/faq" className="hover:text-emerald-400 transition">FAQ</a></li>
                                </ul>
                            </div>
                        </div>
                    </div>

                    <div className="border-t border-white/10 pt-8 flex flex-col md:flex-row justify-between items-center">
                        <p className="text-gray-400 text-sm mb-4 md:mb-0">
                            © 2025 FinWise. All rights reserved.
                        </p>
                        <div className="flex space-x-6">
                            <a href="#" aria-label="Facebook" className="text-gray-400 hover:text-emerald-400 transition">
                                <svg className="h-5 w-5" fill="currentColor" viewBox="0 0 24 24" aria-hidden="true">
                                    <path fillRule="evenodd" d="M22 12c0-5.523-4.477-10-10-10S2 6.477 2 12c0 4.991 3.657 9.128 8.438 9.878v-6.987h-2.54V12h2.54V9.797c0-2.506 1.492-3.89 3.777-3.89 1.094 0 2.238.195 2.238.195v2.46h-1.26c-1.243 0-1.63.771-1.63 1.562V12h2.773l-.443 2.89h-2.33v6.988C18.343 21.128 22 16.991 22 12z" clipRule="evenodd"/>
                                </svg>
                            </a>
                            <a href="#" aria-label="Twitter" className="text-gray-400 hover:text-emerald-400 transition">
                                <svg className="h-5 w-5" fill="currentColor" viewBox="0 0 24 24" aria-hidden="true">
                                    <path d="M8.29 20.251c7.547 0 11.675-6.253 11.675-11.675 0-.178 0-.355-.012-.53A8.348 8.348 0 0022 5.92a8.19 8.19 0 01-2.357.646 4.118 4.118 0 001.804-2.27 8.224 8.224 0 01-2.605.996 4.107 4.107 0 00-6.993 3.743 11.65 11.65 0 01-8.457-4.287 4.106 4.106 0 001.27 5.477A4.072 4.072 0 012.8 9.713v.052a4.105 4.105 0 003.292 4.022 4.095 4.095 0 01-1.853.07 4.108 4.108 0 003.834 2.85A8.233 8.233 0 012 18.407a11.616 11.616 0 006.29 1.84"/>
                                </svg>
                            </a>
                            <a href="#" aria-label="Instagram" className="text-gray-400 hover:text-emerald-400 transition">
                                <svg className="h-5 w-5" fill="currentColor" viewBox="0 0 24 24" aria-hidden="true">
                                    <path fillRule="evenodd" d="M12.315 2c2.43 0 2.784.013 3.808.06 1.064.049 1.791.218 2.427.465a4.902 4.902 0 011.772 1.153 4.902 4.902 0 011.153 1.772c.247.636.416 1.363.465 2.427.048 1.067.06 1.407.06 4.123v.08c0 2.643-.012 2.987-.06 4.043-.049 1.064-.218 1.791-.465 2.427a4.902 4.902 0 01-1.153 1.772 4.902 4.902 0 01-1.772 1.153c-.636.247-1.363.416-2.427.465-1.067.048-1.407.06-4.123.06h-.08c-2.643 0-2.987-.012-4.043-.06-1.064-.049-1.791-.218-2.427-.465a4.902 4.902 0 01-1.772-1.153 4.902 4.902 0 01-1.153-1.772c-.247-.636-.416-1.363-.465-2.427-.047-1.024-.06-1.379-.06-3.808v-.63c0-2.43.013-2.784.06-3.808.049-1.064.218-1.791.465-2.427a4.902 4.902 0 011.153-1.772A4.902 4.902 0 015.45 2.525c.636-.247 1.363-.416 2.427-.465C8.901 2.013 9.256 2 11.685 2h.63zm-.081 1.802h-.468c-2.456 0-2.784.011-3.807.058-.975.045-1.504.207-1.857.344-.467.182-.8.398-1.15.748-.35.35-.566.683-.748 1.15-.137.353-.3.882-.344 1.857-.047 1.023-.058 1.351-.058 3.807v.468c0 2.456.011 2.784.058 3.807.045.975.207 1.504.344 1.857.182.466.399.8.748 1.15.35.35.683.566 1.15.748.353.137.882.3 1.857.344 1.054.048 1.37.058 4.041.058h.08c2.597 0 2.917-.01 3.96-.058.976-.045 1.505-.207 1.858-.344.466-.182.8-.398 1.15-.748.35-.35.566-.683.748-1.15.137-.353.3-.882.344-1.857.048-1.055.058-1.37.058-4.041v-.08c0-2.597-.01-2.917-.058-3.96-.045-.976-.207-1.505-.344-1.858a3.097 3.097 0 00-.748-1.15 3.098 3.098 0 00-1.15-.748c-.353-.137-.882-.3-1.857-.344-1.023-.047-1.351-.058-3.807-.058zM12 6.865a5.135 5.135 0 110 10.27 5.135 5.135 0 010-10.27zm0 1.802a3.333 3.333 0 100 6.666 3.333 3.333 0 000-6.666zm5.338-3.205a1.2 1.2 0 110 2.4 1.2 1.2 0 010-2.4z" clipRule="evenodd"/>
                                </svg>
                            </a>
                        </div>
                    </div>
                </div>
            </footer>
        </div>
    );
}