import React, { useState, useEffect } from 'react';
import { useNavigate, useParams, useLocation } from 'react-router-dom';
import { motion } from 'framer-motion';
import { BookOpen, Plus, School, Calendar, Building2, ArrowRight, Loader2 } from 'lucide-react';
import { createAuthenticatedAxios } from '../../utils/auth';
import Navbar from '../common/Navbar';

const EducationPlanList = () => {
  const { familyProfileId } = useParams();
  const navigate = useNavigate();
  const location = useLocation();
  const [plans, setPlans] = useState([]);  
  const [children, setChildren] = useState([]);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(true);

  const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        setError('');
        
        const authAxios = createAuthenticatedAxios();
        
        // Fetch children first
        const childrenResponse = await authAxios.get(`/api/children/family/${familyProfileId}`);
        const fetchedChildren = childrenResponse.data || [];
        setChildren(fetchedChildren);
        
        // Only fetch plans if there are children
        if (fetchedChildren && fetchedChildren.length > 0) {
          try {
            // Filter out any children without IDs and ensure we have valid IDs
            const childIds = fetchedChildren
              .filter(child => child && typeof child.id !== 'undefined')
              .map(child => child.id);

            if (childIds.length > 0) {
              // Fetch all plans at once
              const response = await authAxios.get(`/api/education-plans/family/${familyProfileId}`);
              const allPlans = Array.isArray(response.data) ? response.data : [];
              console.log('Fetched plans:', allPlans); // Debug log
              setPlans(allPlans);
            } else {
              setPlans([]); // Reset to empty array if no valid child IDs
            }
          } catch (planError) {
            console.error('Error fetching plans:', planError);
            setError('Failed to fetch education plans. Please try again later.');
            setPlans([]); // Reset to empty array on error
          }
        } else {
          setPlans([]); // Reset to empty array if no children
        }
      } catch (error) {
        console.error('Error fetching data:', error);
        if (error.response?.status === 401) {
          // Redirect to login if unauthorized
          navigate('/login', { state: { from: location } });
        } else {
          setError('Failed to fetch data. Please try again later.');
        }
        setChildren([]);
        setPlans([]);
      } finally {
        setLoading(false);
      }
    };

    if (familyProfileId) {
      fetchData();
    } else {
      setLoading(false);
      setChildren([]);
      setPlans([]);
    }
  }, [familyProfileId, navigate, location]);

  const handleCreatePlan = () => {
    navigate(`/education/family/${familyProfileId}/plan/new`);
  };

  const handleViewDetails = (planId) => {
    navigate(`/education/plan/${planId}`);
  };

  const handleEditPlan = (planId) => {
    navigate(`/education/family/${familyProfileId}/edit/${planId}`);
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
          <p className="text-gray-400">Please select a family profile to view education plans.</p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-900 to-indigo-950 text-white pb-10">
      <Navbar 
        title="Education Plans" 
        icon={BookOpen} 
      />

      {/* Main Content */}
      <div className="max-w-6xl mx-auto px-6 md:px-16 lg:px-24 mt-8">
        {error && (
          <motion.div 
            initial={{ opacity: 0, y: -10 }}
            animate={{ opacity: 1, y: 0 }}
            className="bg-red-500/10 border border-red-500/20 rounded-lg p-4 mb-6 text-red-400"
          >
            {error}
          </motion.div>
        )}

        {loading ? (
          <div className="flex items-center justify-center h-64">
            <Loader2 className="w-8 h-8 animate-spin text-emerald-400" />
          </div>
        ) : (
          <>
            {/* Children Section */}
            <motion.section 
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              className="mb-8"
            >
              <h2 className="text-2xl font-semibold mb-4 flex items-center">
                <School className="mr-2 text-emerald-400" />
                Children
              </h2>
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                {children.map(child => (
                  <motion.div
                    key={child?.id}
                    whileHover={{ scale: 1.02 }}
                    className="bg-white/5 backdrop-blur-sm rounded-lg p-6 border border-white/10"
                  >
                    <h3 className="text-lg font-semibold text-emerald-400">{child?.name}</h3>
                    <p className="text-gray-400 mt-2">
                      Current Level: {child?.currentEducationLevel}
                    </p>
                    {plans.filter(plan => plan?.child?.id === child?.id).length === 0 && (
                      <motion.button
                        whileHover={{ scale: 1.02 }}
                        whileTap={{ scale: 0.98 }}
                        onClick={() => {
                          if (child?.id) {
                            navigate(`/education/family/${familyProfileId}/plan/new?childId=${child.id}`);
                          }
                        }}
                        className="w-full p-4 border border-dashed border-white/20 rounded-lg hover:border-emerald-500/50 hover:bg-emerald-500/5 transition-all text-center text-gray-400 hover:text-emerald-400"
                      >
                        <Plus size={20} className="mx-auto mb-1" />
                        Add Education Plan
                      </motion.button>
                    )}
                  </motion.div>
                ))}
              </div>
            </motion.section>

            {/* Plans Section */}
            <motion.section
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.2 }}
            >
              <div className="flex justify-between items-center mb-6">
                <h2 className="text-2xl font-semibold flex items-center">
                  <Calendar className="mr-2 text-emerald-400" />
                  Education Plans
                </h2>
                <motion.button
                  whileHover={{ scale: 1.02 }}
                  whileTap={{ scale: 0.98 }}
                  onClick={handleCreatePlan}
                  className="flex items-center px-4 py-2 bg-emerald-500/20 text-emerald-400 rounded-lg hover:bg-emerald-500/30 transition-colors"
                >
                  <Plus size={20} className="mr-2" />
                  Create New Plan
                </motion.button>
              </div>

              {plans.length > 0 ? (
                <div className="grid gap-6">
                  {plans.map((plan) => (
                    <motion.div
                      key={plan?.id}
                      whileHover={{ scale: 1.01 }}
                      className="bg-white/5 backdrop-blur-sm rounded-lg p-6 border border-white/10"
                    >
                      <div className="flex justify-between items-start">
                        <div className="flex-1">
                          <div className="flex items-center mb-3">
                            <h3 className="text-xl font-semibold text-emerald-400">{plan?.planName}</h3>
                            <span className="ml-3 px-3 py-1 bg-emerald-500/20 text-emerald-400 rounded-full text-sm">
                              {plan?.child?.name || 'Unknown Child'}
                            </span>
                          </div>
                          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                            <div className="flex items-center text-gray-300">
                              <School className="w-4 h-4 mr-2" />
                              <span>{plan?.educationLevel}</span>
                            </div>
                            <div className="flex items-center text-gray-300">
                              <Building2 className="w-4 h-4 mr-2" />
                              <span>{plan?.institutionType}</span>
                            </div>
                            <div className="flex items-center text-gray-300">
                              <Calendar className="w-4 h-4 mr-2" />
                              <span>Starts {plan?.estimatedStartYear}</span>
                            </div>
                          </div>
                        </div>
                        <div className="flex space-x-2">
                          <motion.button
                            whileHover={{ scale: 1.05 }}
                            whileTap={{ scale: 0.95 }}
                            onClick={() => handleViewDetails(plan?.id)}
                            className="px-4 py-2 rounded-lg border border-white/10 text-gray-300 hover:text-white hover:bg-white/5"
                          >
                            View Details
                          </motion.button>
                          <motion.button
                            whileHover={{ scale: 1.05 }}
                            whileTap={{ scale: 0.95 }}
                            onClick={() => handleEditPlan(plan?.id)}
                            className="px-4 py-2 bg-emerald-500/20 text-emerald-400 rounded-lg hover:bg-emerald-500/30"
                          >
                            Edit Plan
                          </motion.button>
                        </div>
                      </div>
                    </motion.div>
                  ))}
                </div>
              ) : (
                <motion.div
                  initial={{ opacity: 0 }}
                  animate={{ opacity: 1 }}
                  className="bg-white/5 backdrop-blur-sm rounded-lg p-8 border border-white/10 text-center"
                >
                  <Calendar className="w-12 h-12 mx-auto mb-4 text-emerald-400" />
                  <h3 className="text-xl font-semibold mb-2">No Education Plans Yet</h3>
                  <p className="text-gray-400 mb-4">Create your first education plan to get started.</p>
                  <motion.button
                    whileHover={{ scale: 1.02 }}
                    whileTap={{ scale: 0.98 }}
                    onClick={handleCreatePlan}
                    className="inline-flex items-center px-4 py-2 bg-emerald-500/20 text-emerald-400 rounded-lg hover:bg-emerald-500/30 transition-colors"
                  >
                    <Plus size={20} className="mr-2" />
                    Create New Plan
                  </motion.button>
                </motion.div>
              )}
            </motion.section>
          </>
        )}
      </div>
    </div>
  );
};

export default EducationPlanList;
