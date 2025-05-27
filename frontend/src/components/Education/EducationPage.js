import React, { useState, useEffect } from 'react';
import { Plus, User, Calendar, GraduationCap, DollarSign, Target, TrendingUp, BookOpen, Edit3, Trash2 } from 'lucide-react';

const EducationPlanningDashboard = () => {
    const [children, setChildren] = useState([
        {
            id: 1,
            name: "Sarah Johnson",
            dateOfBirth: "2015-03-15",
            currentEducationLevel: "Primary School",
            desiredEducationLevel: "Engineering Degree",
            institutionType: "Private",
            estimatedStartYear: 2033,
            estimatedEndYear: 2037,
            estimatedTotalCost: 2500000,
            currentSavings: 150000,
            monthlyContribution: 8000,
            inflationRate: 6.5
        }
    ]);

    const [showAddForm, setShowAddForm] = useState(false);
    const [editingChild, setEditingChild] = useState(null);
    const [newChild, setNewChild] = useState({
        name: '',
        dateOfBirth: '',
        currentEducationLevel: '',
        desiredEducationLevel: '',
        institutionType: 'Government',
        estimatedStartYear: new Date().getFullYear() + 5,
        estimatedEndYear: new Date().getFullYear() + 9,
        estimatedTotalCost: 500000,
        currentSavings: 0,
        monthlyContribution: 5000,
        inflationRate: 6.5
    });

    const educationLevels = [
        'Primary School', 'Middle School', 'High School', 'Undergraduate', 'Postgraduate',
        'Engineering Degree', 'Medical Degree', 'MBA', 'PhD', 'Diploma Course'
    ];

    const institutionTypes = ['Government', 'Private', 'International'];

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

    const calculateFutureValue = (currentSavings, monthlyContribution, years, inflationRate) => {
        const monthlyRate = inflationRate / 100 / 12;
        const months = years * 12;

        const futureValueSavings = currentSavings * Math.pow(1 + inflationRate / 100, years);
        const futureValueContributions = monthlyContribution *
            ((Math.pow(1 + monthlyRate, months) - 1) / monthlyRate);

        return futureValueSavings + futureValueContributions;
    };

    const calculateShortfall = (child) => {
        const yearsToStart = child.estimatedStartYear - new Date().getFullYear();
        const projectedSavings = calculateFutureValue(
            child.currentSavings,
            child.monthlyContribution,
            yearsToStart,
            child.inflationRate
        );
        const inflatedCost = child.estimatedTotalCost * Math.pow(1 + child.inflationRate / 100, yearsToStart);
        return Math.max(0, inflatedCost - projectedSavings);
    };

    const getProgressPercentage = (child) => {
        const yearsToStart = child.estimatedStartYear - new Date().getFullYear();
        const projectedSavings = calculateFutureValue(
            child.currentSavings,
            child.monthlyContribution,
            yearsToStart,
            child.inflationRate
        );
        const inflatedCost = child.estimatedTotalCost * Math.pow(1 + child.inflationRate / 100, yearsToStart);
        return Math.min(100, (projectedSavings / inflatedCost) * 100);
    };

    const handleSubmit = () => {
        if (!newChild.name || !newChild.dateOfBirth || !newChild.currentEducationLevel || !newChild.desiredEducationLevel) {
            alert('Please fill in all required fields');
            return;
        }

        if (editingChild) {
            setChildren(children.map(child =>
                child.id === editingChild.id ? { ...newChild, id: editingChild.id } : child
            ));
            setEditingChild(null);
        } else {
            setChildren([...children, { ...newChild, id: Date.now() }]);
        }
        setNewChild({
            name: '',
            dateOfBirth: '',
            currentEducationLevel: '',
            desiredEducationLevel: '',
            institutionType: 'Government',
            estimatedStartYear: new Date().getFullYear() + 5,
            estimatedEndYear: new Date().getFullYear() + 9,
            estimatedTotalCost: 500000,
            currentSavings: 0,
            monthlyContribution: 5000,
            inflationRate: 6.5
        });
        setShowAddForm(false);
    };

    const handleEdit = (child) => {
        setNewChild(child);
        setEditingChild(child);
        setShowAddForm(true);
    };

    const handleDelete = (childId) => {
        setChildren(children.filter(child => child.id !== childId));
    };

    const totalPlannedExpenses = children.reduce((sum, child) => {
        const yearsToStart = child.estimatedStartYear - new Date().getFullYear();
        return sum + (child.estimatedTotalCost * Math.pow(1 + child.inflationRate / 100, yearsToStart));
    }, 0);

    const totalCurrentSavings = children.reduce((sum, child) => sum + child.currentSavings, 0);
    const totalMonthlyContributions = children.reduce((sum, child) => sum + child.monthlyContribution, 0);

    return (
        <div className="min-h-screen bg-gradient-to-br from-blue-900 via-blue-800 to-indigo-900 text-white">
            {/* Header */}
            <div className="bg-blue-900/50 backdrop-blur-sm border-b border-blue-700/30">
                <div className="max-w-7xl mx-auto px-6 py-4">
                    <div className="flex items-center justify-between">
                        <div className="flex items-center space-x-4">
                            <div className="p-2 bg-green-500 rounded-lg">
                                <GraduationCap className="w-6 h-6" />
                            </div>
                            <div>
                                <h1 className="text-2xl font-bold">Education Planning</h1>
                                <p className="text-blue-200">Plan and track your children's education expenses</p>
                            </div>
                        </div>
                        <button
                            onClick={() => setShowAddForm(true)}
                            className="bg-green-500 hover:bg-green-600 px-4 py-2 rounded-lg flex items-center space-x-2 transition-colors"
                        >
                            <Plus className="w-4 h-4" />
                            <span>Add Child</span>
                        </button>
                    </div>
                </div>
            </div>

            <div className="max-w-7xl mx-auto px-6 py-8">
                {/* Summary Cards */}
                <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
                    <div className="bg-blue-800/40 backdrop-blur-sm border border-blue-700/30 rounded-xl p-6">
                        <div className="flex items-center justify-between mb-4">
                            <div className="p-2 bg-blue-500 rounded-lg">
                                <User className="w-5 h-5" />
                            </div>
                            <TrendingUp className="w-4 h-4 text-blue-400" />
                        </div>
                        <div className="text-2xl font-bold">₹{totalCurrentSavings.toLocaleString()}</div>
                        <div className="text-blue-300 text-sm">Total Current Savings</div>
                    </div>

                    <div className="bg-blue-800/40 backdrop-blur-sm border border-blue-700/30 rounded-xl p-6">
                        <div className="flex items-center justify-between mb-4">
                            <div className="p-2 bg-green-500 rounded-lg">
                                <DollarSign className="w-5 h-5" />
                            </div>
                            <TrendingUp className="w-4 h-4 text-green-400" />
                        </div>
                        <div className="text-2xl font-bold">₹{totalMonthlyContributions.toLocaleString()}</div>
                        <div className="text-blue-300 text-sm">Monthly Contributions</div>
                    </div>

                    <div className="bg-blue-800/40 backdrop-blur-sm border border-blue-700/30 rounded-xl p-6">
                        <div className="flex items-center justify-between mb-4">
                            <div className="p-2 bg-orange-500 rounded-lg">
                                <Target className="w-5 h-5" />
                            </div>
                            <TrendingUp className="w-4 h-4 text-orange-400" />
                        </div>
                        <div className="text-2xl font-bold">₹{(totalPlannedExpenses / 100000).toFixed(1)}L</div>
                        <div className="text-blue-300 text-sm">Total Planned Expenses</div>
                    </div>

                    <div className="bg-blue-800/40 backdrop-blur-sm border border-blue-700/30 rounded-xl p-6">
                        <div className="flex items-center justify-between mb-4">
                            <div className="p-2 bg-purple-500 rounded-lg">
                                <BookOpen className="w-5 h-5" />
                            </div>
                            <TrendingUp className="w-4 h-4 text-purple-400" />
                        </div>
                        <div className="text-2xl font-bold">{children.length}</div>
                        <div className="text-blue-300 text-sm">Children Planned</div>
                    </div>
                </div>

                {/* Children Education Plans */}
                <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8">
                    {children.map((child) => {
                        const progress = getProgressPercentage(child);
                        const shortfall = calculateShortfall(child);
                        const yearsToStart = child.estimatedStartYear - new Date().getFullYear();

                        return (
                            <div key={child.id} className="bg-blue-800/40 backdrop-blur-sm border border-blue-700/30 rounded-xl p-6">
                                <div className="flex items-center justify-between mb-4">
                                    <div className="flex items-center space-x-3">
                                        <div className="p-2 bg-indigo-500 rounded-lg">
                                            <User className="w-5 h-5" />
                                        </div>
                                        <div>
                                            <h3 className="text-lg font-semibold">{child.name}</h3>
                                            <p className="text-blue-300 text-sm">Age: {calculateAge(child.dateOfBirth)} years</p>
                                        </div>
                                    </div>
                                    <div className="flex space-x-2">
                                        <button
                                            onClick={() => handleEdit(child)}
                                            className="p-2 bg-blue-600 hover:bg-blue-700 rounded-lg transition-colors"
                                        >
                                            <Edit3 className="w-4 h-4" />
                                        </button>
                                        <button
                                            onClick={() => handleDelete(child.id)}
                                            className="p-2 bg-red-600 hover:bg-red-700 rounded-lg transition-colors"
                                        >
                                            <Trash2 className="w-4 h-4" />
                                        </button>
                                    </div>
                                </div>

                                <div className="space-y-4">
                                    <div className="flex justify-between text-sm">
                                        <span className="text-blue-300">Current Education:</span>
                                        <span>{child.currentEducationLevel}</span>
                                    </div>
                                    <div className="flex justify-between text-sm">
                                        <span className="text-blue-300">Target Education:</span>
                                        <span>{child.desiredEducationLevel}</span>
                                    </div>
                                    <div className="flex justify-between text-sm">
                                        <span className="text-blue-300">Institution Type:</span>
                                        <span>{child.institutionType}</span>
                                    </div>
                                    <div className="flex justify-between text-sm">
                                        <span className="text-blue-300">Duration:</span>
                                        <span>{child.estimatedStartYear} - {child.estimatedEndYear}</span>
                                    </div>
                                    <div className="flex justify-between text-sm">
                                        <span className="text-blue-300">Estimated Cost:</span>
                                        <span>₹{(child.estimatedTotalCost / 100000).toFixed(1)}L</span>
                                    </div>

                                    {/* Progress Bar */}
                                    <div className="space-y-2">
                                        <div className="flex justify-between text-sm">
                                            <span className="text-blue-300">Savings Progress</span>
                                            <span className={progress >= 100 ? 'text-green-400' : progress >= 75 ? 'text-yellow-400' : 'text-red-400'}>
                        {progress.toFixed(1)}%
                      </span>
                                        </div>
                                        <div className="w-full bg-blue-900/50 rounded-full h-2">
                                            <div
                                                className={`h-2 rounded-full ${
                                                    progress >= 100 ? 'bg-green-500' : progress >= 75 ? 'bg-yellow-500' : 'bg-red-500'
                                                }`}
                                                style={{ width: `${Math.min(progress, 100)}%` }}
                                            ></div>
                                        </div>
                                    </div>

                                    {shortfall > 0 && (
                                        <div className="bg-red-500/20 border border-red-500/30 rounded-lg p-3">
                                            <div className="text-sm text-red-300">
                                                <strong>Shortfall:</strong> ₹{(shortfall / 100000).toFixed(1)}L
                                            </div>
                                            <div className="text-xs text-red-400 mt-1">
                                                Consider increasing monthly contribution by ₹{Math.ceil((shortfall / (yearsToStart * 12)) / 1000) * 1000}
                                            </div>
                                        </div>
                                    )}
                                </div>
                            </div>
                        );
                    })}
                </div>

                {/* Add/Edit Child Form */}
                {showAddForm && (
                    <div className="fixed inset-0 bg-black/50 backdrop-blur-sm flex items-center justify-center p-4 z-50">
                        <div className="bg-blue-900 border border-blue-700/30 rounded-xl p-6 w-full max-w-2xl max-h-[90vh] overflow-y-auto">
                            <h2 className="text-xl font-bold mb-6">
                                {editingChild ? 'Edit Child Details' : 'Add New Child'}
                            </h2>

                            <div className="space-y-4">
                                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                                    <div>
                                        <label className="block text-sm font-medium mb-2">Child Name</label>
                                        <input
                                            type="text"
                                            value={newChild.name}
                                            onChange={(e) => setNewChild({...newChild, name: e.target.value})}
                                            className="w-full bg-blue-800/50 border border-blue-700/30 rounded-lg px-3 py-2 text-white placeholder-blue-300"
                                            placeholder="Enter child's name"
                                            required
                                        />
                                    </div>

                                    <div>
                                        <label className="block text-sm font-medium mb-2">Date of Birth</label>
                                        <input
                                            type="date"
                                            value={newChild.dateOfBirth}
                                            onChange={(e) => setNewChild({...newChild, dateOfBirth: e.target.value})}
                                            className="w-full bg-blue-800/50 border border-blue-700/30 rounded-lg px-3 py-2 text-white"
                                            required
                                        />
                                    </div>

                                    <div>
                                        <label className="block text-sm font-medium mb-2">Current Education Level</label>
                                        <select
                                            value={newChild.currentEducationLevel}
                                            onChange={(e) => setNewChild({...newChild, currentEducationLevel: e.target.value})}
                                            className="w-full bg-blue-800/50 border border-blue-700/30 rounded-lg px-3 py-2 text-white"
                                            required
                                        >
                                            <option value="">Select current level</option>
                                            {educationLevels.map(level => (
                                                <option key={level} value={level}>{level}</option>
                                            ))}
                                        </select>
                                    </div>

                                    <div>
                                        <label className="block text-sm font-medium mb-2">Desired Education Level</label>
                                        <select
                                            value={newChild.desiredEducationLevel}
                                            onChange={(e) => setNewChild({...newChild, desiredEducationLevel: e.target.value})}
                                            className="w-full bg-blue-800/50 border border-blue-700/30 rounded-lg px-3 py-2 text-white"
                                            required
                                        >
                                            <option value="">Select target level</option>
                                            {educationLevels.map(level => (
                                                <option key={level} value={level}>{level}</option>
                                            ))}
                                        </select>
                                    </div>

                                    <div>
                                        <label className="block text-sm font-medium mb-2">Institution Type</label>
                                        <select
                                            value={newChild.institutionType}
                                            onChange={(e) => setNewChild({...newChild, institutionType: e.target.value})}
                                            className="w-full bg-blue-800/50 border border-blue-700/30 rounded-lg px-3 py-2 text-white"
                                        >
                                            {institutionTypes.map(type => (
                                                <option key={type} value={type}>{type}</option>
                                            ))}
                                        </select>
                                    </div>

                                    <div>
                                        <label className="block text-sm font-medium mb-2">Estimated Start Year</label>
                                        <input
                                            type="number"
                                            value={newChild.estimatedStartYear}
                                            onChange={(e) => setNewChild({...newChild, estimatedStartYear: parseInt(e.target.value)})}
                                            className="w-full bg-blue-800/50 border border-blue-700/30 rounded-lg px-3 py-2 text-white"
                                            min={new Date().getFullYear()}
                                            required
                                        />
                                    </div>

                                    <div>
                                        <label className="block text-sm font-medium mb-2">Estimated End Year</label>
                                        <input
                                            type="number"
                                            value={newChild.estimatedEndYear}
                                            onChange={(e) => setNewChild({...newChild, estimatedEndYear: parseInt(e.target.value)})}
                                            className="w-full bg-blue-800/50 border border-blue-700/30 rounded-lg px-3 py-2 text-white"
                                            min={newChild.estimatedStartYear}
                                            required
                                        />
                                    </div>

                                    <div>
                                        <label className="block text-sm font-medium mb-2">Estimated Total Cost (₹)</label>
                                        <input
                                            type="number"
                                            value={newChild.estimatedTotalCost}
                                            onChange={(e) => setNewChild({...newChild, estimatedTotalCost: parseInt(e.target.value)})}
                                            className="w-full bg-blue-800/50 border border-blue-700/30 rounded-lg px-3 py-2 text-white"
                                            min="0"
                                            required
                                        />
                                    </div>

                                    <div>
                                        <label className="block text-sm font-medium mb-2">Current Savings (₹)</label>
                                        <input
                                            type="number"
                                            value={newChild.currentSavings}
                                            onChange={(e) => setNewChild({...newChild, currentSavings: parseInt(e.target.value)})}
                                            className="w-full bg-blue-800/50 border border-blue-700/30 rounded-lg px-3 py-2 text-white"
                                            min="0"
                                        />
                                    </div>

                                    <div>
                                        <label className="block text-sm font-medium mb-2">Monthly Contribution (₹)</label>
                                        <input
                                            type="number"
                                            value={newChild.monthlyContribution}
                                            onChange={(e) => setNewChild({...newChild, monthlyContribution: parseInt(e.target.value)})}
                                            className="w-full bg-blue-800/50 border border-blue-700/30 rounded-lg px-3 py-2 text-white"
                                            min="0"
                                            required
                                        />
                                    </div>

                                    <div>
                                        <label className="block text-sm font-medium mb-2">Expected Inflation Rate (%)</label>
                                        <input
                                            type="number"
                                            step="0.1"
                                            value={newChild.inflationRate}
                                            onChange={(e) => setNewChild({...newChild, inflationRate: parseFloat(e.target.value)})}
                                            className="w-full bg-blue-800/50 border border-blue-700/30 rounded-lg px-3 py-2 text-white"
                                            min="0"
                                            max="20"
                                            required
                                        />
                                    </div>
                                </div>

                                <div className="flex justify-end space-x-3 pt-4">
                                    <button
                                        type="button"
                                        onClick={() => {
                                            setShowAddForm(false);
                                            setEditingChild(null);
                                            setNewChild({
                                                name: '',
                                                dateOfBirth: '',
                                                currentEducationLevel: '',
                                                desiredEducationLevel: '',
                                                institutionType: 'Government',
                                                estimatedStartYear: new Date().getFullYear() + 5,
                                                estimatedEndYear: new Date().getFullYear() + 9,
                                                estimatedTotalCost: 500000,
                                                currentSavings: 0,
                                                monthlyContribution: 5000,
                                                inflationRate: 6.5
                                            });
                                        }}
                                        className="px-4 py-2 bg-gray-600 hover:bg-gray-700 rounded-lg transition-colors"
                                    >
                                        Cancel
                                    </button>
                                    <button
                                        type="button"
                                        onClick={handleSubmit}
                                        className="px-4 py-2 bg-green-500 hover:bg-green-600 rounded-lg transition-colors"
                                    >
                                        {editingChild ? 'Update Child' : 'Add Child'}
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                )}

                {children.length === 0 && (
                    <div className="text-center py-12">
                        <BookOpen className="w-16 h-16 text-blue-400 mx-auto mb-4" />
                        <h3 className="text-xl font-semibold mb-2">No Education Plans Yet</h3>
                        <p className="text-blue-300 mb-4">Start planning for your children's education by adding their details.</p>
                        <button
                            onClick={() => setShowAddForm(true)}
                            className="bg-green-500 hover:bg-green-600 px-6 py-3 rounded-lg flex items-center space-x-2 mx-auto transition-colors"
                        >
                            <Plus className="w-5 h-5" />
                            <span>Add Your First Child</span>
                        </button>
                    </div>
                )}
            </div>
        </div>
    );
};

export default EducationPlanningDashboard;