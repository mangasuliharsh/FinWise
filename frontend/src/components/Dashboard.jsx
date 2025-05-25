import { useState, useEffect } from 'react';
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

export default function EnhancedDashboard() {
    const [activeTab, setActiveTab] = useState('overview');
    const [selectedTimeframe, setSelectedTimeframe] = useState('monthly');

    // Calculate total savings across all plans
    const totalEducationSavings = educationPlans.reduce((sum, plan) => sum + plan.currentSavings, 0);
    const totalMarriageSavings = marriagePlans.reduce((sum, plan) => sum + plan.currentSavings, 0);
    const totalGeneralSavings = savingsPlans.reduce((sum, plan) => sum + plan.currentAmount, 0);
    const totalSavings = totalEducationSavings + totalMarriageSavings + totalGeneralSavings;

    // Calculate total monthly contributions
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

    return (
        <div className="min-h-screen bg-gradient-to-br from-blue-900 to-indigo-950 text-white pb-10">
            {/* Enhanced Navigation Bar */}
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

            <div className="max-w-7xl mx-auto px-6 md:px-16 lg:px-24 mt-8">
                {/* Enhanced Dashboard Header */}
                <div className="mb-8">
                    <h2 className="text-3xl font-bold">Family Financial Dashboard</h2>
                    <p className="text-gray-300">Comprehensive financial planning for your family's future</p>
                    <div className="mt-4 flex items-center space-x-6 text-sm">
                        <div className="flex items-center">
                            <Briefcase size={16} className="text-emerald-400 mr-2" />
                            <span>Monthly Income: ₹{familyProfile.monthlyIncome.toLocaleString()}</span>
                        </div>
                        <div className="flex items-center">
                            <Calculator size={16} className="text-red-400 mr-2" />
                            <span>Monthly Expenses: ₹{familyProfile.monthlyExpenses.toLocaleString()}</span>
                        </div>
                        <div className="flex items-center">
                            <TrendingUp size={16} className="text-green-400 mr-2" />
                            <span>Net Savings: ₹{familyProfile.netMonthlySavings.toLocaleString()}</span>
                        </div>
                    </div>
                </div>

                {/* Enhanced Summary Cards */}
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-5 gap-6 mb-8">
                    <div className="bg-white/5 backdrop-blur-sm rounded-xl p-6 border border-white/10 hover:border-emerald-500/50 transition">
                        <div className="flex justify-between">
                            <div>
                                <p className="text-sm font-medium text-gray-300">Total Savings</p>
                                <p className="text-2xl font-bold">₹{totalSavings.toLocaleString()}</p>
                            </div>
                            <div className="h-12 w-12 rounded-lg bg-emerald-500/20 flex items-center justify-center">
                                <DollarSign size={24} className="text-emerald-400" />
                            </div>
                        </div>
                        <div className="mt-2 flex items-center text-xs text-emerald-400">
                            <ArrowUp size={12} className="mr-1" />
                            <span>8.2% from last month</span>
                        </div>
                    </div>

                    <div className="bg-white/5 backdrop-blur-sm rounded-xl p-6 border border-white/10 hover:border-emerald-500/50 transition">
                        <div className="flex justify-between">
                            <div>
                                <p className="text-sm font-medium text-gray-300">Education Funds</p>
                                <p className="text-2xl font-bold">₹{totalEducationSavings.toLocaleString()}</p>
                            </div>
                            <div className="h-12 w-12 rounded-lg bg-emerald-500/20 flex items-center justify-center">
                                <BookOpen size={24} className="text-emerald-400" />
                            </div>
                        </div>
                        <div className="mt-2 text-xs text-gray-400">
                            <span>{educationPlans.length} active plans</span>
                        </div>
                    </div>

                    <div className="bg-white/5 backdrop-blur-sm rounded-xl p-6 border border-white/10 hover:border-emerald-500/50 transition">
                        <div className="flex justify-between">
                            <div>
                                <p className="text-sm font-medium text-gray-300">Marriage Funds</p>
                                <p className="text-2xl font-bold">₹{totalMarriageSavings.toLocaleString()}</p>
                            </div>
                            <div className="h-12 w-12 rounded-lg bg-emerald-500/20 flex items-center justify-center">
                                <Heart size={24} className="text-emerald-400" />
                            </div>
                        </div>
                        <div className="mt-2 text-xs text-gray-400">
                            <span>{marriagePlans.length} wedding plans</span>
                        </div>
                    </div>

                    <div className="bg-white/5 backdrop-blur-sm rounded-xl p-6 border border-white/10 hover:border-emerald-500/50 transition">
                        <div className="flex justify-between">
                            <div>
                                <p className="text-sm font-medium text-gray-300">General Savings</p>
                                <p className="text-2xl font-bold">₹{totalGeneralSavings.toLocaleString()}</p>
                            </div>
                            <div className="h-12 w-12 rounded-lg bg-emerald-500/20 flex items-center justify-center">
                                <Target size={24} className="text-emerald-400" />
                            </div>
                        </div>
                        <div className="mt-2 text-xs text-gray-400">
                            <span>{savingsPlans.length} savings goals</span>
                        </div>
                    </div>

                    <div className="bg-white/5 backdrop-blur-sm rounded-xl p-6 border border-white/10 hover:border-emerald-500/50 transition">
                        <div className="flex justify-between">
                            <div>
                                <p className="text-sm font-medium text-gray-300">Monthly SIP</p>
                                <p className="text-2xl font-bold">₹{totalMonthlyContributions.toLocaleString()}</p>
                            </div>
                            <div className="h-12 w-12 rounded-lg bg-emerald-500/20 flex items-center justify-center">
                                <TrendingUp size={24} className="text-emerald-400" />
                            </div>
                        </div>
                        <div className="mt-2 flex items-center text-xs text-yellow-400">
                            <AlertTriangle size={12} className="mr-1" />
                            <span>{((totalMonthlyContributions / familyProfile.netMonthlySavings) * 100).toFixed(1)}% of net savings</span>
                        </div>
                    </div>
                </div>

                {/* Enhanced Main Content */}
                <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                    {/* Left Column - Enhanced */}
                    <div className="lg:col-span-2 space-y-8">
                        {/* Children & Education Plans */}
                        <div className="bg-white/5 backdrop-blur-sm rounded-xl p-6 border border-white/10">
                            <div className="flex justify-between items-center mb-6">
                                <h3 className="text-xl font-semibold">Children & Education Planning</h3>
                                <button className="text-emerald-400 text-sm hover:underline flex items-center">
                                    <Plus size={16} className="mr-1" />
                                    Add Child
                                </button>
                            </div>

                            <div className="space-y-6">
                                {children.map((child) => {
                                    const childPlans = educationPlans.filter(plan => plan.childId === child.id);
                                    return (
                                        <div key={child.id} className="bg-white/5 rounded-lg p-4">
                                            <div className="flex justify-between items-start mb-4">
                                                <div>
                                                    <h4 className="font-semibold text-lg">{child.name}</h4>
                                                    <p className="text-sm text-gray-400">
                                                        Age: {calculateAge(child.dateOfBirth)} years | Level: {child.currentEducationLevel}
                                                    </p>
                                                </div>
                                                <button className="text-emerald-400 text-sm hover:underline">
                                                    Edit Plans
                                                </button>
                                            </div>

                                            {childPlans.map((plan) => {
                                                const progress = calculateProgressPercentage(plan.currentSavings, plan.estimatedTotalCost);
                                                return (
                                                    <div key={plan.id} className="mb-4 last:mb-0">
                                                        <div className="flex justify-between items-center mb-2">
                                                            <span className="text-sm font-medium">{plan.planName}</span>
                                                            <span className="text-sm">{progress}%</span>
                                                        </div>
                                                        <div className="w-full bg-white/10 rounded-full h-2">
                                                            <div
                                                                className={`h-2 rounded-full ${getProgressColor(progress)}`}
                                                                style={{ width: `${progress}%` }}
                                                            ></div>
                                                        </div>
                                                        <div className="flex justify-between mt-1 text-xs text-gray-400">
                                                            <span>₹{plan.currentSavings.toLocaleString()}</span>
                                                            <span>Target: ₹{plan.estimatedTotalCost.toLocaleString()}</span>
                                                        </div>
                                                        <div className="mt-2 text-xs text-gray-400">
                                                            <span>Monthly SIP: ₹{plan.monthlyContribution.toLocaleString()} | </span>
                                                            <span>Start Year: {plan.estimatedStartYear}</span>
                                                        </div>
                                                    </div>
                                                );
                                            })}
                                        </div>
                                    );
                                })}
                            </div>
                        </div>

                        {/* Marriage Planning */}
                        <div className="bg-white/5 backdrop-blur-sm rounded-xl p-6 border border-white/10">
                            <div className="flex justify-between items-center mb-6">
                                <h3 className="text-xl font-semibold">Marriage Planning</h3>
                                <button className="text-emerald-400 text-sm hover:underline flex items-center">
                                    <Plus size={16} className="mr-1" />
                                    Add Plan
                                </button>
                            </div>

                            <div className="space-y-4">
                                {marriagePlans.map((plan) => {
                                    const progress = calculateProgressPercentage(plan.currentSavings, plan.estimatedTotalCost);
                                    return (
                                        <div key={plan.id} className="bg-white/5 rounded-lg p-4">
                                            <div className="flex justify-between items-center mb-2">
                                                <div>
                                                    <h4 className="font-medium">{plan.planName}</h4>
                                                    <p className="text-sm text-gray-400">
                                                        For: {plan.forName} ({plan.relationship}) | Year: {plan.estimatedYear}
                                                    </p>
                                                </div>
                                                <span className="text-sm font-medium">{progress}%</span>
                                            </div>
                                            <div className="w-full bg-white/10 rounded-full h-2 mb-2">
                                                <div
                                                    className={`h-2 rounded-full ${getProgressColor(progress)}`}
                                                    style={{ width: `${progress}%` }}
                                                ></div>
                                            </div>
                                            <div className="flex justify-between text-xs text-gray-400">
                                                <span>₹{plan.currentSavings.toLocaleString()}</span>
                                                <span>Target: ₹{plan.estimatedTotalCost.toLocaleString()}</span>
                                            </div>
                                        </div>
                                    );
                                })}
                            </div>
                        </div>

                        {/* Recent Transactions */}
                        <div className="bg-white/5 backdrop-blur-sm rounded-xl p-6 border border-white/10">
                            <div className="flex justify-between items-center mb-6">
                                <h3 className="text-xl font-semibold">Recent Transactions</h3>
                                <button className="text-emerald-400 text-sm hover:underline">View All</button>
                            </div>

                            <div className="space-y-3">
                                {recentTransactions.slice(0, 5).map((transaction) => (
                                    <div key={transaction.id} className="flex items-center justify-between p-3 hover:bg-white/5 rounded-lg">
                                        <div className="flex items-center">
                                            <div className="mr-3">
                                                {getCategoryIcon(transaction.relatedPlanType)}
                                            </div>
                                            <div>
                                                <p className="text-sm font-medium">{transaction.description}</p>
                                                <p className="text-xs text-gray-400">{transaction.date}</p>
                                            </div>
                                        </div>
                                        <div className="text-right">
                                            <p className="text-sm font-medium text-emerald-400">
                                                +₹{transaction.amount.toLocaleString()}
                                            </p>
                                            <p className="text-xs text-gray-400">{transaction.type}</p>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        </div>
                    </div>

                    {/* Right Column - Enhanced */}
                    <div className="space-y-6">
                        {/* Investment Portfolio */}
                        <div className="bg-white/5 backdrop-blur-sm rounded-xl p-6 border border-white/10">
                            <div className="flex items-center justify-between mb-4">
                                <h3 className="text-lg font-semibold">Investment Options</h3>
                                <PieChart size={18} className="text-emerald-400" />
                            </div>

                            <div className="space-y-3">
                                {investmentOptions.map((option) => (
                                    <div key={option.id} className="flex justify-between items-center p-3 bg-white/5 rounded-lg">
                                        <div>
                                            <p className="text-sm font-medium">{option.name}</p>
                                            <p className="text-xs text-gray-400">{option.type}</p>
                                        </div>
                                        <div className="text-right">
                                            <p className="text-sm font-medium">{option.expectedAnnualReturn}%</p>
                                            <p className={`text-xs ${getRiskColor(option.riskLevel)}`}>
                                                {option.riskLevel} Risk
                                            </p>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        </div>

                        {/* Savings Goals */}
                        <div className="bg-white/5 backdrop-blur-sm rounded-xl p-6 border border-white/10">
                            <div className="flex items-center justify-between mb-4">
                                <h3 className="text-lg font-semibold">Savings Goals</h3>
                                <Target size={18} className="text-emerald-400" />
                            </div>

                            <div className="space-y-4">
                                {savingsPlans.map((plan) => {
                                    const progress = calculateProgressPercentage(plan.currentAmount, plan.goalAmount);
                                    return (
                                        <div key={plan.id}>
                                            <div className="flex justify-between items-center mb-2">
                                                <div>
                                                    <p className="text-sm font-medium">{plan.planName}</p>
                                                    <p className="text-xs text-gray-400">{plan.purpose}</p>
                                                </div>
                                                <span className={`text-xs px-2 py-1 rounded border ${getPriorityColor(plan.priority)}`}>
                                                    {plan.priority}
                                                </span>
                                            </div>
                                            <div className="w-full bg-white/10 rounded-full h-2 mb-1">
                                                <div
                                                    className={`h-2 rounded-full ${getProgressColor(progress)}`}
                                                    style={{ width: `${progress}%` }}
                                                ></div>
                                            </div>
                                            <div className="flex justify-between text-xs text-gray-400">
                                                <span>₹{plan.currentAmount.toLocaleString()}</span>
                                                <span>₹{plan.goalAmount.toLocaleString()}</span>
                                            </div>
                                        </div>
                                    );
                                })}
                            </div>
                        </div>

                        {/* Notifications */}
                        <div className="bg-white/5 backdrop-blur-sm rounded-xl p-6 border border-white/10">
                            <div className="flex items-center justify-between mb-4">
                                <h3 className="text-lg font-semibold">Notifications</h3>
                                <Bell size={18} className="text-emerald-400" />
                            </div>

                            <div className="space-y-3">
                                {notifications.slice(0, 3).map((notification) => (
                                    <div key={notification.id} className={`p-3 rounded-lg border ${
                                        notification.type === 'WARNING' ? 'bg-yellow-900/20 border-yellow-800/30' :
                                            notification.type === 'SUCCESS' ? 'bg-green-900/20 border-green-800/30' :
                                                'bg-blue-900/20 border-blue-800/30'
                                    }`}>
                                        <div className="flex justify-between items-start">
                                            <div>
                                                <p className="text-sm font-medium">{notification.title}</p>
                                                <p className="text-xs text-gray-400 mt-1">{notification.message}</p>
                                            </div>
                                            {!notification.isRead && (
                                                <div className="h-2 w-2 bg-emerald-400 rounded-full"></div>
                                            )}
                                        </div>
                                    </div>
                                ))}
                            </div>
                        </div>

                        {/* Quick Actions */}
                        <div className="bg-white/5 backdrop-blur-sm rounded-xl p-6 border border-white/10">
                            <h3 className="text-lg font-semibold mb-4">Quick Actions</h3>

                            <div className="grid grid-cols-2 gap-3">
                                <button className="p-3 bg-emerald-500/20 rounded-lg hover:bg-emerald-500/30 flex flex-col items-center">
                                    <Plus size={18} className="text-emerald-400 mb-1" />
                                    <span className="text-sm font-medium">Add Goal</span>
                                </button>

                                <button className="p-3 bg-blue-500/20 rounded-lg hover:bg-blue-500/30 flex flex-col items-center">
                                    <FileText size={18} className="text-blue-400 mb-1" />
                                    <span className="text-sm font-medium">Reports</span>
                                </button>

                                <button className="p-3 bg-purple-500/20 rounded-lg hover:bg-purple-500/30 flex flex-col items-center">
                                    <Calculator size={18} className="text-purple-400 mb-1" />
                                    <span className="text-sm font-medium">Calculator</span>
                                </button>

                                <button className="p-3 bg-yellow-500/20 rounded-lg hover:bg-yellow-500/30 flex flex-col items-center">
                                    <BarChart2 size={18} className="text-yellow-400 mb-1" />
                                    <span className="text-sm font-medium">Analytics</span>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}
