import React, { useState } from 'react';
import { User, Users, DollarSign, GraduationCap, MapPin, Plus, X, Shield } from 'lucide-react';
import axios from "axios";

const FamilyDetails = () => {
    const navigate = useNavigate();
    const [familySize, setFamilySize] = useState('');
    const [monthlyIncome, setMonthlyIncome] = useState('');
    const [monthlyExpenses, setMonthlyExpenses] = useState('');
    const [location, setLocation] = useState('');
    const [riskTolerance, setRiskTolerance] = useState('');
    const [children, setChildren] = useState([]);

    const handleChildChange = (index, field, value) => {
        const updatedChildren = [...children];
        updatedChildren[index][field] = value;
        setChildren(updatedChildren);
    };

    const addChild = () => {
        setChildren([...children, { name: '', dateOfBirth: '', currentEducationLevel: '' }]);
    };

    const removeChild = (index) => {
        const updatedChildren = [...children];
        updatedChildren.splice(index, 1);
        setChildren(updatedChildren);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await axios.post('http://localhost:8080/api/family/profile', form, {withCredentials: true});
            navigate('/dashboard');
        } catch (error) {
            alert('Error saving family details!');

    };
        const familyData = {
            familySize: parseInt(familySize),
            monthlyIncome: parseFloat(monthlyIncome),
            monthlyExpenses: parseFloat(monthlyExpenses),
            location,
            riskTolerance: getRiskToleranceForDb(riskTolerance),
            children: children.map(child => ({
                name: child.name,
                dateOfBirth: child.dateOfBirth,
                currentEducationLevel: child.currentEducationLevel
            }))
        };
        console.log('Submitted Family Data:', familyData);
        // TODO: send to backend via API call
    };

    const getRiskToleranceForDb = (frontendValue) => {
        switch (frontendValue) {
            case 'conservative': return 'LOW';
            case 'moderate': return 'MEDIUM';
            case 'aggressive': return 'HIGH';
            default: return 'MEDIUM';
        }
    };

    const calculateMonthlySurplus = () => {
        const income = parseFloat(monthlyIncome) || 0;
        const expenses = parseFloat(monthlyExpenses) || 0;
        return income - expenses;
    };

    const formatCurrency = (amount) => {
        return new Intl.NumberFormat('en-IN', {
            style: 'currency',
            currency: 'INR',
            maximumFractionDigits: 0
        }).format(amount);
    };

    return (
        <div className="min-h-screen bg-gradient-to-br from-blue-900 to-indigo-950 text-white p-6">
            <div className="max-w-4xl mx-auto">
                {/* Header */}
                <div className="mb-8">
                    <h2 className="text-3xl font-bold mb-2 flex items-center">
                        <Users className="mr-3 text-emerald-400" size={32} />
                        Family Profile Setup
                    </h2>
                    <p className="text-gray-300">Create your family financial profile to get personalized recommendations</p>
                </div>

                <div className="space-y-8">
                    {/* Family Information Section */}
                    <div className="bg-white/5 backdrop-blur-sm rounded-xl p-6 border border-white/10">
                        <h3 className="text-xl font-semibold mb-6 flex items-center">
                            <Users className="mr-2 text-emerald-400" size={20} />
                            Family Information
                        </h3>

                        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                            <div className="space-y-2">
                                <label className="text-sm font-medium text-gray-300">Family Size</label>
                                <div className="relative">
                                    <Users className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" size={16} />
                                    <input
                                        type="number"
                                        min="1"
                                        max="20"
                                        placeholder="Number of family members"
                                        value={familySize}
                                        onChange={(e) => setFamilySize(e.target.value)}
                                        className="w-full pl-10 pr-4 py-3 bg-white/10 border border-white/20 rounded-lg text-white placeholder-gray-400 focus:border-emerald-500 focus:ring-2 focus:ring-emerald-500/20 focus:outline-none transition"
                                        required
                                    />
                                </div>
                                <p className="text-xs text-gray-400">Include yourself and all dependents</p>
                            </div>

                            <div className="space-y-2">
                                <label className="text-sm font-medium text-gray-300">Location</label>
                                <div className="relative">
                                    <MapPin className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" size={16} />
                                    <input
                                        type="text"
                                        maxLength="100"
                                        placeholder="City, State"
                                        value={location}
                                        onChange={(e) => setLocation(e.target.value)}
                                        className="w-full pl-10 pr-4 py-3 bg-white/10 border border-white/20 rounded-lg text-white placeholder-gray-400 focus:border-emerald-500 focus:ring-2 focus:ring-emerald-500/20 focus:outline-none transition"
                                        required
                                    />
                                </div>
                                <p className="text-xs text-gray-400">Used for location-specific recommendations</p>
                            </div>
                        </div>
                    </div>

                    {/* Financial Information Section */}
                    <div className="bg-white/5 backdrop-blur-sm rounded-xl p-6 border border-white/10">
                        <h3 className="text-xl font-semibold mb-6 flex items-center">
                            <DollarSign className="mr-2 text-emerald-400" size={20} />
                            Financial Information
                        </h3>

                        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                            <div className="space-y-2">
                                <label className="text-sm font-medium text-gray-300">Monthly Income (₹)</label>
                                <div className="relative">
                                    <DollarSign className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" size={16} />
                                    <input
                                        type="number"
                                        min="0"
                                        step="1000"
                                        placeholder="Total household income"
                                        value={monthlyIncome}
                                        onChange={(e) => setMonthlyIncome(e.target.value)}
                                        className="w-full pl-10 pr-4 py-3 bg-white/10 border border-white/20 rounded-lg text-white placeholder-gray-400 focus:border-emerald-500 focus:ring-2 focus:ring-emerald-500/20 focus:outline-none transition"
                                        required
                                    />
                                </div>
                                <p className="text-xs text-gray-400">Include all sources of income</p>
                            </div>

                            <div className="space-y-2">
                                <label className="text-sm font-medium text-gray-300">Monthly Expenses (₹)</label>
                                <div className="relative">
                                    <DollarSign className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" size={16} />
                                    <input
                                        type="number"
                                        min="0"
                                        step="1000"
                                        placeholder="Total monthly expenses"
                                        value={monthlyExpenses}
                                        onChange={(e) => setMonthlyExpenses(e.target.value)}
                                        className="w-full pl-10 pr-4 py-3 bg-white/10 border border-white/20 rounded-lg text-white placeholder-gray-400 focus:border-emerald-500 focus:ring-2 focus:ring-emerald-500/20 focus:outline-none transition"
                                        required
                                    />
                                </div>
                                <p className="text-xs text-gray-400">Include rent, utilities, food, etc.</p>
                            </div>
                        </div>

                        {/* Monthly Surplus Display */}
                        {monthlyIncome && monthlyExpenses && (
                            <div className="mt-6 p-4 bg-white/10 rounded-lg border border-white/20">
                                <div className="flex items-center justify-between">
                                    <span className="text-gray-300">Monthly Surplus:</span>
                                    <span className={`font-bold text-lg ${calculateMonthlySurplus() >= 0 ? 'text-emerald-400' : 'text-red-400'}`}>
                                        {formatCurrency(calculateMonthlySurplus())}
                                    </span>
                                </div>
                                {calculateMonthlySurplus() < 0 && (
                                    <p className="text-xs text-red-400 mt-2">
                                        ⚠️ Your expenses exceed your income. Consider reviewing your budget.
                                    </p>
                                )}
                            </div>
                        )}
                    </div>

                    {/* Risk Tolerance Section */}
                    <div className="bg-white/5 backdrop-blur-sm rounded-xl p-6 border border-white/10">
                        <h3 className="text-xl font-semibold mb-6 flex items-center">
                            <Shield className="mr-2 text-emerald-400" size={20} />
                            Investment Risk Tolerance
                        </h3>

                        <div className="space-y-4">
                            <label className="text-sm font-medium text-gray-300">
                                How comfortable are you with investment risk?
                            </label>

                            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                                {[
                                    {
                                        value: 'conservative',
                                        label: 'Conservative',
                                        description: 'Prefer safety over returns',
                                        color: 'text-blue-400'
                                    },
                                    {
                                        value: 'moderate',
                                        label: 'Moderate',
                                        description: 'Balance of safety and growth',
                                        color: 'text-emerald-400'
                                    },
                                    {
                                        value: 'aggressive',
                                        label: 'Aggressive',
                                        description: 'Higher risk for higher returns',
                                        color: 'text-orange-400'
                                    }
                                ].map((option) => (
                                    <label
                                        key={option.value}
                                        className={`cursor-pointer p-4 rounded-lg border-2 transition-all ${
                                            riskTolerance === option.value
                                                ? 'border-emerald-500 bg-emerald-500/10'
                                                : 'border-white/20 bg-white/5 hover:border-white/40'
                                        }`}
                                    >
                                        <input
                                            type="radio"
                                            name="riskTolerance"
                                            value={option.value}
                                            checked={riskTolerance === option.value}
                                            onChange={(e) => setRiskTolerance(e.target.value)}
                                            className="sr-only"
                                            required
                                        />
                                        <div className="text-center">
                                            <div className={`font-semibold ${option.color} mb-1`}>
                                                {option.label}
                                            </div>
                                            <div className="text-xs text-gray-400">
                                                {option.description}
                                            </div>
                                        </div>
                                    </label>
                                ))}
                            </div>
                        </div>
                    </div>

                    {/* Children Details Section */}
                    <div className="bg-white/5 backdrop-blur-sm rounded-xl p-6 border border-white/10">
                        <div className="flex justify-between items-center mb-6">
                            <h3 className="text-xl font-semibold flex items-center">
                                <GraduationCap className="mr-2 text-emerald-400" size={20} />
                                Children Details
                            </h3>
                            <button
                                type="button"
                                onClick={addChild}
                                className="flex items-center px-4 py-2 bg-emerald-500/20 text-emerald-400 rounded-lg hover:bg-emerald-500/30 transition border border-emerald-500/30"
                            >
                                <Plus size={16} className="mr-2" />
                                Add Child
                            </button>
                        </div>

                        {children.length === 0 ? (
                            <div className="text-center py-8 text-gray-400">
                                <GraduationCap size={48} className="mx-auto mb-3 opacity-50" />
                                <p>No children added yet. Click "Add Child" to include them in your financial planning.</p>
                                <p className="text-xs mt-2">This helps us calculate education expenses and future financial needs.</p>
                            </div>
                        ) : (
                            <div className="space-y-4">
                                {children.map((child, index) => (
                                    <div key={index} className="bg-white/5 rounded-lg p-4 border border-white/10">
                                        <div className="flex justify-between items-center mb-4">
                                            <h4 className="text-lg font-medium flex items-center">
                                                <User size={18} className="mr-2 text-emerald-400" />
                                                Child {index + 1}
                                            </h4>
                                            <button
                                                type="button"
                                                onClick={() => removeChild(index)}
                                                className="flex items-center px-3 py-1 text-red-400 hover:bg-red-500/20 rounded-md transition text-sm"
                                            >
                                                <X size={14} className="mr-1" />
                                                Remove
                                            </button>
                                        </div>

                                        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                                            <div className="space-y-2">
                                                <label className="text-sm font-medium text-gray-300">Name</label>
                                                <input
                                                    type="text"
                                                    maxLength="100"
                                                    placeholder="Child's name"
                                                    value={child.name}
                                                    onChange={(e) => handleChildChange(index, 'name', e.target.value)}
                                                    className="w-full px-3 py-2 bg-white/10 border border-white/20 rounded-lg text-white placeholder-gray-400 focus:border-emerald-500 focus:ring-2 focus:ring-emerald-500/20 focus:outline-none transition text-sm"
                                                    required
                                                />
                                            </div>

                                            <div className="space-y-2">
                                                <label className="text-sm font-medium text-gray-300">Date of Birth</label>
                                                <input
                                                    type="date"
                                                    value={child.dateOfBirth}
                                                    max={new Date().toISOString().split('T')[0]}
                                                    onChange={(e) => handleChildChange(index, 'dateOfBirth', e.target.value)}
                                                    className="w-full px-3 py-2 bg-white/10 border border-white/20 rounded-lg text-white placeholder-gray-400 focus:border-emerald-500 focus:ring-2 focus:ring-emerald-500/20 focus:outline-none transition text-sm"
                                                    required
                                                />
                                            </div>

                                            <div className="space-y-2">
                                                <label className="text-sm font-medium text-gray-300">Current Education Level</label>
                                                <select
                                                    value={child.currentEducationLevel}
                                                    onChange={(e) => handleChildChange(index, 'currentEducationLevel', e.target.value)}
                                                    className="w-full px-3 py-2 bg-white/10 border border-white/20 rounded-lg text-white focus:border-emerald-500 focus:ring-2 focus:ring-emerald-500/20 focus:outline-none transition text-sm"
                                                >
                                                    <option value="" className="bg-gray-800">Select level</option>
                                                    <option value="not_started" className="bg-gray-800">Not Started</option>
                                                    <option value="nursery" className="bg-gray-800">Nursery</option>
                                                    <option value="primary" className="bg-gray-800">Primary School</option>
                                                    <option value="secondary" className="bg-gray-800">Secondary School</option>
                                                    <option value="high_school" className="bg-gray-800">High School</option>
                                                    <option value="undergraduate" className="bg-gray-800">Undergraduate</option>
                                                    <option value="postgraduate" className="bg-gray-800">Postgraduate</option>
                                                </select>
                                            </div>
                                        </div>

                                        {/* Display child's age if date of birth is provided */}
                                        {child.dateOfBirth && (
                                            <div className="mt-3 text-sm text-gray-400">
                                                Age: {Math.floor((new Date() - new Date(child.dateOfBirth)) / (1000 * 60 * 60 * 24 * 365.25))} years old
                                            </div>
                                        )}
                                    </div>
                                ))}
                            </div>
                        )}
                    </div>

                    {/* Submit Button */}
                    <div className="bg-white/5 backdrop-blur-sm rounded-xl p-6 border border-white/10">
                        <button
                            onClick={handleSubmit}
                            className="w-full py-4 bg-gradient-to-r from-emerald-500 to-emerald-600 hover:from-emerald-600 hover:to-emerald-700 text-white font-bold rounded-lg transition-all duration-200 shadow-lg hover:shadow-emerald-500/25 transform hover:scale-[1.02]"
                        >
                            Create Family Profile
                        </button>
                        <p className="text-center text-sm text-gray-400 mt-3">
                            🔒 Your information is secure and encrypted. We use it only to provide personalized financial recommendations.
                        </p>

                        {/* Data preview */}
                        {(familySize || monthlyIncome || monthlyExpenses) && (
                            <div className="mt-4 p-3 bg-white/5 rounded-lg border border-white/10">
                                <details className="text-sm">
                                    <summary className="cursor-pointer text-gray-300 hover:text-white">
                                        Preview submitted data
                                    </summary>
                                    <pre className="mt-2 text-xs text-gray-400 overflow-x-auto">
                                        {JSON.stringify({
                                            familySize: parseInt(familySize) || 0,
                                            monthlyIncome: parseFloat(monthlyIncome) || 0,
                                            monthlyExpenses: parseFloat(monthlyExpenses) || 0,
                                            location,
                                            riskTolerance: getRiskToleranceForDb(riskTolerance),
                                            children: children.length
                                        }, null, 2)}
                                    </pre>
                                </details>
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default FamilyDetails;