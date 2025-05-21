import { useState } from 'react';
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
    ChevronRight
} from 'lucide-react';

// Sample data for demonstration
const financialSummary = {
    totalSavings: 425000,
    educationFunds: 180000,
    marriageFunds: 120000,
    emergencyFunds: 75000,
    investmentFunds: 50000,
    monthlyContribution: 15000
};

const upcomingMilestones = [
    { name: "College Tuition Payment", type: "education", amount: 50000, date: "Aug 15, 2025" },
    { name: "Marriage Venue Booking", type: "marriage", amount: 25000, date: "Dec 10, 2025" },
    { name: "Emergency Fund Goal", type: "savings", amount: 100000, date: "Feb 28, 2026" }
];

const savingsProgress = {
    education: { target: 250000, current: 180000 },
    marriage: { target: 350000, current: 120000 },
    emergency: { target: 100000, current: 75000 }
};

const recentTransactions = [
    { description: "Monthly Education Fund", category: "education", amount: 5000, date: "May 15, 2025" },
    { description: "Marriage Fund Deposit", category: "marriage", amount: 7000, date: "May 10, 2025" },
    { description: "Emergency Fund Contribution", category: "savings", amount: 3000, date: "May 5, 2025" }
];

const financialAlerts = [
    { message: "Education fund is 28% below target for timeline", severity: "high" },
    { message: "Consider increasing monthly marriage fund contribution", severity: "medium" },
    { message: "Emergency fund on track", severity: "low" }
];

export default function Dashboard() {
    const [activeTab, setActiveTab] = useState('overview');

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
        <div className="min-h-screen bg-gradient-to-br from-blue-900 to-indigo-950 text-white pb-10">
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
                        onClick={() => setActiveTab('overview')}
                        className={`px-4 py-2 rounded-md text-sm font-medium ${activeTab === 'overview' ? 'bg-emerald-500/20 text-emerald-400' : 'text-gray-300 hover:bg-white/5'}`}
                    >
                        Overview
                    </button>
                    <button
                        onClick={() => setActiveTab('education')}
                        className={`px-4 py-2 rounded-md text-sm font-medium ${activeTab === 'education' ? 'bg-emerald-500/20 text-emerald-400' : 'text-gray-300 hover:bg-white/5'}`}
                    >
                        Education
                    </button>
                    <button
                        onClick={() => setActiveTab('marriage')}
                        className={`px-4 py-2 rounded-md text-sm font-medium ${activeTab === 'marriage' ? 'bg-emerald-500/20 text-emerald-400' : 'text-gray-300 hover:bg-white/5'}`}
                    >
                        Marriage
                    </button>
                    <button
                        onClick={() => setActiveTab('savings')}
                        className={`px-4 py-2 rounded-md text-sm font-medium ${activeTab === 'savings' ? 'bg-emerald-500/20 text-emerald-400' : 'text-gray-300 hover:bg-white/5'}`}
                    >
                        Savings
                    </button>
                    <button
                        onClick={() => setActiveTab('reports')}
                        className={`px-4 py-2 rounded-md text-sm font-medium ${activeTab === 'reports' ? 'bg-emerald-500/20 text-emerald-400' : 'text-gray-300 hover:bg-white/5'}`}
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
                                <p className="text-2xl font-bold">₹{financialSummary.totalSavings.toLocaleString()}</p>
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
                                <p className="text-2xl font-bold">₹{financialSummary.educationFunds.toLocaleString()}</p>
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
                                <p className="text-2xl font-bold">₹{financialSummary.marriageFunds.toLocaleString()}</p>
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
                                <p className="text-2xl font-bold">₹{financialSummary.monthlyContribution.toLocaleString()}</p>
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
                      {getProgressPercentage(savingsProgress.education.current, savingsProgress.education.target)}%
                    </span>
                                    </div>
                                    <div className="w-full bg-white/10 rounded-full h-2">
                                        <div
                                            className={`h-2 rounded-full ${getProgressColor(savingsProgress.education.current, savingsProgress.education.target)}`}
                                            style={{ width: `${getProgressPercentage(savingsProgress.education.current, savingsProgress.education.target)}%` }}
                                        ></div>
                                    </div>
                                    <div className="flex justify-between mt-1 text-xs text-gray-400">
                                        <span>₹{savingsProgress.education.current.toLocaleString()}</span>
                                        <span>Target: ₹{savingsProgress.education.target.toLocaleString()}</span>
                                    </div>
                                </div>

                                <div>
                                    <div className="flex justify-between items-center mb-2">
                                        <div className="flex items-center">
                                            <Heart size={16} className="text-emerald-400 mr-2" />
                                            <span className="text-sm font-medium">Marriage Fund</span>
                                        </div>
                                        <span className="text-sm font-medium">
                      {getProgressPercentage(savingsProgress.marriage.current, savingsProgress.marriage.target)}%
                    </span>
                                    </div>
                                    <div className="w-full bg-white/10 rounded-full h-2">
                                        <div
                                            className={`h-2 rounded-full ${getProgressColor(savingsProgress.marriage.current, savingsProgress.marriage.target)}`}
                                            style={{ width: `${getProgressPercentage(savingsProgress.marriage.current, savingsProgress.marriage.target)}%` }}
                                        ></div>
                                    </div>
                                    <div className="flex justify-between mt-1 text-xs text-gray-400">
                                        <span>₹{savingsProgress.marriage.current.toLocaleString()}</span>
                                        <span>Target: ₹{savingsProgress.marriage.target.toLocaleString()}</span>
                                    </div>
                                </div>

                                <div>
                                    <div className="flex justify-between items-center mb-2">
                                        <div className="flex items-center">
                                            <DollarSign size={16} className="text-emerald-400 mr-2" />
                                            <span className="text-sm font-medium">Emergency Fund</span>
                                        </div>
                                        <span className="text-sm font-medium">
                      {getProgressPercentage(savingsProgress.emergency.current, savingsProgress.emergency.target)}%
                    </span>
                                    </div>
                                    <div className="w-full bg-white/10 rounded-full h-2">
                                        <div
                                            className={`h-2 rounded-full ${getProgressColor(savingsProgress.emergency.current, savingsProgress.emergency.target)}`}
                                            style={{ width: `${getProgressPercentage(savingsProgress.emergency.current, savingsProgress.emergency.target)}%` }}
                                        ></div>
                                    </div>
                                    <div className="flex justify-between mt-1 text-xs text-gray-400">
                                        <span>₹{savingsProgress.emergency.current.toLocaleString()}</span>
                                        <span>Target: ₹{savingsProgress.emergency.target.toLocaleString()}</span>
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
                                {recentTransactions.map((transaction, index) => (
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
                                {financialAlerts.map((alert, index) => (
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
                                {upcomingMilestones.map((milestone, index) => (
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
    );
}