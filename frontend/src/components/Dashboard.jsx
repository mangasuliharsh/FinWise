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
    ChevronRight,
    Users,
    MapPin,
    Briefcase,
    Calculator,
    FileText,
    Plus
} from 'lucide-react';
import axios from "axios";

// Enhanced data structure based on DB schema
const familyProfile = {
    id: 1,
    familySize: 4,
    monthlyIncome: 150000,
    monthlyExpenses: 80000,
    location: 'BANGALORE',
    riskTolerance: 'MEDIUM',
    netMonthlySavings: 70000
};

const children = [
    { id: 1, name: 'Aarav Kumar', dateOfBirth: '2018-05-15', currentEducationLevel: 'PRIMARY' },
    { id: 2, name: 'Diya Patel', dateOfBirth: '2019-03-22', currentEducationLevel: 'PRIMARY' }
];

const educationPlans = [
    {
        id: 1,
        childId: 1,
        planName: 'Aarav Engineering Education',
        educationLevel: 'UNDERGRADUATE',
        institutionType: 'PRIVATE',
        estimatedStartYear: 2036,
        estimatedEndYear: 2040,
        estimatedTotalCost: 1200000,
        currentSavings: 180000,
        monthlyContribution: 8000,
        inflationRate: 6.0
    },
    {
        id: 2,
        childId: 2,
        planName: 'Diya Medical Education',
        educationLevel: 'UNDERGRADUATE',
        institutionType: 'PRIVATE',
        estimatedStartYear: 2037,
        estimatedEndYear: 2042,
        estimatedTotalCost: 1500000,
        currentSavings: 120000,
        monthlyContribution: 10000,
        inflationRate: 6.0
    }
];

const marriagePlans = [
    {
        id: 1,
        planName: 'Aarav Wedding Plan',
        forName: 'Aarav Kumar',
        relationship: 'Son',
        estimatedYear: 2045,
        estimatedTotalCost: 800000,
        currentSavings: 120000,
        monthlyContribution: 5000,
        inflationRate: 6.0
    },
    {
        id: 2,
        planName: 'Diya Wedding Plan',
        forName: 'Diya Patel',
        relationship: 'Daughter',
        estimatedYear: 2047,
        estimatedTotalCost: 900000,
        currentSavings: 80000,
        monthlyContribution: 4000,
        inflationRate: 6.0
    }
];

const savingsPlans = [
    {
        id: 1,
        planName: 'Emergency Fund',
        goalAmount: 500000,
        currentAmount: 150000,
        monthlyContribution: 8000,
        targetCompletionDate: '2026-12-31',
        purpose: 'Emergency Reserve',
        priority: 'HIGH'
    },
    {
        id: 2,
        planName: 'Retirement Fund',
        goalAmount: 5000000,
        currentAmount: 800000,
        monthlyContribution: 15000,
        targetCompletionDate: '2050-12-31',
        purpose: 'Retirement Planning',
        priority: 'HIGH'
    },
    {
        id: 3,
        planName: 'Home Renovation',
        goalAmount: 300000,
        currentAmount: 75000,
        monthlyContribution: 5000,
        targetCompletionDate: '2027-06-30',
        purpose: 'Home Improvement',
        priority: 'MEDIUM'
    }
];

const investmentOptions = [
    { id: 1, name: 'SIP Mutual Funds', type: 'EQUITY', riskLevel: 'MEDIUM', expectedAnnualReturn: 12.0 },
    { id: 2, name: 'PPF', type: 'DEBT', riskLevel: 'LOW', expectedAnnualReturn: 7.1 },
    { id: 3, name: 'ELSS', type: 'EQUITY', riskLevel: 'HIGH', expectedAnnualReturn: 15.0 },
    { id: 4, name: 'Fixed Deposits', type: 'DEBT', riskLevel: 'LOW', expectedAnnualReturn: 6.5 }
];

const recentTransactions = [
    { id: 1, type: 'DEPOSIT', amount: 8000, date: '2025-05-20', description: 'Aarav Education Fund SIP', relatedPlanType: 'EDUCATION' },
    { id: 2, type: 'DEPOSIT', amount: 10000, date: '2025-05-20', description: 'Diya Education Fund SIP', relatedPlanType: 'EDUCATION' },
    { id: 3, type: 'DEPOSIT', amount: 5000, date: '2025-05-15', description: 'Aarav Marriage Fund', relatedPlanType: 'MARRIAGE' },
    { id: 4, type: 'DEPOSIT', amount: 8000, date: '2025-05-10', description: 'Emergency Fund Contribution', relatedPlanType: 'SAVINGS' },
    { id: 5, type: 'DEPOSIT', amount: 15000, date: '2025-05-05', description: 'Retirement Fund SIP', relatedPlanType: 'SAVINGS' }
];

const notifications = [
    { id: 1, title: 'Education Fund Alert', message: 'Aarav education fund is 15% below target for timeline', type: 'WARNING', isRead: false },
    { id: 2, title: 'Investment Maturity', message: 'Your PPF investment will mature next month', type: 'INFO', isRead: false },
    { id: 3, title: 'Goal Achievement', message: 'Emergency fund reached 30% of target!', type: 'SUCCESS', isRead: true }
];

// Education Page Component
function EducationPage({ childrenList }) {
    return (
        <div className="space-y-6">
            <h2 className="text-3xl font-bold text-white mb-6">Education Planning</h2>

            <div className="grid gap-6 md:grid-cols-2">
                {educationPlans.map(plan => {
                    const progress = Math.min((plan.currentSavings / plan.estimatedTotalCost) * 100, 100);
                    const yearsLeft = plan.estimatedStartYear - new Date().getFullYear();

                    return (
                        <div key={plan.id} className="bg-white/5 backdrop-blur-sm rounded-xl p-6 border border-white/10">
                            <div className="flex items-center justify-between mb-4">
                                <h3 className="text-xl font-semibold text-white">{plan.planName}</h3>
                                <BookOpen className="text-emerald-400" size={24} />
                            </div>

                            <div className="space-y-4">
                                <div>
                                    <div className="flex justify-between mb-2">
                                        <span className="text-gray-300">Progress</span>
                                        <span className="text-emerald-400">{progress.toFixed(1)}%</span>
                                    </div>
                                    <div className="w-full bg-gray-700 rounded-full h-2">
                                        <div
                                            className="bg-emerald-500 h-2 rounded-full transition-all duration-300"
                                            style={{ width: `${progress}%` }}
                                        ></div>
                                    </div>
                                </div>

                                <div className="grid grid-cols-2 gap-4 text-sm">
                                    <div>
                                        <p className="text-gray-400">Current Savings</p>
                                        <p className="text-white font-semibold">₹{plan.currentSavings.toLocaleString()}</p>
                                    </div>
                                    <div>
                                        <p className="text-gray-400">Target Amount</p>
                                        <p className="text-white font-semibold">₹{plan.estimatedTotalCost.toLocaleString()}</p>
                                    </div>
                                    <div>
                                        <p className="text-gray-400">Monthly SIP</p>
                                        <p className="text-white font-semibold">₹{plan.monthlyContribution.toLocaleString()}</p>
                                    </div>
                                    <div>
                                        <p className="text-gray-400">Years Left</p>
                                        <p className="text-white font-semibold">{yearsLeft} years</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    );
                })}
            </div>

            <div className="bg-white/5 backdrop-blur-sm rounded-xl p-6 border border-white/10">
                <h3 className="text-xl font-semibold text-white mb-4">Children Details</h3>
                <div className="grid gap-4 md:grid-cols-2">
                    {childrenList && childrenList.map((child, idx) => (
                        <div key={idx} className="bg-white/10 rounded-lg p-4">
                            <h4 className="text-white font-semibold">{child.name}</h4>
                            <p className="text-gray-300 text-sm">Age: {new Date().getFullYear() - new Date(child.dateOfBirth).getFullYear()} years</p>
                            <p className="text-gray-300 text-sm">Education Level: {child.currentEducationLevel}</p>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
}

// Marriage Page Component
function MarriagePage() {
    return (
        <div className="space-y-6">
            <h2 className="text-3xl font-bold text-white mb-6">Marriage Planning</h2>

            <div className="grid gap-6 md:grid-cols-2">
                {marriagePlans.map(plan => {
                    const progress = Math.min((plan.currentSavings / plan.estimatedTotalCost) * 100, 100);
                    const yearsLeft = plan.estimatedYear - new Date().getFullYear();

                    return (
                        <div key={plan.id} className="bg-white/5 backdrop-blur-sm rounded-xl p-6 border border-white/10">
                            <div className="flex items-center justify-between mb-4">
                                <h3 className="text-xl font-semibold text-white">{plan.planName}</h3>
                                <Heart className="text-emerald-400" size={24} />
                            </div>

                            <div className="space-y-4">
                                <div>
                                    <p className="text-gray-300 mb-2">For: {plan.forName} ({plan.relationship})</p>
                                    <div className="flex justify-between mb-2">
                                        <span className="text-gray-300">Progress</span>
                                        <span className="text-emerald-400">{progress.toFixed(1)}%</span>
                                    </div>
                                    <div className="w-full bg-gray-700 rounded-full h-2">
                                        <div
                                            className="bg-emerald-500 h-2 rounded-full transition-all duration-300"
                                            style={{ width: `${progress}%` }}
                                        ></div>
                                    </div>
                                </div>

                                <div className="grid grid-cols-2 gap-4 text-sm">
                                    <div>
                                        <p className="text-gray-400">Current Savings</p>
                                        <p className="text-white font-semibold">₹{plan.currentSavings.toLocaleString()}</p>
                                    </div>
                                    <div>
                                        <p className="text-gray-400">Target Amount</p>
                                        <p className="text-white font-semibold">₹{plan.estimatedTotalCost.toLocaleString()}</p>
                                    </div>
                                    <div>
                                        <p className="text-gray-400">Monthly SIP</p>
                                        <p className="text-white font-semibold">₹{plan.monthlyContribution.toLocaleString()}</p>
                                    </div>
                                    <div>
                                        <p className="text-gray-400">Estimated Year</p>
                                        <p className="text-white font-semibold">{plan.estimatedYear} ({yearsLeft} years)</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    );
                })}
            </div>
        </div>
    );
}

// Investment Page Component
function InvestmentPage() {
    return (
        <div className="space-y-6">
            <h2 className="text-3xl font-bold text-white mb-6">Investment Options</h2>

            <div className="grid gap-6 md:grid-cols-2">
                {investmentOptions.map(option => {
                    const getRiskColor = (risk) => {
                        switch (risk) {
                            case 'HIGH': return 'text-red-400 bg-red-900/20';
                            case 'MEDIUM': return 'text-yellow-400 bg-yellow-900/20';
                            case 'LOW': return 'text-green-400 bg-green-900/20';
                            default: return 'text-gray-400 bg-gray-900/20';
                        }
                    };

                    return (
                        <div key={option.id} className="bg-white/5 backdrop-blur-sm rounded-xl p-6 border border-white/10">
                            <div className="flex items-center justify-between mb-4">
                                <h3 className="text-xl font-semibold text-white">{option.name}</h3>
                                <TrendingUp className="text-emerald-400" size={24} />
                            </div>

                            <div className="space-y-4">
                                <div className="grid grid-cols-2 gap-4 text-sm">
                                    <div>
                                        <p className="text-gray-400">Type</p>
                                        <p className="text-white font-semibold">{option.type}</p>
                                    </div>
                                    <div>
                                        <p className="text-gray-400">Expected Return</p>
                                        <p className="text-emerald-400 font-semibold">{option.expectedAnnualReturn}% p.a.</p>
                                    </div>
                                </div>

                                <div>
                                    <p className="text-gray-400 mb-2">Risk Level</p>
                                    <span className={`px-3 py-1 rounded-full text-xs font-semibold ${getRiskColor(option.riskLevel)}`}>
                                        {option.riskLevel}
                                    </span>
                                </div>

                                <button className="w-full bg-emerald-500 hover:bg-emerald-600 text-white font-semibold py-2 px-4 rounded-lg transition-colors">
                                    Invest Now
                                </button>
                            </div>
                        </div>
                    );
                })}
            </div>
        </div>
    );
}

export default function EnhancedDashboard() {
    const [activeTab, setActiveTab] = useState('overview');
    const [selectedTimeframe, setSelectedTimeframe] = useState('monthly');

    // Calculate totals
    const totalEducationSavings = educationPlans.reduce((sum, plan) => sum + plan.currentSavings, 0);
    const totalMarriageSavings = marriagePlans.reduce((sum, plan) => sum + plan.currentSavings, 0);
    const totalGeneralSavings = savingsPlans.reduce((sum, plan) => sum + plan.currentAmount, 0);
    const totalSavings = totalEducationSavings + totalMarriageSavings + totalGeneralSavings;

    const totalMonthlyContributions = [
        ...educationPlans.map(p => p.monthlyContribution),
        ...marriagePlans.map(p => p.monthlyContribution),
        ...savingsPlans.map(p => p.monthlyContribution)
    ].reduce((sum, amount) => sum + amount, 0);

    const getCategoryIcon = (category) => {
        switch (category) {
            case 'EDUCATION': return <BookOpen size={16} className="text-emerald-400" />;
            case 'MARRIAGE': return <Heart size={16} className="text-emerald-400" />;
            case 'SAVINGS': return <DollarSign size={16} className="text-emerald-400" />;
            default: return <DollarSign size={16} className="text-emerald-400" />;
        }
    };

    const getRiskColor = (risk) => {
        switch (risk) {
            case 'HIGH': return 'text-red-400';
            case 'MEDIUM': return 'text-yellow-400';
            case 'LOW': return 'text-green-400';
            default: return 'text-gray-400';
        }
    };

    const getPriorityColor = (priority) => {
        switch (priority) {
            case 'HIGH': return 'bg-red-900/20 text-red-400 border-red-800/30';
            case 'MEDIUM': return 'bg-yellow-900/20 text-yellow-400 border-yellow-800/30';
            case 'LOW': return 'bg-green-900/20 text-green-400 border-green-800/30';
            default: return 'bg-gray-900/20 text-gray-400 border-gray-800/30';
        }
    };

    const calculateAge = (dateOfBirth) => {
        const today = new Date();
        const birth = new Date(dateOfBirth);
        let age = today.getFullYear() - birth.getFullYear();
        const monthDiff = today.getMonth() - birth.getMonth();
        if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birth.getDate())) {
            age--;
        }
        return age;
    };

    const calculateProgressPercentage = (current, target) => {
        return Math.min(Math.round((current / target) * 100), 100);
    };

    const getProgressColor = (percentage) => {
        if (percentage >= 75) return "bg-emerald-500";
        if (percentage >= 50) return "bg-yellow-500";
        return "bg-red-500";
    };

    // Dashboard tab state
    const [dashboardTab, setDashboardTab] = useState('overview');

    return (
        <div className="min-h-screen bg-gradient-to-br from-blue-900 to-indigo-950 text-white pb-10">
            {/* Navigation Bar */}
            <nav className="px-6 py-4 flex justify-between items-center md:px-16 lg:px-24 border-b border-white/10">
                <div className="flex items-center space-x-3">
                    <DollarSign size={24} className="text-emerald-400" />
                    <h1 className="text-2xl font-bold tracking-tight">
                        <span className="text-emerald-400">Fin</span>Wise
                    </h1>
                </div>
                <div className="flex items-center space-x-4">
                    <div className="relative">
                        <button className="p-2 rounded-full hover:bg-white/5 relative">
                            <Bell size={20} className="text-gray-300" />
                            {notifications.filter(n => !n.isRead).length > 0 && (
                                <span className="absolute -top-1 -right-1 h-3 w-3 bg-red-500 rounded-full text-xs"></span>
                            )}
                        </button>
                    </div>
                    <button className="p-2 rounded-full hover:bg-white/5">
                        <Settings size={20} className="text-gray-300" />
                    </button>
                    <div className="flex items-center space-x-2">
                        <div className="h-8 w-8 rounded-full bg-emerald-500/20 flex items-center justify-center text-emerald-400">
                            <User size={18} />
                        </div>
                        <div className="text-sm">
                            <span className="font-medium text-gray-300">Akash Family</span>
                            <div className="flex items-center text-xs text-gray-400">
                                <Users size={12} className="mr-1" />
                                {familyProfile.familySize} members
                                <MapPin size={12} className="ml-2 mr-1" />
                                {familyProfile.location}
                            </div>
                        </div>
                    </div>
                </div>
            </nav>

            {/* Dashboard Tabs */}
            <div className="max-w-7xl mx-auto px-6 md:px-16 lg:px-24 mt-8">
                <div className="flex gap-4 mb-8">
                    <button
                        className={`px-4 py-2 rounded-lg font-semibold transition-colors ${
                            dashboardTab === 'overview'
                                ? 'bg-emerald-500 text-white'
                                : 'bg-white/10 text-gray-200 hover:bg-white/20'
                        }`}
                        onClick={() => setDashboardTab('overview')}
                    >
                        Overview
                    </button>
                    <button
                        className={`px-4 py-2 rounded-lg font-semibold transition-colors ${
                            dashboardTab === 'education'
                                ? 'bg-emerald-500 text-white'
                                : 'bg-white/10 text-gray-200 hover:bg-white/20'
                        }`}
                        onClick={() => setDashboardTab('education')}
                    >
                        Education
                    </button>
                    <button
                        className={`px-4 py-2 rounded-lg font-semibold transition-colors ${
                            dashboardTab === 'marriage'
                                ? 'bg-emerald-500 text-white'
                                : 'bg-white/10 text-gray-200 hover:bg-white/20'
                        }`}
                        onClick={() => setDashboardTab('marriage')}
                    >
                        Marriage
                    </button>
                    <button
                        className={`px-4 py-2 rounded-lg font-semibold transition-colors ${
                            dashboardTab === 'investment'
                                ? 'bg-emerald-500 text-white'
                                : 'bg-white/10 text-gray-200 hover:bg-white/20'
                        }`}
                        onClick={() => setDashboardTab('investment')}
                    >
                        Investment
                    </button>
                </div>

                {/* Tab Content */}
                {dashboardTab === 'overview' && (
                    <div className="space-y-8">
                        {/* Header */}
                        <div className="mb-8">
                            <h2 className="text-3xl font-bold">Family Financial Dashboard</h2>
                            <p className="text-gray-300">Comprehensive financial planning for your family's future</p>
                        </div>

                        {/* Summary Cards */}
                        <div className="grid md:grid-cols-2 lg:grid-cols-4 gap-6">
                            <div className="bg-white/5 backdrop-blur-sm rounded-xl p-6 border border-white/10">
                                <div className="flex items-center justify-between mb-4">
                                    <div className="bg-emerald-500/20 rounded-lg p-3">
                                        <DollarSign className="h-6 w-6 text-emerald-400" />
                                    </div>
                                    <ArrowUp className="h-4 w-4 text-emerald-400" />
                                </div>
                                <h3 className="text-2xl font-bold text-white">₹{totalSavings.toLocaleString()}</h3>
                                <p className="text-gray-400">Total Savings</p>
                            </div>

                            <div className="bg-white/5 backdrop-blur-sm rounded-xl p-6 border border-white/10">
                                <div className="flex items-center justify-between mb-4">
                                    <div className="bg-blue-500/20 rounded-lg p-3">
                                        <TrendingUp className="h-6 w-6 text-blue-400" />
                                    </div>
                                    <ArrowUp className="h-4 w-4 text-green-400" />
                                </div>
                                <h3 className="text-2xl font-bold text-white">₹{familyProfile.monthlyIncome.toLocaleString()}</h3>
                                <p className="text-gray-400">Monthly Income</p>
                            </div>

                            <div className="bg-white/5 backdrop-blur-sm rounded-xl p-6 border border-white/10">
                                <div className="flex items-center justify-between mb-4">
                                    <div className="bg-purple-500/20 rounded-lg p-3">
                                        <Target className="h-6 w-6 text-purple-400" />
                                    </div>
                                    <ArrowDown className="h-4 w-4 text-red-400" />
                                </div>
                                <h3 className="text-2xl font-bold text-white">₹{familyProfile.monthlyExpenses.toLocaleString()}</h3>
                                <p className="text-gray-400">Monthly Expenses</p>
                            </div>

                            <div className="bg-white/5 backdrop-blur-sm rounded-xl p-6 border border-white/10">
                                <div className="flex items-center justify-between mb-4">
                                    <div className="bg-yellow-500/20 rounded-lg p-3">
                                        <PieChart className="h-6 w-6 text-yellow-400" />
                                    </div>
                                    <ArrowUp className="h-4 w-4 text-emerald-400" />
                                </div>
                                <h3 className="text-2xl font-bold text-white">₹{totalMonthlyContributions.toLocaleString()}</h3>
                                <p className="text-gray-400">Monthly Investments</p>
                            </div>
                        </div>

                        {/* Recent Transactions */}
                        <div className="bg-white/5 backdrop-blur-sm rounded-xl p-6 border border-white/10">
                            <h3 className="text-xl font-semibold mb-4">Recent Transactions</h3>
                            <div className="space-y-3">
                                {recentTransactions.slice(0, 5).map(transaction => (
                                    <div key={transaction.id} className="flex items-center justify-between p-3 bg-white/5 rounded-lg">
                                        <div className="flex items-center space-x-3">
                                            {getCategoryIcon(transaction.relatedPlanType)}
                                            <div>
                                                <p className="text-white font-medium">{transaction.description}</p>
                                                <p className="text-gray-400 text-sm">{transaction.date}</p>
                                            </div>
                                        </div>
                                        <span className="text-emerald-400 font-semibold">
                                            +₹{transaction.amount.toLocaleString()}
                                        </span>
                                    </div>
                                ))}
                            </div>
                        </div>
                    </div>
                )}

                {dashboardTab === 'education' && (
                    <EducationPage childrenList={children} />
                )}

                {dashboardTab === 'marriage' && (
                    <MarriagePage />
                )}

                {dashboardTab === 'investment' && (
                    <InvestmentPage />
                )}
            </div>
        </div>
    );
}