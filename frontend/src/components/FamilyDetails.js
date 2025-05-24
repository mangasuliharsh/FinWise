import React, { useState } from 'react';
import { User, Users, DollarSign, GraduationCap, Heart, Plus, X } from 'lucide-react';

const FamilyDetails = () => {
    const [name, setname] = useState('');
    const [Age, setAge] = useState('');
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

    const handleSubmit = (e) => {
        e.preventDefault();
        const familyData = {
            name,
            Age,
            familySize,
            monthlyIncome,
            monthlyExpenses,
            location,
            riskTolerance,
            children,
        };
        console.log('Submitted Family Data:', familyData);
        // TODO: send to backend via API call
    };

    return (
        <div className="min-h-screen bg-gradient-to-br from-blue-900 to-indigo-950 text-white p-6">
            <div className="max-w-4xl mx-auto">
                {/* Header */}
                <div className="mb-8">
                    <h2 className="text-3xl font-bold mb-2 flex items-center">
                        <Users className="mr-3 text-emerald-400" size={32} />
                        Family Details
                    </h2>
                    <p className="text-gray-300">Enter your family information to create a personalized financial plan</p>
                </div>

                <div className="space-y-8">
                    {/* Personal Information Section */}
                    <div className="bg-white/5 backdrop-blur-sm rounded-xl p-6 border border-white/10">
                        <h3 className="text-xl font-semibold mb-6 flex items-center">
                            <User className="mr-2 text-emerald-400" size={20} />
                            Personal Information
                        </h3>
                        <div className="space-y-2">
                            <label className="text-sm font-medium text-gray-300">name</label>
                            <div className="relative">
                                <User className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" size={16} />
                                <input
                                    type="text"
                                    placeholder="Enter your name"
                                    value={name}
                                    onChange={(e) => setAge(e.target.value)}
                                    className="w-full pl-10 pr-4 py-3 bg-white/10 border border-white/20 rounded-lg text-white placeholder-gray-400 focus:border-emerald-500 focus:ring-2 focus:ring-emerald-500/20 focus:outline-none transition"
                                    required
                                />
                            </div>
                        </div>
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                            <div className="space-y-2">
                                <label className="text-sm font-medium text-gray-300">Age</label>
                                <div className="relative">
                                    <Users className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" size={16} />
                                    <input
                                        type="number"
                                        placeholder="Enter your Age"
                                        value={Age}
                                        onChange={(e) => setAge(e.target.value)}
                                        className="w-full pl-10 pr-4 py-3 bg-white/10 border border-white/20 rounded-lg text-white placeholder-gray-400 focus:border-emerald-500 focus:ring-2 focus:ring-emerald-500/20 focus:outline-none transition"
                                        required
                                    />
                                </div>
                            </div>
                        </div>
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                            <div className="space-y-2">
                                <label className="text-sm font-medium text-gray-300">Family Size</label>
                                <div className="relative">
                                    <Users className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" size={16} />
                                    <input
                                        type="number"
                                        placeholder="Number of family members"
                                        value={familySize}
                                        onChange={(e) => setFamilySize(e.target.value)}
                                        className="w-full pl-10 pr-4 py-3 bg-white/10 border border-white/20 rounded-lg text-white placeholder-gray-400 focus:border-emerald-500 focus:ring-2 focus:ring-emerald-500/20 focus:outline-none transition"
                                        required
                                    />
                                </div>
                            </div>

                            <div className="space-y-2">
                                <label className="text-sm font-medium text-gray-300">Location</label>
                                <div className="relative">
                                    <User className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" size={16} />
                                    <input
                                        type="text"
                                        placeholder="Enter your location"
                                        value={location}
                                        onChange={(e) => setLocation(e.target.value)}
                                        className="w-full pl-10 pr-4 py-3 bg-white/10 border border-white/20 rounded-lg text-white placeholder-gray-400 focus:border-emerald-500 focus:ring-2 focus:ring-emerald-500/20 focus:outline-none transition"
                                        required
                                    />
                                </div>
                            </div>

                            <div className="space-y-2">
                                <label className="text-sm font-medium text-gray-300">Monthly Income (₹)</label>
                                <div className="relative">
                                    <DollarSign className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" size={16} />
                                    <input
                                        type="number"
                                        placeholder="Enter total monthly income"
                                        value={monthlyIncome}
                                        onChange={(e) => setMonthlyIncome(e.target.value)}
                                        className="w-full pl-10 pr-4 py-3 bg-white/10 border border-white/20 rounded-lg text-white placeholder-gray-400 focus:border-emerald-500 focus:ring-2 focus:ring-emerald-500/20 focus:outline-none transition"
                                        required
                                    />
                                </div>
                            </div>

                            <div className="space-y-2">
                                <label className="text-sm font-medium text-gray-300">Monthly Expenses (₹)</label>
                                <div className="relative">
                                    <DollarSign className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" size={16} />
                                    <input
                                        type="number"
                                        placeholder="Enter monthly expenses"
                                        value={monthlyExpenses}
                                        onChange={(e) => setMonthlyExpenses(e.target.value)}
                                        className="w-full pl-10 pr-4 py-3 bg-white/10 border border-white/20 rounded-lg text-white placeholder-gray-400 focus:border-emerald-500 focus:ring-2 focus:ring-emerald-500/20 focus:outline-none transition"
                                        required
                                    />
                                </div>
                            </div>

                            <div className="md:col-span-2 space-y-2">
                                <label className="text-sm font-medium text-gray-300">Risk Tolerance</label>
                                <div className="relative">
                                    <select
                                        value={riskTolerance}
                                        onChange={(e) => setRiskTolerance(e.target.value)}
                                        className="w-full px-4 py-3 bg-white/10 border border-white/20 rounded-lg text-white focus:border-emerald-500 focus:ring-2 focus:ring-emerald-500/20 focus:outline-none transition"
                                        required
                                    >
                                        <option value="" className="bg-gray-800">Select your risk tolerance</option>
                                        <option value="conservative" className="bg-gray-800">Conservative</option>
                                        <option value="moderate" className="bg-gray-800">Moderate</option>
                                        <option value="aggressive" className="bg-gray-800">Aggressive</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>

                    {/* Children Details Section */}
                    <div className="bg-white/5 backdrop-blur-sm rounded-xl p-6 border border-white/10">
                        <div className="flex justify-between items-center mb-6">
                            <h3 className="text-xl font-semibold flex items-center">
                                <Users className="mr-2 text-emerald-400" size={20} />
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
                                <Users size={48} className="mx-auto mb-3 opacity-50" />
                                <p>No children added yet. Click "Add Child" to get started.</p>
                            </div>
                        ) : (
                            <div className="space-y-4">
                                {children.map((child, index) => (
                                    <div key={index} className="bg-white/5 rounded-lg p-4 border border-white/10">
                                        <div className="flex justify-between items-center mb-4">
                                            <h4 className="text-lg font-medium">Child {index + 1}</h4>
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
                                                    placeholder="Date of birth"
                                                    value={child.dateOfBirth}
                                                    onChange={(e) => handleChildChange(index, 'dateOfBirth', e.target.value)}
                                                    className="w-full px-3 py-2 bg-white/10 border border-white/20 rounded-lg text-white placeholder-gray-400 focus:border-emerald-500 focus:ring-2 focus:ring-emerald-500/20 focus:outline-none transition text-sm"
                                                    required
                                                />
                                            </div>

                                            <div className="space-y-2">
                                                <label className="text-sm font-medium text-gray-300 flex items-center">
                                                    <GraduationCap size={14} className="mr-1 text-emerald-400" />
                                                    Current Education Level
                                                </label>
                                                <select
                                                    value={child.currentEducationLevel}
                                                    onChange={(e) => handleChildChange(index, 'currentEducationLevel', e.target.value)}
                                                    className="w-full px-3 py-2 bg-white/10 border border-white/20 rounded-lg text-white focus:border-emerald-500 focus:ring-2 focus:ring-emerald-500/20 focus:outline-none transition text-sm"
                                                >
                                                    <option value="" className="bg-gray-800">Select level</option>
                                                    <option value="nursery" className="bg-gray-800">Nursery</option>
                                                    <option value="primary" className="bg-gray-800">Primary School</option>
                                                    <option value="secondary" className="bg-gray-800">Secondary School</option>
                                                    <option value="high_school" className="bg-gray-800">High School</option>
                                                    <option value="undergraduate" className="bg-gray-800">Undergraduate</option>
                                                    <option value="postgraduate" className="bg-gray-800">Postgraduate</option>
                                                    <option value="not_started" className="bg-gray-800">Not Started</option>
                                                </select>
                                            </div>

                                            <div className="space-y-2">
                                                <label className="text-sm font-medium text-gray-300">Education Expense Per Year (₹)</label>
                                                <div className="relative">
                                                    <DollarSign className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" size={16} />
                                                    <input
                                                        type="number"
                                                        placeholder="Education Expense Per Year"
                                                        value={child.EducationExpensePerYear}
                                                        onChange={(e) => handleChildChange(index, 'EducationExpensePerYear', e.target.value)}
                                                        className="w-full pl-10 pr-4 py-3 bg-white/10 border border-white/20 rounded-lg text-white placeholder-gray-400 focus:border-emerald-500 focus:ring-2 focus:ring-emerald-500/20 focus:outline-none transition text-sm"
                                                        required
                                                    />
                                                </div>
                                            </div>
                                        </div>
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
                            Save Family Details
                        </button>
                        <p className="text-center text-sm text-gray-400 mt-3">
                            Your information is secure and will be used to create your personalized financial plan
                        </p>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default FamilyDetails;