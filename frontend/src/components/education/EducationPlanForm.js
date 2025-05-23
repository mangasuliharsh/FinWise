import React, { useState, useEffect } from 'react';
import { useNavigate, useParams, useSearchParams } from 'react-router-dom';
import axios from 'axios';
import { motion } from 'framer-motion';
import { BookOpen, Save, ArrowLeft, School, Building2, Calculator, Calendar, Wallet, CreditCard } from 'lucide-react';
import Navbar from '../common/Navbar';

const educationLevels = ['PRIMARY', 'SECONDARY', 'UNDERGRADUATE', 'POSTGRADUATE'];
const institutionTypes = ['PUBLIC', 'PRIVATE'];

const EducationPlanForm = () => {
  const navigate = useNavigate();
  const { familyProfileId, planId } = useParams();
  const [searchParams] = useSearchParams();
  const preselectedChildId = searchParams.get('childId');
  
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [children, setChildren] = useState([]);
  const [selectedChildId, setSelectedChildId] = useState(preselectedChildId || '');
  const [existingPlan, setExistingPlan] = useState(null);
  
  const [plan, setPlan] = useState({
    planName: '',
    educationLevel: '',
    institutionType: '',
    estimatedStartYear: new Date().getFullYear(),
    estimatedEndYear: new Date().getFullYear() + 4,
    estimatedTotalCost: 0,
    currentSavings: 0,
    monthlyContribution: 0,
    inflationRate: 6.00,
    notes: '',
    child: preselectedChildId ? { id: preselectedChildId } : null
  });

  // Fetch existing plan if editing
  useEffect(() => {
    const fetchPlan = async () => {
      if (planId) {
        try {
          setLoading(true);
          const response = await axios.get(`http://localhost:8080/api/education-plans/${planId}`);
          setExistingPlan(response.data);
          setPlan(response.data);
          setSelectedChildId(response.data.child?.id || '');
        } catch (err) {
          setError('Failed to fetch plan details. Please try again.');
        } finally {
          setLoading(false);
        }
      }
    };
    fetchPlan();
  }, [planId]);

  // Fetch children for the family profile
  useEffect(() => {
    const fetchChildren = async () => {
      try {
        setLoading(true);
        const response = await axios.get(`http://localhost:8080/api/children/family/${familyProfileId}`);
        setChildren(response.data);
        if (response.data.length === 1 && !planId) {
          setSelectedChildId(response.data[0].id);
          setPlan(prev => ({ ...prev, child: { id: response.data[0].id } }));
        }
      } catch (err) {
        setError('Failed to fetch children. Please try again.');
      } finally {
        setLoading(false);
      }
    };

    if (familyProfileId) {
      fetchChildren();
    }
  }, [familyProfileId, planId]);

  const handleChildChange = (e) => {
    const childId = e.target.value;
    setSelectedChildId(childId);
    setPlan(prev => ({
      ...prev,
      child: { id: childId }
    }));
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setPlan(prev => ({
      ...prev,
      [name]: ['estimatedTotalCost', 'currentSavings', 'monthlyContribution', 'estimatedStartYear', 'estimatedEndYear', 'inflationRate'].includes(name)
        ? Number(value)
        : value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!selectedChildId) {
      setError('Please select a child first.');
      return;
    }

    try {
      setLoading(true);
      const planToSubmit = {
        ...plan,
        child: { id: selectedChildId }
      };

      const url = planId
        ? `http://localhost:8080/api/education-plans/${planId}`
        : 'http://localhost:8080/api/education-plans';
      
      await (planId
        ? axios.put(url, planToSubmit)
        : axios.post(url, planToSubmit));

      navigate(`/education/family/${familyProfileId}/children`);
    } catch (error) {
      console.error('Error saving education plan:', error);
      setError(error.response?.data?.message || 'Failed to save education plan. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  if (!familyProfileId) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-900 to-indigo-950">
        <div className="bg-white/5 backdrop-blur-sm p-8 rounded-lg border border-white/10 text-white text-center">
          <div className="mb-4 text-red-400">
            <svg className="w-12 h-12 mx-auto" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
            </svg>
          </div>
          <h3 className="text-xl font-semibold mb-2">No Family Profile Selected</h3>
          <p className="text-gray-400">Please select a family profile first.</p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-900 to-indigo-950 text-white pb-10">
      <Navbar 
        title={planId ? 'Edit Education Plan' : 'Create Education Plan'} 
        icon={BookOpen} 
      />

      {/* Main Content */}
      <div className="max-w-4xl mx-auto px-6 md:px-16 lg:px-24 mt-8">
        <motion.button
          whileHover={{ scale: 1.02 }}
          whileTap={{ scale: 0.98 }}
          onClick={() => navigate(`/education/family/${familyProfileId}/children`)}
          className="flex items-center text-gray-300 hover:text-white mb-6"
        >
          <ArrowLeft size={20} className="mr-2" />
          Back to Plans
        </motion.button>

        {error && (
          <motion.div
            initial={{ opacity: 0, y: -10 }}
            animate={{ opacity: 1, y: 0 }}
            className="bg-red-500/10 border border-red-500/20 rounded-lg p-4 mb-6 text-red-400"
          >
            {error}
          </motion.div>
        )}

        <motion.form
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          onSubmit={handleSubmit}
          className="space-y-6"
        >
          {/* Child Selection */}
          <div className="bg-white/5 backdrop-blur-sm rounded-lg p-6 border border-white/10">
            <h2 className="text-xl font-semibold text-emerald-400 mb-4 flex items-center">
              <School className="mr-2" />
              Select Child
            </h2>
            <select
              value={selectedChildId}
              onChange={handleChildChange}
              required
              className="w-full bg-white/10 border border-white/10 rounded-lg px-4 py-2.5 text-white focus:outline-none focus:ring-2 focus:ring-emerald-500/50 [&>option]:bg-slate-800 [&>option]:text-white"
            >
              <option value="" className="bg-slate-800 text-white">Choose a child...</option>
              {children.map((child) => (
                <option key={child.id} value={child.id} className="bg-slate-800 text-white">
                  {child.name} ({child.currentEducationLevel})
                </option>
              ))}
            </select>
          </div>

          {/* Basic Information */}
          <div className="bg-white/5 backdrop-blur-sm rounded-lg p-6 border border-white/10">
            <h2 className="text-xl font-semibold text-emerald-400 mb-4 flex items-center">
              <Building2 className="mr-2" />
              Plan Details
            </h2>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div>
                <label className="block text-sm font-medium text-gray-300 mb-2">Plan Name</label>
                <input
                  type="text"
                  name="planName"
                  value={plan.planName}
                  onChange={handleChange}
                  required
                  className="w-full bg-white/5 border border-white/10 rounded-lg px-4 py-2.5 text-white focus:outline-none focus:ring-2 focus:ring-emerald-500/50"
                  placeholder="Enter plan name"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-300 mb-2">Education Level</label>
                <select
                  name="educationLevel"
                  value={plan.educationLevel}
                  onChange={handleChange}
                  required
                  className="w-full bg-white/10 border border-white/10 rounded-lg px-4 py-2.5 text-white focus:outline-none focus:ring-2 focus:ring-emerald-500/50 [&>option]:bg-slate-800 [&>option]:text-white"
                >
                  <option value="" className="bg-slate-800 text-white">Select education level...</option>
                  {educationLevels.map(level => (
                    <option key={level} value={level} className="bg-slate-800 text-white">
                      {level}
                    </option>
                  ))}
                </select>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-300 mb-2">Institution Type</label>
                <select
                  name="institutionType"
                  value={plan.institutionType}
                  onChange={handleChange}
                  required
                  className="w-full bg-white/10 border border-white/10 rounded-lg px-4 py-2.5 text-white focus:outline-none focus:ring-2 focus:ring-emerald-500/50 [&>option]:bg-slate-800 [&>option]:text-white"
                >
                  <option value="" className="bg-slate-800 text-white">Select institution type...</option>
                  {institutionTypes.map(type => (
                    <option key={type} value={type} className="bg-slate-800 text-white">
                      {type}
                    </option>
                  ))}
                </select>
              </div>
            </div>
          </div>

          {/* Timeline */}
          <div className="bg-white/5 backdrop-blur-sm rounded-lg p-6 border border-white/10">
            <h2 className="text-xl font-semibold text-emerald-400 mb-4 flex items-center">
              <Calendar className="mr-2" />
              Timeline
            </h2>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div>
                <label className="block text-sm font-medium text-gray-300 mb-2">Start Year</label>
                <input
                  type="number"
                  name="estimatedStartYear"
                  value={plan.estimatedStartYear}
                  onChange={handleChange}
                  required
                  min={new Date().getFullYear()}
                  className="w-full bg-white/5 border border-white/10 rounded-lg px-4 py-2.5 text-white focus:outline-none focus:ring-2 focus:ring-emerald-500/50"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-300 mb-2">End Year</label>
                <input
                  type="number"
                  name="estimatedEndYear"
                  value={plan.estimatedEndYear}
                  onChange={handleChange}
                  required
                  min={plan.estimatedStartYear}
                  className="w-full bg-white/5 border border-white/10 rounded-lg px-4 py-2.5 text-white focus:outline-none focus:ring-2 focus:ring-emerald-500/50"
                />
              </div>
            </div>
          </div>

          {/* Financial Details */}
          <div className="bg-white/5 backdrop-blur-sm rounded-lg p-6 border border-white/10">
            <h2 className="text-xl font-semibold text-emerald-400 mb-4 flex items-center">
              <Wallet className="mr-2" />
              Financial Details
            </h2>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div>
                <label className="block text-sm font-medium text-gray-300 mb-2">Estimated Total Cost (₹)</label>
                <input
                  type="number"
                  name="estimatedTotalCost"
                  value={plan.estimatedTotalCost}
                  onChange={handleChange}
                  required
                  min="1"
                  className="w-full bg-white/5 border border-white/10 rounded-lg px-4 py-2.5 text-white focus:outline-none focus:ring-2 focus:ring-emerald-500/50"
                  placeholder="Enter estimated total cost"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-300 mb-2">Current Savings (₹)</label>
                <input
                  type="number"
                  name="currentSavings"
                  value={plan.currentSavings}
                  onChange={handleChange}
                  required
                  min="0"
                  className="w-full bg-white/5 border border-white/10 rounded-lg px-4 py-2.5 text-white focus:outline-none focus:ring-2 focus:ring-emerald-500/50"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-300 mb-2">Monthly Contribution (₹)</label>
                <input
                  type="number"
                  name="monthlyContribution"
                  value={plan.monthlyContribution}
                  onChange={handleChange}
                  required
                  min="0"
                  className="w-full bg-white/5 border border-white/10 rounded-lg px-4 py-2.5 text-white focus:outline-none focus:ring-2 focus:ring-emerald-500/50"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-300 mb-2">Inflation Rate (%)</label>
                <input
                  type="number"
                  name="inflationRate"
                  value={plan.inflationRate}
                  onChange={handleChange}
                  required
                  step="0.01"
                  min="0"
                  max="20"
                  className="w-full bg-white/5 border border-white/10 rounded-lg px-4 py-2.5 text-white focus:outline-none focus:ring-2 focus:ring-emerald-500/50"
                />
              </div>
            </div>
          </div>

          {/* Notes */}
          <div className="bg-white/5 backdrop-blur-sm rounded-lg p-6 border border-white/10">
            <h2 className="text-xl font-semibold text-emerald-400 mb-4">Additional Notes</h2>
            <textarea
              name="notes"
              value={plan.notes}
              onChange={handleChange}
              rows="4"
              className="w-full bg-white/5 border border-white/10 rounded-lg px-4 py-2.5 text-white focus:outline-none focus:ring-2 focus:ring-emerald-500/50"
              placeholder="Add any additional notes or comments..."
            />
          </div>

          {/* Action Buttons */}
          <div className="flex justify-end space-x-4">
            <motion.button
              whileHover={{ scale: 1.02 }}
              whileTap={{ scale: 0.98 }}
              type="button"
              onClick={() => navigate(`/education/family/${familyProfileId}/children`)}
              className="px-6 py-2.5 rounded-lg border border-white/10 text-gray-300 hover:text-white hover:bg-white/5"
              disabled={loading}
            >
              Cancel
            </motion.button>
            <motion.button
              whileHover={{ scale: 1.02 }}
              whileTap={{ scale: 0.98 }}
              type="submit"
              className="px-6 py-2.5 bg-emerald-500/20 text-emerald-400 rounded-lg hover:bg-emerald-500/30 transition-colors flex items-center disabled:opacity-50"
              disabled={loading}
            >
              <Save size={20} className="mr-2" />
              {loading ? 'Saving...' : (planId ? 'Update Plan' : 'Create Plan')}
            </motion.button>
          </div>
        </motion.form>
      </div>
    </div>
  );
};

export default EducationPlanForm;
