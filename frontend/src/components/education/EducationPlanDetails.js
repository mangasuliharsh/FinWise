import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import { BookOpen, Edit2, Trash2, ArrowLeft, TrendingUp, Wallet, CalendarClock, School } from 'lucide-react';

const EducationPlanDetails = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [plan, setPlan] = useState(null);
  const [fundingGap, setFundingGap] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    loadPlanDetails();
  }, [id]);

  const loadPlanDetails = async () => {
    try {
      setLoading(true);
      const [planResponse, gapResponse] = await Promise.all([
        axios.get(`http://localhost:8080/api/education-plans/${id}`),
        axios.get(`http://localhost:8080/api/education-plans/${id}/funding-gap`)
      ]);
      setPlan(planResponse.data);
      setFundingGap(gapResponse.data);
    } catch (error) {
      setError('Failed to load education plan details.');
      console.error('Error loading plan details:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = () => {
    navigate(`/education/edit/${id}`);
  };

  const handleDelete = async () => {
    if (window.confirm('Are you sure you want to delete this education plan?')) {
      try {
        await axios.delete(`http://localhost:8080/api/education-plans/${id}`);
        navigate(`/education/child/${plan.childId}`);
      } catch (error) {
        setError('Failed to delete education plan.');
        console.error('Error deleting plan:', error);
      }
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-blue-900 to-indigo-950 text-white flex items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-emerald-400"></div>
      </div>
    );
  }

  if (!plan) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-blue-900 to-indigo-950 text-white p-6">
        <div className="bg-red-900/20 border border-red-800/30 rounded-lg p-4 text-red-400">
          Education plan not found
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-900 to-indigo-950 text-white pb-10">
      {/* Top Navigation Bar */}
      <nav className="px-6 py-4 flex justify-between items-center md:px-16 lg:px-24 border-b border-white/10">
        <div className="flex items-center space-x-3">
          <BookOpen size={24} className="text-emerald-400" />
          <h1 className="text-2xl font-bold tracking-tight">
            Education Plan Details
          </h1>
        </div>
        <div className="flex items-center space-x-4">
          <button
            onClick={handleEdit}
            className="flex items-center px-4 py-2 bg-emerald-500/20 text-emerald-400 rounded-lg hover:bg-emerald-500/30 transition-colors"
          >
            <Edit2 size={18} className="mr-2" />
            Edit Plan
          </button>
          <button
            onClick={handleDelete}
            className="flex items-center px-4 py-2 bg-red-500/20 text-red-400 rounded-lg hover:bg-red-500/30 transition-colors"
          >
            <Trash2 size={18} className="mr-2" />
            Delete
          </button>
        </div>
      </nav>

      {/* Main Content */}
      <div className="max-w-7xl mx-auto px-6 md:px-16 lg:px-24 mt-8">
        <button
          onClick={() => navigate(`/education/child/${plan.childId}`)}
          className="flex items-center text-gray-300 hover:text-white mb-6"
        >
          <ArrowLeft size={20} className="mr-2" />
          Back to Plans
        </button>

        {error && (
          <div className="mb-6 p-4 bg-red-900/20 border border-red-800/30 rounded-lg text-red-400">
            {error}
          </div>
        )}

        <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
          <div className="bg-white/5 backdrop-blur-sm rounded-lg p-6 border border-white/10">
            <div className="flex items-start justify-between mb-4">
              <div>
                <h2 className="text-2xl font-bold text-emerald-400">{plan.planName}</h2>
                <p className="text-gray-300 mt-1">Plan Overview</p>
              </div>
              <School size={24} className="text-emerald-400" />
            </div>
            <div className="grid grid-cols-2 gap-4">
              <div>
                <p className="text-sm text-gray-300">Education Level</p>
                <p className="font-medium">{plan.educationLevel}</p>
              </div>
              <div>
                <p className="text-sm text-gray-300">Institution Type</p>
                <p className="font-medium">{plan.institutionType}</p>
              </div>
            </div>
          </div>

          <div className="bg-white/5 backdrop-blur-sm rounded-lg p-6 border border-white/10">
            <div className="flex items-start justify-between mb-4">
              <div>
                <h2 className="text-2xl font-bold text-emerald-400">Timeline</h2>
                <p className="text-gray-300 mt-1">Duration Details</p>
              </div>
              <CalendarClock size={24} className="text-emerald-400" />
            </div>
            <div className="grid grid-cols-2 gap-4">
              <div>
                <p className="text-sm text-gray-300">Start Year</p>
                <p className="font-medium">{plan.estimatedStartYear}</p>
              </div>
              <div>
                <p className="text-sm text-gray-300">End Year</p>
                <p className="font-medium">{plan.estimatedEndYear}</p>
              </div>
            </div>
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
          <div className="bg-white/5 backdrop-blur-sm rounded-lg p-6 border border-white/10">
            <div className="flex items-start justify-between mb-4">
              <div>
                <h2 className="text-2xl font-bold text-emerald-400">Financial Details</h2>
                <p className="text-gray-300 mt-1">Current Status</p>
              </div>
              <Wallet size={24} className="text-emerald-400" />
            </div>
            <div className="grid grid-cols-2 gap-4">
              <div>
                <p className="text-sm text-gray-300">Current Savings</p>
                <p className="font-medium">₹{plan.currentSavings?.toLocaleString()}</p>
              </div>
              <div>
                <p className="text-sm text-gray-300">Monthly Contribution</p>
                <p className="font-medium">₹{plan.monthlyContribution?.toLocaleString()}</p>
              </div>
              <div>
                <p className="text-sm text-gray-300">Total Cost</p>
                <p className="font-medium">₹{plan.estimatedTotalCost?.toLocaleString()}</p>
              </div>
              <div>
                <p className="text-sm text-gray-300">Inflation Rate</p>
                <p className="font-medium">{plan.inflationRate}%</p>
              </div>
            </div>
          </div>

          <div className="bg-white/5 backdrop-blur-sm rounded-lg p-6 border border-white/10">
            <div className="flex items-start justify-between mb-4">
              <div>
                <h2 className="text-2xl font-bold text-emerald-400">Funding Gap Analysis</h2>
                <p className="text-gray-300 mt-1">Financial Planning</p>
              </div>
              <TrendingUp size={24} className="text-emerald-400" />
            </div>
            {fundingGap && (
              <div className="space-y-4">
                <div>
                  <p className="text-sm text-gray-300">Total Required</p>
                  <p className="font-medium">₹{fundingGap.totalRequired?.toLocaleString()}</p>
                </div>
                <div>
                  <p className="text-sm text-gray-300">Projected Savings</p>
                  <p className="font-medium">₹{fundingGap.projectedSavings?.toLocaleString()}</p>
                </div>
                <div>
                  <p className="text-sm text-gray-300">Funding Gap</p>
                  <p className="font-medium text-red-400">₹{fundingGap.gap?.toLocaleString()}</p>
                </div>
                <div>
                  <p className="text-sm text-gray-300">Recommended Monthly Increase</p>
                  <p className="font-medium text-emerald-400">₹{fundingGap.recommendedMonthlyIncrease?.toLocaleString()}</p>
                </div>
              </div>
            )}
          </div>
        </div>

        {plan.notes && (
          <div className="bg-white/5 backdrop-blur-sm rounded-lg p-6 border border-white/10">
            <h2 className="text-xl font-bold text-emerald-400 mb-4">Notes</h2>
            <p className="text-gray-300 whitespace-pre-line">{plan.notes}</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default EducationPlanDetails;
