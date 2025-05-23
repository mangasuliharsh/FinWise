import { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import {
    DollarSign,
    BookOpen,
    Heart,
    TrendingUp,
    AlertTriangle,
    Calendar,
    BarChart2,
    Settings,
    Bell,
    User,
    PieChart,
    Target,
    ArrowUp,
    ArrowDown,
    Shield,
    ChevronRight,
    Loader2
} from 'lucide-react';
import { createAuthenticatedAxios, isAuthenticated } from '../utils/auth';
import Navbar from './common/Navbar';

const Dashboard = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [dashboardData, setDashboardData] = useState({
        financialSummary: {
            totalSavings: 0,
            educationFunds: 0,
            marriageFunds: 0,
            emergencyFunds: 0,
            investmentFunds: 0,
            monthlyContribution: 0
        },
        upcomingMilestones: [],
        savingsProgress: {
            education: { target: 0, current: 0 },
            marriage: { target: 0, current: 0 },
            emergency: { target: 0, current: 0 }
        },
        recentTransactions: [],
        financialAlerts: []
    });

    useEffect(() => {
        const fetchDashboardData = async () => {
            try {
                // Check authentication first
                if (!isAuthenticated()) {
                    navigate('/login', { 
                        state: { 
                            from: {
                                pathname: location.pathname,
                                search: location.search
                            }
                        } 
                    });
                    return;
                }

                setLoading(true);
                setError('');

                // Get user's family profile ID from localStorage or context
                const familyProfileId = localStorage.getItem('familyProfileId');
                if (!familyProfileId) {
                    navigate('/onboarding');
                    return;
                }

                const authAxios = createAuthenticatedAxios();

                // Fetch education plans
                const educationResponse = await authAxios.get(`/api/education-plans/family/${familyProfileId}`);
                const educationPlans = educationResponse.data || [];

                // Calculate education metrics
                const educationMetrics = calculateEducationMetrics(educationPlans);

                // Fetch marriage plans (assuming you have this endpoint)
                const marriageResponse = await authAxios.get(`/api/marriage-plans/family/${familyProfileId}`);
                const marriagePlans = marriageResponse.data || [];

                // Calculate marriage metrics
                const marriageMetrics = calculateMarriageMetrics(marriagePlans);

                // Update dashboard data
                setDashboardData({
                    financialSummary: {
                        totalSavings: educationMetrics.currentSavings + marriageMetrics.currentSavings,
                        educationFunds: educationMetrics.currentSavings,
                        marriageFunds: marriageMetrics.currentSavings,
                        emergencyFunds: 0,
                        investmentFunds: 0,
                        monthlyContribution: educationMetrics.monthlyContribution + marriageMetrics.monthlyContribution
                    },
                    upcomingMilestones: [
                        ...educationMetrics.milestones,
                        ...marriageMetrics.milestones
                    ].sort((a, b) => new Date(a.date) - new Date(b.date)),
                    savingsProgress: {
                        education: {
                            target: educationMetrics.targetAmount,
                            current: educationMetrics.currentSavings
                        },
                        marriage: {
                            target: marriageMetrics.targetAmount,
                            current: marriageMetrics.currentSavings
                        },
                        emergency: { target: 100000, current: 0 }
                    },
                    recentTransactions: [],
                    financialAlerts: generateAlerts(educationMetrics, marriageMetrics)
                });
            } catch (error) {
                console.error('Error fetching dashboard data:', error);
                if (error.response?.status === 401) {
                    // Clear any stale auth data and redirect to login
                    localStorage.removeItem('token');
                    localStorage.removeItem('user');
                    navigate('/login', { 
                        state: { 
                            from: {
                                pathname: location.pathname,
                                search: location.search
                            }
                        } 
                    });
                } else {
                    setError('Failed to load dashboard data. Please try again later.');
                }
            } finally {
                setLoading(false);
            }
        };

        fetchDashboardData();
    }, [navigate, location.pathname, location.search]);

    const calculateEducationMetrics = (plans) => {
        const currentDate = new Date();
        let totalSavings = 0;
        let totalTarget = 0;
        let totalMonthlyContribution = 0;
        const milestones = [];

        plans.forEach(plan => {
            totalSavings += plan.currentSavings || 0;
            totalTarget += plan.estimatedTotalCost || 0;
            totalMonthlyContribution += plan.monthlyContribution || 0;

            if (plan.estimatedStartYear) {
                milestones.push({
                    name: `${plan.child?.name}'s ${plan.educationLevel} Education`,
                    type: "education",
                    amount: plan.estimatedTotalCost,
                    date: `${plan.estimatedStartYear}-01-01`
                });
            }
        });

        return {
            currentSavings: totalSavings,
            targetAmount: totalTarget,
            monthlyContribution: totalMonthlyContribution,
            milestones
        };
    };

    const calculateMarriageMetrics = (plans) => {
        // Similar to education metrics calculation
        // TODO: Implement when marriage planning is added
        return {
            currentSavings: 0,
            targetAmount: 0,
            monthlyContribution: 0,
            milestones: []
        };
    };

    const generateAlerts = (educationMetrics, marriageMetrics) => {
        const alerts = [];
        
        // Check education savings progress
        if (educationMetrics.targetAmount > 0) {
            const educationProgress = (educationMetrics.currentSavings / educationMetrics.targetAmount) * 100;
            if (educationProgress < 50) {
                alerts.push({
                    message: `Education fund is ${Math.round(100 - educationProgress)}% below target`,
                    severity: "high"
                });
            }
        }

        // Add more alerts based on other metrics
        return alerts;
    };

    const handleEducationClick = () => {
        // For now, we'll use a default familyProfileId of 1
        // In a real application, you would get this from your user/family context
        navigate('/education/family/1/children');
    };

    const getCategoryIcon = (category) => {
        switch (category) {
            case 'education': return <BookOpen size={16} className="text-emerald-400" />;
            case 'marriage': return <Heart size={16} className="text-emerald-400" />;
            case 'savings': return <DollarSign size={16} className="text-emerald-400" />;
            default: return <DollarSign size={16} className="text-emerald-400" />;
        }
    };

    const getSeverityColor = (severity) => {
        switch (severity) {
            case 'high': return 'bg-red-900/20 text-red-400 border-red-800/30';
            case 'medium': return 'bg-yellow-900/20 text-yellow-400 border-yellow-800/30';
            case 'low': return 'bg-green-900/20 text-green-400 border-green-800/30';
            default: return 'bg-gray-900/20 text-gray-400 border-gray-800/30';
        }
    };

    const getProgressColor = (current, target) => {
        const percentage = (current / target) * 100;
        if (percentage >= 75) return "bg-emerald-500";
        if (percentage >= 50) return "bg-yellow-500";
        return "bg-red-500";
    };

    const getProgressPercentage = (current, target) => {
        return Math.round((current / target) * 100);
    };

    return (
        <div className="min-h-screen bg-gradient-to-br from-blue-900 to-indigo-950">
            <Navbar title="Dashboard" icon={BarChart2} />
            <main className="p-6 md:p-16 lg:p-24">
                {loading ? (
                    <div className="flex justify-center items-center min-h-[60vh]">
                        <Loader2 className="w-8 h-8 text-white animate-spin" />
                    </div>
                ) : error ? (
                    <div className="text-center text-red-400 bg-red-500/10 border border-red-500/20 rounded-lg p-4">
                        {error}
                    </div>
                ) : (
                    <div className="space-y-8">
                        {/* Top Navigation Bar */}
                        <nav className="px-6 py-4 flex justify-between items-center md:px-16 lg:px-24 border-b border-white/10">
                            <div className="flex items-center space-x-3">
                                <DollarSign size={24} className="text-emerald-400" />
                                <h1 className="text-2xl font-bold tracking-tight">
                                    <span className="text-emerald-400">Fin</span>Wise
                                </h1>
                            </div>
                            <div className="flex items-center space-x-4">
                                <button className="p-2 rounded-full hover:bg-white/5">
                                    <Bell size={20} className="text-gray-300" />
                                </button>
                                <button className="p-2 rounded-full hover:bg-white/5">
                                    <Settings size={20} className="text-gray-300" />
                                </button>
                                <div className="flex items-center space-x-2">
                                    <div className="h-8 w-8 rounded-full bg-emerald-500/20 flex items-center justify-center text-emerald-400">
                                        <User size={18} />
                                    </div>
                                    <span className="text-sm font-medium text-gray-300">Akash Family</span>
                                </div>
                            </div>
                        </nav>

                        {/* Main Dashboard Content */}
                        <div className="max-w-7xl mx-auto px-6 md:px-16 lg:px-24 mt-8">
                            {/* Dashboard Header */}
                            <div className="mb-8">
                                <h2 className="text-3xl font-bold">Dashboard</h2>
                                <p className="text-gray-300">Welcome back! Here's your financial planning overview.</p>
                            </div>

                            {/* Dashboard Tabs */}
                            <div className="bg-white/5 backdrop-blur-sm rounded-lg p-1 flex mb-8 border border-white/10">
                                <button
                                    onClick={() => navigate('/dashboard')}
                                    className={`px-4 py-2 rounded-md text-sm font-medium ${window.location.pathname === '/dashboard' ? 'bg-emerald-500/20 text-emerald-400' : 'text-gray-300 hover:bg-white/5'}`}
                                >
                                    Overview
                                </button>
                                <button
                                    onClick={handleEducationClick}
                                    className={`px-4 py-2 rounded-md text-sm font-medium ${window.location.pathname === '/education/family/1/children' ? 'bg-emerald-500/20 text-emerald-400' : 'text-gray-300 hover:bg-white/5'}`}
                                >
                                    Education
                                </button>
                                <button
                                    onClick={() => navigate('/marriage')}
                                    className={`px-4 py-2 rounded-md text-sm font-medium ${window.location.pathname === '/marriage' ? 'bg-emerald-500/20 text-emerald-400' : 'text-gray-300 hover:bg-white/5'}`}
                                >
                                    Marriage
                                </button>
                                <button
                                    onClick={() => navigate('/savings')}
                                    className={`px-4 py-2 rounded-md text-sm font-medium ${window.location.pathname === '/savings' ? 'bg-emerald-500/20 text-emerald-400' : 'text-gray-300 hover:bg-white/5'}`}
                                >
                                    Savings
                                </button>
                                <button
                                    onClick={() => navigate('/reports')}
                                    className={`px-4 py-2 rounded-md text-sm font-medium ${window.location.pathname === '/reports' ? 'bg-emerald-500/20 text-emerald-400' : 'text-gray-300 hover:bg-white/5'}`}
                                >
                                    Reports
                                </button>
                            </div>

                            {/* Summary Cards */}
                            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
                                <div className="bg-white/5 backdrop-blur-sm rounded-xl p-6 border border-white/10 hover:border-emerald-500/50 transition">
                                    <div className="flex justify-between">
                                        <div>
                                            <p className="text-sm font-medium text-gray-300">Total Savings</p>
                                            <p className="text-2xl font-bold">₹{dashboardData.financialSummary.totalSavings.toLocaleString()}</p>
                                        </div>
                                        <div className="h-12 w-12 rounded-lg bg-emerald-500/20 flex items-center justify-center">
                                            <DollarSign size={24} className="text-emerald-400" />
                                        </div>
                                    </div>
                                    <div className="mt-2 flex items-center text-xs text-emerald-400">
                                        <ArrowUp size={12} className="mr-1" />
                                        <span>5.2% from last month</span>
                                    </div>
                                </div>

                                <div className="bg-white/5 backdrop-blur-sm rounded-xl p-6 border border-white/10 hover:border-emerald-500/50 transition">
                                    <div className="flex justify-between">
                                        <div>
                                            <p className="text-sm font-medium text-gray-300">Education Funds</p>
                                            <p className="text-2xl font-bold">₹{dashboardData.financialSummary.educationFunds.toLocaleString()}</p>
                                        </div>
                                        <div className="h-12 w-12 rounded-lg bg-emerald-500/20 flex items-center justify-center">
                                            <BookOpen size={24} className="text-emerald-400" />
                                        </div>
                                    </div>
                                    <div className="mt-2 flex items-center text-xs text-emerald-400">
                                        <ArrowUp size={12} className="mr-1" />
                                        <span>3.8% from last month</span>
                                    </div>
                                </div>

                                <div className="bg-white/5 backdrop-blur-sm rounded-xl p-6 border border-white/10 hover:border-emerald-500/50 transition">
                                    <div className="flex justify-between">
                                        <div>
                                            <p className="text-sm font-medium text-gray-300">Marriage Funds</p>
                                            <p className="text-2xl font-bold">₹{dashboardData.financialSummary.marriageFunds.toLocaleString()}</p>
                                        </div>
                                        <div className="h-12 w-12 rounded-lg bg-emerald-500/20 flex items-center justify-center">
                                            <Heart size={24} className="text-emerald-400" />
                                        </div>
                                    </div>
                                    <div className="mt-2 flex items-center text-xs text-emerald-400">
                                        <ArrowUp size={12} className="mr-1" />
                                        <span>4.5% from last month</span>
                                    </div>
                                </div>

                                <div className="bg-white/5 backdrop-blur-sm rounded-xl p-6 border border-white/10 hover:border-emerald-500/50 transition">
                                    <div className="flex justify-between">
                                        <div>
                                            <p className="text-sm font-medium text-gray-300">Monthly Contribution</p>
                                            <p className="text-2xl font-bold">₹{dashboardData.financialSummary.monthlyContribution.toLocaleString()}</p>
                                        </div>
                                        <div className="h-12 w-12 rounded-lg bg-emerald-500/20 flex items-center justify-center">
                                            <TrendingUp size={24} className="text-emerald-400" />
                                        </div>
                                    </div>
                                    <div className="mt-2 flex items-center text-xs text-red-400">
                                        <ArrowDown size={12} className="mr-1" />
                                        <span>Need +₹5,000/month to reach goals</span>
                                    </div>
                                </div>
                            </div>

                            {/* Main Content Sections */}
                            <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                                {/* Left Column */}
                                <div className="lg:col-span-2 space-y-8">
                                    {/* Progress Cards */}
                                    <div className="bg-white/5 backdrop-blur-sm rounded-xl p-6 border border-white/10">
                                        <div className="flex justify-between items-center mb-6">
                                            <h3 className="text-xl font-semibold">Savings Progress</h3>
                                            <button className="text-emerald-400 text-sm hover:underline">View All</button>
                                        </div>

                                        <div className="space-y-6">
                                            <div>
                                                <div className="flex justify-between items-center mb-2">
                                                    <div className="flex items-center">
                                                        <BookOpen size={16} className="text-emerald-400 mr-2" />
                                                        <span className="text-sm font-medium">Education Fund</span>
                                                    </div>
                                                    <span className="text-sm font-medium">
                                                  {getProgressPercentage(dashboardData.savingsProgress.education.current, dashboardData.savingsProgress.education.target)}%
                                                </span>
                                                </div>
                                                <div className="w-full bg-white/10 rounded-full h-2">
                                                    <div
                                                        className={`h-2 rounded-full ${getProgressColor(dashboardData.savingsProgress.education.current, dashboardData.savingsProgress.education.target)}`}
                                                        style={{ width: `${getProgressPercentage(dashboardData.savingsProgress.education.current, dashboardData.savingsProgress.education.target)}%` }}
                                                    ></div>
                                                </div>
                                                <div className="flex justify-between mt-1 text-xs text-gray-400">
                                                    <span>₹{dashboardData.savingsProgress.education.current.toLocaleString()}</span>
                                                    <span>Target: ₹{dashboardData.savingsProgress.education.target.toLocaleString()}</span>
                                                </div>
                                            </div>

                                            <div>
                                                <div className="flex justify-between items-center mb-2">
                                                    <div className="flex items-center">
                                                        <Heart size={16} className="text-emerald-400 mr-2" />
                                                        <span className="text-sm font-medium">Marriage Fund</span>
                                                    </div>
                                                    <span className="text-sm font-medium">
                                                  {getProgressPercentage(dashboardData.savingsProgress.marriage.current, dashboardData.savingsProgress.marriage.target)}%
                                                </span>
                                                </div>
                                                <div className="w-full bg-white/10 rounded-full h-2">
                                                    <div
                                                        className={`h-2 rounded-full ${getProgressColor(dashboardData.savingsProgress.marriage.current, dashboardData.savingsProgress.marriage.target)}`}
                                                        style={{ width: `${getProgressPercentage(dashboardData.savingsProgress.marriage.current, dashboardData.savingsProgress.marriage.target)}%` }}
                                                    ></div>
                                                </div>
                                                <div className="flex justify-between mt-1 text-xs text-gray-400">
                                                    <span>₹{dashboardData.savingsProgress.marriage.current.toLocaleString()}</span>
                                                    <span>Target: ₹{dashboardData.savingsProgress.marriage.target.toLocaleString()}</span>
                                                </div>
                                            </div>

                                            <div>
                                                <div className="flex justify-between items-center mb-2">
                                                    <div className="flex items-center">
                                                        <DollarSign size={16} className="text-emerald-400 mr-2" />
                                                        <span className="text-sm font-medium">Emergency Fund</span>
                                                    </div>
                                                    <span className="text-sm font-medium">
                                                  {getProgressPercentage(dashboardData.savingsProgress.emergency.current, dashboardData.savingsProgress.emergency.target)}%
                                                </span>
                                                </div>
                                                <div className="w-full bg-white/10 rounded-full h-2">
                                                    <div
                                                        className={`h-2 rounded-full ${getProgressColor(dashboardData.savingsProgress.emergency.current, dashboardData.savingsProgress.emergency.target)}`}
                                                        style={{ width: `${getProgressPercentage(dashboardData.savingsProgress.emergency.current, dashboardData.savingsProgress.emergency.target)}%` }}
                                                    ></div>
                                                </div>
                                                <div className="flex justify-between mt-1 text-xs text-gray-400">
                                                    <span>₹{dashboardData.savingsProgress.emergency.current.toLocaleString()}</span>
                                                    <span>Target: ₹{dashboardData.savingsProgress.emergency.target.toLocaleString()}</span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    {/* Recent Transactions */}
                                    <div className="bg-white rounded-lg shadow-sm p-6">
                                        <div className="flex justify-between items-center mb-4">
                                            <h3 className="text-lg font-semibold text-gray-800">Recent Transactions</h3>
                                            <button className="text-blue-600 text-sm hover:underline">View All</button>
                                        </div>

                                        <div className="space-y-3">
                                            {dashboardData.recentTransactions.map((transaction, index) => (
                                                <div key={index} className="flex items-center justify-between p-3 hover:bg-gray-50 rounded-lg">
                                                    <div className="flex items-center">
                                                        <div className="mr-3">
                                                            {getCategoryIcon(transaction.category)}
                                                        </div>
                                                        <div>
                                                            <p className="text-sm font-medium text-gray-800">{transaction.description}</p>
                                                            <p className="text-xs text-gray-500">{transaction.date}</p>
                                                        </div>
                                                    </div>
                                                    <p className="text-sm font-medium text-gray-800">₹{transaction.amount.toLocaleString()}</p>
                                                </div>
                                            ))}
                                        </div>

                                        <div className="mt-4 pt-4 border-t border-gray-200">
                                            <button className="w-full py-2 bg-blue-50 text-blue-600 text-sm font-medium rounded-md hover:bg-blue-100">
                                                Add Transaction
                                            </button>
                                        </div>
                                    </div>
                                </div>

                                {/* Right Column */}
                                <div className="space-y-6">
                                    {/* Financial Alerts */}
                                    <div className="bg-white rounded-lg shadow-sm p-6">
                                        <div className="flex items-center justify-between mb-4">
                                            <h3 className="text-lg font-semibold text-gray-800">Financial Alerts</h3>
                                            <AlertTriangle size={18} className="text-yellow-500" />
                                        </div>

                                        <div className="space-y-3">
                                            {dashboardData.financialAlerts.map((alert, index) => (
                                                <div
                                                    key={index}
                                                    className={`p-3 rounded-lg border ${getSeverityColor(alert.severity)} text-sm`}
                                                >
                                                    {alert.message}
                                                </div>
                                            ))}
                                        </div>
                                    </div>

                                    {/* Upcoming Milestones */}
                                    <div className="bg-white rounded-lg shadow-sm p-6">
                                        <div className="flex items-center justify-between mb-4">
                                            <h3 className="text-lg font-semibold text-gray-800">Upcoming Milestones</h3>
                                            <Calendar size={18} className="text-blue-500" />
                                        </div>

                                        <div className="space-y-4">
                                            {dashboardData.upcomingMilestones.map((milestone, index) => (
                                                <div key={index} className="flex items-start">
                                                    <div className="mt-1 mr-3">
                                                        {getCategoryIcon(milestone.type)}
                                                    </div>
                                                    <div>
                                                        <p className="text-sm font-medium text-gray-800">{milestone.name}</p>
                                                        <p className="text-xs text-gray-500">₹{milestone.amount.toLocaleString()} - {milestone.date}</p>
                                                    </div>
                                                </div>
                                            ))}
                                        </div>

                                        <div className="mt-4">
                                            <button className="text-blue-600 text-sm hover:underline flex items-center">
                                                <Calendar size={14} className="mr-1" />
                                                View Full Timeline
                                            </button>
                                        </div>
                                    </div>

                                    {/* Action Buttons */}
                                    <div className="bg-white rounded-lg shadow-sm p-6">
                                        <h3 className="text-lg font-semibold text-gray-800 mb-4">Quick Actions</h3>

                                        <div className="grid grid-cols-2 gap-3">
                                            <button className="p-3 bg-blue-50 rounded-lg hover:bg-blue-100 flex flex-col items-center">
                                                <Target size={18} className="text-blue-600 mb-1" />
                                                <span className="text-sm font-medium text-gray-700">Set New Goal</span>
                                            </button>

                                            <button className="p-3 bg-green-50 rounded-lg hover:bg-green-100 flex flex-col items-center">
                                                <TrendingUp size={18} className="text-green-600 mb-1" />
                                                <span className="text-sm font-medium text-gray-700">Adjust Plan</span>
                                            </button>

                                            <button className="p-3 bg-purple-50 rounded-lg hover:bg-purple-100 flex flex-col items-center">
                                                <PieChart size={18} className="text-purple-600 mb-1" />
                                                <span className="text-sm font-medium text-gray-700">View Reports</span>
                                            </button>

                                            <button className="p-3 bg-yellow-50 rounded-lg hover:bg-yellow-100 flex flex-col items-center">
                                                <BarChart2 size={18} className="text-yellow-600 mb-1" />
                                                <span className="text-sm font-medium text-gray-700">Forecasting</span>
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                )}
            </main>
        </div>
    );
}

export default Dashboard;