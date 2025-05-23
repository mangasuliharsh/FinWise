import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { ArrowRight, ArrowLeft, Check, User, Users, Baby } from 'lucide-react';

const Onboarding = () => {
    const navigate = useNavigate();
    const [step, setStep] = useState(1);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [formData, setFormData] = useState({
        familyProfile: {
            primaryGuardianName: '',
            primaryGuardianEmail: '',
            primaryGuardianPhone: '',
            secondaryGuardianName: '',
            secondaryGuardianEmail: '',
            secondaryGuardianPhone: '',
            address: '',
            annualIncome: ''
        },
        children: [{
            name: '',
            dateOfBirth: '',
            gender: '',
            currentEducationLevel: ''
        }]
    });

    const handleFamilyProfileChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            familyProfile: {
                ...prev.familyProfile,
                [name]: value
            }
        }));
    };

    const handleChildChange = (index, e) => {
        const { name, value } = e.target;
        setFormData(prev => {
            const newChildren = [...prev.children];
            newChildren[index] = {
                ...newChildren[index],
                [name]: value
            };
            return {
                ...prev,
                children: newChildren
            };
        });
    };

    const addChild = () => {
        setFormData(prev => ({
            ...prev,
            children: [...prev.children, {
                name: '',
                dateOfBirth: '',
                gender: '',
                currentEducationLevel: ''
            }]
        }));
    };

    const removeChild = (index) => {
        setFormData(prev => ({
            ...prev,
            children: prev.children.filter((_, i) => i !== index)
        }));
    };

    const handleSubmit = async () => {
        try {
            setLoading(true);
            setError('');
            
            // Submit family profile
            const familyResponse = await axios.post('http://localhost:8080/api/family-profiles', {
                ...formData.familyProfile,
                onboardingComplete: false
            });
            const familyProfileId = familyResponse.data.id;
            
            // Submit children
            await Promise.all(formData.children.map(child =>
                axios.post(`http://localhost:8080/api/children/family/${familyProfileId}`, child)
            ));

            // Save family profile ID to localStorage
            localStorage.setItem('familyProfileId', familyProfileId);
            
            // Mark onboarding as complete
            await axios.put(`http://localhost:8080/api/family-profiles/${familyProfileId}`, {
                ...familyResponse.data,
                onboardingComplete: true
            });

            // Navigate to dashboard
            navigate('/dashboard');
        } catch (err) {
            setError('Failed to save family profile. Please try again.');
            console.error('Onboarding error:', err);
        } finally {
            setLoading(false);
        }
    };

    const renderFamilyProfileForm = () => (
        <div className="space-y-6">
            <div className="grid grid-cols-2 gap-6">
                <div>
                    <label className="block text-sm font-medium text-gray-300">Primary Guardian Name</label>
                    <input
                        type="text"
                        name="primaryGuardianName"
                        value={formData.familyProfile.primaryGuardianName}
                        onChange={handleFamilyProfileChange}
                        className="mt-1 block w-full rounded-md bg-white/5 border border-white/10 px-3 py-2 text-white focus:border-emerald-500 focus:ring-emerald-500"
                        required
                    />
                </div>
                <div>
                    <label className="block text-sm font-medium text-gray-300">Primary Guardian Email</label>
                    <input
                        type="email"
                        name="primaryGuardianEmail"
                        value={formData.familyProfile.primaryGuardianEmail}
                        onChange={handleFamilyProfileChange}
                        className="mt-1 block w-full rounded-md bg-white/5 border border-white/10 px-3 py-2 text-white focus:border-emerald-500 focus:ring-emerald-500"
                        required
                    />
                </div>
            </div>

            <div className="grid grid-cols-2 gap-6">
                <div>
                    <label className="block text-sm font-medium text-gray-300">Secondary Guardian Name</label>
                    <input
                        type="text"
                        name="secondaryGuardianName"
                        value={formData.familyProfile.secondaryGuardianName}
                        onChange={handleFamilyProfileChange}
                        className="mt-1 block w-full rounded-md bg-white/5 border border-white/10 px-3 py-2 text-white focus:border-emerald-500 focus:ring-emerald-500"
                    />
                </div>
                <div>
                    <label className="block text-sm font-medium text-gray-300">Secondary Guardian Email</label>
                    <input
                        type="email"
                        name="secondaryGuardianEmail"
                        value={formData.familyProfile.secondaryGuardianEmail}
                        onChange={handleFamilyProfileChange}
                        className="mt-1 block w-full rounded-md bg-white/5 border border-white/10 px-3 py-2 text-white focus:border-emerald-500 focus:ring-emerald-500"
                    />
                </div>
            </div>

            <div>
                <label className="block text-sm font-medium text-gray-300">Address</label>
                <textarea
                    name="address"
                    value={formData.familyProfile.address}
                    onChange={handleFamilyProfileChange}
                    rows={3}
                    className="mt-1 block w-full rounded-md bg-white/5 border border-white/10 px-3 py-2 text-white focus:border-emerald-500 focus:ring-emerald-500"
                    required
                />
            </div>

            <div>
                <label className="block text-sm font-medium text-gray-300">Annual Income</label>
                <input
                    type="number"
                    name="annualIncome"
                    value={formData.familyProfile.annualIncome}
                    onChange={handleFamilyProfileChange}
                    className="mt-1 block w-full rounded-md bg-white/5 border border-white/10 px-3 py-2 text-white focus:border-emerald-500 focus:ring-emerald-500"
                    required
                />
            </div>
        </div>
    );

    const renderChildrenForm = () => (
        <div className="space-y-8">
            {formData.children.map((child, index) => (
                <div key={index} className="p-6 bg-white/5 rounded-lg border border-white/10">
                    <div className="flex justify-between items-center mb-4">
                        <h3 className="text-lg font-medium">Child {index + 1}</h3>
                        {formData.children.length > 1 && (
                            <button
                                onClick={() => removeChild(index)}
                                className="text-red-400 hover:text-red-300"
                            >
                                Remove
                            </button>
                        )}
                    </div>
                    
                    <div className="grid grid-cols-2 gap-6">
                        <div>
                            <label className="block text-sm font-medium text-gray-300">Name</label>
                            <input
                                type="text"
                                name="name"
                                value={child.name}
                                onChange={(e) => handleChildChange(index, e)}
                                className="mt-1 block w-full rounded-md bg-white/5 border border-white/10 px-3 py-2 text-white focus:border-emerald-500 focus:ring-emerald-500"
                                required
                            />
                        </div>
                        <div>
                            <label className="block text-sm font-medium text-gray-300">Date of Birth</label>
                            <input
                                type="date"
                                name="dateOfBirth"
                                value={child.dateOfBirth}
                                onChange={(e) => handleChildChange(index, e)}
                                className="mt-1 block w-full rounded-md bg-white/5 border border-white/10 px-3 py-2 text-white focus:border-emerald-500 focus:ring-emerald-500"
                                required
                            />
                        </div>
                    </div>

                    <div className="grid grid-cols-2 gap-6 mt-4">
                        <div>
                            <label className="block text-sm font-medium text-gray-300">Gender</label>
                            <select
                                name="gender"
                                value={child.gender}
                                onChange={(e) => handleChildChange(index, e)}
                                className="mt-1 block w-full rounded-md bg-white/5 border border-white/10 px-3 py-2 text-white focus:border-emerald-500 focus:ring-emerald-500"
                                required
                            >
                                <option value="">Select Gender</option>
                                <option value="male">Male</option>
                                <option value="female">Female</option>
                                <option value="other">Other</option>
                            </select>
                        </div>
                        <div>
                            <label className="block text-sm font-medium text-gray-300">Current Education Level</label>
                            <select
                                name="currentEducationLevel"
                                value={child.currentEducationLevel}
                                onChange={(e) => handleChildChange(index, e)}
                                className="mt-1 block w-full rounded-md bg-white/5 border border-white/10 px-3 py-2 text-white focus:border-emerald-500 focus:ring-emerald-500"
                                required
                            >
                                <option value="">Select Level</option>
                                <option value="preschool">Preschool</option>
                                <option value="primary">Primary School</option>
                                <option value="secondary">Secondary School</option>
                                <option value="higher_secondary">Higher Secondary</option>
                                <option value="undergraduate">Undergraduate</option>
                                <option value="postgraduate">Postgraduate</option>
                            </select>
                        </div>
                    </div>
                </div>
            ))}

            <button
                type="button"
                onClick={addChild}
                className="w-full mt-4 px-4 py-2 bg-emerald-500/20 text-emerald-400 rounded-md hover:bg-emerald-500/30 focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:ring-offset-2 focus:ring-offset-gray-900"
            >
                Add Another Child
            </button>
        </div>
    );

    return (
        <div className="min-h-screen bg-gradient-to-br from-blue-900 to-indigo-950 py-12 px-4 sm:px-6 lg:px-8">
            <div className="max-w-3xl mx-auto">
                <div className="text-center mb-8">
                    <h2 className="text-3xl font-bold text-white">Welcome to FinWise</h2>
                    <p className="mt-2 text-gray-300">Let's set up your family profile</p>
                </div>

                {/* Progress Steps */}
                <div className="mb-8">
                    <div className="flex items-center justify-center space-x-4">
                        <div className={`w-8 h-8 rounded-full flex items-center justify-center ${step === 1 ? 'bg-emerald-500 text-white' : 'bg-white/10 text-gray-400'}`}>
                            <User size={16} />
                        </div>
                        <div className="w-16 h-1 bg-white/10" />
                        <div className={`w-8 h-8 rounded-full flex items-center justify-center ${step === 2 ? 'bg-emerald-500 text-white' : 'bg-white/10 text-gray-400'}`}>
                            <Baby size={16} />
                        </div>
                    </div>
                </div>

                {error && (
                    <div className="mb-4 p-4 bg-red-500/20 border border-red-500/50 rounded-md text-red-400">
                        {error}
                    </div>
                )}

                <div className="bg-white/10 backdrop-blur-sm rounded-xl p-8">
                    <form onSubmit={(e) => e.preventDefault()}>
                        {step === 1 ? renderFamilyProfileForm() : renderChildrenForm()}

                        <div className="mt-8 flex justify-between">
                            {step > 1 && (
                                <button
                                    type="button"
                                    onClick={() => setStep(1)}
                                    className="inline-flex items-center px-4 py-2 bg-white/5 text-white rounded-md hover:bg-white/10"
                                >
                                    <ArrowLeft size={16} className="mr-2" />
                                    Back
                                </button>
                            )}
                            <button
                                type="button"
                                onClick={() => {
                                    if (step === 1) {
                                        setStep(2);
                                    } else {
                                        handleSubmit();
                                    }
                                }}
                                disabled={loading}
                                className={`inline-flex items-center px-4 py-2 ${step === 2 ? 'bg-emerald-500 hover:bg-emerald-600' : 'bg-white/10 hover:bg-white/20'} text-white rounded-md ml-auto`}
                            >
                                {loading ? (
                                    <span>Processing...</span>
                                ) : (
                                    <>
                                        {step === 1 ? 'Next' : 'Complete Setup'}
                                        {step === 1 ? <ArrowRight size={16} className="ml-2" /> : <Check size={16} className="ml-2" />}
                                    </>
                                )}
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default Onboarding;
